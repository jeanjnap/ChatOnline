package com.jeanjnap.chat.Util;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

    Context context;

    public Base64Util() {
    }

    public Base64Util(Context context) {
        this.context = context;
    }

    public String stringToBase64(String s){
        String base64 = null;
        // Sending side
        byte[] data = new byte[0];
        try {
            data = s.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        base64 = base64.substring(0, base64.length() - 1);

        return base64;
    }

    public String base64toString(String s) {
        String res = null;

        // Receiving side
        byte[] data = Base64.decode(s, Base64.DEFAULT);
        try {
            res = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return res;
    }
}
