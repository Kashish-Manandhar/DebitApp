package com.example.debitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhotoCopyAdapter extends RecyclerView.Adapter<PhotoCopyAdapter.PhotoCopyViewHolder>{


    ArrayList<Photcopy> photcopyList;

    public interface OnClick{
        void onClickUpdate(int index);
        void onClickPayment(int index);
    }

    OnClick clicked;


    public PhotoCopyAdapter(Context context, ArrayList<Photcopy> photcopyList) {
        clicked =(OnClick) context;
        this.photcopyList = photcopyList;
    }

    @NonNull
    @Override
    public PhotoCopyAdapter.PhotoCopyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new PhotoCopyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCopyAdapter.PhotoCopyViewHolder holder, final int position) {

        holder.itemView.setTag(photcopyList.get(position));
        holder.tVname.setText(photcopyList.get(position).getName());
        holder.tVprice.setText(photcopyList.get(position).getPrice()+"");


    }

    @Override
    public int getItemCount() {
        return photcopyList.size();
    }

    public class PhotoCopyViewHolder extends RecyclerView.ViewHolder{
        TextView tVname,tVprice;
        Button payment,update;
        public PhotoCopyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tVname=itemView.findViewById(R.id.tVName);
            tVprice=itemView.findViewById(R.id.tVPrice);
            payment=itemView.findViewById(R.id.pay);
            update=itemView.findViewById(R.id.update);
            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onClickPayment(photcopyList.indexOf((Photcopy) itemView.getTag()));
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onClickUpdate(photcopyList.indexOf((Photcopy) itemView.getTag()));
                }
            });

        }
    }
}
