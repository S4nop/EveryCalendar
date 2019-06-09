package edu.skku.everycalendar.functions;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    static Toast toast;

    public static void makeToast(Context context, String msg){
        try{
            toast.cancel();
        }
        catch(Exception e){}
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
