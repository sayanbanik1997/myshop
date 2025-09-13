package com.google.sayanbanik1997.myshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class RecyAdapter extends RecyclerView.Adapter<Vh> {
    int count, layout;
    RecyAdapter(int layout, int count){
        this.count = count;
        this.layout = layout;
    }
    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(layout, parent, false);
        return onCreate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return count;
    }
    abstract void bind(Vh holder, int position);
    abstract Vh onCreate(View view);
}
abstract class Vh extends RecyclerView.ViewHolder{
    ArrayList<View> arrView = new ArrayList<>();
    public Vh(@NonNull View itemView) {
        super(itemView);
        initiateInsideViewHolder(itemView);
    }
    abstract void initiateInsideViewHolder(View itemView);
}