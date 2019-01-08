package com.jeanjnap.chat.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;

import com.jeanjnap.chat.R;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    Context context;

    public ImageUtil(Context context) {
        this.context = context;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String resizeBase64Image(String base64image){
        byte [] encodeByte=Base64.decode(base64image.getBytes(),Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 300 && image.getWidth() <= 300){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 300, 300, false);

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public static Bitmap base64ToBitmap(String base64image){
        byte[] decodedString = Base64.decode(base64image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    public Drawable getCircleDrawnable(Context context, Bitmap bitmap) {
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        dr.setCornerRadius(bitmap.getWidth());
        return  dr;
    }

    public Bitmap getBitmapFromDrawnable(int dranable) {
        return BitmapFactory.decodeResource(context.getResources(), dranable);
    }
}
