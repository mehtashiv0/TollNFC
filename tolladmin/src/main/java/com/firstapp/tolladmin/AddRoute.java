package com.firstapp.tolladmin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import Models.Route;
import Models.Way;

public class AddRoute extends AppCompatActivity {

    ArrayList<String> highwaylist;
    DatabaseReference databaseReference,refrout;
    Spinner spinner;
    EditText source,destination,singleTravel,returnTravel;
    Button submit;
    Utility utility;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route2);
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

        spinner = findViewById(R.id.spinner);
        highwaylist = new ArrayList<String>();


        source=findViewById(R.id.source);
        destination=findViewById(R.id.destination);
        singleTravel=findViewById(R.id.singleTravel);
        returnTravel=findViewById(R.id.returnTravel);
        submit=findViewById(R.id.submit);
        utility = new Utility(AddRoute.this);
        refrout = FirebaseDatabase.getInstance().getReference("Routes");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;
                if (!Validation.nullValidator(source)) {
                    check = false;
                    source.setError("Enter Your Location");
                }
                if (!Validation.nullValidator(destination)) {
                    check = false;
                    destination.setError("Ennter the Destination");
                }
                if (!Validation.nullValidator(singleTravel)) {
                    check = false;
                    singleTravel.setError("Enter Price of Single Journey");
                }
                if (!Validation.nullValidator(returnTravel)) {
                    check = false;
                    returnTravel.setError("Enter Price of Return Journey");
                }
                if (check) {
                    if (utility.checkInternetConnectionALL()) {

                        refrout.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChildren()) {
                                    boolean isdata = true;

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        Route r = data.getValue(Route.class);
                                        if (r.getFrom().equals(source.getText().toString()) && r.getTo().equals(destination.getText().toString())) {
                                            isdata = false;
                                            break;
                                        }
                                    }

                                    if (isdata) {
                                        String id = refrout.push().getKey();

                                        Route r = new Route(id, source.getText().toString(),
                                                destination.getText().toString(), Double.parseDouble(singleTravel.getText().toString()),
                                                Double.parseDouble(returnTravel.getText().toString()), spinner.getSelectedItem().toString());
                                        refrout.child(id).setValue(r);
                                        utility.toast("Route Added");
                                    } else {
                                        utility.toast("Route Already Added");

                                    }
                                } else {
                                    String id = refrout.push().getKey();

                                    Route r = new Route(id, source.getText().toString(),
                                            destination.getText().toString(), Double.parseDouble(singleTravel.getText().toString()),
                                            Double.parseDouble(returnTravel.getText().toString()), spinner.getSelectedItem().toString());
                                    refrout.child(id).setValue(r);
                                    utility.toast("Route Added");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

        });

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
        navigationView.setItemIconTintList(null);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    // Handle the camera action
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else if (id == R.id.addhighway) {
                    startActivity(new Intent(getApplicationContext(), AddHighway.class));


                } else if (id == R.id.addroute) {
                    startActivity(new Intent(getApplicationContext(), AddRoute.class));

                }
                else if (id == R.id.viewroute) {
                    startActivity(new Intent(getApplicationContext(),ViewRoute.class));

                }
                else if (id == R.id.viewcardreq) {
                    startActivity(new Intent(getApplicationContext(),Viewrequestcard.class));

                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Highway Name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    highwaylist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Way h = ds.getValue(Way.class);
                        highwaylist.add(h.getName());
                    }

                    ArrayAdapter<String> al = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, highwaylist);
                    al.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(al);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
