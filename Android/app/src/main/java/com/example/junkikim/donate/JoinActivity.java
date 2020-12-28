package com.example.junkikim.donate;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class JoinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //생년원일 입력시 필요한 변수
    int year, month, day;
    ImageButton calButton;

    //단과별 대학 및 전공선택을 위한 Spinner와 ArrayList
    Spinner majorSpinner, subSpinner;

    //BackPress를 눌렀을시 필요한 클래스를 객체로 만듬.
    BackPressCloseHandler backPressCloseHandler;

    //회원가입에 필요한 정보 text,spinner,radio버튼 객체(변수)들
    EditText idText,passwordText, passwordCheckText, nameText, phoneText, birthText;
    RadioGroup genderGroup;
    RadioButton genderMale,genderFemale;
    String id, password, name, phone, birth, gender, passwordCheck,major,subMajor; //데이터베이스 넣기 위한 문자열


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //생년월일 객체
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        //BackPressCloseHandler객체에 context를 주고 또한 메소드를 이용하기위하여 선언
        backPressCloseHandler = new BackPressCloseHandler(this);


        //회원가입에 필요한 정보를 id를 통하여 변수에 저장.
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        birthText = (EditText) findViewById(R.id.birthText);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        genderMale=(RadioButton)findViewById(R.id.gender1);
        genderFemale=(RadioButton)findViewById(R.id.gender2);
        passwordCheckText = (EditText) findViewById(R.id.passwordCheckText);
        calButton=(ImageButton)findViewById(R.id.calbutton);

        //단과별대학 추가 메소드
        addMajorSpinner();

        /*
        //자동완성기능 추가 예정
        String[] idList=getResources().getStringArray(R.array.id);
        ArrayAdapter<String> arid = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,idList);
        idText.setAdapter(arid);
        */

        calButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             new DatePickerDialog(JoinActivity.this,dataSetListener,year,month,day).show();
                                         }
                                     }
        );
    }
    private DatePickerDialog.OnDateSetListener dataSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String msg = String.format("%d%02d%02d", year,monthOfYear+1, dayOfMonth);
            birthText.setText(msg);
            //Toast.makeText(JoinActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
    };

    //백버튼 눌렸을때 이벤트처리
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

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
            Toast.makeText(getApplicationContext(), "정보를 다 입력해 주세요.", Toast.LENGTH_LONG).show();
        }
        //모든 정보가 null이 아닐때
        else
        {
            if (!password.equals(passwordCheck)) {
                Toast.makeText(getApplicationContext(), "패스워드가 다릅니다.", Toast.LENGTH_LONG).show();
            }
            else {
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                String token= FirebaseInstanceId.getInstance().getToken();
                ////Log.d("token",token);

                //AsyncTask로 데이터베이스를 입력하기 위하여 아래 메소드안에 InnerClass로 작성.
                insertToDatabase(id, password, name, phone, birth, gender,major,subMajor,token);
                Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }

    //단과별대학 메소드
    public void addMajorSpinner() {
        //데이터 준비
        String[] majorList=getResources().getStringArray(R.array.major);
        //어댑터 준비 현재 컨텍스트(액티비티),표시할 레이아웃형식(기본 안드로이드 dropdown이용),데이터 원본)
        ArrayAdapter<String> majorAdpater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,majorList);
        //스피너와 어댑터 연결
        majorSpinner = (Spinner) findViewById(R.id.major);
        majorSpinner.setAdapter(majorAdpater); //어댑터 객체와  뷰를 연결
        majorSpinner.setOnItemSelectedListener(this); //아래의 onItemSeleted의 이벤트를 리스너로 지정
    }

    public void addSegmentSpinner(String inmajor) {
        subSpinner = (Spinner) findViewById(R.id.subMajor);
        ArrayAdapter subadpater;
        switch (inmajor) {
            case "대학선택":
                subadpater =ArrayAdapter.createFromResource(this,R.array.select,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //ArrayAdapter<String> adpater0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subList);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "미디어커뮤니케이션대학":
                subadpater =ArrayAdapter.createFromResource(this,R.array.media,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "디자인대학":
                subadpater = ArrayAdapter.createFromResource(this, R.array.design, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "국제비즈니스대학":
                subadpater = ArrayAdapter.createFromResource(this, R.array.global, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "공공인재대학":
                subadpater = ArrayAdapter.createFromResource(this, R.array.common, android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "과학기술대학":
                subadpater = ArrayAdapter.createFromResource(this, R.array.science,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
                break;
            case "의료생명대학":
                subadpater = ArrayAdapter.createFromResource(this, R.array.medical,android.R.layout.simple_spinner_item);
                subadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner.setAdapter(subadpater);
                subSpinner.setOnItemSelectedListener(this);
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
                major=majorSpinner.getSelectedItem().toString();
                subMajor=(String)subSpinner.getSelectedItem();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void insertToDatabase(String id, String password, String name, String phone, String birth, String gender,String major,String subMajor,String token) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            // Background 작업 시작전에 UI 작업을 진행 한다.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinActivity.this, "Please Wait", null, true, true);
            }

            //Background 작업이 끝난 후 UI 작업을 진행 한다.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ////Log.d("join",s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
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
                    String token = (String)params[8];

                    String link = "http://1.243.135.179:8080/insert.php/";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                    data += "&" + URLEncoder.encode("birth", "UTF-8") + "=" + URLEncoder.encode(birth, "UTF-8");
                    data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    data += "&" + URLEncoder.encode("major", "UTF-8") + "=" + URLEncoder.encode(major, "UTF-8");
                    data += "&" + URLEncoder.encode("subMajor", "UTF-8") + "=" + URLEncoder.encode(subMajor, "UTF-8");
                    data += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
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
        task.execute(id, password, name, phone, birth, gender,major,subMajor,token);
    }
}

