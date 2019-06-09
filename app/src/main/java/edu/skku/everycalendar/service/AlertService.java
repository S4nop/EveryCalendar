package edu.skku.everycalendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import edu.skku.everycalendar.functions.CallableArg;
import edu.skku.everycalendar.R;
import edu.skku.everycalendar.functions.RealTimeDBPull;

public class AlertService extends Service {
    private String id;
    private IBinder mBinder = new ServiceBinder();
    class ServiceBinder extends Binder {
        AlertService getService(){
            return AlertService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d("LOG_SERV", "onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("LOG_SERV", "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LOG_SERV", "onStart");
        id = intent.getStringExtra("ID");
        Log.d("LOG_SERV", "onStart_ID : " + id);
        chkRequest();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void chkRequest(){
        Log.d("LOG_SERV", "chkRequest called");
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin"),
            new CallArgFuncE(), null, true);
    }

    class CallArgFuncE extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                Log.d("LOG_SERV", "[" + arg + "] : [" + id + "]");
                if(id.equals(arg)){
                    Log.d("LOG_SERV", "Alert!");
                    Notification("EveryCalendar", "친구가 시간표 조율 요청을 보내왔습니다.", 23);

                }
            }catch(Exception e){}
            return null;
        }
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    private void SendMessage(String message){
//        Log.d("messageService", "Broadcasting message");
//        Intent intent = new Intent("DataReceiver");
//        intent.putExtra("RcvData", message);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
//
    private void Notification(String title, String body, int nid){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(AlertService.this)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Log.d("LOG_SERV", "NOTI");
        NotificationManager mNotManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotManager.notify(nid, nBuilder.build());
    }
}
