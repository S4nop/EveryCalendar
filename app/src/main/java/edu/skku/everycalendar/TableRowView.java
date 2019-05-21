package edu.skku.everycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.helpers.AttributesImpl;

public class TableRowView extends ConstraintLayout {
    String hour;
    TextView tTime, tSun, tMon, tTue, tWed, tThu, tFri, tSat;

    public TableRowView(Context context) {
        super(context);
        init();
    }

    public TableRowView(Context context, AttributeSet attrs) {
        super(context);
        init();
        getAttrs(attrs);
    }

    public TableRowView(Context context, AttributeSet attrs, int dStyle) {
        super(context);
        init();
        getAttrs(attrs, dStyle);
    }

    private void init() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.tablerow, this, false);
        addView(v);

        tTime = findViewById(R.id.tTime);
        tSun = findViewById(R.id.tSun);
        tMon = findViewById(R.id.tMon);
        tTue = findViewById(R.id.tTue);
        tWed = findViewById(R.id.tWed);
        tThu = findViewById(R.id.tThu);
        tFri = findViewById(R.id.tFri);
        tSat = findViewById(R.id.tSat);
    }

    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableRow);

        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int dStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableRow, dStyle, 0);

        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray attrs){
        hour = attrs.getString(R.styleable.TableRow_hour);
        tTime.setText(hour.length() > 1 ? hour + ":00" : "0" + hour + ":00");

        attrs.recycle();
    }

}
