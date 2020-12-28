package com.example.junkikim.donate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junkikim on 2016-11-18.
 */

public class getBuyPostingArrayList {
    String myJson3;
    HashMap<String, String> writing;
    ArrayList<HashMap<String,String>> boardList;


    public getBuyPostingArrayList(String myJson3) {
        this.myJson3 = myJson3;
    }

    public ArrayList<HashMap<String, String>> excuteArrayList() {
        try {
            boardList=new ArrayList<HashMap<String,String>>();
            ////Log.d("myJson3",myJson3);
            JSONObject jsonObj = new JSONObject(myJson3);
            JSONArray board = jsonObj.getJSONArray("result");
            for (int i = 0; i < board.length(); i++) {
                JSONObject c = board.getJSONObject(i);
                String boadNumber = c.getString("boardNumber");
                String id = c.getString("id");
                String title = c.getString("title");
                String dayResult = c.getString("dayResult");
                String gender = c.getString("gender");
                String writeDate = c.getString("writeDate");
                String type = c.getString("type");
                String startHour = c.getString("startHour");
                String endHour = c.getString("endHour");
                String category = c.getString("category");
                String talent = c.getString("talent");
                String contents = c.getString("contents");
                String filePath = c.getString("filePath");

                writing = new HashMap<String, String>();
                writing.put("boardNumber", boadNumber);
                writing.put("id", id);
                writing.put("title", title);
                writing.put("dayResult", dayResult);
                writing.put("gender", gender);
                writing.put("writeDate", writeDate);
                writing.put("type", type);
                writing.put("startHour", startHour);
                writing.put("endHour", endHour);
                writing.put("category", category);
                writing.put("talent", talent);
                writing.put("contents", contents);
                writing.put("filePath", filePath);
                boardList.add(writing);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return boardList;
    }
}