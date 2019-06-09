package edu.skku.everycalendar.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class ServiceMaker extends AppCompatActivity {
    AlertService aServ;
    boolean isBind = false;
    Context context;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ServiceConnection sconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlertService.ServiceBinder sv = (AlertService.ServiceBinder) iBinder;
            Log.d("LOG_ONSERVCONN", "HERE");
            aServ = sv.getService();

            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            aServ = null;
            isBind = false;
        }
    };

    public void setActivity(Context cont, String id){
        context = cont;
        this.id = id;
    }

    public void startServ(){
        Intent srv = new Intent(context, AlertService.class);
        Log.d("LOG_SERVMAKER", id);
        srv.putExtra("ID", id);
        context.startService(srv);
    }

    public void stopServ(){
        context.stopService(new Intent(context, AlertService.class));
    }

    public void bindServ(){
        if(!isBind) {
            Log.d("LOG_ISBIND", "false");
            context.bindService(new Intent(context, AlertService.class), sconn, BIND_AUTO_CREATE);
        }
    }

    public void unbindServ(){
        if(isBind)
            context.unbindService(sconn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindServ();
        stopServ();
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
