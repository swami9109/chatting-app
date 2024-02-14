package com.example.mychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button logInBtn;
    EditText emailET, passwordEt;
    TextView signInTv;
    FirebaseAuth auth;
    public String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        logInBtn = findViewById(R.id.logInBtn);
        emailET = findViewById(R.id.emailET);
        passwordEt = findViewById(R.id.passwordET);
        signInTv = findViewById(R.id.singInTn);

        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = emailET.getText().toString();
                String password = passwordEt.getText().toString();

                if ((TextUtils.isEmpty(Email))){
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Enter Email",
                            Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Enter The Password"
                            , Toast.LENGTH_SHORT).show();
                }else if (!Email.matches(emailPattern)){
                    progressDialog.dismiss();
                    emailET.setError("Give Proper Email Address");
                }else if (password.length() < 6){
                    progressDialog.dismiss();
                    passwordEt.setError("More Then Six Characters");
                    Toast.makeText(Login.this, "Password Needs " +
                            "To Be Longer Than ^ Characters"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.show();
                                try {
                                    Intent intent = new Intent(Login.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(Login.this, e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Login.this, task.getException().getMessage()
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}