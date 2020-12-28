package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class PostReview_dbName_notes extends AsyncTask<String, Void, String> {
    ProgressDialog loading;

    // Background 작업 시작전에 UI 작업을 진행 한다.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //Background 작업이 끝난 후 UI 작업을 진행 한다.
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    //Background 작업을 진행 한다.
    protected String doInBackground(String... params) {
        try {
            String id = (String) params[0];
            String writeDate = (String) params[1];
            String score = (String) params[2];
            String contents = (String) params[3];
            String boardNumber = (String) params[4];
            String link = (String) params[5];


            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("writeDate", "UTF-8") + "=" + URLEncoder.encode(writeDate, "UTF-8");
            data += "&" + URLEncoder.encode("score", "UTF-8") + "=" + URLEncoder.encode(score, "UTF-8");
            data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(contents, "UTF-8");
            data += "&" + URLEncoder.encode("boardNumber", "UTF-8") + "=" + URLEncoder.encode(boardNumber, "UTF-8");


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
