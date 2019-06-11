package edu.skku.everycalendar.functions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CaptureScreen {
    public static void capture(Activity act, String fileName){
        View rootView = act.getWindow().getDecorView();

        File screenShot = ScreenShot(rootView, fileName);
        if(screenShot!=null){
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
            Utilities.makeToast("스크린샷이 갤러리에 저장되었습니다");
        }
    }

    private static File ScreenShot(View view, String fileName){
        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache();

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EveryCalendar";
        makeDir(sdPath);
        File file = new File(sdPath, fileName + ".png");
        FileOutputStream os;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    private static void makeDir(String dir){
        File file = new File(dir);
        if(!file.exists())
            file.mkdirs();
    }
}
