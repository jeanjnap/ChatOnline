package com.jeanjnap.chat.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeanjnap.chat.R;
import com.jeanjnap.chat.Util.ImageUtil;

import java.util.ArrayList;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

    Context context;
    ArrayList<String> posts;
    ImageUtil imageUtil;

    public ConversationsAdapter(Context context, ArrayList<String> posts) {
        this.context = context;
        this.posts = posts;
        imageUtil = new ImageUtil(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String s = posts.get(position);

        Bitmap bitmap = imageUtil.getBitmapFromDrawnable(R.drawable.profile);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        dr.setCornerRadius(bitmap.getWidth());
        holder.photo.setImageDrawable(dr);

        holder.name.setText(s);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("_res", s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView name, message, hour;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            hour = itemView.findViewById(R.id.hour);
            photo = itemView.findViewById(R.id.photo);
        }
    }
}
