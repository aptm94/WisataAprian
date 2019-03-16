package com.apriantrimulyana.soaltesterporate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import static com.apriantrimulyana.soaltesterporate.Login.act_login;

public class Register extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText etEmail,etPassword;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btRegister = findViewById(R.id.bt_register);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email       = etEmail.getText().toString().trim();
                password    = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!",
                            Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!",
                            Toast.LENGTH_SHORT).show();
                }else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(),
                            "Password too short, enter minimum 6 characters!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    register();
                }
            }
        });
    }

    private void register(){
        OtherUtil.hideAlertDialog();
        OtherUtil.showAlertDialogLoading(Register.this, "Please Wait ...");
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        OtherUtil.hideAlertDialog();
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Prefs.with(Register.this).write("login_email", email);

                            startActivity(new Intent(Register.this, Home.class));
                            finish();
                            act_login.finish();
                        }
                    }
                });
    }
}
