package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by junki on 2016-11-27.
 */

public class SendMessage_dbName_notes extends AsyncTask <String, Void, String> {
    ProgressDialog loading;
    FragmentManager fm;
    Fragment fragment;
    FragmentTransaction tr;
    int layout;
    Context context;

    public SendMessage_dbName_notes(FragmentManager fm, Fragment fragment, FragmentTransaction tr, int layout, ProgressDialog loading, Context context) {
        this.fm=fm;
        this.fragment=fragment;
        this.tr=tr;
        this.layout=layout;
        this.loading=loading;
        this.context=context;
    }

    // Background 작업 시작전에 UI 작업을 진행 한다.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //Background 작업이 끝난 후 UI 작업을 진행 한다.
    @Override
    protected void onPostExecute(String s) {
        loading.dismiss();
        Log.d("messageResult",s);
        if(!(s.equalsIgnoreCase("failure"))){
            Toast.makeText(context, "전송하였습니다. " + "", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "메세지를 전송하는데 실패했습니다.", Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(s);
    }

    //Background 작업을 진행 한다.
    protected String doInBackground(String... params) {
        try {
            String id_recv = (String) params[0];
            String id_send = (String) params[1];
            String msg_contents = (String) params[2];
            String msg_writeDate = (String) params[3];


            String link = "http://1.243.135.179:8080/get_notes.php/";
            String data = URLEncoder.encode("id_recv", "UTF-8") + "=" + URLEncoder.encode(id_recv, "UTF-8");
            data += "&" + URLEncoder.encode("id_send", "UTF-8") + "=" + URLEncoder.encode(id_send, "UTF-8");
            data += "&" + URLEncoder.encode("msg_contents", "UTF-8") + "=" + URLEncoder.encode(msg_contents, "UTF-8");
            data += "&" + URLEncoder.encode("msg_writeDate", "UTF-8") + "=" + URLEncoder.encode(msg_writeDate, "UTF-8");


            //data=data.replaceAll("<br />", "\n");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
