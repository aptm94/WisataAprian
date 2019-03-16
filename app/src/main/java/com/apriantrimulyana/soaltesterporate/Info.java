package com.apriantrimulyana.soaltesterporate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import es.dmoral.prefs.Prefs;

public class Info extends AppCompatActivity {
    private TextView tvAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chev_white_left));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); } });

        tvAkun     = findViewById(R.id.tv_akun);

        String login_gmail = Prefs.with(Info.this).read("login_email");
        tvAkun.setText(login_gmail);
    }
}
