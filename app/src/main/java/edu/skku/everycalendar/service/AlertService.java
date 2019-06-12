package edu.skku.everycalendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import edu.skku.everycalendar.activities.LoginActivity;
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
        //Log.d("LOG_SERV", "onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.d("LOG_SERV", "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("LOG_SERV", "onStart");
        try {
            id = intent.getStringExtra("ID");
        } catch(Exception e){ stopSelf(startId);}
        if(id == null) stopSelf(startId);
        else chkRequest();

        //Log.d("LOG_SERV", "onStart_ID : " + id);
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
        //Log.d("LOG_SERV", "chkRequest called");
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoinReq").child(id),
            new CallArgFuncE(), null, false);
    }

    class CallArgFuncE extends CallableArg<String> {
        @Override
        public Void call() {
            try{
                //Log.d("LOG_SERV", "[" + arg + "]");
                Notification("EveryCalendar", "친구가 시간표 조율 요청을 보내왔습니다.", 23);
                sendMessage(arg);
//                if(id.equals(arg)){
//                }
            }catch(Exception e){}
            return null;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    private void sendMessage(String message){
        //Log.d("messageService", "Broadcasting message");
        Intent intent = new Intent("DataReceiver");
        intent.putExtra("RcvData", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void Notification(String title, String body, int nid){
        PendingIntent mPendingIntent = PendingIntent.getActivity(AlertService.this, 0, new
                        Intent(getApplicationContext(), LoginActivity.class).setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(AlertService.this)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(mPendingIntent);


        //Log.d("LOG_SERV", "NOTI");
        NotificationManager mNotManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotManager.notify(nid, nBuilder.build());
    }
}
