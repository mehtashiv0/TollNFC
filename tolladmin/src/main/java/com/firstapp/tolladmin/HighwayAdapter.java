package com.firstapp.tolladmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import Models.Way;

public class HighwayAdapter extends FirebaseRecyclerAdapter<Way, HighwayAdapter.VH> {

    public HighwayAdapter(@NonNull FirebaseRecyclerOptions<Way> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, final int position, @NonNull final Way model) {

        holder.tv.setText(model.getName());



    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardhighway,parent,false);
        return new VH(view);    }


    public static  class VH extends RecyclerView.ViewHolder  {
        TextView tv;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tvhw);

        }

    }
}
