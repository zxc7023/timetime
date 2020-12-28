package com.example.junkikim.donate;


import android.app.ProgressDialog;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
 * Created by junkikim on 2016-10-11.
 */

//Mode_BuyTime_Frag2 에서 해당하는 id와 name를 실행하면 선택한 그 name를 가지고 해당하는 name를 가지고 있다고 쓴 게시물들이 다뜸
public class MyMenu_1 extends Fragment {

    private static final String TAG_RESULTS="result";
    private static final String TAG_BOARDNUMBER="boardNumber";
    private static final String TAG_ID="id";
    private static final String TAG_TITLE="title";
    private static final String TAG_DAYRESULT="dayResult";
    private static final String TAG_GENDER="gender";
    private static final String TAG_WRITEDATE="writeDate";
    private static final String TAG_TYPE="type";
    private static final String TAG_STARTHOUR="startHour";
    private static final String TAG_ENDHOUR="endHour";
    private static final String TAG_TALENT="talent";
    private static final String TAG_CONTENTS="contents";
    private static final String TAG_FILEPATH="filePath";

    String boardNumber;
    String id;
    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList;
    HashMap<String, String> writing;
    ListView listView;
    String myJSON;
    Mode_BuyTime_Frag2_list_detail modeBuyTimeFrag2_list_detail;
    Mode_SellTime_Frag2_list_detail modeSellTimeFrag2_list_detail;


    //Mode_BuyTime_Frag2 에서 해당하는 id와 name를 실행하면 선택한 그 name를 프래그먼트의 인자로 넘겨줘서 받음.
    public static MyMenu_1 newInstance(String id){
        MyMenu_1 Mymenu_1 = new MyMenu_1();
        Bundle args = new Bundle();
        args.putString("id",id);
        Mymenu_1.setArguments(args);
        return Mymenu_1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mymenu_listview,container,false); //카테고리 화면 인플레이터해줌
        if(container.getId()==R.id.maincontainer1){
            ActionBar actionBar;
            actionBar = ((Mode_SellTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  구하미관리");
        }
        else {

            ActionBar actionBar;
            actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  구하미관리");
        }


        listView=(ListView)root.findViewById(R.id.listView11);
        boardList = new ArrayList<HashMap<String, String>>();
        Bundle args =getArguments();
        if(args!=null){
            id=args.getString("id");
        }

        getId(id);


        //Toast.makeText(getActivity(),id+"", Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm=getFragmentManager();;
                FragmentTransaction tr;
                HashMap<String,String> tmp =(HashMap<String,String>)boardList.get(position);
                tr =fm.beginTransaction();
                if(container.getId()==R.id.maincontainer1){
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    modeSellTimeFrag2_list_detail = new Mode_SellTime_Frag2_list_detail(tmp);
                    tr.replace(R.id.maincontainer1, modeSellTimeFrag2_list_detail, "mymenu_1");
                }
                else {
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    modeSellTimeFrag2_list_detail = new Mode_SellTime_Frag2_list_detail(tmp);
                    tr.replace(R.id.maincontainer2, modeSellTimeFrag2_list_detail, "mymenu_1");
                }
                tr.addToBackStack(null);
                tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                tr.commit();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"오랜클릭 클릭되었습", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return root;
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            board=jsonObj.getJSONArray(TAG_RESULTS);
            //Toast.makeText(getActivity(),"여기까지 진입", Toast.LENGTH_LONG).show();

            for (int i = 0; i < board.length(); i++) {
                JSONObject c = board.getJSONObject(i);
                String boadNumber=c.getString(TAG_BOARDNUMBER);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String dayResult = c.getString(TAG_DAYRESULT);
                String gender = c.getString(TAG_GENDER);
                String writeDate = c.getString(TAG_WRITEDATE);
                String type =c.getString(TAG_TYPE);
                String startHour=c.getString(TAG_STARTHOUR);
                String endHour=c.getString(TAG_ENDHOUR);
                String category =c.getString("category");
                String talent=c.getString(TAG_TALENT);
                String contents = c.getString(TAG_CONTENTS);
                String filePath  = c.getString(TAG_FILEPATH);

                writing = new HashMap<String,String>();
                writing.put(TAG_BOARDNUMBER,boadNumber);
                writing.put(TAG_ID, id);
                writing.put(TAG_TITLE, title);
                writing.put(TAG_DAYRESULT,dayResult);
                writing.put(TAG_GENDER,gender);
                writing.put(TAG_WRITEDATE,writeDate);
                writing.put(TAG_TYPE,type);
                writing.put(TAG_STARTHOUR,startHour);
                writing.put(TAG_ENDHOUR,endHour);
                writing.put("category",category);
                writing.put(TAG_TALENT,talent);
                writing.put(TAG_CONTENTS, contents);
                writing.put(TAG_FILEPATH,filePath);
                boardList.add(writing);


            }
            ListAdapter adapter = new SimpleAdapter(getActivity(), boardList, R.layout.frag_buytime_2_board_list,
                    new String[]{TAG_BOARDNUMBER,TAG_ID, TAG_TITLE, TAG_WRITEDATE,TAG_CONTENTS},
                    new int[]{R.id.boardNumber,R.id.id, R.id.title, R.id.writeDate,R.id.contents}
            );

            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void getId(String id){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id = params[0];

                    String link ="http://1.243.135.179:8080/my_talent_buy_posting.php/";
                    String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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
                myJSON=result;
                //Toast.makeText(getActivity(),""+myJSON, Toast.LENGTH_LONG).show();

                showList();
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id);
    }
}
