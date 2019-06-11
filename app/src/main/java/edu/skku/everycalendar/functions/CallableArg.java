package edu.skku.everycalendar.functions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.Scopes;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.activities.MainActivity;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.googleCalendar.EventListAdapter;
import edu.skku.everycalendar.googleCalendar.EventListItem;
import edu.skku.everycalendar.googleCalendar.GoogleCalRequest;
import edu.skku.everycalendar.googleCalendar.GoogleCalTask;
import edu.skku.everycalendar.monthItems.MonthCalendar;

public abstract class CallableArg<T> implements Callable<Void> {
    public T arg;

    void setArg(T arg){
        this.arg = arg;
    }

    @Override
    public abstract Void call();

    public static class GoogleCalFragment extends Fragment {
        ImageButton btn_add;
        ImageButton btn_month;

        TextView week_text;

        ListView listView;

        MainActivity activity;
        Context context;

        String st_date;
        String ed_date;

        ArrayList<TimetableData> table;
        ArrayList<EventListItem> list;
        EventListAdapter adapter;

        GoogleCalRequest GCL;
        GoogleCalTask googleCalTask;
        GoogleAccountCredential mCred;
        private static final String[] SCOPES = {CalendarScopes.CALENDAR};

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

            activity = (MainActivity) getActivity();
            context = activity.mainContext;

            btn_add = rootView.findViewById(R.id.btn_add);
            btn_month = rootView.findViewById(R.id.month_btn);

            st_date = getCurSunday();
            ed_date = getCurSaturday();
            week_text = rootView.findViewById(R.id.week_text);
            week_text.setText(st_date+" ~ "+ed_date);

            listView = rootView.findViewById(R.id.listView);

            list = new ArrayList<>();
            adapter = new EventListAdapter(list,context);

            listView.setAdapter(adapter);

            GCL = new GoogleCalRequest(context, activity, "Account");
            mCred = GoogleAccountCredential.usingOAuth2(
                    context,
                    Arrays.asList(SCOPES)
            ).setBackOff(new ExponentialBackOff());
            googleCalTask = new GoogleCalTask(mCred);
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

                            //형식 변환된 date, time ( yyyy-mm-dd, hh:mm )
                            String form_st_date =st_year+"-"+st_month+"-"+st_date;
                            String form_ed_date =ed_year+"-"+ed_month+"-"+ed_date;

                            /*구글 캘린더에 넣을 때 date 형식이어야 하면 쓰세요
                            try {
                                Date mSt_date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(form_st_date);
                                Date mEd_date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(form_ed_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/

                            googleCalTask.setModeAdd(name,loca,desc,new DateTime(form_st_date + "T00:00:00.000+09:00"), new DateTime(form_ed_date + "T23:59:59.000+09:00") );
                            googleCalTask.addEvent();

                            EventListItem item = new EventListItem(name,form_st_date,form_ed_date,loca,desc);
                            list.add(item);
                            adapter.notifyDataSetChanged();
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

            btn_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MonthCalendar monthCalendar = new MonthCalendar(context,2);
                    monthCalendar.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
                    monthCalendar.show();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int index = position;
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayoutView = inflater.inflate(R.layout.dialog_event_info, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("일정 정보");
                    builder.setView(alertLayoutView);

                    TextView name_text = alertLayoutView.findViewById(R.id.name);
                    TextView st_date_text = alertLayoutView.findViewById(R.id.st_date);
                    TextView ed_date_text = alertLayoutView.findViewById(R.id.ed_date);
                    TextView loca_text = alertLayoutView.findViewById(R.id.loca);
                    TextView desc_text = alertLayoutView.findViewById(R.id.desc);

                    EventListItem item = list.get(position);

                    String name = item.getEvent_name();
                    String st_date = item.getEvent_st_date();
                    String ed_date = item.getEvent_ed_date();
                    String loca = item.getEvent_loca();
                    String desc = item.getEvent_desc();

                    name_text.setText(name);
                    st_date_text.setText(st_date);
                    ed_date_text.setText(ed_date);
                    loca_text.setText(loca);
                    desc_text.setText(desc);

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String calendarTitle = list.get(index).getEvent_name();

                            try{
                                googleCalTask.getmServ().calendars().delete(googleCalTask.getCalendarID(calendarTitle)).execute();
                            }catch(Exception e){
                                e.printStackTrace();
                                Log.d("Delete","100");
                            }
                            list.remove(index);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            });

            return rootView;
        }

        public void setWeek(String st_date, String ed_date){
            this.ed_date = ed_date;
            this.st_date = st_date;
            week_text.setText(st_date+" ~ "+ed_date);
            GCL.getCalendarData(new DateTime( st_date + "T00:00:00.000+09:00"), new DateTime(ed_date + "T23:59:59.000+09:00"));
            Log.d("Thread","0");
            while(!GCL.getFinished()) {
                try {
                    Log.d("Thread","1");
                    Thread.sleep(500);      // thread 계속돌아서 에러남
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Thread","2");
            try{
                table.addAll(GCL.getEvents());
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
            Log.d("Thread","3");
            list.clear();
            if(table!=null){
                for(int loop=0; loop<table.size(); loop++){
                    EventListItem item = new EventListItem(table.get(loop).getName(), st_date, ed_date, table.get(loop).getPlace(),table.get(loop).getDescript());
                    list.add(item);
                }
            }
            adapter.notifyDataSetChanged();
        }
        public static String getCurSaturday(){
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
            return formatter.format(c.getTime());
        }

        public static String getCurSunday(){
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            return formatter.format(c.getTime());
        }
    }
}
