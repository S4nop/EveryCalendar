package edu.skku.everycalendar.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.everytime.LoginRequest;

import static android.os.SystemClock.sleep;

public class LoginActivity extends AppCompatActivity {

    String login_id, login_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        final EditText txtID = findViewById(R.id.txtID);
        final EditText txtPW = findViewById(R.id.txtPW);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        final Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);



        SharedPreferences loginPref = this.getPreferences(getApplicationContext().MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPref.edit();

        if(getIntent().hasExtra("Logout")){
            editor.clear();
            editor.commit();
        }
        login_id= loginPref.getString("login", null);
        if (login_id != null) {
            login_pw=loginPref.getString("pw",null);
            LoginRequest lr = new LoginRequest(login_id,login_pw);
            while(!lr.getFinished()){
                sleep(100);
            }
            if(lr.getLogined()){ // 조건문 내부를 true로 바꾸시면 로그인 없이 화면 전환됩니다.
                mainIntent.putExtra("Cookie", lr.getCookie().split("etsid=")[1].split(";")[0]);
                mainIntent.putExtra("ID", login_id);
                LoginActivity.this.startActivity(mainIntent);
                this.finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                //LoginActivity.this.startActivity(mainIntent);
                String user_id = txtID.getText().toString();
                String user_pw = txtPW.getText().toString();
                if(checkBox.isChecked()){
                    login_id=user_id;
                    login_pw=user_pw;
                    editor.putString("login",login_id);
                    editor.putString("pw",login_pw);
                    editor.commit();
                }
                else{
                    editor.putString("login",null);
                    editor.putString("pw",null);
                    editor.commit();
                }

                LoginRequest lr = new LoginRequest(user_id, user_pw);
                while(!lr.getFinished()){
                    sleep(100);
                }
                if(lr.getLogined()){ // 조건문 내부를 true로 바꾸시면 로그인 없이 화면 전환됩니다.
                    mainIntent.putExtra("Cookie", lr.getCookie().split("etsid=")[1].split(";")[0]);
                    Log.d("LOG_LOGINID", user_id);
                    mainIntent.putExtra("ID", user_id);
                    LoginActivity.this.startActivity(mainIntent);
                    LoginActivity.this.finish();

                }
                else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);

                    adb.setTitle("Error");

                    adb
                            .setMessage("로그인에 실패하였습니다")
                            .setPositiveButton("확인", null)
                            .setCancelable(false);
                    AlertDialog ad = adb.create();
                    ad.show();
                }

            }
        });
    }


}
