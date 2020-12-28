package com.example.junkikim.donate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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
public class MyMenu_3 extends Fragment{

    private static final String TAG_REVIEW_RESULTS="reviewResult";
    private static final String TAG_REVIEW_ID="id";
    private static final String TAG_REVIEW_WRITEDATE="writeDate";
    private static final String TAG_REVIEW_SCORE="score";
    private static final String TAG_REVIEW_CONTENTS="contents";
    private static final String TAG_REVIEW_NUM="num";


    String boardNumber;
    String id;
    JSONArray board = null;
    ListView listView;
    Mode_BuyTime_Frag2_list_detail modeBuyTimeFrag2_list_detail;

    String myReviewJson;
    ArrayList<HashMap<String, String>> reviewArrayList;
    HashMap<String, String> reviewHash;
    JSONArray jsonArray = null;
    float totalRating;
    String link;

    //Mode_BuyTime_Frag2 에서 해당하는 id와 name를 실행하면 선택한 그 name를 프래그먼트의 인자로 넘겨줘서 받음.
    public static MyMenu_3 newInstance(String id){
        MyMenu_3 mymenu_3 = new MyMenu_3();
        Bundle args = new Bundle();
        args.putString("id",id);
        mymenu_3.setArguments(args);
        return mymenu_3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mymenu_listview,container,false); //카테고리 화면 인플레이터해줌

        if(container.getId()==R.id.maincontainer1){
            ActionBar actionBar;
            actionBar = ((Mode_SellTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  후기관리");
            link ="http://1.243.135.179:8080/my_review_buy.php/";
        }
        else {
            ActionBar actionBar;
            actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  후기관리");
            link ="http://1.243.135.179:8080/my_review.php/";
        }

        reviewArrayList  = new ArrayList<HashMap<String, String>>();
        listView=(ListView)root.findViewById(R.id.listView11);
        Bundle args =getArguments();
        if(args!=null){
            id=args.getString("id");
        }

        getReviewPosting(id);

        Toast.makeText(getActivity(),id+"", Toast.LENGTH_SHORT).show();

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm=getFragmentManager();
                if(((ViewGroup) getView().getParent()).getId()==R.id.maincontainer1){
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer1);
                    FragmentTransaction tr;
                    HashMap<String,String> tmp =(HashMap<String,String>)reviewArrayList.get(position);
                    //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                    modeBuyTimeFrag2_list_detail = new Mode_BuyTime_Frag2_list_detail(tmp);

                    //Mymenu_1_2 = new Mymenu_1_2(tmp);
                    tr =fm.beginTransaction();
                    tr.replace(R.id.maincontainer1, modeBuyTimeFrag2_list_detail,"Review");
                    tr.addToBackStack(null);
                    tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    tr.commit();
                }
                else {

                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"오랜클릭 클릭되었습", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        */
        return root;
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myReviewJson);
            jsonArray=jsonObj.getJSONArray(TAG_REVIEW_RESULTS);
            //Toast.makeText(getActivity(),""+"여기까지 진행함", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String id = c.getString(TAG_REVIEW_ID);
                String writeDate = c.getString(TAG_REVIEW_WRITEDATE);
                String score = c.getString(TAG_REVIEW_SCORE);
                String contents = c.getString(TAG_REVIEW_CONTENTS);
                String num = c.getString(TAG_REVIEW_NUM);


                reviewHash = new HashMap<String, String>();
                reviewHash.put(TAG_REVIEW_ID, id);
                reviewHash.put(TAG_REVIEW_WRITEDATE, writeDate);
                reviewHash.put(TAG_REVIEW_SCORE, score);
                reviewHash.put(TAG_REVIEW_CONTENTS, contents);
                reviewHash.put(TAG_REVIEW_NUM, num);


                reviewArrayList.add(reviewHash);
                //Toast.makeText(getActivity(),""+id+"\n"+writeDate+"\n"+score+"\n"+contents+"\n"+num, Toast.LENGTH_SHORT).show();
            }

            ListAdapter adapter = new ExtendedSimpleAdapter(getActivity(), reviewArrayList, R.layout.mymenu_frag_review_,
                    new String[]{TAG_REVIEW_ID,TAG_REVIEW_WRITEDATE, TAG_REVIEW_SCORE,TAG_REVIEW_CONTENTS},
                    new int[]{R.id.id,R.id.writeDate, R.id.score,R.id.contents});
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getReviewPosting(String id){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id = params[0];

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
            ProgressDialog loading;
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                myReviewJson=result;
                // Toast.makeText(getActivity(),myReviewJson, Toast.LENGTH_SHORT).show();
                showList();
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id);
    }

}