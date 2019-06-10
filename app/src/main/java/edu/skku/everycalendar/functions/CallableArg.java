package edu.skku.everycalendar.functions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.concurrent.Callable;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.activities.MainActivity;
import edu.skku.everycalendar.everytime.AddFriendRequest;

public abstract class CallableArg<T> implements Callable<Void> {
    public T arg;

    void setArg(T arg){
        this.arg = arg;
    }

    @Override
    public abstract Void call();

    public static class GoogleCalFragment extends Fragment {
        ImageButton btn_add;
        ImageButton btn_delete;

        MainActivity activity;
        Context context;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

            activity = (MainActivity) getActivity();
            context = activity.mainContext;

            btn_add = rootView.findViewById(R.id.btn_add);
            btn_delete = rootView.findViewById(R.id.btn_delete);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayoutView = inflater.inflate(R.layout.dialog_add_event, null);

                    final EditText name_edit = alertLayoutView.findViewById(R.id.name_edit);
                    final EditText desc_edit = alertLayoutView.findViewById(R.id.desc_edit);
                    final EditText st_year_edit = alertLayoutView.findViewById(R.id.st_year);
                    final EditText st_month_edit = alertLayoutView.findViewById(R.id.st_month);
                    final EditText st_date_edit = alertLayoutView.findViewById(R.id.st_date);
                    final EditText ed_year_edit = alertLayoutView.findViewById(R.id.ed_year);
                    final EditText ed_month_edit = alertLayoutView.findViewById(R.id.ed_month);
                    final EditText ed_date_edit = alertLayoutView.findViewById(R.id.ed_date);
                    final EditText st_hour_edit = alertLayoutView.findViewById(R.id.st_hour);
                    final EditText st_min_edit = alertLayoutView.findViewById(R.id.st_min);
                    final EditText ed_hour_edit = alertLayoutView.findViewById(R.id.ed_hour);
                    final EditText ed_min_edit = alertLayoutView.findViewById(R.id.ed_min);
                    final EditText loca_edit = alertLayoutView.findViewById(R.id.loca_edit);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context); // mainContext 변경 (-Activity.this -> this)
                    builder.setTitle("일정 추가하기");
                    builder.setView(alertLayoutView);
                    builder.setCancelable(false); // 바깥 클릭해도 안꺼지게

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = name_edit.getText().toString();
                            String desc = desc_edit.getText().toString();
                            String loca = loca_edit.getText().toString();

                            String st_year = st_year_edit.getText().toString();
                            String st_month = st_month_edit.getText().toString();
                            String st_date = st_date_edit.getText().toString();
                            String st_hour = st_hour_edit.getText().toString();
                            String st_min = st_min_edit.getText().toString();

                            String ed_year = ed_year_edit.getText().toString();
                            String ed_month = ed_month_edit.getText().toString();
                            String ed_date = ed_date_edit.getText().toString();
                            String ed_hour = ed_hour_edit.getText().toString();
                            String ed_min = ed_min_edit.getText().toString();
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });

            return rootView;
        }
    }
}
