package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by junkikim on 2016-12-04.
 */

public class GetSearchByText extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog loading;

    public GetSearchByText(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String keyword = params[0];
            String link= params[1];


            String data = URLEncoder.encode("keyword","UTF-8")+"="+URLEncoder.encode(keyword,"UTF-8");

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
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            return sb.toString().trim();

        }catch(Exception e){
            return null;
        }



    }
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context,"Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        loading.dismiss();
    }
}

