package edu.skku.everycalendar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import javax.annotation.Nullable;

public class AlertService extends Service {

    class ServiceBinder extends Binder {
        AlertService getService(){
            return AlertService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
