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

//Mode_BuyTime_Frag2 에서 해당하는 category와 talent를 실행하면 선택한 그 talent를 가지고 해당하는 talent를 가지고 있다고 쓴 게시물들이 다뜸
public class Mode_BuyTime_Frag2_list extends Fragment {

    private static final String TAG_RESULTS="result";
    private static final String TAG_BOARDNUMBER="boardNumber";
    private static final String TAG_ID="id";
    private static final String TAG_TITLE="title";
    private static final String TAG_DAYRESULT="dayResult";
    private static final String TAG_GENDER="gender";
    private static final String TAG_WRITEDATE="writeDate";
    private static final String TAG_TYPE="type";
    private static final String TAG_MAJOR="major";
    private static final String TAG_SUBMAJOR="subMajor";
    private static final String TAG_STARTHOUR="startHour";
    private static final String TAG_ENDHOUR="endHour";
    private static final String TAG_CATEGORY="category";
    private static final String TAG_TALENT="talent";
    private static final String TAG_CONTENTS="contents";
    private static final String TAG_FILEPATH="filePath";

    String category,talent;
    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList;
    HashMap<String, String> writing;
    ListView listView;
    String myJSON;
    Mode_BuyTime_Frag2_list_detail modeBuyTimeFrag2_list_detail;


    ActionBar actionBar;
    //Mode_BuyTime_Frag2 에서 해당하는 category와 talent를 실행하면 선택한 그 talent를 프래그먼트의 인자로 넘겨줘서 받음.
    public static Mode_BuyTime_Frag2_list newInstance(String category, String talent){
        Mode_BuyTime_Frag2_list modeBuyTimeFrag2_list = new Mode_BuyTime_Frag2_list();
        Bundle args = new Bundle();
        args.putString("category",category);
        args.putString("talent",talent);
        modeBuyTimeFrag2_list.setArguments(args);
        return modeBuyTimeFrag2_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.frag_buytime_2_board, container, false); //카테고리 화면 인플레이터해줌

        listView = (ListView) root.findViewById(R.id.listView);
        boardList = new ArrayList<HashMap<String, String>>();
        Bundle args = getArguments();
        if (args != null) {
            category = args.getString("category");
            talent = args.getString("talent");
        }
        actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();

        getMediaData(category, talent);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                    FragmentTransaction tr;
                    HashMap<String, String> tmp = (HashMap<String, String>) boardList.get(position);
                    //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                    modeBuyTimeFrag2_list_detail = new Mode_BuyTime_Frag2_list_detail(tmp);
                    //tr.hide(fragment);
                    tr = fm.beginTransaction();
                    tr.replace(R.id.maincontainer2, modeBuyTimeFrag2_list_detail, "modeBuyTimeFrag2_list_detail");
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
            return root;
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            board=jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < board.length(); i++) {
                JSONObject c = board.getJSONObject(i);
                String boadNumber=c.getString(TAG_BOARDNUMBER);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String dayResult = c.getString(TAG_DAYRESULT);
                String gender = c.getString(TAG_GENDER);
                String writeDate = c.getString(TAG_WRITEDATE);
                String type =c.getString(TAG_TYPE);
                String major=c.getString(TAG_MAJOR);
                String subMajor=c.getString(TAG_SUBMAJOR);
                String startHour=c.getString(TAG_STARTHOUR);
                String endHour=c.getString(TAG_ENDHOUR);
                String category=c.getString(TAG_CATEGORY);
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
                writing.put(TAG_MAJOR,major);
                writing.put(TAG_SUBMAJOR,subMajor);
                writing.put(TAG_STARTHOUR,startHour);
                writing.put(TAG_ENDHOUR,endHour);
                writing.put(TAG_CATEGORY,category);
                writing.put(TAG_TALENT,talent);
                writing.put(TAG_CONTENTS, contents);
                writing.put(TAG_FILEPATH,filePath);
                boardList.add(writing);
                //Toast.makeText(getActivity(),""+boadNumber+id+title+writeDate+type+major+subMajor+startHour+endHour+talent+contents, Toast.LENGTH_SHORT).show();
            }
                ListAdapter adapter = new SimpleAdapter(getActivity(), boardList, R.layout.frag_buytime_2_board_list,
                        new String[]{TAG_BOARDNUMBER,TAG_ID, TAG_TITLE, TAG_WRITEDATE,TAG_CONTENTS},
                        new int[]{R.id.boardNumber,R.id.id, R.id.title, R.id.writeDate,R.id.contents});
                        listView.setAdapter(adapter);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getMediaData(final String category, final String talent){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String category = params[0];
                    String talent = params[1];

                    String link ="http://1.243.135.179:8080/get_talent_sale_posting.php/";
                    String data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category,"UTF-8");
                    data += "&" + URLEncoder.encode("talent", "UTF-8") + "=" + URLEncoder.encode(talent, "UTF-8");
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
            ProgressDialog loading;
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                myJSON=result;
                showList();
                actionBar.setSubtitle(category+"  -  "+talent+"  -  "+"("+board.length()+")");
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(category,talent);
    }
}

