package com.firstapp.tolladmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

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

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Models.Route;

public class ViewRoute extends AppCompatActivity {

    RecyclerView rv;
    DatabaseReference databaseReference;
    EditText sjprice, rjprice;
    Button btnedit, btndel;
    FirebaseRecyclerAdapter<Route, RH> recyclerAdapter;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseRecyclerOptions<Route> o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route2);
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

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        rv = findViewById(R.id.rvroutes);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ViewRoute.this));
        Intent i = getIntent();
        String s = i.getStringExtra("hn");
       /* Toast.makeText(getApplicationContext(), "before" + s, Toast.LENGTH_LONG).show();*/
        databaseReference = FirebaseDatabase.getInstance().getReference("Routes");

        Query q = databaseReference.orderByChild("highway").equalTo(s);

        o = new FirebaseRecyclerOptions.Builder<Route>()
                .setQuery(q, Route.class).build();
        recyclerAdapter = new FirebaseRecyclerAdapter<Route, RH>(o) {
            @Override
            protected void onBindViewHolder(@NonNull RH holder, final int position, @NonNull Route model) {

                //Toast.makeText(getApplicationContext(), "A" + model.getHighway(), Toast.LENGTH_LONG).show();

                holder.hn.setText(model.getHighway());
                holder.tvfr.setText("From:" + model.getFrom());
                holder.tvto.setText("To:" + model.getTo());
                holder.tvsj.setText("Single Journey" + String.valueOf(model.getSp()));
                holder.tvrj.setText("Retun Journey" + String.valueOf(model.getRp()));

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final Route route=getItem(position);
                        Toast.makeText(getApplicationContext(),route.getHighway(),Toast.LENGTH_LONG).show();
                        Dialog dialog=new Dialog(ViewRoute.this);
                        dialog.setContentView(R.layout.routdialog);
                        dialog.setTitle("Edit Or Delete Route Info...");
                        sjprice= dialog.findViewById(R.id.sjprice);
                        rjprice= dialog.findViewById(R.id.rjprice);
                        btnedit= dialog.findViewById(R.id.btneditd);
                        btndel= dialog.findViewById(R.id.btndel);

                        btnedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean b=true;

                                if(!Validation.nullValidator(sjprice))
                                {
                                    sjprice.setError("Enter Single Journey Price");
                                    b=false;
                                }


                                if(!Validation.nullValidator(rjprice))
                                {
                                    rjprice.setError("Enter Single Journey Price");
                                    b=false;
                                }

                                if (b)

                                {
                                    databaseReference.child(route.getId()).child("sp").setValue(sjprice.getText().toString());
                                    databaseReference.child(route.getId()).child("rp").setValue(rjprice.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Edited",Toast.LENGTH_SHORT).show();


                                }

                            }
                        });


                        btndel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReference.child(route.getId()).removeValue();
                                Toast.makeText(getApplicationContext(),"Deleted.",Toast.LENGTH_SHORT).show();

                            }
                        });
                        dialog.show();
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public RH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardroutes, parent, false);
                return new RH(view);
            }
        };
        recyclerAdapter.startListening();

        rv.setAdapter(recyclerAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static class RH extends RecyclerView.ViewHolder {
        TextView hn, tvfr, tvto, tvsj, tvrj;

        public RH(@NonNull View itemView) {
            super(itemView);
            hn = itemView.findViewById(R.id.hn);
            tvfr = itemView.findViewById(R.id.tvfr);
            tvto = itemView.findViewById(R.id.tvto);
            tvsj = itemView.findViewById(R.id.tvsj);
            tvrj = itemView.findViewById(R.id.tvrj);
        }
    }
}


/*


    public static class RVH extends RecyclerView.ViewHolder {
        TextView hn,tvfr,tvto,tvsj,tvrj;
        public RVH(@NonNull View itemView) {
            super(itemView);
            hn=itemView.findViewById(R.id.hn);
            tvfr=itemView.findViewById(R.id.tvfr);
            tvto=itemView.findViewById(R.id.tvto);
            tvsj=itemView.findViewById(R.id.tvsj);
            tvrj=itemView.findViewById(R.id.tvrj);
        }
    }



        FirebaseRecyclerOptions<Route> options = new FirebaseRecyclerOptions.Builder<Route>().setQuery(databaseReference, Route.class).build();
     FirebaseRecyclerAdapter<Route,RVH>   adapter = new FirebaseRecyclerAdapter<Route, RVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RVH holder, final int position, @NonNull Route model) {
                holder.hn.setText(model.getHighway());
                holder.tvfr.setText("From:" + model.getFrom());
                holder.tvto.setText("To:" + model.getTo());
                holder.tvsj.setText("Single Journey" + String.valueOf(model.getSp()));
                holder.tvrj.setText("Retun Journey" + String.valueOf(model.getRp()));

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final Route route=getItem(position);
                        Toast.makeText(getApplicationContext(),route.getHighway(),Toast.LENGTH_LONG).show();
                        Dialog dialog=new Dialog(ViewRoute.this);
                        dialog.setContentView(R.layout.routdialog);
                        dialog.setTitle("Edit Or Delete Route Info...");
                        sjprice= dialog.findViewById(R.id.sjprice);
                        rjprice= dialog.findViewById(R.id.rjprice);
                        btnedit= dialog.findViewById(R.id.btneditd);
                        btndel= dialog.findViewById(R.id.btndel);

                        btnedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean b=true;

                                if(!Validation.nullValidator(sjprice))
                                {
                                    sjprice.setError("Enter Single Journey Price");
                                    b=false;
                                }


                                if(!Validation.nullValidator(rjprice))
                                {
                                    rjprice.setError("Enter Single Journey Price");
                                    b=false;
                                }

                                if (b)

                                {
                                    databaseReference.child(route.getId()).child("sp").setValue(sjprice.getText().toString());
                                    databaseReference.child(route.getId()).child("rp").setValue(rjprice.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Edited",Toast.LENGTH_SHORT).show();


                                }

                            }
                        });


                        btndel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReference.child(route.getId()).removeValue();
                                Toast.makeText(getApplicationContext(),"Deleted.",Toast.LENGTH_SHORT).show();

                            }
                        });
                        dialog.show();
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public RVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardhighway,parent,false);
                return new RVH(view);
            }
        };
        adapter.startListening();
        rv.setAdapter(adapter);
*/