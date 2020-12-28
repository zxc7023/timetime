package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junki on 2016-11-30.
 */

public class readMessage_dbName_notes extends AsyncTask<String, Void, String> {

    private static final String TAG_SEND_MESSAGE_RESULTS="messageSendResult";
    private static final String TAG_SEND_MESSAGE_NUM="num";
    private static final String TAG_SEND_MESSAGE_ID_RECV="id_recv";
    private static final String TAG_SEND_MESSAGE_ID_SEND="id_send";
    private static final String TAG_SEND_MESSAGE_CONTENTS="contents";
    private static final String TAG_SEND_MESSAGE_DATE_SEND="date_send";
    private static final String TAG_SEND_MESSAGE_READ_RECV="read_recv";
    private static final String TAG_SEND_MESSAGE_DEL_RECV="del_recv";
    private static final String TAG_SEND_MESSAGE_DEL_SEND="del_send";


    ArrayList<HashMap<String, String>> messageArrayList;
    HashMap<String, String> messageHash;
    JSONArray mssageJsonArray = null;
    String myMessageJson;

    protected String doInBackground(String... params) {
        try {
            String id_recv = params[0];

            String link ="http://1.243.135.179:8080/read_notes_recv.php/";
            String data = URLEncoder.encode("id_recv","UTF-8")+"="+URLEncoder.encode(id_recv,"UTF-8");
            //data += "&" + URLEncoder.encode("talent", "UTF-8") + "=" + URLEncoder.encode(talent, "UTF-8");
            BufferedReader bufferedReader = null;

            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true); //Post방식으로 전송하겠다.
            con.setDoInput(true);
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
    ProgressDialog loading;
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        myMessageJson=result;
        //Toast.makeText(getActivity(),""+result, Toast.LENGTH_SHORT).show();
    }
    public ArrayList<HashMap<String,String>> getMessage_JsonArray(String myMessageJson) {
        JSONObject jsonObj = null;
        try {
            messageArrayList = new ArrayList<HashMap<String, String>>();
            jsonObj = new JSONObject(myMessageJson);
            mssageJsonArray = jsonObj.getJSONArray(TAG_SEND_MESSAGE_RESULTS);
            for (int i = 0; i < mssageJsonArray.length(); i++) {


                JSONObject c = mssageJsonArray.getJSONObject(i);
                String num = c.getString(TAG_SEND_MESSAGE_NUM);
                String id_recv = c.getString(TAG_SEND_MESSAGE_ID_RECV);
                String id_send = c.getString(TAG_SEND_MESSAGE_ID_SEND);
                String contents = c.getString(TAG_SEND_MESSAGE_CONTENTS);
                String date_send = c.getString(TAG_SEND_MESSAGE_DATE_SEND);
                String read_recv = c.getString(TAG_SEND_MESSAGE_READ_RECV);
                String del_recv = c.getString(TAG_SEND_MESSAGE_DEL_RECV);
                String del_send = c.getString(TAG_SEND_MESSAGE_DEL_SEND);


                messageHash = new HashMap<String, String>();
                messageHash.put(TAG_SEND_MESSAGE_NUM, num);
                messageHash.put(TAG_SEND_MESSAGE_ID_RECV, id_recv);
                messageHash.put(TAG_SEND_MESSAGE_ID_SEND, id_send);
                messageHash.put(TAG_SEND_MESSAGE_CONTENTS, contents);
                messageHash.put(TAG_SEND_MESSAGE_DATE_SEND, date_send);
                messageHash.put(TAG_SEND_MESSAGE_READ_RECV, read_recv);
                messageHash.put(TAG_SEND_MESSAGE_DEL_RECV, del_recv);
                messageHash.put(TAG_SEND_MESSAGE_DEL_SEND, del_send);

                messageArrayList.add(messageHash);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageArrayList;
    }
}
