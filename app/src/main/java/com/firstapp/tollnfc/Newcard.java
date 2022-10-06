package com.firstapp.tollnfc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.firstapp.tollnfc.R;
import com.firstapp.tollnfc.Utility;
import com.firstapp.tollnfc.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import models.Card;

public class Newcard extends AppCompatActivity {

    EditText name,email,mob,dln;
    ImageView photo;
    Button submit;
    Utility utility;
    Uri url;
    private final static int CODE = 1;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mob = findViewById(R.id.mob);
        dln = findViewById(R.id.dln);
        submit = findViewById(R.id.submit);
        photo = findViewById(R.id.dlphoto);

        progressDialog = new ProgressDialog(Newcard.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        utility=new Utility(Newcard.this);
        storageReference = FirebaseStorage.getInstance().getReference("documents");
        databaseReference = FirebaseDatabase.getInstance().getReference("Nfcdetails");
        firebaseAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),Homepage.class));

                }
                else if (id == R.id.ncr){
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }

                    else if (id == R.id.ncr) {

                    startActivity(new Intent(getApplicationContext(),Newcard.class));
                    {
                        Query q = databaseReference.orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid());

                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    Toast.makeText(getApplicationContext(), "You Already have a NFC card available" , Toast.LENGTH_LONG).show();
                                }
                                else{
                                    startActivity(new Intent(getApplicationContext(),Newcard.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;

                if (!Validation.nullValidator(name)) {
                    name.setError("Enter Name");
                    check = false;
                }
                if (!Validation.nullValidator(email)) {
                    email.setError("Enter Email");
                    check = false;
                }
                if (!Validation.nullValidator(mob)) {
                    mob.setError("Enter Mobile");
                    check = false;
                }
                if (!Validation.nullValidator(dln)) {
                    dln.setError("Enter DL");
                    check = false;
                }

                if (check) {

                    if (utility.checkInternetConnectionALL()) {

                        if(uri !=null)
                        {
                            progressDialog.show();

                            Query q=databaseReference.orderByChild("id").equalTo(firebaseAuth.getUid());
                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren())
                                    {
                                        boolean isavail=true;
                                        for (DataSnapshot data :dataSnapshot.getChildren())
                                        {
                                            Card card=data.getValue(Card.class);
                                            if(card.isAssign())
                                            {
                                                isavail=false;
                                                break;
                                            }
                                        }

                                        if(isavail)
                                        {
                                            Card card1 =new Card(firebaseAuth.getUid(),name.getText().toString(),
                                                    email.getText().toString(),
                                                    mob.getText().toString(),dln.getText().toString(),url.toString(),0,true);
                                            databaseReference.child(firebaseAuth.getUid()).setValue(card1);
                                            utility.toast("New Card Request Sent...");
                                            progressDialog.dismiss();
                                        }else{
                                            utility.toast("Already NFC card Assigned.");
                                            progressDialog.dismiss();
                                        }

                                    }
                                    else
                                    {
                                        Card card1 =new Card(firebaseAuth.getUid(),name.getText().toString(),
                                                email.getText().toString(),mob.getText().toString(),dln.getText().toString(),url.toString(),0,true);
                                        databaseReference.child(firebaseAuth.getUid()).setValue(card1);
                                        utility.toast("New Card Request Sent...");
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else {
                            utility.toast("Select an Image");
                        }
                    }
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, CODE);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.newcard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==CODE && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                photo.setImageBitmap(bitmap);
                progressDialog.show();


                UploadTask uploadTask = storageReference.child(uri.getLastPathSegment()).putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        url = uriTask.getResult();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        double pro =(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("UPLOADINGG..." + (int) pro + "%");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        utility.toast(e.getMessage());
                        progressDialog.dismiss();

                    }
                });


                /*UploadTask uploadTask=  storageReference.child(uri.getLastPathSegment()).putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                         url = uriTask.getResult();

                        Toast.makeText(Newcard.this, "Upload Success, download URL " +
                                url.toString(), Toast.LENGTH_LONG).show();
                    }
                });*/



            }
            catch (Exception e)
            {
                utility.toast(e.getMessage());
            }
        }
    }
    }
