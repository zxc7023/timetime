package com.example.junkikim.donate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junkikim.donate.communicateServerByPHP.DeleteBoard_dbName_talent_sale_posting;
import com.example.junkikim.donate.communicateServerByPHP.PostReview_dbName_notes;
import com.example.junkikim.donate.communicateServerByPHP.SendMessage_dbName_notes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by junkikim on 2016-10-05.
 */
public class Mode_BuyTime_Frag2_list_detail extends Fragment {

    private static final String TAG_REVEIW_RESULTS = "reviewResult";
    private static final String TAG_REVEIW_ID = "id";
    private static final String TAG_REVEIW_WRITEDATE = "writeDate";
    private static final String TAG_REVEIW_SCORE = "score";
    private static final String TAG_REVEIW_CONTENTS = "contents";

    private int boardNumber;

    CheckBox cb[];
    int[] dayBit = new int[]{64, 32, 16, 8, 4, 2, 1};

    ImageView imView;
    String imgUrl = "http://1.243.135.179:8080/";
    Bitmap bmImg;
    back task;

    HashMap<String, String> boardHash;

    TextView boardNumberText, writeDateText, idText, titleText, majorText, subMajorText, startHourText, endHourText, talentText, contentsText;
    RadioButton male, female, nomal, present;

    TextView textDialog;
    String id_recv, id_send, msg_contents, msg_writeDate;
    AlertDialog.Builder alertDialog;

    int dayValue;
    String gender, type, dayResult;
    private ProgressDialog loading;

    String myReviewJson;
    ArrayList<HashMap<String, String>> reviewArrayList;
    HashMap<String, String> reviewHash;
    JSONArray jsonArray = null;
    ScrollView scrollView;
    LinearLayout scrollLinear;
    RatingBar totalRatingBar;
    float totalRating;

    TextView reviewTextView;
    RatingBar reviewRatingBar;
    String postId;


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Toast.makeText(getActivity(), ""+LoginActivity.id+boardHash.get("id"), Toast.LENGTH_LONG).show();
        menu.findItem(R.id.action_search).setVisible(false);
        if (LoginActivity.id.equals(boardHash.get("id"))) {
            menu.findItem(R.id.modify_talentSalePosting).setVisible(true);
            menu.findItem(R.id.delete_talentSalePosting).setVisible(true);

        } else {
            menu.findItem(R.id.message).setVisible(true);
            menu.findItem(R.id.post_review_buy).setVisible(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.message:
                alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertDialogView = inflater.inflate(R.layout.write_message, null);
                alertDialog.setView(alertDialogView);

                textDialog = (TextView) alertDialogView.findViewById(R.id.editText3);
                // textDialog.setText(questionMissing);
                alertDialog.setPositiveButton("전송하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        id_recv = boardHash.get("id");
                        id_send = LoginActivity.id;
                        msg_contents = textDialog.getText().toString();
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        msg_writeDate = sdfNow.format(date);
                        //Toast.makeText(getActivity(), ""+id_recv+id_send+msg_contents+msg_writeDate, Toast.LENGTH_LONG).show();
                        loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
                        FragmentManager fm = getFragmentManager();
                        Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                        FragmentTransaction tr = fm.beginTransaction();
                        int layout=R.id.maincontainer2;
                        SendMessage_dbName_notes send = new SendMessage_dbName_notes(fm,fragment,tr,layout,loading,getActivity());
                        send.execute(id_recv, id_send, msg_contents, msg_writeDate);

                    }

                });
                alertDialog.show();
                break;
            case R.id.modify_talentSalePosting:
                ////Log.d("test", String.valueOf(((ViewGroup) getView().getParent()).getId()));
                if (((ViewGroup) getView().getParent()).getId() == R.id.maincontainer1) {
                    Mode_BuyTime_Frag2_list_detail_modify modeBuyTimeFrag2_list_detail_modify = new Mode_BuyTime_Frag2_list_detail_modify(boardHash, bmImg);
                    //(boardHash.get("type"),boardHash.get("title"));
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, modeBuyTimeFrag2_list_detail_modify, "modeBuyTimeFrag2_list_detail_modify");
                    tr.commit();
                } else {
                    Mode_BuyTime_Frag2_list_detail_modify modeBuyTimeFrag2_list_detail_modify = new Mode_BuyTime_Frag2_list_detail_modify(boardHash, bmImg);
                    //(boardHash.get("type"),boardHash.get("title"));
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer2, modeBuyTimeFrag2_list_detail_modify, "modeBuyTimeFrag2_list_detail_modify");
                    tr.commit();

                }
                break;
            case R.id.delete_talentSalePosting:
                ////Log.d("delete_talentSalePosting",boardHash.get("boardNumber"));
                //delete_talentSalePosting(boardHash.get("boardNumber"));
                String link = "http://1.243.135.179:8080/delete_talent_sale_posting.php/";
                ProgressDialog loading1 = ProgressDialog.show(getActivity(), "Please Wait", "삭제하는중 입니다.", true, true);
                int layout;
                if (((ViewGroup) getView().getParent()).getId() == R.id.maincontainer1){
                    layout=R.id.maincontainer1;
                }
                else {
                    layout=R.id.maincontainer2;
                }
                FragmentManager fm = getFragmentManager();
                Fragment fragment = fm.findFragmentById(layout);
                FragmentTransaction tr = fm.beginTransaction();

                DeleteBoard_dbName_talent_sale_posting task = new DeleteBoard_dbName_talent_sale_posting(fm,fragment,tr,layout,loading1,getActivity());
                task.execute(boardHash.get("boardNumber"), link);

                break;
            case R.id.post_review_buy:
                alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = getActivity().getLayoutInflater();
                View alertDialogView2 = inflater2.inflate(R.layout.review_rating, null);
                alertDialog.setView(alertDialogView2);
                reviewTextView = (TextView) alertDialogView2.findViewById(R.id.reviewTextView);
                reviewRatingBar = (RatingBar) alertDialogView2.findViewById(R.id.reviewRating);
                // textDialog.setText(questionMissing);
                alertDialog.setPositiveButton("전송하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        postId = LoginActivity.id;
                        msg_contents = reviewTextView.getText().toString();
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        msg_writeDate = sdfNow.format(date);
                        String rating = String.valueOf(reviewRatingBar.getRating());
                        //Toast.makeText(getActivity(), ""+id_recv+id_send+msg_contents+msg_writeDate, Toast.LENGTH_LONG).show();
                        ////Log.d("test1",postId+msg_writeDate+rating+msg_contents+boardHash.get("boardNumber"));
                        String postResult = null;
                        PostReview_dbName_notes post = new PostReview_dbName_notes();
                        try {
                            String link="http://1.243.135.179:8080/insert_review_sale.php/";
                            postResult = post.execute(postId, msg_writeDate, rating, msg_contents, boardHash.get("boardNumber"),link).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (postResult.equals("success")) {
                            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
                            Toast.makeText(getActivity(), "등록하였습니다. " + "", Toast.LENGTH_LONG).show();
                            Mode_BuyTime_Frag2_list_detail mode_buyTime_frag2_list_detail = new Mode_BuyTime_Frag2_list_detail(boardHash);
                            FragmentManager fm = getFragmentManager();
                            Fragment fragment = fm.findFragmentById(R.id.maincontainer2);
                            FragmentTransaction tr;
                            tr = fm.beginTransaction();
                            tr.remove(fragment);
                            tr.replace(R.id.maincontainer2, mode_buyTime_frag2_list_detail, "mode_buyTime_frag2_list_detail");
                            tr.commit();
                            loading.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "실패했습니다.", Toast.LENGTH_LONG).show();
                        }
                        //postReview(postId,msg_writeDate,rating,msg_contents,boardHash.get("boardNumber"));
                        //sendToMsg(id_recv,id_send,msg_contents,msg_writeDate);
                    }

                });
                alertDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
        //return super.onOptionsItemSelected(item);

    }


    public Mode_BuyTime_Frag2_list_detail(HashMap<String, String> boardHash) {
        this.boardHash = boardHash;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2_board_list_detail, container, false);
        scrollLinear = (LinearLayout) root.findViewById(R.id.scrollLinear);
        scrollView = (ScrollView) root.findViewById(R.id.scrollView5);
        /*boardNumberText = (TextView) root.findViewById(R.id.boardNumber);
        boardNumberText.setText(boardHash.get("boardNumber"));*/
        titleText = (TextView) root.findViewById(R.id.title);
        titleText.setText(boardHash.get("title"));
        idText = (TextView) root.findViewById(R.id.id);
        idText.setText(boardHash.get("id"));
        writeDateText = (TextView) root.findViewById(R.id.writeDate);
        writeDateText.setText(boardHash.get("writeDate"));
        nomal = (RadioButton) root.findViewById(R.id.nomalType);
        present = (RadioButton) root.findViewById(R.id.presentType);
        male = (RadioButton) root.findViewById(R.id.male);
        female = (RadioButton) root.findViewById(R.id.female);
        type = boardHash.get("type");
        //Toast.makeText(getActivity(), tmp, Toast.LENGTH_SHORT).show();
        if (type.equals("일반")) {
            nomal.setChecked(true);
        } else {
            present.setChecked(true);
        }
        gender = boardHash.get("gender");
        //Toast.makeText(getActivity(), tmp, Toast.LENGTH_SHORT).show();
        if (gender.equals("남자")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        cb = new CheckBox[]{(CheckBox) root.findViewById(R.id.sun),
                (CheckBox) root.findViewById(R.id.mon),
                (CheckBox) root.findViewById(R.id.tue),
                (CheckBox) root.findViewById(R.id.wen),
                (CheckBox) root.findViewById(R.id.thu),
                (CheckBox) root.findViewById(R.id.fri),
                (CheckBox) root.findViewById(R.id.sat)
        };
        dayResult = boardHash.get("dayResult");
        dayValue = Integer.parseInt(dayResult);
        //Toast.makeText(getActivity(), ""+dayValue, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < dayBit.length; i++) {
            int tmp = dayBit[i] & dayValue;
            switch (tmp) {
                case 64:
                    cb[0].setChecked(true);
                    break;
                case 32:
                    cb[1].setChecked(true);
                    break;
                case 16:
                    cb[2].setChecked(true);
                    break;
                case 8:
                    cb[3].setChecked(true);
                    break;
                case 4:
                    cb[4].setChecked(true);
                    break;
                case 2:
                    cb[5].setChecked(true);
                    break;
                case 1:
                    cb[6].setChecked(true);
                    break;
            }
        }
        /*
        typeText = (TextView) root.findViewById(R.id.type);
        typeText.setText(boardHash.get("type"));
        */
        majorText = (TextView) root.findViewById(R.id.major);
        majorText.setText(boardHash.get("major"));
        subMajorText = (TextView) root.findViewById(R.id.subMajor);
        subMajorText.setText(boardHash.get("subMajor"));
        startHourText = (TextView) root.findViewById(R.id.startHour);
        startHourText.setText(boardHash.get("startHour"));
        endHourText = (TextView) root.findViewById(R.id.endHour);
        endHourText.setText(boardHash.get("endHour"));
        contentsText = (TextView) root.findViewById(R.id.contents);
        contentsText.setText(boardHash.get("contents"));
        talentText = (TextView) root.findViewById(R.id.talent);
        talentText.setText(boardHash.get("talent"));
        imView = (ImageView) root.findViewById(R.id.groundImage);
        //Toast.makeText(getActivity(),""+boardHash.get("filePath"), Toast.LENGTH_SHORT).show();
        task = new back();
        task.execute(imgUrl + boardHash.get("filePath"));

        totalRatingBar = (RatingBar) root.findViewById(R.id.averageRating);
        reviewArrayList = new ArrayList<HashMap<String, String>>();
        getReviewPosting(boardHash.get("boardNumber"));
        /*
        reviewListView = (ListView)root.findViewById(R.id.reviewListView);
        reviewListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        */
        return root;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myReviewJson);
            jsonArray = jsonObj.getJSONArray(TAG_REVEIW_RESULTS);
            //Toast.makeText(getActivity(),""+"여기까지 진행함", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String id = c.getString(TAG_REVEIW_ID);
                String writeDate = c.getString(TAG_REVEIW_WRITEDATE);
                String score = c.getString(TAG_REVEIW_SCORE);
                String contents = c.getString(TAG_REVEIW_CONTENTS);
                // Toast.makeText(getActivity(),""+id+writeDate+score+contents, Toast.LENGTH_SHORT).show();
                //String filePath  = c.getString(TAG_FILEPATH);

                reviewHash = new HashMap<String, String>();
                reviewHash.put(TAG_REVEIW_ID, id);
                reviewHash.put(TAG_REVEIW_WRITEDATE, writeDate);
                reviewHash.put(TAG_REVEIW_SCORE, score);
                reviewHash.put(TAG_REVEIW_CONTENTS, contents);
                //reviewHash.put(TAG_FILEPATH,filePath);
                reviewArrayList.add(reviewHash);
                totalRating = totalRating + Float.parseFloat(score);
                View row = getActivity().getLayoutInflater().inflate(R.layout.frag_review, null);
                ((TextView) row.findViewById(R.id.id)).setText(id + "");
                ((TextView) row.findViewById(R.id.writeDate)).setText(writeDate);
                ((RatingBar) row.findViewById(R.id.score)).setRating(Float.parseFloat(score));
                ((TextView) row.findViewById(R.id.contents)).setText(contents);
                scrollLinear.addView(row);
                //Toast.makeText(getActivity(),""+boadNumber+id+title+writeDate+type+major+subMajor+startHour+endHour+talent+contents, Toast.LENGTH_SHORT).show();
            }
            totalRating = totalRating / jsonArray.length();
            totalRatingBar.setRating(Float.parseFloat(String.valueOf(totalRating)));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class back extends AsyncTask<String, Integer, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img) {
            loading.dismiss();
            imView.setImageBitmap(bmImg);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }
    }

    public void getReviewPosting(String boardNumber) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String boardNumber = params[0];

                    String link = "http://1.243.135.179:8080/get_sale_review_posting.php/";
                    String data = URLEncoder.encode("boardNumber", "UTF-8") + "=" + URLEncoder.encode(boardNumber, "UTF-8");
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
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }

            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                myReviewJson = result;
                // Toast.makeText(getActivity(),myReviewJson, Toast.LENGTH_SHORT).show();
                showList();
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(boardNumber);
    }
}

