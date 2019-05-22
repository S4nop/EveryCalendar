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

import java.util.ArrayList;

public class TableView extends ConstraintLayout {

    String stHour, edHour;
    TableRowView[] trs = new TableRowView[24];
    ArrayList<Button> btnList = new ArrayList<>();
    public TableView(Context context) {
        super(context);
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

    private void init() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.tableview, this, false);
        //addView(v);
        for(int i = 9; i < 20; i++){
            TableRowView trv = new TableRowView(v, i);
            trv.makeRow();
            trs[i] = trv;
        }
        addView(v);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                addView(addSchedule(new TimetableData("테스트", "", "", "2", 144, 156)));
            }
        }, 500);


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

    public Button addSchedule(TimetableData event){
        String title = event.getName();
        String desc = event.getDescript();
        Integer week = Integer.parseInt(event.getWeekDay());
        Integer stTime = event.getStartTime();
        Integer edTime = event.getEndTime();
        TableRowView targTR = trs[stTime / 12];
        int pos[];
        int vWidth, vHeight;
        pos = targTR.getTBLocation(week);
        vWidth = targTR.getTBWidth(week);
        vHeight = targTR.getTBHeight(week);

        Button btnSched = new Button(getContext());

        btnSched.setText(title);
        btnSched.setTop(pos[0]);
        btnSched.setLeft(pos[1]);
        ConstraintLayout.LayoutParams btnLParam = new ConstraintLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        Log.d("LOGPARAM", Integer.toString(vHeight) + " " + vWidth);
        btnLParam.leftMargin = 0;
        btnLParam.rightMargin = 0;
        btnLParam.width = vWidth;
        btnLParam.height = (edTime - stTime) * vHeight / 12;
        btnSched.setLayoutParams(btnLParam);
        return btnSched;
    }

}
