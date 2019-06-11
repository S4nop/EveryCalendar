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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.activities.MainActivity;
import edu.skku.everycalendar.googleCalendar.EventListAdapter;
import edu.skku.everycalendar.googleCalendar.EventListItem;
import edu.skku.everycalendar.monthItems.MonthCalendar;

public abstract class CallableArg<T> implements Callable<Void> {
    public T arg;

    void setArg(T arg){
        this.arg = arg;
    }

    @Override
    public abstract Void call();

}
