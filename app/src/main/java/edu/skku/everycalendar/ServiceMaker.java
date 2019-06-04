package edu.skku.everycalendar;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceMaker {
    AlertService aServ;
    boolean isBind;

    ServiceConnection sconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlertService.ServiceBinder sv = (AlertService.ServiceBinder) iBinder;
            aServ = sv.getService();

            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            aServ = null;
            isBind = false;
        }
    };

}
