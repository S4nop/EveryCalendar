package edu.skku.everycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TableView extends ConstraintLayout {

    String stHour, edHour;
    TableRowView[] trs = new TableRowView[24];
    ArrayList<TimetableData> events;
    ArrayList<Button> btnList = new ArrayList<>();
    Integer stTime;
    Context context;
    Toast toast;
    public TableView(Context context) {
        super(context);
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
        this.stHour = stHour;
        this.edHour = edHour;
        this.events = events;
        init();
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context);
        init();
        getAttrs(attrs);
    }

    public TableView(Context context, AttributeSet attrs, int dStyle) {
        super(context);
        init();
        getAttrs(attrs, dStyle);
    }

    public void setEvents(ArrayList<TimetableData> events){
        this.events = events;
    }

    private void init() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.tableview, this, false);
        stTime = 9 * 12;
        //addView(v);
        for(int i = 8; i < 20; i++){
            TableRowView trv = new TableRowView(v, i);
            trv.makeRow();
            trs[i] = trv;
        }

        addView(v);

        addView(addSchedule(new TimetableData("테스트", "", "", "3", 120, 144, 1)));

    }

    public void addEvents(ArrayList<TimetableData> events){
        int pos[];
        pos = trs[stTime / 12].getTBLocation(1);
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

    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableView);

        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int dStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableView, dStyle, 0);

        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray attrs){
        stHour = attrs.getString(R.styleable.TableView_stHour);
        edHour = attrs.getString(R.styleable.TableView_edHour);


        attrs.recycle();
    }

    private Button addSchedule(TimetableData event){
        Integer week = Integer.parseInt(event.getWeekDay());
        Integer stTime = event.getStartTime();
        Integer edTime = event.getEndTime();
        Log.d("LOG_STTIME", Integer.toString(stTime));
        TableRowView targTR = trs[stTime / 12];
        int pos[];
        int vWidth, vHeight;
        pos = targTR.getTBLocation(week);
        vWidth = targTR.getTBWidth(week);
        vHeight = targTR.getTBHeight(week);

        return setBtnClickListener(makeButton(stTime, edTime, pos, vWidth, vHeight, event.getIdNum()), event);
    }

    private Button makeButton(Integer stTime, Integer edTime, int pos[], int vWidth, int vHeight, int clr){
        Button btnSched = new Button(getContext());

        //btnSched.setText(title);
        btnSched.setY(pos[1] + vHeight * (stTime % 12) / 12 + 18);
        btnSched.setX(pos[0] + 12);
        btnSched.setBackgroundColor(clr);
        ConstraintLayout.LayoutParams btnLParam = new ConstraintLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        btnLParam.leftMargin = 0;
        btnLParam.rightMargin = 0;
        btnLParam.width = vWidth + 2;
        btnLParam.height = (edTime - stTime) * vHeight / 12  + 40 + 6 * ((edTime - stTime) / 12 - 1) - 30;
        btnSched.setLayoutParams(btnLParam);

        return btnSched;
    }

    private Button setBtnClickListener(Button btn, final TimetableData event){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ToastMaker.makeToast(context, String.format("[ %s ]\n%s\n%s", event.getName(), event.getDescript(), event.getPlace()));
            }
        });
        return btn;
    }
}
