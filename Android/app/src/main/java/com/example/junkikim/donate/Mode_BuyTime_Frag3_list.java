package com.example.junkikim.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junkikim on 2016-11-05.
 */
public class Mode_BuyTime_Frag3_list extends Fragment{
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
    private static final String TAG_TALENT="talent";
    private static final String TAG_CONTENTS="contents";
    private static final String TAG_FILEPATH="filePath";


    ListView listView;
    ArrayList<HashMap<String, String>> boardList;
    HashMap<String, String> writing;
    Mode_BuyTime_Frag2_list_detail modeBuyTimeFrag2_list_detail;
    public Mode_BuyTime_Frag3_list(ArrayList<HashMap<String, String>> boardList) {
        this.boardList=boardList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2_board,container,false);


        listView=(ListView)root.findViewById(R.id.listView) ;

        ListAdapter adapter = new SimpleAdapter(getActivity(), boardList, R.layout.frag_buytime_2_board_list,
                new String[]{TAG_BOARDNUMBER,TAG_ID, TAG_TITLE, TAG_WRITEDATE,TAG_CONTENTS},
                new int[]{R.id.boardNumber,R.id.id, R.id.title, R.id.writeDate,R.id.contents});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm=getFragmentManager();
                Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                FragmentTransaction tr;
                HashMap<String,String> tmp =(HashMap<String,String>)boardList.get(position);
                //Toast.makeText(getActivity(),tmp.get("boardNumber")+tmp.get("id")+tmp.get("title"),Toast.LENGTH_LONG).show();
                modeBuyTimeFrag2_list_detail = new Mode_BuyTime_Frag2_list_detail(tmp);
                //tr.hide(fragment);
                tr =fm.beginTransaction();
                tr.replace(R.id.maincontainer2, modeBuyTimeFrag2_list_detail,"modeBuyTimeFrag2_list_detail");
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
}
