package com.song.openapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyPagerAdapter extends RecyclerView.Adapter<MyPagerAdapter.VH> {

    Context context;
    ArrayList<String> imgItems;

    public MyPagerAdapter(Context context, ArrayList<String> imgItems) {
        this.context = context;
        this.imgItems = imgItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.specific_pager,parent,false);
        VH holder=new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        Glide.with(context).load(imgItems.get(position)).into(holder.ivImg);

    }

    @Override
    public int getItemCount() {
        return imgItems.size();
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView ivImg;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivImg=itemView.findViewById(R.id.spPager_iv);
        }
    }
}


