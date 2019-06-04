package edu.skku.everycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.Gravity;
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

    public void makeRow(){
        TableLayout tb = sup.findViewById(R.id.tbView);
        tr = new TableRow(sup.getContext());
        tr.setBackgroundColor(Color.parseColor("#ffe3de"));
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));

        makeLeft();
        makeRights();
        tb.addView(tr);
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
            tv[i].setId(i);
            tr.addView(tv[i]);
            setMargin(tv[i]);
        }
    }

    private TextView makeTextView(String sText){
        TextView textView = new TextView(sup.getContext());
        textView.setText(sText);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setPadding(0,0,0,0);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT));

        return textView;
    }

    private void setMargin(TextView trv){
        TableRow.LayoutParams lp = (TableRow.LayoutParams)trv.getLayoutParams();
        lp.setMargins(3,3,3,3);
    }

    public int[] getTBLocation(int week){
        int[] out = new int[2];
        int[] out2 = new int[2];
        tv[week].getLocationOnScreen(out);
        tr.getLocationOnScreen(out2);
        out[0] -= (out2[0] + 14);
        out[1] = (int)tr.getY() - 18;
        return out;
    }

    public int getTBWidth(int week){
        return tv[week].getWidth();
    }

    public int getTBHeight(int week){
        return tv[week].getHeight();
    }

}
