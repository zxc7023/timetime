package com.example.junkikim.donate;

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

import com.example.junkikim.donate.communicateServerByPHP.GetSearchByText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by junkikim on 2016-11-19.
 */

public class Mode_BuyTime_Activity_searchFrag extends Fragment {
    String query,result;
    ListView listView;
    getSalePostingArrayList getspa;
    ActionBar actionBar;

    String link="http://1.243.135.179:8080/get_talent_sale_posting_by_search_keyword.php/";

    ArrayList<HashMap<String,String>> boardList;
    Mode_BuyTime_Frag2_list_detail modeBuyTimeFrag2_list_detail;
    public Mode_BuyTime_Activity_searchFrag(String query) {
        this.query=query;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2_board, container, false);
        actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
        GetSearchByText get = new GetSearchByText(getActivity());
        try {
            result=get.execute(query,link).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        boardList= new ArrayList<HashMap<String,String>>();
        listView=(ListView)root.findViewById(R.id.listView);
        getspa=new getSalePostingArrayList(result);
        boardList=getspa.excuteArrayList();
        actionBar.setSubtitle("키워드검색 결과 : 총 "+boardList.size()+"개");
        ListAdapter adapter = new SimpleAdapter(getActivity(), boardList, R.layout.frag_buytime_2_board_list,
                new String[]{"boardNumber","id", "title", "writeDate","contents"},
                new int[]{R.id.boardNumber,R.id.id, R.id.title, R.id.writeDate,R.id.contents});
        listView.setAdapter(adapter);

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
        return root;
    }
}
