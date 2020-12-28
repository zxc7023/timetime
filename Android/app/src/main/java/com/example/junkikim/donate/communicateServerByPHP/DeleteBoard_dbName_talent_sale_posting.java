package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * Created by junki on 2016-11-25.
 */

public class DeleteBoard_dbName_talent_sale_posting extends AsyncTask<String, Void, String>{
    FragmentManager fm;
    Fragment fragment;
    FragmentTransaction tr;
    int layout;
    Context context;
    ProgressDialog loading1;
    public DeleteBoard_dbName_talent_sale_posting(FragmentManager fm, Fragment fragment, FragmentTransaction tr, int layout, ProgressDialog loading1, Context context) {
        this.fm=fm;
        this.fragment=fragment;
        this.tr=tr;
        this.layout=layout;
        this.loading1=loading1;
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
        if (s.equals("success")){
            tr=fm.beginTransaction();
            tr.remove(fragment);
            fm.popBackStack();
            tr.commit();
        }
        loading1.dismiss();
        Toast.makeText(context, "삭제하였습니다. " + "", Toast.LENGTH_LONG).show();
        super.onPostExecute(s);
    }

    //Background 작업을 진행 한다.
    protected String doInBackground(String... params) {
        try {
            String boardNumber = (String) params[0];
            String link= (String)params[1];

            String data = URLEncoder.encode("boardNumber", "UTF-8") + "=" + URLEncoder.encode(boardNumber, "UTF-8");


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
