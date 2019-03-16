package com.apriantrimulyana.soaltesterporate;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apriantrimulyana.soaltesterporate.util.OtherUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.prefs.Prefs;

public class Login extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText etEmail,etPassword;
    private Button btLogin,btRegister;
    private String email,password;
    public static Activity act_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        act_login = Login.this;
        auth = FirebaseAuth.getInstance();

        etEmail     = findViewById(R.id.et_email);
        etPassword  = findViewById(R.id.et_password);
        btLogin     = findViewById(R.id.bt_login);
        btRegister  = findViewById(R.id.bt_register);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveRegister = new Intent(Login.this, Register.class);
                startActivity(moveRegister);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email      = etEmail.getText().toString();
                password   = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!",
                            Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    login();
                }
            }
        });
    }

    private void login(){
        OtherUtil.hideAlertDialog();
        OtherUtil.showAlertDialogLoading(Login.this, "Please Wait ...");
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        OtherUtil.hideAlertDialog();
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(Login.this,
                                        "Password Harus Lebih Dari 6 Karakter",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Login.this,
                                        "Gagal Login, Email atau Password Salah",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Prefs.with(Login.this).write("login_email", email);

                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtil.hideAlertDialog();
    }
}
