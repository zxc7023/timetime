package com.example.junkikim.donate;

/**
 * Created by junki on 2016-11-09.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

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
 * Created by Hana on 2016-11-07.
 */
public class MyMenu extends Fragment{




    Button button1,button2,button3,button4,button5,button6,button7;
    ; // 레이아웃을 xml이 아닌 직접 만들어 사용    //정보를 미리 입력하기 위해 쓰는 변수들
    JSONArray infoArray = null;
    ArrayList<HashMap<String, String>> infoList;
    HashMap<String, String> infoHash;
    HashMap<String, String> sendHash;
    String myBoardJson;
    ViewGroup containerId;



    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mymenu,container,false);
        containerId=container;
        //=getResources().getResourceEntryName(container.getId());
        //Log.d("container", String.valueOf(container.getId()));
        //Log.d("res", String.valueOf(R.id.maincontainer1));
        //Log.d("res2", String.valueOf(R.id.maincontainer2));
        button1=(Button)view.findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenu_1 mymenu_1=MyMenu_1.newInstance(LoginActivity.id);
                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.replace(R.id.maincontainer1, mymenu_1, "mymenu_1");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.replace(R.id.maincontainer2,mymenu_1 , "mymenu_1");
                }

                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();

            }

        });
        button2=(Button)view.findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenu_2 mymenu_2=MyMenu_2.newInstance(LoginActivity.id);
                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.replace(R.id.maincontainer1, mymenu_2, "mymenu_2");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.replace(R.id.maincontainer2,mymenu_2 , "mymenu_2");
                }
                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();

            }
        });
        button3=(Button)view.findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenu_3 mymenu_3=MyMenu_3.newInstance(LoginActivity.id);
                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.replace(R.id.maincontainer1, mymenu_3, "mymenu_3");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.replace(R.id.maincontainer2,mymenu_3 , "mymenu_3");
                }
                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();

            }
        });
        button4=(Button)view.findViewById(R.id.button4);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenu_4 mymenu_4=MyMenu_4.newInstance(LoginActivity.id);
                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.replace(R.id.maincontainer1, mymenu_4, "mymenu_4");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.replace(R.id.maincontainer2,mymenu_4 , "mymenu_4");
                }
                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();

            }
        });
        button5=(Button)view.findViewById(R.id.button5);
        button5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenu_5 mymenu_5=MyMenu_5.newInstance(LoginActivity.id);
                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    tr.replace(R.id.maincontainer1, mymenu_5, "mymenu_5");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    tr.replace(R.id.maincontainer2,mymenu_5 , "mymenu_5");
                }
                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();

            }
        });
        button6=(Button)view.findViewById(R.id.button6);
        button6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                readProfile(LoginActivity.id);

            }
        });





        button7=(Button)view.findViewById(R.id.button7);
        button7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        return view;
    }
    public void readProfile(String id){
        class GetJoinDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;
            @Override
            protected String doInBackground(String... params) {
                try {
                    String id = params[0];

                    String link ="http://1.243.135.179:8080/read_profile.php/";
                    String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                myBoardJson=result;
                showList();
                progressDialog.dismiss();
            }
        }
        GetJoinDataJSON g = new GetJoinDataJSON();
        g.execute(id);
    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myBoardJson);
            infoArray=jsonObj.getJSONArray("result");
            //Log.d("test1",myBoardJson);
            //Log.d("test2", String.valueOf(infoArray));

            for (int i = 0; i < infoArray.length(); i++) {
                JSONObject c = infoArray.getJSONObject(i);
                String id = c.getString("id");
                String password = c.getString("password");
                String name = c.getString("name");
                String phone = c.getString("phone");
                String birth = c.getString("birth");
                String gender = c.getString("gender");
                String major = c.getString("major");
                String subMajor = c.getString("subMajor");
                infoHash = new HashMap<String,String>();
                // Toast.makeText(getActivity(),""+id+writeDate+score+contents, Toast.LENGTH_SHORT).show();
                infoHash.put("id",id);
                infoHash.put("password", password);
                infoHash.put("name", name);
                infoHash.put("phone",phone);
                infoHash.put("birth",birth);
                infoHash.put("gender",gender);
                infoHash.put("major",major);
                infoHash.put("subMajor",subMajor);
                //Log.d("test3", infoHash.get("subMajor"));

                FragmentManager fm= getFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                //Log.d("test5",infoHash.get("name"));
                MyMenu_6 mymenu_6=new MyMenu_6(infoHash);

                if(containerId.getId() ==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    if(fragment==null) {
                        tr.remove(fragment);
                    }
                    tr.replace(R.id.maincontainer1, mymenu_6, "mymenu_6");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    if(fragment==null) {
                        tr.remove(fragment);
                    }
                    tr.replace(R.id.maincontainer2,mymenu_6 , "mymenu_6");
                }

                tr.commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}