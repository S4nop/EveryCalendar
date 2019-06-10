package edu.skku.everycalendar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import edu.skku.everycalendar.R;

public class FriendsRecommActivity extends AppCompatActivity {
    ImageButton btn_back;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_recomm);

        btn_back = findViewById(R.id.btn_back);

        listView = findViewById(R.id.listView);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab spec 1");
        tabSpec1.setContent(R.id.content1);
        tabSpec1.setIndicator("recomm_tab");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab spec 2");
        tabSpec2.setContent(R.id.content2);
        tabSpec2.setIndicator("req_tab");
        tabHost.addTab(tabSpec2);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
