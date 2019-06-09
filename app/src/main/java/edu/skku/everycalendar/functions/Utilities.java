package edu.skku.everycalendar.functions;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class Utilities {
    static Toast toast;
    static Context context;

    public static void setContext(Context c){
        context = c;
    }

    public static void makeToast(Context context, String msg){
        try{
            toast.cancel();
        }
        catch(Exception e){}
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void makeToast(String msg){
        try{
            toast.cancel();
        }
        catch(Exception e){}
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
