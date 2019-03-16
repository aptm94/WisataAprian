package com.apriantrimulyana.soaltesterporate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apriantrimulyana.soaltesterporate.adapter.ListWisataAdapter;
import com.apriantrimulyana.soaltesterporate.model.DatabaseManager;
import com.apriantrimulyana.soaltesterporate.model.ListModel;
import com.apriantrimulyana.soaltesterporate.util.GridSpacingItemDecoration;
import com.apriantrimulyana.soaltesterporate.util.OtherUtil;
import com.apriantrimulyana.soaltesterporate.util.RecyclerTouchListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.prefs.Prefs;

import static com.apriantrimulyana.soaltesterporate.util.OtherUtil.round;
import static com.apriantrimulyana.soaltesterporate.util.intUtil.dpToPx;

public class Home extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private ArrayList<ListModel> postArrayList;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvView;
    private RecyclerView.LayoutManager layoutManager;
    private ListWisataAdapter adapter;
    private RelativeLayout rlInfo,rlLogout;
    private EditText pencarian;
    private String edit_search;
    private DatabaseManager dm;
    private Timer timerCek;

    private GoogleApiClient mGoogleApiClient;
    private LocationListener locListener;
    private LocationManager locationManager;
    private Location mLastLocation;
    private String latitude,longitude,latitude_lis,longitude_lis,sAllLatitude,sAllLongitude;
    private String tamJarak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); } });

        dm = new DatabaseManager(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //lo
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new myLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);


        rlInfo      = findViewById(R.id.rl_info);
        rlLogout    = findViewById(R.id.rl_logout);
        swipe       = findViewById(R.id.swipe);
        rvView      = findViewById(R.id.rv_main);
        pencarian   = findViewById(R.id.editText);
        
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           calllistData();
                       }
                   }
        );

        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvView.setHasFixedSize(true);
        rvView.setLayoutManager(mLayoutManager);
        rvView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10,this), true));
        rvView.setItemAnimator(new DefaultItemAnimator());

        rvView.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ListModel j = postArrayList.get(position);
                String nama_pariwisata      = j.getData2();
                String alamat_pariwisata    = j.getData3();
                String detail_pariwisata    = j.getData4();
                String gambar_pariwisata    = j.getData5();
                String check_maps           = j.getData6();
                String latitude_wisata      = j.getData7();
                String longitude_wisata     = j.getData8();
                String result_maps_wisata   = j.getData9();
                String jarak_wisata         = j.getData10();

                Intent moveDetailWisata = new Intent(Home.this,DetailWisata.class);
                Bundle detail = new Bundle();
                detail.putString("nama_pariwisata", nama_pariwisata);
                detail.putString("alamat_pariwisata", alamat_pariwisata);
                detail.putString("detail_pariwisata", detail_pariwisata);
                detail.putString("gambar_pariwisata", gambar_pariwisata);
                detail.putString("check_maps", check_maps);
                detail.putString("latitude_wisata", latitude_wisata);
                detail.putString("longitude_wisata", longitude_wisata);
                detail.putString("result_maps_wisata", result_maps_wisata);
                detail.putString("jarak_wisata", jarak_wisata);
                moveDetailWisata.putExtras(detail);
                startActivity(moveDetailWisata);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        pencarian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_search = charSequence.toString();
                if(adapter != null){
                    adapter.filter(edit_search);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveInfo = new Intent(Home.this,Info.class);
                startActivity(moveInfo);
            }
        });

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.with(Home.this).clear();
                Intent moveLogin = new Intent(Home.this,Login.class);
                startActivity(moveLogin);
                finish();
            }
        });


        timerCek = new Timer();
        timerCek.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
                            convertAlamatWisata();
                        }
                    }
                });
            }
        }, 0, 30000);
    }

    private class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                latitude_lis = String.valueOf(location.getLatitude());
                longitude_lis = String.valueOf(location.getLongitude());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void calllistData(){
        swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://erporate.com/bootcamp/jsonBootcamp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String result = json.getString("result");
                            if (result.equals("true")) {
                                JSONArray data = json.getJSONArray("data");
                                for (int i=0; i<data.length(); i++) {
                                    JSONObject ar = data.getJSONObject(i);
                                    String nama_pariwisata      = ar.getString("nama_pariwisata").trim();
                                    String alamat_pariwisata    = ar.getString("alamat_pariwisata").trim();
                                    String detail_pariwisata    = ar.getString("detail_pariwisata").trim();
                                    String gambar_pariwisata    = ar.getString("gambar_pariwisata").trim();

                                    ArrayList<ArrayList<Object>> data_check_wisata = dm.ambilBarisCheckWisata(nama_pariwisata);
                                    if (data_check_wisata.size() < 1) {
                                        dm.addRowWisata(nama_pariwisata,alamat_pariwisata,
                                                detail_pariwisata,gambar_pariwisata,"0",
                                                "0","0");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e("List Error","error "+e);
                        }
                        viewData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        viewData();
                        Toast.makeText(Home.this,"Gagal Update",Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.e("VOLLEYERROR","AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("VOLLEYERROR","ServerError");
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.e("VOLLEYERROR","NetworkError");
                            //TODO
                        } else if (error instanceof ParseError) {
                            Log.e("VOLLEYERROR","ParseError");
                            //TODO
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                Log.w("DATA_POST"," list : "+params);
                //returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    private void viewData(){
        postArrayList=new ArrayList<>();
        ArrayList<ArrayList<Object>> data_wisata_all = dm.ambilBarisWisataAll();
        if (data_wisata_all.size() > 0) {
            for(int i=0; i<data_wisata_all.size(); i++) {
                ArrayList<Object> baris = data_wisata_all.get(i);
                String hsIdWisata           = baris.get(0).toString();
                String hsNamaPariwisata     = baris.get(1).toString();
                String hsAlamatPariwisata   = baris.get(2).toString();
                String hsDetailPariwisata   = baris.get(3).toString();
                String hsGambarPariwisata   = baris.get(4).toString();
                String hsCheckMaps          = baris.get(5).toString();
                String hsLatitude           = baris.get(6).toString();
                String hsLogitude           = baris.get(7).toString();
                String hsResultMaps         = baris.get(8).toString();


                if(latitude == null){
                    sAllLatitude = latitude_lis;
                    sAllLongitude = longitude_lis;
                }else if(latitude.equals("")){
                    sAllLatitude = latitude_lis;
                    sAllLongitude = longitude_lis;
                }else{
                    sAllLatitude = latitude;
                    sAllLongitude = longitude;
                }

                if(hsCheckMaps.equals("1")){
                    if(hsResultMaps.equals("1")){
                        Double djarak = OtherUtil.distance(sAllLatitude,sAllLongitude,hsLatitude,hsLogitude);
                        tamJarak = ""+djarak;
                        if(!tamJarak.equals("null")){
                            double jarak = Double.parseDouble(tamJarak);
                            tamJarak = round(jarak, 2)+" KM";
                        }
                    }else{
                        tamJarak = "Jarak Tidak Ditemukan";
                    }
                }else{
                    tamJarak = "Sedang Memproses Jarak";
                }


                postArrayList.add(new ListModel(hsIdWisata,
                        hsNamaPariwisata, hsAlamatPariwisata, hsDetailPariwisata, hsGambarPariwisata,
                        hsCheckMaps, hsLatitude, hsLogitude,hsResultMaps,tamJarak));
            }
            adapter = new ListWisataAdapter(Home.this,postArrayList);
            rvView.setAdapter(adapter);
        }
        swipe.setRefreshing(false);
    }

    private void convertAlamatWisata(){
        ArrayList<ArrayList<Object>> data_wisata_check_maps_0 = dm.ambilBarisWisataCheckMaps0();
        if (data_wisata_check_maps_0.size() > 0) {
            for(int i=0; i<data_wisata_check_maps_0.size(); i++) {
                ArrayList<Object> baris_check = data_wisata_check_maps_0.get(0);
                String hsCheckIdWisata           = baris_check.get(0).toString();
                String hsCheckNamaPariwisata     = baris_check.get(1).toString();
                String hsCheckAlamatPariwisata   = baris_check.get(2).toString();
                String hsCheckDetailPariwisata   = baris_check.get(3).toString();
                String hsCheckGambarPariwisata   = baris_check.get(4).toString();
                String hsCheckCheckMaps          = baris_check.get(5).toString();
                String hsCheckLatitude           = baris_check.get(6).toString();
                String hsCheckLogitude           = baris_check.get(7).toString();

                sendCheckMaps(hsCheckIdWisata,hsCheckAlamatPariwisata);
            }
        }
    }

    public void sendCheckMaps(final String sIdWisata, String sAlamatPariwisata){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?address="+sAlamatPariwisata+"&key=AIzaSyD4TIm5aNxpvSe4ExGrQoU0u0qYocTHk00",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");

                            if(status.equals("OK")){
                                JSONArray results = json.getJSONArray("results");
                                JSONObject arResult  = results.getJSONObject(0);
                                String geometry = arResult.getString("geometry");
                                JSONObject jsonGeometry = new JSONObject(geometry);
                                String cnvrtLocation = jsonGeometry.getString("location");
                                JSONObject jsonCnvrtLocation = new JSONObject(cnvrtLocation);
                                String resultLat = jsonCnvrtLocation.getString("lat");
                                String resultLng = jsonCnvrtLocation.getString("lng");

                                dm.updateBaristTbAssignment(sIdWisata,"1",resultLat,
                                        resultLng,"1");
                            }else{
                                dm.updateBaristTbAssignment(sIdWisata,"1","0",
                                        "0","0");
                            }
                        } catch (Exception e) {
                            Log.e("List Error","error "+e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.e("VOLLEYERROR","AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("VOLLEYERROR","ServerError");
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.e("VOLLEYERROR","NetworkError");
                            //TODO
                        } else if (error instanceof ParseError) {
                            Log.e("VOLLEYERROR","ParseError");
                            //TODO
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                Log.w("DATA_POST"," list : "+params);
                //returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    private void getMyLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = "" + String.valueOf(mLastLocation.getLatitude());
                longitude = "" + String.valueOf(mLastLocation.getLongitude());
            }
        } catch (SecurityException e) {

        }
    }


    @Override
    public void onRefresh() {
        calllistData();
        pencarian.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
