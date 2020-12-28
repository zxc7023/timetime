package com.example.junkikim.donate.communicateServerByPHP;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.example.junkikim.donate.ResizeBitmapImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by junkikim on 2016-12-02.
 */

public class InsertBoard_dbName_talent_buy_posting extends AsyncTask<String, Void, String> {

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    ProgressDialog dialog;
    Context context;

    FragmentManager fm;
    Fragment fragment;
    FragmentTransaction tr;
    int layout;
    Fragment myFragment;


    public InsertBoard_dbName_talent_buy_posting(FragmentManager fm, Fragment fragment, FragmentTransaction tr, Fragment myFragment, int layout, ProgressDialog dialog, Context context) {
        this.fm=fm;
        this.fragment=fragment;
        this.tr=tr;
        this.myFragment=myFragment;
        this.layout=layout;
        this.dialog=dialog;
        this.context=context;
    }


    // Background 작업 시작전에 UI 작업을 진행 한다.
    @Override
    protected void onPreExecute() {
        //context=Mode_BuyTime_MainActivity.mainActivtyContext;
        //loading = ProgressDialog.show(context, "Please Wait", "올리는 중입니다.", true, true);
        super.onPreExecute();

    }

    //Background 작업이 끝난 후 UI 작업을 진행 한다.
    @Override
    protected void onPostExecute(String s) {
        if(s.equals("success")){
            tr=fm.beginTransaction();
            tr.remove(fragment);
            tr.replace(layout,myFragment,"myFragment0");
            tr.commit();
        }
        else {

        }
        dialog.dismiss();
        Toast.makeText(context,"업로드 하였습니다.",Toast.LENGTH_LONG).show();
        super.onPostExecute(s);
    }

    //Background 작업을 진행 한다.
    @Override
    protected String doInBackground(String... params) {
        //email,password,name,phone,birth,gender
        try {
            String id = (String) params[0];
            String title = (String) params[1];
            String dayResult = (String) params[2];
            String writeDate = (String) params[3];
            String type = (String) params[4];
            String startHour = (String) params[5];
            String endHour = (String) params[6];
            String category = (String) params[7];
            String talent = (String) params[8];
            String contents = (String) params[9];
            String fileName = (String) params[10];
            String link = (String)params[11];


            URL url = new URL(link); //link를 url화
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();// url 연결한 객체를 생성
            conn.setDoOutput(true); // 쓰기모드 허용
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"id\"\r\n\r\n" + URLEncoder.encode(id, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n" + URLEncoder.encode(title, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"dayResult\"\r\n\r\n" + URLEncoder.encode(dayResult, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"writeDate\"\r\n\r\n" + URLEncoder.encode(writeDate, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"type\"\r\n\r\n" + URLEncoder.encode(type, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"startHour\"\r\n\r\n" + URLEncoder.encode(startHour, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"endHour\"\r\n\r\n" + URLEncoder.encode(endHour, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"category\"\r\n\r\n" + URLEncoder.encode(category, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"talent\"\r\n\r\n" + URLEncoder.encode(talent, "UTF-8"));
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"contents\"\r\n\r\n" + URLEncoder.encode(contents, "UTF-8"));
            if (fileName == null) {

            } else {
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");


                ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap src = BitmapFactory.decodeFile(fileName, options);
                ResizeBitmapImage re = new ResizeBitmapImage();
                Bitmap resized = re.resize(src, 2028);
                int height = resized.getHeight();
                int width = resized.getWidth();
                Log.d("widheig", width + "\t" + height);
                resized.compress(Bitmap.CompressFormat.JPEG, 100, outPutStream);
                byte[] byteArray = outPutStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
                int bytesAvailable = inputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];


                int bytesRead = inputStream.read(buffer, 0, bufferSize);// InputStream에서 bufferSize만큼을 읽어 byte buffer[]의 0(offset) 위치부터 저장하고 읽은 바이트 수를 반환한다.
                while (bytesRead > 0) {// Upload file part(s)
                    DataOutputStream dataWrite = new DataOutputStream(conn.getOutputStream());
                    dataWrite.write(buffer, 0, bufferSize);
                    bytesAvailable = inputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = inputStream.read(buffer, 0, bufferSize);
                }
                inputStream.close();

            }

            wr.writeBytes("\r\n--" + boundary + "--\r\n");
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
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());

        }
    }
}

