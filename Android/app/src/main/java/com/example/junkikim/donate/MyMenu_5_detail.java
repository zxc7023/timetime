package com.example.junkikim.donate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by junkikim on 2016-11-11.
 */

public class MyMenu_5_detail extends Fragment{

    HashMap<String, String> messageHash;
    AlertDialog.Builder alertDialog;
    TextView textDialog;

    String id_recv,id_send,msg_contents,date_send;

    TextView send_id,writeDate,contents;

    public MyMenu_5_detail(HashMap<String, String> tmp) {
        messageHash=tmp;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mymenu_message_receive,container,false);
        send_id = (TextView)root.findViewById(R.id.send_id);
        writeDate=(TextView)root.findViewById(R.id.writeDate);
        contents=(TextView)root.findViewById(R.id.contents);
        send_id.setText(messageHash.get("id_send"));
        writeDate.setText(messageHash.get("date_send"));
        contents.setText(messageHash.get("contents"));
        setReadRecv(messageHash.get("num"));
        return root;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.message).setVisible(true);
        menu.findItem(R.id.message).setTitle("답장하기");
        menu.findItem(R.id.delect).setVisible(true);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.message:
                alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertDialogView = inflater.inflate(R.layout.write_message, null);
                alertDialog.setView(alertDialogView);
                textDialog = (TextView) alertDialogView.findViewById(R.id.editText3);
                // textDialog.setText(questionMissing);
                alertDialog.setPositiveButton("전송하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        id_recv =messageHash.get("id_send");
                        id_send = LoginActivity.id;
                        msg_contents = textDialog.getText().toString();
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        date_send = sdfNow.format(date);
                        //Toast.makeText(getActivity(), ""+id_recv+id_send+msg_contents+msg_writeDate, Toast.LENGTH_LONG).show();

                        sendToMsg(id_recv,id_send,msg_contents,date_send);
                    }

                });
                alertDialog.show();
                break;
            case  R.id.delect:
                Toast.makeText(getActivity(), ""+messageHash.get("num")+messageHash.get("del_send"), Toast.LENGTH_LONG).show();

                delectMsg(messageHash.get("num"),messageHash.get("del_send"));

                break;

            default:
               super.onOptionsItemSelected(item);
        }

        return false;
        //return super.onOptionsItemSelected(item);
        }
    private void delectMsg(String num,String del_send) {

        class delectMessage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            // Background 작업 시작전에 UI 작업을 진행 한다.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            //Background 작업이 끝난 후 UI 작업을 진행 한다.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), " "+s , Toast.LENGTH_LONG).show();
                if (!(s.equalsIgnoreCase("failure"))) {
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();
                    fm.popBackStack();
                    tr.commit();
                }
                else{
                    Toast.makeText(getActivity(), "삭제되지 않았습니다..", Toast.LENGTH_LONG).show();
                }

            }

            //Background 작업을 진행 한다.
            protected String doInBackground(String... params) {
                try {
                    String num = (String) params[0];
                    String del_send = (String) params[1];

                    String link = "http://1.243.135.179:8080/del_notes_recv.php/";
                    String data = URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");
                    data += "&" + URLEncoder.encode("del_send", "UTF-8") + "=" + URLEncoder.encode(del_send, "UTF-8");


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
        delectMessage task = new delectMessage();
        task.execute(num,del_send);
    }

    private void setReadRecv(String num) {

        class upDateMssage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            // Background 작업 시작전에 UI 작업을 진행 한다.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            //Background 작업이 끝난 후 UI 작업을 진행 한다.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                }
                else{
                    Toast.makeText(getActivity(), "읽음처리가 되지 않았습니다..", Toast.LENGTH_LONG).show();
                }
            }

            //Background 작업을 진행 한다.
            protected String doInBackground(String... params) {
                try {
                    String num = (String) params[0];

                    String link = "http://1.243.135.179:8080/read_notes_recv_more.php/";
                    String data = URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");


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
        upDateMssage task = new upDateMssage();
        task.execute(num);
    }

    private void sendToMsg(String id_recv, String id_send, String msg_contents, String msg_writeDate) {

        class InsertMssage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            // Background 작업 시작전에 UI 작업을 진행 한다.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            //Background 작업이 끝난 후 UI 작업을 진행 한다.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Log.d("pushResult",s);
                if (!(s.equalsIgnoreCase("failure"))) {
                    alertDialog.setCancelable(false);
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();
                    fm.popBackStack();
                    tr.commit();
                    Toast.makeText(getActivity(), "전송하였습니다. " + "", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "메세지를 전송하는데 실패했습니다.", Toast.LENGTH_LONG).show();
                }
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
        InsertMssage task = new InsertMssage();
        task.execute(id_recv, id_send,msg_contents,msg_writeDate);
    }
}
