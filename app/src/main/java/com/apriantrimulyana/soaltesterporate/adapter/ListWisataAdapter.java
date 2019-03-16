package com.apriantrimulyana.soaltesterporate.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apriantrimulyana.soaltesterporate.R;
import com.apriantrimulyana.soaltesterporate.model.ListModel;
import com.apriantrimulyana.soaltesterporate.util.OtherUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListWisataAdapter extends RecyclerView.Adapter<ListWisataAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListModel> ListItems = null;
    private ArrayList<ListModel> arraylist;

    public ListWisataAdapter(Activity activity, List<ListModel> ListItems) {
        this.activity = activity;
        this.ListItems = ListItems;
        this.arraylist = new ArrayList<ListModel>();
        this.arraylist.addAll(ListItems);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ListModel j = ListItems.get(position);

        holder.titleContent.setText(j.getData2());
        holder.alamatContent.setText(j.getData3());

        String checkMaps   = j.getData6();
        String resultMaps  = j.getData9();

        if(checkMaps.equals("1")){
            if(resultMaps.equals("1")){
                holder.jarakContent.setText(j.getData10()+" Dari Tempat Anda");
            }else{
                holder.jarakContent.setText(j.getData10());
            }
        }else{
            holder.jarakContent.setText(j.getData10());
        }

        try {
            String sGambar = j.getData5().trim();
            if(sGambar.equals("")){
                holder.imgContent.setImageResource(R.mipmap.ic_launcher);
            }else{
                Glide.with(activity)
                        .load(sGambar)
                        .centerCrop()
                        .error(activity.getResources().getDrawable( R.mipmap.ic_launcher)) // Error Gambar
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                holder.imgContent.setImageDrawable(resource);
                            }
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable){

                            }
                            @Override
                            public void onLoadStarted(Drawable placeholder){

                            }
                        });
            }

        }catch (Exception E){}

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_wisata_rv_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleContent,alamatContent,jarakContent;
        public ImageView imgContent;

        public ViewHolder(View v) {
            super(v);
            titleContent    = v.findViewById(R.id.title_content);
            alamatContent   = v.findViewById(R.id.alamat_content);
            jarakContent    = v.findViewById(R.id.jarak_content);
            imgContent      = v.findViewById(R.id.img_content);
        }
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return ListItems.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ListItems.clear();
        if (charText.length() == 0) {
            ListItems.addAll(arraylist);
        } else {
            for (ListModel postDetail : arraylist) {
                if (postDetail.getData2().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ListItems.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
