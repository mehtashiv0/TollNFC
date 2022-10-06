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
import com.google.firebase.auth.FirebaseAuth;
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

public class Acceptedreq extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Cardreq, ACR> c;
    TextView name,dlno,tvemail,mob;
    Button block,pedreq;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptedreq);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rvaccept);
        databaseReference = FirebaseDatabase.getInstance().getReference("Nfcdetails");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Acceptedreq.this));
        pedreq = findViewById(R.id.penreq);

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

                } else if (id == R.id.acceptedreq) {
                    startActivity(new Intent(getApplicationContext(), Acceptedreq.class));
                }else if (id == R.id.logout) {

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        pedreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Viewrequestcard.class);
                startActivity(i);
            }
        });

        Query q=databaseReference.orderByChild("status").equalTo(true);
        final FirebaseRecyclerOptions<Cardreq> s = new FirebaseRecyclerOptions.Builder<Cardreq>().setQuery(q,Cardreq.class).build();

        c=new FirebaseRecyclerAdapter<Cardreq, Acceptedreq.ACR>(s) {

            @Override
            protected void onBindViewHolder(@NonNull Acceptedreq.ACR holder, final int position, @NonNull Cardreq model) {

                holder.name.setText(model.getName());

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        final Cardreq req=getItem(position);
                        Dialog dialog=new Dialog(Acceptedreq.this);
                        dialog.setContentView(R.layout.cardaccept);

                        name=dialog.findViewById(R.id.name);
                        dlno=dialog.findViewById(R.id.dlno);
                        tvemail=dialog.findViewById(R.id.email);
                        mob=dialog.findViewById(R.id.mob);
                        pedreq=dialog.findViewById(R.id.block);

                        name.setText(req.getName());
                        dlno.setText(req.getDlno());
                        tvemail.setText(req.getEmail());
                        mob.setText(req.getMob());

                        pedreq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.child(req.getId()).child("status").setValue(false);

                                Toast.makeText(getApplicationContext(),"Successfully Blocked!",Toast.LENGTH_LONG).show();
                            }
                        });

                        dialog.show();
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public ACR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardname,parent,false);
                return new Acceptedreq.ACR(view);
            }
        };
        c.startListening();
        recyclerView.setAdapter(c);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acceptedreq, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static class ACR extends RecyclerView.ViewHolder {

        TextView name;
        public ACR(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
