package com.example.junkikim.donate;



import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
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


public class Mode_BuyTime_Frag3 extends Fragment implements AdapterView.OnItemSelectedListener {

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

    RadioGroup radioGroup1;
    RadioButton radioButton1,radioButton2,radioButton3;
    String generResult;

    RadioGroup radioGroup2;
    RadioButton typeButton1,typeButton2;
    String typeResult;

    Spinner majorSpinner, subSpinner;
    String major,subMajor="";


    EditText startTimeText,finishTimeText;
    String startHour="무관",endHour="무관";

    Spinner talentSpinner1,talentSpinner2,talentSpinner3;
    String talent1,talent2,talent3;

    int s_hour,f_hour;
    int TimeFlag=0; //위의 변수 시작 시간,분과 종료 시간,분으로 나누어 저장하기위한 변수

    JSONArray board = null;
    ArrayList<HashMap<String, String>> boardList;
    HashMap<String, String> writing;
    String myJSON;

    Button talentSelectButton;

    //TimePicker 시간 15분으로 주기위한 변수들
    private static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;
    RadioGroup dayGroup;
    RadioButton weekdayButton;
    RadioButton weekendButton;
    RadioButton dayUnrelateButton;
    RadioButton directSelectButton;
    LinearLayout directSeletPage;
    CheckBox[] cb;
    String dayResult="무관";
    int tmp=0;
    int[] dayBit=new int[]{64,32,16,8,4,2,1};


    CheckBox timeCheckBox;

    Mode_BuyTime_Frag3_list modeBuyTimeFrag3_list;

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_3, container, false);
        actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
        addMajorSpinner(root);
        add_first_talent(root);
        timeCheckBox=(CheckBox)root.findViewById(R.id.timeCheck);
        boardList = new ArrayList<HashMap<String, String>>();
        radioGroup1=(RadioGroup)root.findViewById(R.id.radioGroup1);
        radioButton1=(RadioButton)root.findViewById(R.id.male);
        radioButton2=(RadioButton)root.findViewById(R.id.female);
        radioButton3=(RadioButton)root.findViewById(R.id.unrelated);

        radioGroup2=(RadioGroup)root.findViewById(R.id.radioGroup2);
        typeButton1=(RadioButton)root.findViewById(R.id.nomal);
        typeButton2=(RadioButton)root.findViewById(R.id.present);

        startTimeText=(EditText)root.findViewById(R.id.startTime);
        finishTimeText=(EditText)root.findViewById(R.id.finishTime);
        talentSelectButton =(Button)root.findViewById(R.id.search);

        talentSelectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }

        });

        startTimeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFlag = 0;
                new TimePickerDialog(getActivity(), timeSetListener, s_hour, 0,false).show();

            }
        });
        finishTimeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFlag = 1;
                new TimePickerDialog(getActivity(), timeSetListener, f_hour, 0, false).show();
            }
        });
        dayGroup=(RadioGroup)root.findViewById(R.id.dayGroup);
        weekdayButton=(RadioButton)root.findViewById(R.id.weekday);
        weekendButton=(RadioButton)root.findViewById(R.id.weekend);
        dayUnrelateButton=(RadioButton)root.findViewById(R.id.dayUnrelate);
        directSelectButton=(RadioButton)root.findViewById(R.id.directSelectButton);
        directSeletPage =(LinearLayout)root.findViewById(R.id.directSelectPage);
        cb = new CheckBox[]
                {(CheckBox)root.findViewById(R.id.sun),
                        (CheckBox)root.findViewById(R.id.mon),
                        (CheckBox)root.findViewById(R.id.tue),
                        (CheckBox)root.findViewById(R.id.wen),
                        (CheckBox)root.findViewById(R.id.thu),
                        (CheckBox)root.findViewById(R.id.fri),
                        (CheckBox)root.findViewById(R.id.sat),
                };

        dayGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.weekday:
                        directSeletPage.setVisibility(View.GONE);
                        dayResult="62";
                        break;
                    case R.id.weekend:
                        directSeletPage.setVisibility(View.GONE);
                        dayResult="65";
                        break;
                    case R.id.directSelectButton:
                        directSeletPage.setVisibility(View.VISIBLE);
                        break;
                    case R.id.dayUnrelate:
                        directSeletPage.setVisibility(View.GONE);
                        dayResult="무관";
                        break;
                }
            }
        });
        timeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(timeCheckBox.isChecked()){
                    startTimeText.setClickable(false);
                    startTimeText.setEnabled(false);
                    finishTimeText.setClickable(false);
                    finishTimeText.setEnabled(false);
                    startTimeText.setText("");
                    finishTimeText.setText("");
                    startHour="무관";
                    endHour="무관";
                }
                else {
                    startTimeText.setClickable(true);
                    finishTimeText.setClickable(true);
                    startTimeText.setEnabled(true);
                    finishTimeText.setEnabled(true);
                    startTimeText.setText("");
                    finishTimeText.setText("");
                    startHour="";
                    endHour="";

                }
            }
        });
        return root;
    }
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d", hourOfDay);
            //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            if(TimeFlag==0){
                startTimeText.setText(msg);
                startHour=String.valueOf(msg);
                TimeFlag=1;
            }
            else{
                finishTimeText.setText(msg);
                endHour=String.valueOf(msg);
                TimeFlag=0;
            }
        }
    };

    public void search(View v){
        if(radioGroup1.getCheckedRadioButtonId()==R.id.male)
            generResult= radioButton1.getText().toString();
        else if(radioGroup1.getCheckedRadioButtonId()==R.id.female)
            generResult= radioButton2.getText().toString();
        else if(radioGroup1.getCheckedRadioButtonId()==R.id.unrelated)
            generResult= radioButton3.getText().toString();

        if(radioGroup2.getCheckedRadioButtonId()==R.id.nomal)
            typeResult=typeButton1.getText().toString();
        else if(radioGroup2.getCheckedRadioButtonId()==R.id.present)
            typeResult=typeButton2.getText().toString();
        if(directSelectButton.isChecked()){
            tmp=0;
            for(int i=0;i<cb.length;i++){
                if(cb[i].isChecked()){
                    tmp=tmp|dayBit[i];
                }
            }
            dayResult= Integer.toString(tmp);

        }

        String msg= generResult+"\n"+typeResult +"\n" +major+"\n" +subMajor+"\n"+dayResult+"\n"+startHour+"\n"+endHour +"\n"+ talent1 +"\n"+ talent2 +"\n"+ talent3;
        Log.d("msg",msg);
        getSearchData(generResult,typeResult,major,subMajor,dayResult,startHour,endHour,talent1,talent2,talent3);
        //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void add_first_talent(View root){
        String[] talentList=getResources().getStringArray(R.array.total_talent);
        ArrayAdapter<CharSequence> talentAdapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item,talentList);
        talentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        talentSpinner1=(Spinner)root.findViewById(R.id.spinner);
        talentSpinner1.setAdapter(talentAdapter);
        talentSpinner1.setOnItemSelectedListener(this);
        talentSpinner2=(Spinner)root.findViewById(R.id.spinner2);
        talentSpinner3=(Spinner)root.findViewById(R.id.spinner3);

    }

    public void addMajorSpinner(View root){
        //데이터 준비
        String[] majorList=getResources().getStringArray(R.array.major);
        //어댑터 준비 현재 컨텍스트(액티비티),표시할 레이아웃형식(기본 안드로이드 dropdown이용),데이터 원본)
        ArrayAdapter<CharSequence> majorAdpater = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item,majorList);
        //스피너와 어댑터 연결
        majorSpinner = (Spinner)root.findViewById(R.id.major);
        majorSpinner.setAdapter(majorAdpater); //어댑터 객체와  뷰를 연결
        majorSpinner.setOnItemSelectedListener(this); //아래의 onItemSeleted의 이벤트를 리스너로 지정

    }
    public void addSegmentSpinner(String inmajor) {
        subSpinner = (Spinner) getActivity().findViewById(R.id.subMajor);
        ArrayAdapter subadpater;
        switch (inmajor) {
            case "대학선택":
                subadpater =ArrayAdapter.createFromResource(getActivity(),R.array.select,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "미디어커뮤니케이션대학":
                subadpater =ArrayAdapter.createFromResource(getActivity(),R.array.media,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "디자인대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.design, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "국제비즈니스대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.global, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "공공인재대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.common, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "과학기술대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.science,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "의료생명대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.medical,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;

        }
    }
    public void addSecondTalent(String secondTalent){
        ArrayAdapter talentAdapter2;
        switch (secondTalent) {
            case "무관":
                talentAdapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.select,android.R.layout.simple_spinner_item);
                talentAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                talentSpinner2.setAdapter(talentAdapter2);
                talentSpinner2.setOnItemSelectedListener(this);
                ArrayAdapter talentAdapter3 = ArrayAdapter.createFromResource(getActivity(),R.array.select,android.R.layout.simple_spinner_item);
                talentAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                talentSpinner3.setAdapter(talentAdapter3);
                talentSpinner3.setOnItemSelectedListener(this);
                break;
            default:
                talentAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.total_talent, android.R.layout.simple_spinner_item);
                talentAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                talentSpinner2.setAdapter(talentAdapter2);
                talentSpinner2.setOnItemSelectedListener(this);
                //talent3=talentSpinner3.getSelectedItem().toString();
                break;
        }
    }
    public void addThirdTalent(String ThirdTalent){
        ArrayAdapter talentAdapter3;
        switch (ThirdTalent) {
            case "무관":
                talentAdapter3 = ArrayAdapter.createFromResource(getActivity(),R.array.select,android.R.layout.simple_spinner_item);
                talentAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                talentSpinner3.setAdapter(talentAdapter3);
                talentSpinner3.setOnItemSelectedListener(this);
                talent2="";
                talent3="";
                break;
            default:
                talentAdapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.total_talent, android.R.layout.simple_spinner_item);
                talentAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                talentSpinner3.setAdapter(talentAdapter3);
                talentSpinner3.setOnItemSelectedListener(this);
                break;
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //int id = parent.getId();
        switch (parent.getId()) {
            case R.id.major:
                addSegmentSpinner(parent.getItemAtPosition(position).toString());
                if(majorSpinner.getSelectedItem().equals("대학선택")){
                    major="대학선택";
                    subMajor="학과선택";
                }
                else {
                    major=parent.getItemAtPosition(position).toString();
                }
                break;
            case R.id.subMajor:
                subMajor=(String)subSpinner.getSelectedItem();
                break;

            case R.id.spinner:
                addSecondTalent(parent.getItemAtPosition(position).toString());
                if(talentSpinner1.getSelectedItem().equals("무관")){
                    talent1=talentSpinner1.getSelectedItem().toString();
                    talent2="";
                    talent3="";
                }
                else {
                    talent1=talentSpinner1.getSelectedItem().toString();
                }
                break;
            case R.id.spinner2:
                addThirdTalent(parent.getItemAtPosition(position).toString());
                if(talentSpinner2.getSelectedItem().equals("무관")){
                    talent2="";
                    talent3="";
                }
                else {
                    talent2=talentSpinner2.getSelectedItem().toString();
                }
                break;
            case R.id.spinner3:
                if(talentSpinner3.getSelectedItem().equals("무관")){
                    talent3="";
                }
                else {
                    talent3=talentSpinner3.getSelectedItem().toString();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void showList() {
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
                String category=c.getString("category");
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
                writing.put("category",category);
                writing.put(TAG_TALENT,talent);
                writing.put(TAG_CONTENTS, contents);
                writing.put(TAG_FILEPATH,filePath);
                boardList.add(writing);
               // Toast.makeText(getActivity(),""+boadNumber+id+title+dayResult+writeDate+type+major+subMajor+startHour+endHour+talent+contents, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSearchData(String gender, String type, String major,String subMajor, String dayResult, String startHour, String endHour, String talent1, String talent2, String talent3){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String gender = params[0];
                    String type = params[1];
                    String major = params[2];
                    String subMajor = params[3];
                    String dayResult = params[4];
                    String startHour = params[5];
                    String endHour = params[6];
                    String talent1=params[7];
                    String talent2=params[8];
                    String talent3=params[9];

                    String link ="http://1.243.135.179:8080/search.php";
                    String data = URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8");
                    data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                    data += "&" + URLEncoder.encode("major", "UTF-8") + "=" + URLEncoder.encode(major, "UTF-8");
                    data += "&" + URLEncoder.encode("subMajor", "UTF-8") + "=" + URLEncoder.encode(subMajor, "UTF-8");
                    data += "&" + URLEncoder.encode("dayResult", "UTF-8") + "=" + URLEncoder.encode(dayResult, "UTF-8");
                    data += "&" + URLEncoder.encode("startHour", "UTF-8") + "=" + URLEncoder.encode(startHour, "UTF-8");
                    data += "&" + URLEncoder.encode("endHour", "UTF-8") + "=" + URLEncoder.encode(endHour, "UTF-8");
                    data += "&" + URLEncoder.encode("talent1", "UTF-8") + "=" + URLEncoder.encode(talent1, "UTF-8");
                    data += "&" + URLEncoder.encode("talent2", "UTF-8") + "=" + URLEncoder.encode(talent2, "UTF-8");
                    data += "&" + URLEncoder.encode("talent3", "UTF-8") + "=" + URLEncoder.encode(talent3, "UTF-8");

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
                }
                catch(Exception e){
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
                if (!(result.equalsIgnoreCase("해당하는 검색결과가 없습니다."))) {
                    //Toast.makeText(getActivity(),result, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),"성공했습니다.", Toast.LENGTH_SHORT).show();
                    myJSON=result;
                    showList();
                    actionBar.setSubtitle("인재검색 결과 : 총 "+board.length()+"개");
                    FragmentManager fm=getFragmentManager();
                    Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                    FragmentTransaction tr;
                    modeBuyTimeFrag3_list = new Mode_BuyTime_Frag3_list(boardList);
                    tr =fm.beginTransaction();
                    tr.replace(R.id.maincontainer2, modeBuyTimeFrag3_list,"modeBuyTimeFrag3_list");
                    tr.commit();
                }
                else {
                    Toast.makeText(getActivity(),result+"옆의 오류로 실패했습니다." , Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }

            
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(gender,type,major,subMajor,dayResult,startHour,endHour,talent1,talent2,talent3);
    }

}
