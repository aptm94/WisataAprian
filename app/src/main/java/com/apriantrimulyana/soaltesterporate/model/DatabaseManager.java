package com.apriantrimulyana.soaltesterporate.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager {
    private static final String NAMA_DB = "db_aprian_tri_mulyana";
    private static final int DB_VERSION = 2;

    //variable simpan json
    private static final String ROW_ID_WISATA                   = "wisata_id";
    private static final String ROW_WISATA_NAMA_PARIWISATA      = "wisata_nama_pariwisata";
    private static final String ROW_WISATA_ALAMAT_PARIWISATA    = "wisata_alamat_pariwisata";
    private static final String ROW_WISATA_DETAIL_PARIWISATA    = "wisata_detail_pariwisata";
    private static final String ROW_WISATA_GAMBAR_PARIWISATA    = "wisata_gambar_pariwisata";
    private static final String ROW_WISATA_CHECK_MAPS           = "wisata_check_maps";
    private static final String ROW_WISATA_RESULT_MAPS          = "wisata_result_maps";
    private static final String ROW_WISATA_LATITUDE             = "wisata_latitude";
    private static final String ROW_WISATA_LONGITUDE            = "wisata_longitude";
    private static final String NAMA_TABEL_WISATA               = "tb_wisata";
    private static final String CREATE_TABLE_WISATA             = "create table "
            + NAMA_TABEL_WISATA             +" ("
            + ROW_ID_WISATA                 + " integer PRIMARY KEY autoincrement,"
            + ROW_WISATA_NAMA_PARIWISATA    + " text,"
            + ROW_WISATA_ALAMAT_PARIWISATA  + " text,"
            + ROW_WISATA_DETAIL_PARIWISATA  + " text,"
            + ROW_WISATA_GAMBAR_PARIWISATA  + " text,"
            + ROW_WISATA_CHECK_MAPS         + " text,"
            + ROW_WISATA_RESULT_MAPS         + " text,"
            + ROW_WISATA_LATITUDE           + " text,"
            + ROW_WISATA_LONGITUDE          + " text)";
    //end variable simpan json


    private final Context context;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseOpenHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }

    private static class DatabaseOpenHelper extends
            SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context) {
            super(context, NAMA_DB, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_WISATA);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int
                newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL_WISATA);


            onCreate(db);
        }
    }

    public void close() {
        dbHelper.close();
    }


    //table simpan json
    public void addRowWisata(String namaPariwisata,String alamatPariwisata,String detailPariwisata,
                             String gambarPariwisata,String checkMaps,String latitude,
                             String longitude) {
        ContentValues values = new ContentValues();
        values.put(ROW_WISATA_NAMA_PARIWISATA, namaPariwisata);
        values.put(ROW_WISATA_ALAMAT_PARIWISATA, alamatPariwisata);
        values.put(ROW_WISATA_DETAIL_PARIWISATA, detailPariwisata);
        values.put(ROW_WISATA_GAMBAR_PARIWISATA, gambarPariwisata);
        values.put(ROW_WISATA_CHECK_MAPS, checkMaps);
        values.put(ROW_WISATA_LATITUDE, latitude);
        values.put(ROW_WISATA_LONGITUDE, longitude);
        values.put(ROW_WISATA_RESULT_MAPS, "0");


        try {
            db.insert(NAMA_TABEL_WISATA, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> ambilBarisWisataAll() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_WISATA, new String[] {  ROW_ID_WISATA,
                            ROW_WISATA_NAMA_PARIWISATA,
                            ROW_WISATA_ALAMAT_PARIWISATA,
                            ROW_WISATA_DETAIL_PARIWISATA,
                            ROW_WISATA_GAMBAR_PARIWISATA,
                            ROW_WISATA_CHECK_MAPS,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_RESULT_MAPS},
                    null, null,
                    null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataList.add(cur.getString(4));
                    dataList.add(cur.getString(5));
                    dataList.add(cur.getString(6));
                    dataList.add(cur.getString(7));
                    dataList.add(cur.getString(8));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisWisata "+e.toString());
        } finally {
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }

    public ArrayList<ArrayList<Object>> ambilBarisCheckWisata(String namaWisata) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_WISATA, new String[] {  ROW_ID_WISATA,
                            ROW_WISATA_NAMA_PARIWISATA,
                            ROW_WISATA_ALAMAT_PARIWISATA,
                            ROW_WISATA_DETAIL_PARIWISATA,
                            ROW_WISATA_GAMBAR_PARIWISATA,
                            ROW_WISATA_CHECK_MAPS,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_RESULT_MAPS},
                    ROW_WISATA_NAMA_PARIWISATA + " = '"+namaWisata+"'", null,
                    null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataList.add(cur.getString(4));
                    dataList.add(cur.getString(5));
                    dataList.add(cur.getString(6));
                    dataList.add(cur.getString(7));
                    dataList.add(cur.getString(8));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisWisata "+e.toString());
        } finally {
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }

    public ArrayList<ArrayList<Object>> ambilBarisWisataCheckMaps0() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_WISATA, new String[] {  ROW_ID_WISATA,
                            ROW_WISATA_NAMA_PARIWISATA,
                            ROW_WISATA_ALAMAT_PARIWISATA,
                            ROW_WISATA_DETAIL_PARIWISATA,
                            ROW_WISATA_GAMBAR_PARIWISATA,
                            ROW_WISATA_CHECK_MAPS,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_LONGITUDE,
                            ROW_WISATA_RESULT_MAPS},
                    ROW_WISATA_CHECK_MAPS + " = '0'", null,
                    null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataList.add(cur.getString(4));
                    dataList.add(cur.getString(5));
                    dataList.add(cur.getString(6));
                    dataList.add(cur.getString(7));
                    dataList.add(cur.getString(8));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisWisata "+e.toString());
        } finally {
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }


    public void updateBaristTbAssignment(String idWisata,String checkMaps, String latitude,
                                         String longitude,String resultMaps) {
        ContentValues cv = new ContentValues();
        cv.put(ROW_WISATA_CHECK_MAPS, checkMaps);
        cv.put(ROW_WISATA_LATITUDE, latitude);
        cv.put(ROW_WISATA_LONGITUDE, longitude);
        cv.put(ROW_WISATA_RESULT_MAPS, resultMaps);

        try {
            db.update(NAMA_TABEL_WISATA, cv,
                    ROW_ID_WISATA+ " = '"+idWisata+"'", null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Db Error", e.toString());
        }
    }



}
