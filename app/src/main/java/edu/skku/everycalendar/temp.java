package edu.skku.everycalendar;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static edu.skku.everycalendar.UserLogIn.getFirebaseDatabase;

@SuppressWarnings("unused")
class temp{
    private static DatabaseReference mPostReference;
    private static boolean isLoggedIn = false;
    private static String id;
    private static String password;
    private static String name;

    private temp() { }

    boolean isLoggedIn() {
        return isLoggedIn;
    }

    static boolean chkUser(String email, String password) {
        return true;
    }

    private static void postUser(String user_id, String user_pw){
        String user_name;

        //temp init
        user_name = "temp";

        Map<String, Object> user_update = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("id",user_id);
        user.put("name",user_name);
        user.put("password",user_pw);

        user_update.put("/user/"+user_id,user);
        mPostReference.updateChildren(user_update);

        getFirebaseDatabase();
    }

    static JSONObject getUser() {
        JSONObject user = null;
        try {
            user = new JSONObject().put("id", id)
                    .put("password", password)
                    .put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", "JSONException occurred in UserLoggedIn.java");
        }
        return user;
    }
}
