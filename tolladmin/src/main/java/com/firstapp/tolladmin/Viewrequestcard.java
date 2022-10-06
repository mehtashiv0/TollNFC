package com.firstapp.tolladmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import Models.Cardreq;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Viewrequestcard extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter <Cardreq ,VCR> c;
    TextView  name,dlno,tvemail,mob;
    Button verify,acceptedreq;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrequestcard);
        setTitle("Card Requests");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.vcreq);
        databaseReference = FirebaseDatabase.getInstance().getReference("Nfcdetails");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Viewrequestcard.this));
        acceptedreq=findViewById(R.id.accreq);

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

                } else if (id == R.id.viewroute) {
                    startActivity(new Intent(getApplicationContext(), ViewRoute.class));

                } else if (id == R.id.viewcardreq) {
                    startActivity(new Intent(getApplicationContext(), Viewrequestcard.class));

                }else if (id == R.id.acceptedreq) {
                    startActivity(new Intent(getApplicationContext(), Acceptedreq.class));
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        acceptedreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Acceptedreq.class);
                startActivity(i);
            }
        });

        Query q=databaseReference.orderByChild("status").equalTo(1);
        final FirebaseRecyclerOptions<Cardreq> s = new FirebaseRecyclerOptions.Builder<Cardreq>().setQuery(q,Cardreq.class).build();

        c=new FirebaseRecyclerAdapter<Cardreq, VCR>(s) {
            @Override
            protected void onBindViewHolder(@NonNull VCR holder, final int position, @NonNull Cardreq model) {

                holder.name.setText(model.getName());

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        final Cardreq req=getItem(position);
                        Dialog dialog=new Dialog(Viewrequestcard.this);
                        dialog.setContentView(R.layout.cardreqnfc);


                        name=dialog.findViewById(R.id.name);
                        dlno=dialog.findViewById(R.id.dlno);
                        tvemail=dialog.findViewById(R.id.email);
                        mob=dialog.findViewById(R.id.mob);
                        verify=dialog.findViewById(R.id.verify);

                        name.setText(req.getName());
                        dlno.setText(req.getDlno());
                        tvemail.setText(req.getEmail());
                        mob.setText(req.getMob());
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.child(req.getId()).child("status").setValue(2);

                                Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_LONG).show();
                            }
                        });

                        dialog.show();
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public VCR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardname,parent,false);
                return new VCR(view);
            }
        };
        c.startListening();
        recyclerView.setAdapter(c);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.viewrequestcard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static class VCR extends RecyclerView.ViewHolder {
            TextView name;
        public VCR(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
