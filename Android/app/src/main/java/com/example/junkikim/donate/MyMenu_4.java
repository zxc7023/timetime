package com.example.junkikim.donate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junkikim on 2016-11-11.
 */
public class MyMenu_4 extends Fragment {

    private static final String TAG_SEND_MESSAGE_RESULTS="messageSendResult";
    private static final String TAG_SEND_MESSAGE_NUM="num";
    private static final String TAG_SEND_MESSAGE_ID_RECV="id_recv";
    private static final String TAG_SEND_MESSAGE_ID_SEND="id_send";
    private static final String TAG_SEND_MESSAGE_CONTENTS="contents";
    private static final String TAG_SEND_MESSAGE_DATE_SEND="date_send";
    private static final String TAG_SEND_MESSAGE_READ_RECV="read_recv";
    private static final String TAG_SEND_MESSAGE_DEL_RECV="del_recv";
    private static final String TAG_SEND_MESSAGE_DEL_SEND="del_send";

    String id; //id를 받아옴
    ListView listView; //각각의 List를 붙여줄 ListView;

    ArrayList<HashMap<String, String>> messageArrayList;
    HashMap<String, String> messageHash;
    JSONArray mssageJsonArray = null;
    String myMessageJson;

    AlertDialog.Builder alertBuilder;

    public static MyMenu_4 newInstance(String id){
        MyMenu_4 mymenu_4 = new MyMenu_4();
        Bundle args = new Bundle();
        args.putString("id",id);
        mymenu_4.setArguments(args);
        return mymenu_4;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mymenu_listview,container,false); //카테고리 화면 인플레이터해줌
        MyMenu myMenu=new MyMenu();

        if(container.getId()==R.id.maincontainer1){
            ActionBar actionBar;
            actionBar = ((Mode_SellTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  보낸쪽지함");
        }
        else {
            ActionBar actionBar;
            actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  보낸쪽지함");
        }

        messageArrayList  = new ArrayList<HashMap<String, String>>();
        listView=(ListView)root.findViewById(R.id.listView11);
        final Bundle args =getArguments();
        if(args!=null){
            id=args.getString("id");
        }
        getMessagePosting(id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("원하시는 기능을 선택하세요.");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
                adapter.add("삭제");
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (adapter.getItem(which)){
                            case "삭제":
                                HashMap<String,String> tmp = (HashMap<String, String>) messageArrayList.get(position);
                                String num=tmp.get("num");
                                String del_recv=tmp.get("del_recv");
                                delectMsg(num,del_recv);
                               // Toast.makeText(getActivity(), ""+num+'\n'+del_recv, Toast.LENGTH_LONG).show();
                                break;

                        }
                    }
                });
                alertBuilder.show();


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "오랜클릭 클릭되었습", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        return root;
    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myMessageJson);
            mssageJsonArray=jsonObj.getJSONArray(TAG_SEND_MESSAGE_RESULTS);
            for (int i = 0; i < mssageJsonArray.length(); i++) {
                JSONObject c = mssageJsonArray.getJSONObject(i);
                String num = c.getString(TAG_SEND_MESSAGE_NUM);
                String id_recv = c.getString(TAG_SEND_MESSAGE_ID_RECV);
                String id_send = c.getString(TAG_SEND_MESSAGE_ID_SEND);
                String contents = c.getString(TAG_SEND_MESSAGE_CONTENTS);
                String date_send = c.getString(TAG_SEND_MESSAGE_DATE_SEND);
                String read_recv= c.getString(TAG_SEND_MESSAGE_READ_RECV);
                String del_recv=c.getString(TAG_SEND_MESSAGE_DEL_RECV);
                String del_send=c.getString(TAG_SEND_MESSAGE_DEL_SEND);


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

            ListAdapter adapter = new SimpleAdapter(getActivity(), messageArrayList, R.layout.frag_mymenu_message_send,
                    new String[]{TAG_SEND_MESSAGE_ID_RECV, TAG_SEND_MESSAGE_CONTENTS, TAG_SEND_MESSAGE_DATE_SEND,TAG_SEND_MESSAGE_READ_RECV },
                    new int[]{R.id.receive_id,R.id.contents,R.id.writeDate,R.id.read_recv});
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getMessagePosting(String id_send){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id_send = params[0];

                    String link ="http://1.243.135.179:8080/read_notes_sent.php/";
                    String data = URLEncoder.encode("id_send","UTF-8")+"="+URLEncoder.encode(id_send,"UTF-8");
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
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                myMessageJson=result;
                //Toast.makeText(getActivity(),""+result, Toast.LENGTH_SHORT).show();
                showList();
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id_send);
    }

    private void delectMsg(String num,String del_recv) {

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

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();
                    MyMenu_4 myMenu_4 =MyMenu_4.newInstance(id);

                    if(((ViewGroup) getView().getParent()).getId()==R.id.maincontainer1){

                        Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                        tr.remove(fragment);
                        tr.replace(R.id.maincontainer1, myMenu_4, "myMenu_4");
                    }
                    else {
                        Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                        tr.remove(fragment);
                        tr.replace(R.id.maincontainer2,myMenu_4 , "myMenu_4");
                    }

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
                    String del_recv = (String) params[1];

                    String link = "http://1.243.135.179:8080/del_notes_send.php/";
                    String data = URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");
                    data += "&" + URLEncoder.encode("del_recv", "UTF-8") + "=" + URLEncoder.encode(del_recv, "UTF-8");


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
        task.execute(num,del_recv);
    }


}

