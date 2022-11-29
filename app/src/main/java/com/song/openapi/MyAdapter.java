package com.song.openapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

    Context context;
    ArrayList<PrfItems> prfItems;


    public MyAdapter(Context context, ArrayList<PrfItems> prfItems) {
        this.context = context;
        this.prfItems = prfItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.recycler_item,parent,false);
        VH holder=new VH(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PrfItems prfItem=prfItems.get(position);

        holder.prfNm.setText(prfItem.prfNm);
        holder.stDate.setText(prfItem.stDate+"~");
        holder.edDate.setText(prfItem.edDate);
        if(prfItem.stDate.equals(prfItem.edDate)) holder.stDate.setText("");
        holder.fcltyNm.setText(prfItem.fcltyNm);
        Glide.with(context).load(prfItem.poster).into(holder.poster);
        holder.genreNm.setText(prfItem.genreNm);
        holder.state.setText(prfItem.state);
        holder.festYN.setText(prfItem.festYN);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),SpecificPrfActivity.class);
                intent.putExtra("prfId",prfItem.prfId);
                view.getContext().startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return prfItems.size();
    }

    class VH extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView prfNm;
        TextView stDate;
        TextView edDate;
        TextView fcltyNm;
        ImageView poster;
        TextView genreNm;
        TextView state;
        TextView festYN;

        public VH(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            prfNm=itemView.findViewById(R.id.tv_prfNm);
            stDate=itemView.findViewById(R.id.tv_stDate);
            edDate=itemView.findViewById(R.id.tv_endDate);
            fcltyNm=itemView.findViewById(R.id.tv_fcltyNm);
            poster=itemView.findViewById(R.id.iv_poster);
            genreNm=itemView.findViewById(R.id.tv_genre);
            state=itemView.findViewById(R.id.tv_state);
            festYN=itemView.findViewById(R.id.tv_fest);

        }

    }


}






