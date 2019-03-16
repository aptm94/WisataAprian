package com.apriantrimulyana.soaltesterporate;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class DetailWisata extends AppCompatActivity {

    private String getNamaPariwisata,getAlamatPariwisata,getDetailPariwisata,getGambarPariwisata,
            getCheckMaps,getResultMapsWisata,getJarakWisata;
    private ImageView imgWisata;
    private TextView tvJudul,tvAlamat,tvJarak,tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chev_white_left));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); } });

        getNamaPariwisata       = getIntent().getExtras().getString("nama_pariwisata");
        getAlamatPariwisata     = getIntent().getExtras().getString("alamat_pariwisata");
        getDetailPariwisata     = getIntent().getExtras().getString("detail_pariwisata");
        getGambarPariwisata     = getIntent().getExtras().getString("gambar_pariwisata");
        getCheckMaps            = getIntent().getExtras().getString("check_maps");
        getResultMapsWisata     = getIntent().getExtras().getString("result_maps_wisata");
        getJarakWisata          = getIntent().getExtras().getString("jarak_wisata");

        tvJudul     = findViewById(R.id.tv_judul);
        tvAlamat    = findViewById(R.id.tv_alamat);
        tvJarak     = findViewById(R.id.tv_jarak);
        tvDetail    = findViewById(R.id.tv_detail);
        imgWisata   = findViewById(R.id.img);


        tvJudul.setText(getNamaPariwisata);

        if(getCheckMaps.equals("1")){
            if(getResultMapsWisata.equals("1")){
                tvJarak.setText(getJarakWisata+" Dari Tempat Anda");
            }
        }

        tvAlamat.setText(getAlamatPariwisata);
        tvDetail.setText(getDetailPariwisata);

        try {
            String sGambar = getGambarPariwisata;
            if(sGambar.equals("")){
                imgWisata.setImageResource(R.mipmap.ic_launcher);
            }else{
                Glide.with(this)
                        .load(sGambar)
                        .centerCrop()
                        .error(getResources().getDrawable( R.mipmap.ic_launcher)) // Error Gambar
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                // Gambar siap di show
                                imgWisata.setImageDrawable(resource);
                            }
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable){
                                // Gambar gagal diload
                            }
                            @Override
                            public void onLoadStarted(Drawable placeholder){
                                // Gambar gagal mulai diload
                            }
                        });
            }

        }catch (Exception E){}

    }
}
