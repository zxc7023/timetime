package com.example.junkikim.donate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.junkikim.donate.communicateServerByPHP.readMessage_dbName_notes;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by junkikim on 2016-11-11.
 */
public class MyMenu_5 extends Fragment {

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
    ArrayList<HashMap<String,String>> tmpHash;
    HashMap<String, String> messageHash;
    JSONArray mssageJsonArray = null;
    String myMessageJson;

    MyMenu_5_detail myMenu_5_detail;

    public static MyMenu_5 newInstance(String id){
        MyMenu_5 mymenu_5 = new MyMenu_5();
        Bundle args = new Bundle();
        args.putString("id",id);
        //.Log.d("ttttt",id);
        mymenu_5.setArguments(args);
        return mymenu_5;
    }
    public static MyMenu_5 newInstance(String id,String request){
        MyMenu_5 mymenu_5 = new MyMenu_5();
        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("request",request);
        //.Log.d("ttttt",id);
        mymenu_5.setArguments(args);
        return mymenu_5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mymenu_listview,container,false); //카테고리 화면 인플레이터해줌
        if(container.getId()==R.id.maincontainer1){
            ActionBar actionBar;
            actionBar = ((Mode_SellTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  받은쪽지함");
        }
        else {
            ActionBar actionBar;
            actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  받은쪽지함");
        }


        messageArrayList  = new ArrayList<HashMap<String, String>>();
        listView=(ListView)root.findViewById(R.id.listView11);
        Bundle args =getArguments();
        if(args!=null){
            id=args.getString("id");
            Log.d("ttttt2",id);
        }
        String result = null;
        ProgressDialog loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        readMessage_dbName_notes read = new readMessage_dbName_notes();
        try {
            result = read.execute(id).get();
            Log.d("result",result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        tmpHash = read.getMessage_JsonArray(result);
        ListAdapter adapter = new SimpleAdapter(getActivity(), tmpHash, R.layout.frag_mymenu_message_receive_preview,
                new String[]{TAG_SEND_MESSAGE_ID_SEND, TAG_SEND_MESSAGE_DATE_SEND},
                new int[]{R.id.id,R.id.writeDate});
        listView.setAdapter(adapter);

        //getMessagePosting(id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tr;
                HashMap<String, String> tmp = (HashMap<String, String>) tmpHash.get(position);
                //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                myMenu_5_detail = new MyMenu_5_detail(tmp);
                tr = fm.beginTransaction();

                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, myMenu_5_detail, "myMenu_5_detail");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer2,myMenu_5_detail , "myMenu_5_detail");
                }

                tr.addToBackStack(null);
                tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                tr.commit();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "오랜클릭 클릭되었습", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        loading.dismiss();
        return root;

    }

    @Override
    public void onResume() {
        Bundle args =getArguments();
        if(args!=null) {
            if (args.getString("request") != null) {
                Log.d("request_args", args.getString("request"));
                if (args.getString("request").equals("ok")) {
                    HashMap<String, String> sendHash = (HashMap<String, String>) tmpHash.get(tmpHash.size() - 1);
                    myMenu_5_detail = new MyMenu_5_detail(sendHash);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tr;
                    Fragment fragment;
                    if (((ViewGroup) getView().getParent()).getId() == R.id.maincontainer1) {
                        fragment = fm.findFragmentById(R.id.maincontainer1);
                        tr = fm.beginTransaction();
                        tr.remove(fragment);
                        tr.addToBackStack(null);
                        tr.replace(R.id.maincontainer1, myMenu_5_detail, "myMenu_5_detetail");
                        tr.commit();
                    } else {
                        fragment = fm.findFragmentById(R.id.maincontainer2);
                        tr = fm.beginTransaction();
                        tr.remove(fragment);
                        tr.addToBackStack(null);
                        tr.replace(R.id.maincontainer2, myMenu_5_detail, "myMenu_5_detetail");
                        tr.commit();
                    }
                    args.putString("request", "checked");
                    Log.d("request_args", args.getString("request"));
                }
            }
        }
        super.onResume();
    }

    class getMessagePostion extends AsyncTask<String, Void, String> {

        @Override
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
            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            myMessageJson=result;
            //Toast.makeText(getActivity(),""+result, Toast.LENGTH_SHORT).show();
            //showList();
            loading.dismiss();
        }
    }
}

