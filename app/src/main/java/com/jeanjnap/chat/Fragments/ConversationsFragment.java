package com.jeanjnap.chat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeanjnap.chat.Adapters.ConversationsAdapter;
import com.jeanjnap.chat.R;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment {

    Context context;
    ArrayList<String> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);
        this.context =  getActivity();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("_log", "onActivityCreated");
        
        getConversations();
    }

    private void getConversations() {
        for(int i=0; i<10; i++){
            posts.add("Post number: " + i);
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(this.context,1);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        try {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            ConversationsAdapter adapter = new ConversationsAdapter(context, posts);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("_log", "Error: " + e.getMessage());
        }
    }
}
