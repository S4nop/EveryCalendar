package edu.skku.everycalendar;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.os.SystemClock.sleep;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        final EditText txtID = findViewById(R.id.txtID);
        final EditText txtPW = findViewById(R.id.txtPW);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                //LoginActivity.this.startActivity(mainIntent);
                String user_id = txtID.getText().toString();
                String user_pw = txtPW.getText().toString();

                LoginRequest lr = new LoginRequest(user_id, user_pw);
                while(!lr.getFinished()){
                    sleep(100);
                }
                if(lr.getLogined()){ // 조건문 내부를 true로 바꾸시면 로그인 없이 화면 전환됩니다.
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.putExtra("Cookie", lr.getCookie().split("etsid=")[1].split(";")[0]);

                    if(temp.chkUser(user_id,user_pw))
                        LoginActivity.this.startActivity(mainIntent);
                    else Log.d("firebase_test","firebase_user auth error");
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
