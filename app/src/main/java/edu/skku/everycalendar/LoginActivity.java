package edu.skku.everycalendar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.os.SystemClock.sleep;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);
        final EditText txtID = findViewById(R.id.txtID);
        final EditText txtPW = findViewById(R.id.txtPW);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest lr = new LoginRequest(txtID.getText().toString(), txtPW.getText().toString());
                while(!lr.getFinished()){
                    sleep(100);
                }
                if(lr.getLogined()){
                    //Make intent
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
