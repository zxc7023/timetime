package com.example.junkikim.donate;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by junkikim on 2016-11-18.
 */

public class Mode_BuyTime_Frag0 extends Fragment {


    private static final String TAG_RESULTS = "result";
    private static final String TAG_BOARDNUMBER = "boardNumber";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DAYRESULT = "dayResult";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_WRITEDATE = "writeDate";
    private static final String TAG_TYPE = "type";
    private static final String TAG_MAJOR = "major";
    private static final String TAG_SUBMAJOR = "subMajor";
    private static final String TAG_STARTHOUR = "startHour";
    private static final String TAG_ENDHOUR = "endHour";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TALENT = "talent";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_FILEPATH = "filePath";
    private static final String TAG_SCORE = "score";
    private static final String TAG_REVIEW = "review";

    private static final String TAG_BITMAP = "bitmap";


    String imgUrl = "http://1.243.135.179:8080/";
    JSONArray jsonAarray = null;
    ArrayList<HashMap<String, Object>> boardList, boardList2;
    HashMap<String, Object> writing, writing2;
    ArrayList<HashMap<String, Object>> thumbnail, thumbnail2;
    HashMap<String, Object> tmp, tmp2;


    ListView listView, listView2;
    String myJSON, myJSON2;

    String myJson3;

    Bitmap bmImg;
    Bitmap resized;

    getImage task;

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_frag_buytime_0_list, container, false);
        actionBar = ((Mode_BuyTime_MainActivity) getActivity()).getSupportActionBar();
        actionBar.setSubtitle("메인화면");
        listView = (ListView) view.findViewById(R.id.listview1);
        listView2 = (ListView) view.findViewById(R.id.listview2);
        boardList = new ArrayList<HashMap<String, Object>>();
        boardList2 = new ArrayList<HashMap<String, Object>>();


        GetDataJSON g = new GetDataJSON();
        try {
            myJSON = g.execute("http://1.243.135.179:8080/get_hot_talent_sale_posting.php/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Log.d("myJson",myJSON); //myJson으로 Json 받아옴
        showList(myJSON);

        ListAdapter adapter = new ExtendedSimpleAdapter(getActivity(), boardList, R.layout.test_frag_buytime_0_detail,
                new String[]{TAG_BITMAP, TAG_TITLE, TAG_WRITEDATE, TAG_ID, TAG_SCORE, TAG_REVIEW},
                new int[]{R.id.groundImage, R.id.titleText, R.id.writeDate, R.id.id, R.id.rating, R.id.reviewCount});

        View header = getActivity().getLayoutInflater().inflate(R.layout.test_frag_buytime_0_detail_header, null, false);
        listView.addHeaderView(header);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.test_frag_buytime_0_detail_footer, null, false);
        listView.addFooterView(footer);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);


        GetDataJSON g2 = new GetDataJSON();
        try {
            myJSON2 = g2.execute("http://1.243.135.179:8080/get_new_talent_sale_posting.php/").get();
            //Log.d("myJson2",myJSON2); //myJson으로 Json 받아옴
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        showList2(myJSON2);
        ListAdapter adapter2 = new ExtendedSimpleAdapter(getActivity(), boardList2, R.layout.test_frag_buytime_0_detail,
                new String[]{TAG_BITMAP, TAG_TITLE, TAG_WRITEDATE, TAG_ID, TAG_SCORE, TAG_REVIEW},
                new int[]{R.id.groundImage, R.id.titleText, R.id.writeDate, R.id.id, R.id.rating, R.id.reviewCount});
        listView2.setAdapter(adapter2);
        setListViewHeightBasedOnChildren(listView2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
                dialog.show();
                HashMap<String, Object> tmp = (HashMap<String, Object>) boardList.get(position - 1);
                //Log.d("boardNumber", (String) tmp.get("boardNumber"));
                String boardNumber = (String) tmp.get("boardNumber");
                getTalent_Buy_posting_detail get = new getTalent_Buy_posting_detail();
                try {
                    myJSON = get.execute(boardNumber).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                getSalePostingHashMap gethash = new getSalePostingHashMap(myJSON);
                HashMap tmp2 = gethash.excuteHash();
                Mode_BuyTime_Frag2_list_detail mode_buyTime_frag2_list_detail = new Mode_BuyTime_Frag2_list_detail(tmp2);
                FragmentManager fm = getFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                FragmentTransaction tr;
                //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                //tr.hide(fragment);
                tr = fm.beginTransaction();
                tr.remove(fragment);
                tr.replace(R.id.maincontainer2, mode_buyTime_frag2_list_detail, "mode_buyTime_frag2_list_detail");
                tr.addToBackStack(null);
                tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                tr.commit();
                dialog.dismiss();
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProgressDialog dialog2 = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
                dialog2.show();
                HashMap<String, Object> tmp = (HashMap<String, Object>) boardList2.get(position);
                //Log.d("boardNumber", (String) tmp.get("boardNumber"));
                String boardNumber = (String) tmp.get("boardNumber");
                getTalent_Buy_posting_detail get = new getTalent_Buy_posting_detail();
                try {
                    myJSON2 = get.execute(boardNumber).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                getSalePostingHashMap gethash = new getSalePostingHashMap(myJSON2);
                HashMap tmp2 = gethash.excuteHash();
                Mode_BuyTime_Frag2_list_detail mode_buyTime_frag2_list_detail = new Mode_BuyTime_Frag2_list_detail(tmp2);
                FragmentManager fm = getFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                FragmentTransaction tr;
                //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                //tr.hide(fragment);
                tr = fm.beginTransaction();
                tr.remove(fragment);
                tr.replace(R.id.maincontainer2, mode_buyTime_frag2_list_detail, "mode_buyTime_frag2_list_detail");
                tr.addToBackStack(null);
                tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                tr.commit();
                dialog2.dismiss();
            }
        });

        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void showList(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            jsonAarray = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < jsonAarray.length(); i++) {
                JSONObject c = jsonAarray.getJSONObject(i);
                String boadNumber = c.getString(TAG_BOARDNUMBER);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String dayResult = c.getString(TAG_DAYRESULT);
                String gender = c.getString(TAG_GENDER);
                String writeDate = c.getString(TAG_WRITEDATE);
                String type = c.getString(TAG_TYPE);
                String major = c.getString(TAG_MAJOR);
                String subMajor = c.getString(TAG_SUBMAJOR);
                String startHour = c.getString(TAG_STARTHOUR);
                String endHour = c.getString(TAG_ENDHOUR);
                String category = c.getString(TAG_CATEGORY);
                String talent = c.getString(TAG_TALENT);
                String contents = c.getString(TAG_CONTENTS);
                String filePath = c.getString(TAG_FILEPATH);
                String score = c.getString(TAG_SCORE);
                String review = c.getString(TAG_REVIEW);

                //Log.d("hashId",id);
                writing = new HashMap<String, Object>();
                writing.put(TAG_BOARDNUMBER, boadNumber);
                writing.put(TAG_ID, id);
                writing.put(TAG_TITLE, title);
                writing.put(TAG_DAYRESULT, dayResult);
                writing.put(TAG_GENDER, gender);
                writing.put(TAG_WRITEDATE, writeDate);
                writing.put(TAG_TYPE, type);
                writing.put(TAG_MAJOR, major);
                writing.put(TAG_SUBMAJOR, subMajor);
                writing.put(TAG_STARTHOUR, startHour);
                writing.put(TAG_ENDHOUR, endHour);
                writing.put(TAG_CATEGORY, category);
                writing.put(TAG_TALENT, talent);
                writing.put(TAG_CONTENTS, contents);
                writing.put(TAG_SCORE, score);
                writing.put(TAG_REVIEW, review);
                task = new getImage();
                writing.put(TAG_BITMAP, task.execute(imgUrl + filePath).get());
                boardList.add(writing);
                Iterator<String> iterator = writing.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    //Log.d("writing","key+ "+key +"value= "+writing.get(key)+"\n");
                }
                //Toast.makeText(getActivity(),""+boadNumber+id+title+writeDate+type+major+subMajor+startHour+endHour+talent+contents, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void showList2(String jsonString2) {
        try {
            jsonAarray = null;
            JSONObject jsonObj = null;
            jsonObj = new JSONObject(jsonString2);
            jsonAarray = jsonObj.getJSONArray(TAG_RESULTS);
            //Log.d("jsonArray", String.valueOf(jsonAarray));
            for (int i = 0; i < jsonAarray.length(); i++) {
                JSONObject c = null;
                c = jsonAarray.getJSONObject(i);
                String boadNumber = c.getString(TAG_BOARDNUMBER);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String dayResult = c.getString(TAG_DAYRESULT);
                String gender = c.getString(TAG_GENDER);
                String writeDate = c.getString(TAG_WRITEDATE);
                String type = c.getString(TAG_TYPE);
                String major = c.getString(TAG_MAJOR);
                String subMajor = c.getString(TAG_SUBMAJOR);
                String startHour = c.getString(TAG_STARTHOUR);
                String endHour = c.getString(TAG_ENDHOUR);
                String category = c.getString(TAG_CATEGORY);
                String talent = c.getString(TAG_TALENT);
                String contents = c.getString(TAG_CONTENTS);
                String filePath = c.getString(TAG_FILEPATH);
                String score = c.getString(TAG_SCORE);
                String review = c.getString(TAG_REVIEW);

                //Log.d("hashId2",id);
                writing2 = new HashMap<String, Object>();
                writing2.put(TAG_BOARDNUMBER, boadNumber);
                writing2.put(TAG_ID, id);
                writing2.put(TAG_TITLE, title);
                writing2.put(TAG_DAYRESULT, dayResult);
                writing2.put(TAG_GENDER, gender);
                writing2.put(TAG_WRITEDATE, writeDate);
                writing2.put(TAG_TYPE, type);
                writing2.put(TAG_MAJOR, major);
                writing2.put(TAG_SUBMAJOR, subMajor);
                writing2.put(TAG_STARTHOUR, startHour);
                writing2.put(TAG_ENDHOUR, endHour);
                writing2.put(TAG_CATEGORY, category);
                writing2.put(TAG_TALENT, talent);
                writing2.put(TAG_CONTENTS, contents);
                writing2.put(TAG_SCORE, score);
                writing2.put(TAG_REVIEW, review);
                task = new getImage();
                writing2.put(TAG_BITMAP, task.execute(imgUrl + filePath).get());
                boardList2.add(writing2);
                Iterator<String> iterator = writing2.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    //Log.d("writing","key+ "+key +"value= "+writing2.get(key)+"\n");
                }
                //Toast.makeText(getActivity(),""+boadNumber+id+title+writeDate+type+major+subMajor+startHour+endHour+talent+contents, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                String link = params[0];
                //String data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category,"UTF-8");
                //data += "&" + URLEncoder.encode("talent", "UTF-8") + "=" + URLEncoder.encode(talent, "UTF-8");
                BufferedReader bufferedReader = null;

                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setDoOutput(true); //Post방식으로 전송하겠다.
                con.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                //wr.write(data);
                //wr.flush();

                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }

                return sb.toString().trim();

            } catch (Exception e) {
                return null;
            }


        }


        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //myJSON=result;
        }
    }

    private class getImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Rect rect = new Rect(0, 0, 0, 0);
                Bitmap src = BitmapFactory.decodeStream(is, rect, options);
                if(src==null){
                    return bmImg;
                }
                ResizeBitmapImage re = new ResizeBitmapImage();
                resized = re.resize(src, 256);
                //bmImg = BitmapFactory.decodeStream(is);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return resized;
        }

        protected void onPostExecute(Bitmap img) {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
