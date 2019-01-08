package com.jeanjnap.chat.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeanjnap.chat.Models.User;
import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.Base64Util;
import com.jeanjnap.chat.Util.Constants;
import com.jeanjnap.chat.Util.PreferencesUtil;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, pass;
    Button register;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private PreferencesUtil preferencesUtil;
    private Base64Util base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.register);

        mDatabase = FirebaseDatabase.getInstance().getReference("contatos");

        base64 = new Base64Util(this);
        preferencesUtil = new PreferencesUtil(this);

        //Log.i("_res", base64.stringToBase64("teste"));
        //Log.i("_res", base64.base64toString("dGVzdGU="));

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    
    private void register() {
        register.setText(R.string.loading);

        final String emailStr = email.getText().toString();
        String passStr = pass.getText().toString();
        
        mAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    register.setText(R.string.register);
                    Log.i("_res", task.getException().getMessage());
                    // Show error message (dialog or toast)
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("_res", task.getResult().getUser().getEmail());
                    saveUser(emailStr);
                }
            }
        });
    }

    private void saveUser(String email) {
        String emailb64 = base64.stringToBase64(email);

        Log.i("_res", String.format("email [%s] em base b64: [%s]", email, emailb64));

        String nome = name.getText().toString();
        User user = new User(nome);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("_res", "Client saved. " + dataSnapshot.getKey());
                onCreated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("_res", "Client saved. " + databaseError.getMessage() + ", details:: " + databaseError.getDetails());
                onError();
            }
        });

        // pushing user to 'users' node using the userId
        mDatabase.child(emailb64).push().setValue(user);
        
    }
    
    private void onCreated(){
        register.setText(R.string.register);
        preferencesUtil.putString(Constants.EMAIL, email.getText().toString());
        preferencesUtil.putString(Constants.PASS, pass.getText().toString());

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void onError(){
        register.setText(R.string.register);
        Toast.makeText(this, R.string.creteNameError, Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
