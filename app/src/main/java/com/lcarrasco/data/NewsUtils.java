package com.lcarrasco.data;

import android.net.Uri;

import com.lcarrasco.chihuahua_noticias.R;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lcarrasco on 7/19/16.
 */
public class NewsUtils {
    public static Uri getImageUri(String arrayImages){
        Uri uri = Uri.parse("res:///" + R.drawable.no_thumbnail);
        if (arrayImages != null) {
            String[] images = arrayImages
                    .replace("[", "").replace("]", "").split(",");
            if (images.length > 0 && !images[0].equals(""))
                uri = Uri.parse(images[0].replace(" ", "%20"));
        }

        return uri;
    }

    public static String getSource(String source) {
        Pattern regex = Pattern.compile("([\\da-z\\.-]+[a-z\\.]{2,6})\\/");
        Matcher simpleSource = regex.matcher(source);
        simpleSource.find();
        return  simpleSource.group(1);
    }

    public static String getDate(String createdAt) {
        DateFormat newFormat = new SimpleDateFormat("dd/MMM/yyyy");
        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = createdAt.split("[a-zA-Z]")[0];
        String newDateString = "";
        try {
            newDateString = newFormat.format(oldFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }
}
