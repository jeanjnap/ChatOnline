package com.jeanjnap.chat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeanjnap.chat.Activities.MessageActivity;
import com.jeanjnap.chat.Models.Contact;
import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.ImageUtil;

import java.util.ArrayList;

public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    ArrayList<Contact> contacts;
    ImageUtil imageUtil;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        imageUtil = new ImageUtil(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Contact c = contacts.get(position);

        Bitmap bitmap = imageUtil.getBitmapFromDrawnable(R.drawable.profile);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        dr.setCornerRadius(bitmap.getWidth());
        holder.photo.setImageDrawable(dr);

        holder.name.setText(c.getNome());
        holder.email.setText(c.getEmail());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("_res", c.getNome());
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("contact", c);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView name, email;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            photo = itemView.findViewById(R.id.photo);
        }
    }
}