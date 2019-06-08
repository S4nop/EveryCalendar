package edu.skku.everycalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import javax.annotation.Nullable;

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
        RealTimeDBPull.getDatatListFromDB(FirebaseDatabase.getInstance().getReference().child("SchedJoin").child("rshtiger"),
                new CallArgFuncE(), null, true);
    }

    class CallArgFuncE extends CallableArg<String>{
        @Override
        public Void call() {
            try{
                //TODO : Use class??
                String msg = arg;
                Log.d("LOG_MSG", msg);
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
//    private void HandleData(String data){
//        String[] datas = data.split("\"rowid\"");
//        Log.d("MainActivity_Log_HData", data.substring(11));
//        String lecture, tio;
//        for(int i = 1; i < datas.length; i++){
//            String dt = datas[i];
//
//            lecture = dt.split("\"")[15];
//            tio = dt.split("\"tot_dhw\":\"")[1].split("\"")[0]; // "tot":"   or    "jagwa3":"
//            if(!tio.split(" / ")[0].equals(tio.split(" / ")[1])) {
//                Notification("SugangAlarmy", lecture + " 의 자리가 났습니다!", i);
//            }
//        }
//    }
//
//    private void Notification(String title, String body, int nid){
//        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(SearchService.this)
//                .setSmallIcon(R.drawable.background)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
//
//        NotificationManager mNotManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        mNotManager.notify(nid, nBuilder.build());
//    }
}
