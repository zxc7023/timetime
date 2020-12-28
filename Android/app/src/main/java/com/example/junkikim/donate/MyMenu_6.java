package com.example.junkikim.donate;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by junkikim on 2016-11-13.
 */
public class MyMenu_6 extends Fragment implements AdapterView.OnItemSelectedListener {



    HashMap<String,String>tmpHash;

    String id=LoginActivity.id;  // id를 저장할 변수

    TextView titleTextView; // activity_join.xml의 title을 프로필로 바꾸기위해서 변수가필요.
    Button modifyButton; //activity_join.xml의 title을 프로필로 바꾸고, 수정을 위해서 변수가 필요

    //생년원일 입력시 필요한 변수
    int year, month, day;
    ImageButton calButton;

    //단과별 대학 및 전공선택을 위한 Spinner와 ArrayList
    Spinner majorSpinner, subSpinner;

    //회원가입에 필요한 정보 text,spinner,radio버튼 객체(변수)들
    EditText idText,passwordText, passwordCheckText, nameText, phoneText, birthText;
    RadioGroup genderGroup;
    RadioButton genderMale,genderFemale;
    String password, name, phone, birth, gender, passwordCheck,major,subMajor; //데이터베이스 넣기 위한 문자열


    String tmpMajor,tmpSubMajor;
    ProgressDialog loading;
    int mPosition,sPosition;

    public MyMenu_6(HashMap<String, String> infoHash) {
        tmpHash=infoHash;
        //Log.d("test4",tmpHash.get("major"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_join, container, false);
        MyMenu myMenu = new MyMenu();
        /*
        if(myMenu.getContainerId().equals("maincontainer1")){
            ActionBar actionBar;
            actionBar = ((Mode_SellTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  프로필");
        }
        else {
            ActionBar actionBar;
            actionBar = ((Mode_BuyTime_MainActivity)getActivity()).getSupportActionBar();
            actionBar.setSubtitle("마이메뉴  -  프로필");
        }
        */
        titleTextView= ((TextView)root.findViewById(R.id.titleTextView));
        titleTextView.setText("프로필");
        modifyButton=(Button)root.findViewById(R.id.joinMemberButton);
        modifyButton.setText("수정하기");

        //생년월일 객체
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        //회원가입에 필요한 정보를 id를 통하여 변수에 저장.
        idText = (EditText)root.findViewById(R.id.idText);
        idText.setClickable(false);
        passwordText = (EditText) root.findViewById(R.id.passwordText);
        nameText = (EditText) root.findViewById(R.id.nameText);
        phoneText = (EditText) root.findViewById(R.id.phoneText);
        birthText = (EditText)root. findViewById(R.id.birthText);
        genderGroup = (RadioGroup) root.findViewById(R.id.genderGroup);
        genderMale=(RadioButton)root.findViewById(R.id.gender1);
        genderFemale=(RadioButton)root.findViewById(R.id.gender2);
        passwordCheckText = (EditText)root. findViewById(R.id.passwordCheckText);
        calButton=(ImageButton)root.findViewById(R.id.calbutton);
        majorSpinner = (Spinner) root.findViewById(R.id.major);
        if(tmpHash!=null){
            idText.setText(tmpHash.get("id"));
            phoneText.setText(tmpHash.get("phone"));
            birthText.setText(tmpHash.get("birth"));
            nameText.setText(tmpHash.get("name"));
            if(tmpHash.get("gender").equals("남자")){
                genderMale.setChecked(true);
            }
            else{
                genderFemale.setChecked(true);
            }
            String[] tmpRes=getResources().getStringArray(R.array.major);
            for(int i=0;i<tmpRes.length;i++){
                if(tmpRes[i].equals(tmpHash.get("major"))) {
                    mPosition = i;
                }
            }
        }


        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(v);
            }
        });
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),dataSetListener,year,month,day).show();
            }
        });
        addMajorSpinner(root);
        return root;
    }
   

    private DatePickerDialog.OnDateSetListener dataSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String msg = String.format("%d%02d%02d", year,monthOfYear+1, dayOfMonth);
            birthText.setText(msg);
            //Toast.makeText(JoinActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    //가입 버튼에서 OnClicked 속성을 사용한 메소드
    //각각의 EditText,RadioGroup,Spinner에 입력된 값을 가져와서 텍스트에 저장.
    public void insert(View view) {
        id = idText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        passwordCheck = passwordCheckText.getText().toString().trim();
        name = nameText.getText().toString().trim();
        phone = phoneText.getText().toString().trim();
        birth = birthText.getText().toString().trim();
        if(genderGroup.getCheckedRadioButtonId()==R.id.gender1){
            gender=genderMale.getText().toString();
        }
        else if(genderGroup.getCheckedRadioButtonId()==R.id.gender2) {
            gender = genderFemale.getText().toString();
        }
        else
            gender=null;

        //정보가 전부다 입력 되었는지 확인하고 위에 조건문에서 처리된것을 디비에 전송
        if (id == null || id.equals("") || password == null || password.equals("") || name == null || name.equals("") ||
                phone == null || phone.equals("") || birth == null || birth.equals("") || gender==null||major.equals("대학선택"))
        {
            //정보가 null이 아니면 디비에 넣음.
            Toast.makeText(getActivity(), "정보를 다 입력해 주세요.", Toast.LENGTH_LONG).show();
        }
        //모든 정보가 null이 아닐때
        else
        {
            if (!password.equals(passwordCheck)) {
                Toast.makeText(getActivity(), "패스워드가 다릅니다.", Toast.LENGTH_LONG).show();
            }
            else {
                //AsyncTask로 데이터베이스를 입력하기 위하여 아래 메소드안에 InnerClass로 작성.
               updateToDatabase(id, password, name, phone, birth, gender,major,subMajor);
            }
        }
    }

    //단과별대학 메소드
    public void addMajorSpinner(View v) {
        //데이터 준비
        String[] majorList=getResources().getStringArray(R.array.major);
        //어댑터 준비 현재 컨텍스트(액티비티),표시할 레이아웃형식(기본 안드로이드 dropdown이용),데이터 원본)
        ArrayAdapter<CharSequence> majorAdpater = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item,majorList);
        //스피너와 어댑터 연결
        majorSpinner = (Spinner) v.findViewById(R.id.major);
        majorSpinner.setAdapter(majorAdpater); //어댑터 객체와  뷰를 연결
        majorSpinner.setOnItemSelectedListener(this); //아래의 onItemSeleted의 이벤트를 리스너로 지정
        majorSpinner.setSelection(mPosition);
    }

    public void addSegmentSpinner(String inmajor) {
        subSpinner = (Spinner) getActivity().findViewById(R.id.subMajor);
        ArrayAdapter subadpater;
        String tmp[];
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

                tmp =getResources().getStringArray(R.array.media);
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }

                subSpinner.setSelection(sPosition);
                break;
            case "디자인대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.design, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                tmp =getResources().getStringArray(R.array.design);

                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }
                subSpinner.setSelection(sPosition);
                break;
            case "국제비즈니스대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.global, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);

                tmp =getResources().getStringArray(R.array.global);
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }
                subSpinner.setSelection(sPosition);
                break;
            case "공공인재대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.common, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);

                tmp =getResources().getStringArray(R.array.common);
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }
                subSpinner.setSelection(sPosition);
                break;
            case "과학기술대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.science,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                tmp =getResources().getStringArray(R.array.science);
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }
                subSpinner.setSelection(sPosition);
                break;
            case "의료생명대학":
                subadpater = ArrayAdapter.createFromResource(getActivity(), R.array.medical,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                tmp =getResources().getStringArray(R.array.medical);
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].equals(tmpHash.get("subMajor"))){
                        sPosition=i;
                    }
                }
                subSpinner.setSelection(sPosition);
                break;

        }
    }

    //인자 어댑터뷰,클릭된뷰,항목의 위치,id
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //int id = parent.getId();
        switch (parent.getId()) {
            case R.id.major:
                addSegmentSpinner(parent.getItemAtPosition(position).toString());
                major=parent.getItemAtPosition(position).toString();

                break;
            case R.id.subMajor:
               // major=majorSpinner.getSelectedItem().toString();
                subMajor=(String)subSpinner.getSelectedItem();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void updateToDatabase(String id, String password, String name, String phone, String birth, String gender,String major,String subMajor) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            // Background 작업 시작전에 UI 작업을 진행 한다.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            //Background 작업이 끝난 후 UI 작업을 진행 한다.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                    MyMenu myMenu =new MyMenu();
                    FragmentManager fm= getFragmentManager();
                    FragmentTransaction tr;
                    tr = fm.beginTransaction();

                    if(((ViewGroup) getView().getParent()).getId()==R.id.maincontainer1){
                        Fragment fragment = fm.findFragmentById(R.id.maincontainer1);
                        if(fragment==null) {
                            tr.remove(fragment);
                        }
                        tr.replace(R.id.maincontainer1, myMenu, "myMenu");
                    }
                    else {
                        Fragment fragment=fm.findFragmentById(R.id.maincontainer2);
                        if(fragment==null) {
                            tr.remove(fragment);
                        }
                        tr.replace(R.id.maincontainer2,myMenu , "myMenu");
                    }

                    tr.commit();
                }
                else {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                }
                loading.dismiss();

            }

            //Background 작업을 진행 한다.
            @Override
            protected String doInBackground(String... params) {
                //id,password,name,phone,birth,gender
                try {
                    String id = (String) params[0];
                    String password = (String) params[1];
                    String name = (String) params[2];
                    String phone = (String) params[3];
                    String birth = (String) params[4];
                    String gender = (String) params[5];
                    String major =(String)params[6];
                    String subMajor = (String)params[7];

                    String link = "http://1.243.135.179:8080/mod_profile.php/";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                    data += "&" + URLEncoder.encode("birth", "UTF-8") + "=" + URLEncoder.encode(birth, "UTF-8");
                    data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    data += "&" + URLEncoder.encode("major", "UTF-8") + "=" + URLEncoder.encode(major, "UTF-8");
                    data += "&" + URLEncoder.encode("subMajor", "UTF-8") + "=" + URLEncoder.encode(subMajor, "UTF-8");
                    //data=data.replaceAll("<br />", "\n");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(id, password, name, phone, birth, gender,major,subMajor);
    }
}
