package com.example.firebasepractise.Util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile(
                    "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +       //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
            );

    public static List<String> roles = new ArrayList<>(Arrays.asList("PlannerRole", "AdminRole", "UserRole"));
    public static List<String> parentCategory = new ArrayList<>(Arrays.asList("Travel", "Catering", "Decor", "DJ"));
    public static List<String> travelItems = new ArrayList<>(Arrays.asList("Affordable", "economical", "luxury"));
    public static List<String> cateringItems = new ArrayList<>(Arrays.asList("Desert", "Dinner", "Break fast"));
    public static List<String> decorItems = new ArrayList<>(Arrays.asList("Concert", "Birthday", "Marriage Ceremony"));
    public static List<String> djItems = new ArrayList<>(Arrays.asList("Aslam Song Composer", "Hannan Song Writer", "Kaifi Song Marketer"));

    public static void StatusBarColour(Activity activity, int id) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, id));
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

}
