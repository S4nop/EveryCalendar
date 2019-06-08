package edu.skku.everycalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdjustFragment extends Fragment {
    Button result_btn;
    Button reset_btn;

    MainActivity activity;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_adjust, container, false);
        inflater.inflate(R.layout.fragment_adjust, container, false);

        activity = (MainActivity) getActivity();
        context = activity.context;

        result_btn = rootView.findViewById(R.id.btn_result);
        reset_btn = rootView.findViewById(R.id.btn_reset);

        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdjustResultActivity.class);

                startActivity(intent);
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        JoinSchedule js = new JoinSchedule(9, 20);
        js.test();
        return rootView;
    }
}
