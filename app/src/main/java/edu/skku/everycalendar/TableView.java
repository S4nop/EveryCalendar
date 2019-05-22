package edu.skku.everycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class TableView extends ConstraintLayout {

    String stHour, edHour;
    TableRow[] trs = new TableRow[24];
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
        for(int i = 0; i < 24; i++){
            TableRowView trv = new TableRowView(v, i);
            trs[i] = trv.makeRow();
        }
        addView(v);

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

    public void addSchedule(TimetableData event){

    }

}
