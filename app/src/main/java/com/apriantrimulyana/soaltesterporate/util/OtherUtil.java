package com.apriantrimulyana.soaltesterporate.util;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apriantrimulyana.soaltesterporate.R;
import java.text.DecimalFormat;

public class OtherUtil {
    public static AlertDialog alertDialog;
    public static String aff;

    public static double distance(String slat1, String slon1, String slat2,
                                  String slon2) {

        Double lat1 = Double.parseDouble(slat1);
        Double lon1 = Double.parseDouble(slon1);
        Double lat2 = Double.parseDouble(slat2);
        Double lon2 = Double.parseDouble(slon2);

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 10; // convert to km
        //double distance = R * c * 1000; // convert to meters

        //double height = el1 - el2;

        //distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    public static double distanceTo(String slat1, String slon1, String slat2,
                                    String slon2) {
        Double currentLatitude  = Double.parseDouble(slat1);
        Double currentLongitude = Double.parseDouble(slon1);
        Double latitude         = Double.parseDouble(slat2);
        Double longitude        = Double.parseDouble(slon2);

        float distance=0;
        Location crntLocation=new Location("crntlocation");
        crntLocation.setLatitude(currentLatitude);
        crntLocation.setLongitude(currentLongitude);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);

//float distance = crntLocation.distanceTo(newLocation);  in meters
        distance = crntLocation.distanceTo(newLocation) / 1000; // in km
        return distance;
        //return Math.sqrt(distance);
    }

    public static double distFromTxt(String slat1, String slng1, String slat2, String slng2) {
        Double lat1 = Double.parseDouble(slat1);
        Double lng1 = Double.parseDouble(slng1);
        Double lat2 = Double.parseDouble(slat2);
        Double lng2 = Double.parseDouble(slng2);

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        /*if(dist>1000){
            return new DecimalFormat("#,##").format(dist/1000)+" km";
        }else{
            return dist+" m";
        }*/
        return dist;
    }


    public static String checkSatuan(Double dist){
        if(dist>1000){
            return new DecimalFormat("#,##").format(dist/1000)+" km";
        }else{
            return dist+" m";
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    public static void showAlertDialogLoading(Context context, String pesan) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            android.support.v7.app.AlertDialog.Builder builder1;
            builder1 = new android.support.v7.app.AlertDialog.Builder(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            TextView textView1 = (TextView) view.findViewById(R.id.textViewPesanDialog);
            textView1.setText(pesan);
            builder1.setView(view);
            alertDialog = builder1.create();
            //alertDialog.getWindow().setBackgroundDrawableResource(0);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } catch(Exception e){
            // the 'Force Close' message
        }
    }

    /**
     * Hide alert dialog merupakan function untuk menutup dialog loading.
     */
    public static boolean hideAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            return true;
        }
        return false;
    }

}
