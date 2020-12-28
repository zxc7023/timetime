package com.example.junkikim.donate;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by junkikim on 2016-11-18.
 */

public class getTalent_Buy_posting_detail extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        try {
            String boardNumber = params[0];


            String link = "http://1.243.135.179:8080/get_talent_sale_posting_by_boardNumber.php/";
            String data = URLEncoder.encode("boardNumber", "UTF-8") + "=" + URLEncoder.encode(boardNumber, "UTF-8");

            BufferedReader bufferedReader = null;

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true); //Post방식으로 전송하겠다.
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(data);
            wr.flush();

            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return null;
        }
        
    }
    
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}

