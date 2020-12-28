package com.example.junkikim.donate;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Mode_SellTime_MainActivity extends AppCompatActivity {


    MenuItem mSearch;
    SearchView mSearchView;

    Mode_SellTime_Frag0 myFragment0;
    Mode_SellTime_Frag1 myFragment1;
    Mode_SellTime_Frag2 myFragment2;
    Mode_SellTime_Frag3 myFragment3;
    MyMenu myFragment5;
    ActionBar actionBar;

    Mode_SellTime_Activity_searchFrag mode_sellTime_activity_searchFrag;


    //BackPress를 눌렀을시 필요한 클래스를 객체로 만듬.
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.rootViewSellTime));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home_button:
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                tr.replace(R.id.maincontainer1, myFragment0, "myfragment0");
                tr.commit();
                //Toast.makeText(this,"홈 아이콘 선택",Toast.LENGTH_SHORT).show();
                actionBar = getSupportActionBar();
                actionBar.setSubtitle("메인화면");
                break;
            case R.id.action_change_button:
                Toast.makeText(this, "전환 아이콘 선택", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Mode_SellTime_MainActivity.this, Mode_BuyTime_MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            /*
            case R.id.action_search:
                Toast.makeText(this,"검색 아이콘  선택",Toast.LENGTH_SHORT).show();
                break;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("myfragment1");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        menu.findItem(R.id.message).setVisible(false);
        menu.findItem(R.id.delect).setVisible(false);
        menu.findItem(R.id.delect_board).setVisible(false);
        menu.findItem(R.id.delete_talentSalePosting).setVisible(false);
        menu.findItem(R.id.modify).setVisible(false);
        menu.findItem(R.id.modify_talentSalePosting).setVisible(false);
        menu.findItem(R.id.post_review_buy).setVisible(false);
        menu.findItem(R.id.post_review_sale).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        mSearch = menu.findItem(R.id.action_search_sell);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearch);
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchString = (String) parent.getItemAtPosition(position);
                searchAutoComplete.setText("" + searchString);
            }
        });
        String[] itemArrayList = getResources().getStringArray(R.array.search_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_list_item, itemArrayList);
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setTextColor(Color.WHITE);
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryHint("검색할 키워드를 입력하세요.");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색어완료시
                //mSearchView.onActionViewCollapsed();
                mSearch.collapseActionView();

                ////Log.d("checkfrag",getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()).getName());
                mode_sellTime_activity_searchFrag = new Mode_SellTime_Activity_searchFrag(query);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                tr.replace(R.id.maincontainer1, mode_sellTime_activity_searchFrag, "mode_buyTime_activity_searchFrag");
                tr.addToBackStack(null);
                tr.commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색어입력시
                return false;
            }
        });
        mSearchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction tr;
                tr = fm.beginTransaction();
                Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                if (fm.getBackStackEntryCount() > 0) {
                    tr.remove(fragment);
                    fm.popBackStack();
                    tr.commit();
                } else {

                }
            }
        });


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selltime);

        myFragment0 = new Mode_SellTime_Frag0();
        myFragment1 = new Mode_SellTime_Frag1();
        myFragment2 = new Mode_SellTime_Frag2();
        myFragment3 = new Mode_SellTime_Frag3();
        myFragment5 = new MyMenu();

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setSubtitle("메인화면");
        actionBar.setTitle("나누미모드");

        LoginActivity loginActivity = (LoginActivity) LoginActivity.loginActivity;
        loginActivity.finish();

        //BackPressCloseHandler객체에 context를 주고 또한 메소드를 이용하기위하여 선언
        backPressCloseHandler = new BackPressCloseHandler(this);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
        FragmentTransaction tr;
        if (fragment == null) {
            tr = fm.beginTransaction();
            tr.replace(R.id.maincontainer1, myFragment0, "myfragment0");
            tr.commit();
        }

    }

    public void onGroundClicked(View v) {
        myFragment1.onGroundClicked(v);
    }

    public void onClickedCategory(View v) {
        myFragment2.onClickedCategory(v);
    }

    public void onClickedTalent(View v) {

        myFragment2.onClickedTalent(v);
    }

    public void mouseOnClicked(View v) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
        FragmentTransaction tr;
        switch (v.getId()) {
            case R.id.button1:
                if (fragment.getTag() == "myfragment1") {
                    Toast.makeText(Mode_SellTime_MainActivity.this, "이미 추가되어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (myFragment1 != null) {
                        myFragment1 = null;
                        myFragment1 = new Mode_SellTime_Frag1();
                    }
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, myFragment1, "myfragment1");//단지 첫번째로 추가하기위하여 add
                    tr.commit();
                }
                actionBar.setSubtitle("나누미등록");
                break;
            case R.id.button2:
                if (fragment.getTag() == "myfragment2") {
                    Toast.makeText(Mode_SellTime_MainActivity.this, "이미 추가되어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, myFragment2, "myfragment2");
                    tr.commit();
                }
                actionBar.setSubtitle("구하미게시판");
                break;
            case R.id.button3:
                if (fragment.getTag() == "myfragment3") {
                    Toast.makeText(Mode_SellTime_MainActivity.this, "이미 추가되어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, myFragment3, "myfragment3");
                    actionBar.setSubtitle("구하미검색");
                    tr.commit();
                }
                break;

            case R.id.button5:
                if (fragment.getTag() == "sellTimeFrag5") {
                    Toast.makeText(Mode_SellTime_MainActivity.this, "이미 추가되어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    tr = fm.beginTransaction();
                    tr.remove(fragment);
                    tr.replace(R.id.maincontainer1, myFragment5, "sellTimeFrag5");
                    tr.commit();
                    actionBar.setSubtitle("마이탐탐");
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr;
        Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
        if (fm.getBackStackEntryCount() > 0) {
            tr = fm.beginTransaction();
            tr.remove(fragment);
            fm.popBackStack();
            tr.commit();
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    class getSearchByText extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String keyword = params[0];

                String link = "http://1.243.135.179:8080/get_talent_buy_posting_by_search_keyword.php/";
                String data = URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8");

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
            loading = ProgressDialog.show(Mode_SellTime_MainActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
        }
    }
}



