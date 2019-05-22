package edu.skku.everycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xml.sax.helpers.AttributesImpl;

public class TableRowView {
    View sup;
    int hour = 0;
    TableRow tr;
    TextView[] tv;

    TableRowView(View sup, int hour){
        this.sup = sup;
        this.hour = hour;
        tv = new TextView[7];
    }

    public TableRow makeRow(){
        TableLayout tb = sup.findViewById(R.id.tbView);
        tr = new TableRow(sup.getContext());
        tr.setBackgroundColor(Color.parseColor("#d6d6d6"));
        makeLeft();
        makeRights();
        tb.addView(tr);
        return tr;
    }

    private void makeLeft(){
        String sText = hour < 10 ? "0" + hour + ":00" : hour + ":00";
        TextView ltv = makeTextView(sText);
        tr.addView(ltv);
        setMargin(ltv);
    }

    private void makeRights(){
        for(int i = 0; i < 7; i++){
            tv[i] = makeTextView("");
            tr.addView(tv[i]);
            setMargin(tv[i]);
        }
    }

    private TextView makeTextView(String sText){
        TextView textView = new TextView(sup.getContext());
        textView.setText(sText);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(1);
        textView.setTextSize(18);
        return textView;
    }

    private void setMargin(TextView trv){
        TableRow.LayoutParams lp = (TableRow.LayoutParams)trv.getLayoutParams();
        lp.setMargins(3,3,3,3);
    }

    public int[] getTBLocation(int week){
        int[] out = new int[2];
        tv[week].getLocationOnScreen(out);
        return out;
    }

    public int getTBWidth(int week){
        return tv[week].getWidth();
    }

    public int getTBHeight(int week){
        return tv[week].getHeight();
    }
}
