package com.firstapp.tolladmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Models.Way;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv);
        databaseReference= FirebaseDatabase.getInstance().getReference("Highway Name");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));

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

        FirebaseRecyclerOptions<Way> o = new FirebaseRecyclerOptions.Builder<Way>().setQuery(databaseReference,Way.class).build();

        FirebaseRecyclerAdapter<Way,HVH> adapter = new FirebaseRecyclerAdapter<Way, HVH>(o) {
            @Override
            protected void onBindViewHolder(@NonNull HVH holder, final int position, @NonNull Way model) {
                holder.tv.setText(model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Way way = getItem(position);
                        startActivity(new Intent(getApplicationContext(),ViewRoute.class).putExtra("hn",way.getName()));
                    }
                });

            }

            @NonNull
            @Override
            public HVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardhighway,parent,false);
                return new HVH(view);              }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static class HVH extends RecyclerView.ViewHolder {
        TextView tv;
        public HVH(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tvhw);

        }
    }
}
