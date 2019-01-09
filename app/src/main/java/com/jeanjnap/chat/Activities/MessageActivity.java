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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeanjnap.chat.Models.Contact;
import com.jeanjnap.chat.Models.Conversation;
import com.jeanjnap.chat.Models.Message;
import com.jeanjnap.chat.Models.User;
import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.Base64Util;
import com.jeanjnap.chat.Util.Constants;
import com.jeanjnap.chat.Util.PreferencesUtil;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    Base64Util base64Util = new Base64Util();
    PreferencesUtil preferencesUtil;

    private DatabaseReference mDatabase;

    Contact contact;

    EditText editTextMessage;
    Button send;

    String messageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        preferencesUtil = new PreferencesUtil(this);

        Intent i = getIntent();
        contact = (Contact) getIntent().getSerializableExtra("contact");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(contact.getNome());

        Log.i("_res", "Tanlking with: " + contact.getNome() + " - " + contact.getEmail());

        editTextMessage = findViewById(R.id.message);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageStr = editTextMessage.getText().toString();

                if(messageStr.length() > 0) {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        String userEmailB64 = base64Util.stringToBase64(preferencesUtil.getString(Constants.EMAIL));
        String contactEmailB64 = base64Util.stringToBase64(contact.getEmail());

        //Log.i("_res", "my email: " + preferencesUtil.getString(Constants.EMAIL));
        //Log.i("_res", "my emailB64: " + userEmailB64);

        Message message = new Message(messageStr, "e");

        mDatabase = FirebaseDatabase.getInstance().getReference(String.format("mensagens/%s/%s", userEmailB64, contactEmailB64));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("_res", String.format("%s sent \"%s\" to %s", preferencesUtil.getString(Constants.EMAIL), messageStr ,contact.getEmail()));
                //Log.i("_res", "Message sent. " + dataSnapshot.getKey());
                sendOuther();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.i("_res", "Message not sent. " + databaseError.getMessage() + ", details:: " + databaseError.getDetails());
                onError();
            }
        });

        // pushing user to 'users' node using the userId
        mDatabase.push().setValue(message);
    }

    private void sendOuther() {
        String userEmailB64 = base64Util.stringToBase64(preferencesUtil.getString(Constants.EMAIL));
        String contactEmailB64 = base64Util.stringToBase64(contact.getEmail());

        Message message = new Message(messageStr, "r");

        mDatabase = FirebaseDatabase.getInstance().getReference(String.format("mensagens/%s/%s", contactEmailB64, userEmailB64));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("_res", String.format("%s received \"%s\" from %s", contact.getEmail(), messageStr, preferencesUtil.getString(Constants.EMAIL)));
                //Log.i("_res", "Message sent " + dataSnapshot.getKey());
                updateConversation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.i("_res", "Message not sent " + databaseError.getMessage() + ", details:: " + databaseError.getDetails());
                onError();
            }
        });

        // pushing user to 'users' node using the userId
        mDatabase.push().setValue(message);
    }

    private void updateConversation() {
        String userEmailB64 = base64Util.stringToBase64(preferencesUtil.getString(Constants.EMAIL));
        String contactEmailB64 = base64Util.stringToBase64(contact.getEmail());
        String nomeUser = preferencesUtil.getString(Constants.NAME);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuario_conversas");

        Conversation conversation1 = new Conversation(contact.getNome(), contact.getEmail());
        Conversation conversation2 = new Conversation(nomeUser, preferencesUtil.getString(Constants.EMAIL));

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( userEmailB64 + "/" + contactEmailB64, conversation1);
        childUpdates.put( contactEmailB64 + "/" + userEmailB64, conversation2);

        mDatabase.updateChildren(childUpdates);
        onCreated();
    }

    private void onCreated() {
        editTextMessage.setText(null);
    }
    
    private void  onError() {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
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
