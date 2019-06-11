package edu.skku.everycalendar.functions;
import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Http_Request {
    private Map<String, List<String>> headers;
    private String result;
    public String date;
    public int request(String mthd, String _url, ContentValues prm, ContentValues _headers){
        HttpURLConnection con = null;
        StringBuffer sbPrm = new StringBuffer();

        if (prm == null)
            sbPrm.append("");
        else{
            boolean isAnd = false;
            String key, value;

            for(Map.Entry<String, Object> param : prm.valueSet()){
                key = param.getKey();
                value = param.getValue().toString();

                if(isAnd)
                    sbPrm.append("&");

                sbPrm.append(key).append("=").append(value);

                if(!isAnd)
                    if(prm.size() >= 2)
                        isAnd = true;
            }
        }

        try{
            URL url = new URL(_url);
            con = (HttpURLConnection)url.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod(mthd);
            //Log.d("Log", "here?");
            if(_headers != null) {
                for (Map.Entry<String, Object> hd : _headers.valueSet()) {
                    con.setRequestProperty(hd.getKey(), hd.getValue().toString());
                    //Log.d("HttpRequest_Log_Hd", hd.getKey() + "," + hd.getValue().toString());
                }
            }

            String strParams = sbPrm.toString();
            //Log.d("HttpRequest_Log_strPm", strParams);
            if(!strParams.equals("")) {
                OutputStream os = null;
                os = con.getOutputStream();
                os.write(strParams.getBytes("UTF-8"));
                os.flush();
                os.close();
            }

            //Log.d("Log", Integer.toString(con.getResponseCode()));
            if(con.getResponseCode() != 200 && con.getResponseCode() != 302) {

                result = null;
                //Log.d("HttpRequest_Log", "Return -1, Http not OK");
                return -1;
            }

            //Log.d("Log", "here?!!");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            //Log.d("Log", "here!!!");
            String line;
            String page = "";

            while((line = reader.readLine()) != null) page += line;
            headers = con.getHeaderFields();
            result = page;

            //Log.d("HttpRequest_Log", "Return 0");
            return 0;
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }
        //Log.d("HttpRequest_Log", "Return -1 end");
        result = null;
        return -1;
    }

    public Map<String, List<String>> getHeaders() {return headers;}
    public String getResult() {return result;}
}
