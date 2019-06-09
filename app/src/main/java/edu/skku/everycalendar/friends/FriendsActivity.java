package edu.skku.everycalendar.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.skku.everycalendar.R;

public class FriendsActivity extends AppCompatActivity {
    ImageButton back_btn;
    ImageButton option_btn;
    TextView name_text;
    FrameLayout table_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        back_btn = findViewById(R.id.back_btn);
        option_btn = findViewById(R.id.option_btn);
        name_text = findViewById(R.id.friend_name);
        table_container = findViewById(R.id.table_container);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
