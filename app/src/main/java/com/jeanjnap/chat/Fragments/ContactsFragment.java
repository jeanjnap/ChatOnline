package com.jeanjnap.chat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeanjnap.chat.Adapters.ContactsAdapter;
import com.jeanjnap.chat.Adapters.ConversationsAdapter;
import com.jeanjnap.chat.Models.Contact;
import com.jeanjnap.chat.Models.User;
import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.Base64Util;
import com.jeanjnap.chat.Util.Constants;
import com.jeanjnap.chat.Util.PreferencesUtil;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    Context context;
    Base64Util base64Util;
    PreferencesUtil preferencesUtil;

    private DatabaseReference mDatabase;

    View rootView;

    EditText ETemail;
    Button add;

    String email;
    String emailB64;

    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        context = getContext();
        base64Util = new Base64Util();
        preferencesUtil = new PreferencesUtil(context);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("_log", "onActivityCreated");

        ETemail = rootView.findViewById(R.id.email);
        add = rootView.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setText(R.string.loading);
                email = ETemail.getText().toString();
                //Log.i("_res", email);
                if(email.length() > 0){
                    boolean added = false;
                    //Log.i("_res", "qtd: " + contacts.size());
                    for (int i=0; i<contacts.size(); i++){
                        Contact c = contacts.get(i);
                        //Log.i("_res", String.format("[%s] == [%s]: %b", email, c.getEmail(), email.equals(c.getEmail())));
                        if(email.equals(c.getEmail())){
                            added = true;
                            //Log.i("_res", "already added");
                        }
                    }
                    if(!added) {
                        check();
                        //Log.i("_res", "add");
                    } else {
                        Toast.makeText(context, "The contact is already added", Toast.LENGTH_SHORT).show();
                        add.setText(R.string.add);
                    }
                    
                } else {
                    email404();
                }

            }
        });
        getContacts();
    }

    void getContacts () {
        emailB64 = base64Util.stringToBase64(preferencesUtil.getString(Constants.EMAIL));
        mDatabase = FirebaseDatabase.getInstance().getReference("usuario_contatos/" + emailB64);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Long res = dataSnapshot.getChildrenCount();

                if(res == 0){
                    email404();
                } else {
                    contacts = new ArrayList<>();
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        Contact contact = d.getValue(Contact.class);
                        Log.i("_res", "contact: " +contact.getNome());
                        contacts.add(contact);
                    }
                    initRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("_res", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(userListener);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.context,1);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        try {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            ContactsAdapter adapter = new ContactsAdapter(context, contacts);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("_log", "Error: " + e.getMessage());
        }
    }

    void check(){
        emailB64 = base64Util.stringToBase64(email);

        mDatabase = FirebaseDatabase.getInstance().getReference("contatos/" + emailB64);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Long res = dataSnapshot.getChildrenCount();

                if(res == 0){
                    email404();
                } else {
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        User user = d.getValue(User.class);
                        Log.i("_res", "U: " +user.getNome());
                        createContact(emailB64, user.getNome());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("_res", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(userListener);

    }

    void email404(){
        Toast.makeText(context, R.string.email404, Toast.LENGTH_SHORT).show();
        add.setText(R.string.add);
    }

    private void createContact(String emailB64, String name) {

        String myEmailB64 = base64Util.stringToBase64(preferencesUtil.getString(Constants.EMAIL));

        mDatabase = FirebaseDatabase.getInstance().getReference("usuario_contatos");

        Contact contact = new Contact(name, email);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("_res", "Contact saved. " + dataSnapshot.getKey());
                onCreated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("_res", "Contact not saved. " + databaseError.getMessage() + ", details:: " + databaseError.getDetails());
                onError();
            }
        });

        // pushing user to 'users' node using the userId
        mDatabase.child(myEmailB64).push().setValue(contact);
    }

    private void onCreated(){
        add.setText(R.string.add);
        ETemail.setText(null);
    }

    private void onError(){
        add.setText(R.string.add);
        Toast.makeText(context, R.string.creteNameError, Toast.LENGTH_SHORT).show();
    }
}
