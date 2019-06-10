package edu.skku.everycalendar.table;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.functions.Utilities;

public class TableView extends ConstraintLayout {

    int stHour, edHour;
    TableRowView[] trs = new TableRowView[24];
    ArrayList<TimetableData> events;
    ArrayList<Button> btnList = new ArrayList<>();
    Integer stPos;
    Context context;
    Toast toast;
    public TableView(Context context, int stHour, int edHour) {
        super(context);
        this.stHour = stHour;
        this.edHour = edHour;
        this.context = context;
        init();
    }

    public TableView(Context context, ArrayList<TimetableData> events) {
        super(context);
        this.events = events;
        init();
    }

    public TableView(Context context, String stHour, String edHour, ArrayList<TimetableData> events){
        super(context);
        this.events = events;
        init();
    }

    public void setEvents(ArrayList<TimetableData> events){
        this.events = events;
    }

    private void init() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.tableview, this, false);
        stPos = stHour * 12;
        //addView(v);
        for(int i = stHour; i < edHour; i++){
            Log.d("LOG_TABLEINIT", "" + i + " : " + stHour + " : " + edHour);
            TableRowView trv = new TableRowView(v, i);
            trv.makeRow();
            trs[i] = trv;
        }

        addView(v);

        //addView(addSchedule(new TimetableData("테스트", "", "", "3", 120, 144, 1)));

    }

    public void addEvents(ArrayList<TimetableData> events){
        int pos[];
        Log.d("LOGEVENT", ""+ stPos);
        pos = trs[stPos / 12].getTBLocation(1);
        while(pos[0] == 0 && pos[1] == 0) {
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(events != null)
            for(TimetableData ttd : events){
                addView(addSchedule(ttd));
            }
    }


    private Button addSchedule(TimetableData event){
        Integer week = Integer.parseInt(event.getWeekDay());
        Integer stTime = event.getStartTime();
        Integer edTime = event.getEndTime();
        Log.d("LOG_STTIME", Integer.toString(stTime) + (trs[stTime / 12] == null ? "NULL" : "X") + (week == null ? "NULL" : week));
        TableRowView targTR = trs[stTime / 12];
        int pos[];
        int vWidth, vHeight;
        vWidth = targTR.getTBWidth(week);
        pos = targTR.getTBLocation(week);
        vHeight = targTR.getTBHeight(week);
        Log.d("LOG_ADDSCHED", "" + stTime + edTime+pos+vWidth+vHeight+event.getColor()+event);
        return setBtnClickListener(makeButton(stTime, edTime, pos, vWidth, vHeight, event.getColor()), event);
    }

    private Button makeButton(Integer stTime, Integer edTime, int pos[], int vWidth, int vHeight, int clr){
        Button btnSched = new Button(getContext());

        //btnSched.setText(title);
        btnSched.setY(pos[1] + vHeight * (stTime % 12) / 12 + 20);
        btnSched.setX(pos[0] + 14);
        btnSched.setBackgroundColor(clr);
        ConstraintLayout.LayoutParams btnLParam = new ConstraintLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        btnLParam.leftMargin = 0;
        btnLParam.rightMargin = 0;
        btnLParam.width = vWidth;
        btnLParam.height = (edTime - stTime) * vHeight / 12 + 6 * ((edTime - stTime) / 12 - 1);
        btnSched.setLayoutParams(btnLParam);

        return btnSched;
    }

    private Button setBtnClickListener(Button btn, final TimetableData event){
        if(!event.getName().equals(""))
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Utilities.makeToast(context, event.getName() + (event.getDescript() != null ? "\n" + event.getDescript() : "")
                            + (event.getPlace() != null ? "\n" + event.getPlace() : ""));
                }
            });
        return btn;
    }
}
