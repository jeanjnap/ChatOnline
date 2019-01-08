package com.jeanjnap.chat.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jeanjnap.chat.Models.User;
import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.Base64Util;
import com.jeanjnap.chat.Util.Constants;
import com.jeanjnap.chat.Util.PreferencesUtil;

public class LoginActivity extends AppCompatActivity {

    TextView createAccount;
    EditText email, pass;
    Button login;

    Base64Util base64;

    private FirebaseAuth mAuth;
    private PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.login);

        base64 = new Base64Util(this);
        preferencesUtil = new PreferencesUtil(this);

        //Log.i("_res", base64.stringToBase64("teste"));
        //Log.i("_res", base64.base64toString("dGVzdGU="));


        mAuth = FirebaseAuth.getInstance();

        createAccount = findViewById(R.id.createAcount);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);

        if(preferencesUtil.getString(Constants.EMAIL) != null){
            email.setText(preferencesUtil.getString(Constants.EMAIL));
            pass.setText(preferencesUtil.getString(Constants.PASS));
            login();
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        login.setText(R.string.loading);
        String emailStr = email.getText().toString();
        String passStr = pass.getText().toString();

        mAuth.signInWithEmailAndPassword(emailStr, passStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                login.setText(R.string.login);
                String msg = authResult.getUser().getEmail();
                Log.i("_res", msg);
                onLoged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show error message (dialog or toast)
                Log.i("_res", e.getMessage());
                login.setText(R.string.login);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoged(){
        preferencesUtil.putString(Constants.EMAIL, email.getText().toString());
        preferencesUtil.putString(Constants.PASS, pass.getText().toString());

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
