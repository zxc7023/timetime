package com.example.junkikim.donate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.junkikim.donate.communicateServerByPHP.ModifyBoard_dbName_talent_buy_posting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by junkikim on 2016-09-21.
 */
public class Mode_BuyTime_Frag2_list_detail_modify extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int PICK_FROM_ALBUM=1004;
    String mlmgPath=null;
    String mlmgTitle = null;
    int idValue,countValue;



    String id;
    String title,writeDate,type,category,talent,startHour,endHour,contents;  //데이터베이스에 입력해야하는 문자열

    EditText titleTextView,startHourText, endHourText,contentsText;
    Spinner categorySpinner, talentSpinner;
    RadioGroup typeRadioGroup;
    RadioButton rb1,rb2;
    Button write;

    RadioGroup dayRadioGroup;
    RadioButton weekdayButton,weekendButton,directSelectButton;
    LinearLayout directSeletPage;
    String dayResult;

    CheckBox daysCheckBox[];
    int[] dayBit=new int[]{64,32,16,8,4,2,1};
    int tmp =0;

    int cPosition,tPosition;
    ImageView[] groundImageView;
    //groundImageView2,groundImageView3;

    int s_hour,s_minute,f_hour,f_minute;
    int TimeFlag; //위의 변수 시작 시간,분과 종료 시간,분으로 나누어 저장하기위한 변수

    Bitmap bmImg;

    HashMap<String,String> sendHash;


    HashMap<String, String> boardHash;
    public Mode_BuyTime_Frag2_list_detail_modify(HashMap<String, String> boardHash, Bitmap bmImg) {
        this.boardHash = boardHash;
        this.bmImg=bmImg;
    }
    public Mode_BuyTime_Frag2_list_detail_modify(HashMap<String, String> boardHash, Bitmap bmImg,int countValue) {
        this.boardHash = boardHash;
        this.bmImg=bmImg;
        this.countValue=countValue;
    }


    public Mode_BuyTime_Frag2_list_detail_modify() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2_board_list_detail_modify, container, false);
        //수정할 정보를 받기위하여 Ui를 root로 불러옴

        typeRadioGroup = (RadioGroup)root.findViewById(R.id.type);
        //일반과 재능기부를 구분해주는 라디오버튼의 상위 뷰

        rb1=(RadioButton)root.findViewById(R.id.nomal);
        rb2=(RadioButton)root.findViewById(R.id.present);
        type=boardHash.get("type");
        //일반과 재능기부에 해당하는 라디오버튼의 변수 선언, 인수로 받은 Hashpmap에 저장된 type을 불러옴.

        if(type.equals("일반")){
            rb1.setChecked(true);
        }
        else{
            rb2.setChecked(true);
        }
        //type에 저장된 문자열을 비교하여 해당하는 버튼을 체크

        titleTextView = (EditText) root.findViewById(R.id.title);
        titleTextView.setText(boardHash.get("title"));
        //제목을 받아들일 EditText 변수 선언 , 인수로받은 Hashmap에 저장된 title을 불러옴

        startHourText = (EditText) root.findViewById(R.id.startHour);
        startHourText.setText(boardHash.get("startHour"));
        //시작시간을 받아들일 EditText 변수 선언 , 인수로받은 Hashmap에 저장된 startHour을 불러옴

        endHourText = (EditText) root.findViewById(R.id.endHour);
        endHourText.setText(boardHash.get("endHour"));
        //종료시간을 받아들일 EditText 변수 선언 , 인수로받은 Hashmap에 저장된 endHour을 불러옴

        startHourText.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFlag = 0;
                new TimePickerDialog(getActivity(), timeSetListener, s_hour, 0, false).show();

            }
        });
        endHourText.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFlag = 1;
                new TimePickerDialog(getActivity(), timeSetListener, f_hour, 0, false).show();
            }
        });
        //시작시간과 종료시간을 timePicker로 시간을 받아옴 timeFlag에 따라 시작시간과 종료시간을 저장

        categorySpinner = (Spinner) root.findViewById(R.id.category);
        String[] tmp =getResources().getStringArray(R.array.category);
        for(int i=0;i<tmp.length;i++){
            if(tmp[i].equals(boardHash.get("category"))){
                cPosition=i;
            }
        }
        //재능을 입력하는 스피너중 첫 번째 분류 카테고리를 선택하는 스피너
        //array.xml에 있는 배열tmp에 받아옴.
        //전달받은 인수중에 해당하는 카테고리를 찾아 스피너의 위치를 cPosition에 저장
        //저장한 후에 위치를 set해주기위해서

        talentSpinner = (Spinner) root.findViewById(R.id.talent);
        //재능을 입력하는 스피너중 두 번째 분류 재능을 선택하는 스피너

        contentsText = (EditText)root.findViewById(R.id.contents);
        contentsText.setText(boardHash.get("contents"));
        //내용을 입력하는 EditText 변수 선언 , 인수로 받은 Hashmap에 저장된 contents를 불러옴.


        dayRadioGroup=(RadioGroup)root.findViewById(R.id.dayGroup);
       //요일을 입력받는 라디오버튼의 상위 View
        weekdayButton=(RadioButton)root.findViewById(R.id.weekday);
        weekendButton=(RadioButton)root.findViewById(R.id.weekend);
        directSelectButton=(RadioButton)root.findViewById(R.id.directSelectButton);
        //요일에 해당하는 라디오버튼 평일,주말,직접선택에 해당.

        directSeletPage =(LinearLayout)root.findViewById(R.id.directSelectPage);
        //직접선택을 누르면 보이는 View

        daysCheckBox = new CheckBox[]
                {(CheckBox)root.findViewById(R.id.sun),
                        (CheckBox)root.findViewById(R.id.mon),
                        (CheckBox)root.findViewById(R.id.tue),
                        (CheckBox)root.findViewById(R.id.wen),
                        (CheckBox)root.findViewById(R.id.thu),
                        (CheckBox)root.findViewById(R.id.fri),
                        (CheckBox)root.findViewById(R.id.sat),
                };
        //각각의 요일을 배열형식으로 CheckBox 선언

        directSelectButton.setChecked(true);
        directSeletPage.setVisibility(View.VISIBLE);
        //입력받은 Hash의 요일을 보여주기위해 먼저 보이도록 실행

        dayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                }
            }
        });
        //dayRadioGroup의 cheke버튼 이벤트 리스너
        //dayRadioGroub에서 평일이면 62 (32+16+8+4+2)  주말이면 65 (62+1) 직접선택이면 해당하는 페이지를 보여주고
        //입력받은 체크박스의 값을 And연산을 하여 DB에 값을 넣어줌

        dayResult=boardHash.get("dayResult");
        int dayValue = Integer.parseInt(dayResult);
        //String 타입으로 받은 dayResult의 결과를 int로 바꿔주고 그러한 정수형의 숫자를 And연산을 통하여 체크버튼을 눌러주고자 함


        for(int i=0;i<dayBit.length;i++){
            int tmp2=0;
            tmp2 =dayBit[i]&dayValue;
            if(tmp2!=0){
                daysCheckBox[i].setChecked(true);
            }
        }
        //dayBit 즉 일월화수목금토  64,32,16,8,4,2,1 로 정의해둔 변수
        //for문안에서 해당하는 dayBit와 dayValue를 And연산을함 이러한 연산을 통해 0이아니면 해당하는 요일을 체크



        groundImageView = new ImageView[]{
                (ImageView)root.findViewById(R.id.mod_ground),(ImageView)root.findViewById(R.id.mod_ground2),(ImageView)root.findViewById(R.id.mod_ground3)
        };
        //이미지 뷰 변수를 3개 선언

        for(int i=0;i<groundImageView.length;i++){
            final int finalI = i;
            groundImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countValue= finalI;
                    Intent intent = new Intent(Intent.ACTION_PICK); // ACTION_PICK 이라는 이름을 가지고 있는 Intent를 호출해라. -> 이미지 선택하는 인탠트를 가져와라
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            });
        }
        // 이미지뷰 3개의 이벤트리스너

        if(bmImg!=null){
            groundImageView[0].setImageBitmap(bmImg);
        }
        //image가 있다면 imageView를

        write = (Button)root.findViewById(R.id.upload);
        write.setText("수정하기");
        write.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Post(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //아래의 전송버튼
        addcategorySpinner(root);

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                getImageNameToUri(uri);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap src = BitmapFactory.decodeFile(mlmgPath, options);
                    //Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    groundImageView[countValue] = (ImageView)getActivity().findViewById(idValue);
                    groundImageView[countValue].setImageBitmap(src);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void getImageNameToUri(Uri data)
    {
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE
        };
        //Data는 이미지 파일의 스트림 데이터 경로를 의미, 어떤 column을 빼낼지 정보

        Cursor cursor = getActivity().getContentResolver().query(data, proj, null, null, null);
        cursor.moveToFirst(); //Cursor를 제일 첫번째 행(Row)으로 이동 시킨다.

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);

        mlmgPath=cursor.getString(column_data);
        mlmgTitle=cursor.getString(column_title);
        //Log.d(mlmgPath,"path");
       //Log.d(mlmgTitle,"title");


        //String imgPath = cursor.getString(column_data);
        //lastIndexOf = 문자열에서 탐색하는 문자열이 마지막으로 등장하는 위치에 대한 index를 반환
        //substring = 해당하는 문자열부터 추출
        //String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
    }

    public void addcategorySpinner(View root){
        //데이터 준비
        String[] categoryList=getResources().getStringArray(R.array.category);
        //어댑터 준비 현재 컨텍스트(액티비티),표시할 레이아웃형식(기본 안드로이드 dropdown이용),데이터 원본)
        ArrayAdapter<CharSequence> categoryAdpater = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item,categoryList);
        //스피너와 어댑터 연결
        categorySpinner = (Spinner)root.findViewById(R.id.category);
        categorySpinner.setAdapter(categoryAdpater); //어댑터 객체와  뷰를 연결
        categorySpinner.setOnItemSelectedListener(this); //아래의 onItemSeleted의 이벤트를 리스너로 지정
        categorySpinner.setSelection(cPosition);
    }

    public void addSegmentSpinner(String subCategory) {
        talentSpinner = (Spinner) getActivity().findViewById(R.id.talent);
        ArrayAdapter talentadpater;
        String[] tmp;

        HashMap<String, Integer> checkTalet1 = new HashMap<String, Integer>();
        checkTalet1.put("카테고리선택",R.array.select);
        checkTalet1.put("디자인",R.array.Design);
        checkTalet1.put("미디어",R.array.Media);
        checkTalet1.put("사무",R.array.office);
        checkTalet1.put("IT",R.array.IT);
        checkTalet1.put("어학",R.array.language);
        checkTalet1.put("생활",R.array.life);
        checkTalet1.put("기타",R.array.etc);

        talentadpater=ArrayAdapter.createFromResource(getActivity(),checkTalet1.get(subCategory),android.R.layout.simple_spinner_item);
        talentSpinner.setAdapter(talentadpater);
        talentSpinner.setOnItemSelectedListener(this);
        tmp=getResources().getStringArray(checkTalet1.get(subCategory));
        for(int i=0;i<tmp.length;i++){
            if(tmp[i].equals(boardHash.get("talent"))){
                tPosition=i;
            }
        }
        talentSpinner.setSelection(tPosition);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.category:
                category=parent.getItemAtPosition(position).toString();
                addSegmentSpinner(category);
                break;
            case R.id.talent:
                talent=(String)talentSpinner.getSelectedItem();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d", hourOfDay, minute);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            if(TimeFlag==0){
                startHourText.setText(msg);
            }
            else{
                endHourText.setText(msg);
            }
        }
    };

    private void Post(View view) throws ExecutionException, InterruptedException {
        ProgressDialog loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);
        //Toast.makeText(getActivity(),formatDate,Toast.LENGTH_SHORT).show();
        id=LoginActivity.id;
        title = titleTextView.getText().toString();
        writeDate=formatDate;
        if(typeRadioGroup.getCheckedRadioButtonId()==R.id.nomal)
            type= rb1.getText().toString();
        else if(typeRadioGroup.getCheckedRadioButtonId()==R.id.present)
            type=rb2.getText().toString();
        category=categorySpinner.getSelectedItem().toString();
        talent=talentSpinner.getSelectedItem().toString();
        startHour=startHourText.getText().toString();
        endHour=endHourText.getText().toString();
        contents=contentsText.getText().toString();

        if(directSelectButton.isChecked()){
            tmp=0;
            for(int i=0;i<daysCheckBox.length;i++){
                if(daysCheckBox[i].isChecked()){
                    tmp=tmp|dayBit[i];
                }
            }
            dayResult= Integer.toString(tmp);
        }else {

        }


        //Toast.makeText(getActivity(),id+"\n"+title+"\n"+writeDate+"\n"+type+"\n"+startHour+"\n"+endHour+"\n"+category+"\n"+talent+"\n"+contents+"\n"+boardHash.get("boardNumber")+mlmgPath,Toast.LENGTH_LONG).show();
        if(id!=null||title!=null||writeDate!=null||type!=null||startHour!=null||endHour!=null||talent!=null||contents!=null) {

            ////Log.d("postValuie",id+title+dayResult+writeDate+type+startHour+endHour+category+talent+contents+boardHash.get("boardNumber"));
            //insertToBoard(id, title, dayResult, writeDate, type, startHour, endHour, category, talent, contents,boardHash.get("boardNumber"));

           // sendHash=task.execute(id,title,dayResult,writeDate,type,startHour,endHour,category,talent,contents,boardHash.get("boardNumber"),mlmgPath,link).get();
            sendHash= new HashMap<String, String>();

            sendHash.put("boardNumber", boardHash.get("boardNumber"));
            sendHash.put("id", id);
            sendHash.put("title", title);
            sendHash.put("dayResult", dayResult);
            sendHash.put("gender", boardHash.get("gender"));
            sendHash.put("writeDate", writeDate);
            sendHash.put("type", type);
            sendHash.put("major",boardHash.get("major"));
            sendHash.put("subMajor",boardHash.get("subMajor"));
            sendHash.put("startHour", startHour);
            sendHash.put("endHour", endHour);
            sendHash.put("category", category);
            sendHash.put("talent", talent);
            sendHash.put("contents", contents);
            String link = "http://1.243.135.179:8080/modify_talent_sale_posting.php";
            if (mlmgPath != null) {
                int lastIndex = mlmgPath.lastIndexOf("/");
                sendHash.put("filePath", "uploads/" + mlmgPath.substring(lastIndex + 1));
                //Toast.makeText(getActivity(), "" + sendHash.get("filePath"), Toast.LENGTH_SHORT).show();
            } else {
                sendHash.put("filePath", boardHash.get("filePath"));
                //Toast.makeText(getActivity(), "" + boardHash.get("filePath"), Toast.LENGTH_SHORT).show();
            }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tr = fm.beginTransaction();
            Fragment viewFragment;
            int layout;
            if (((ViewGroup) getView().getParent()).getId() == R.id.maincontainer1) {
                viewFragment = fm.findFragmentById(R.id.maincontainer1);
                layout = R.id.maincontainer1;
            } else {
                viewFragment = fm.findFragmentById(R.id.maincontainer2);
                layout = R.id.maincontainer2;
            }
            Mode_BuyTime_Frag2_list_detail mode_buyTime_frag2_list_detail = new Mode_BuyTime_Frag2_list_detail(sendHash);
            ModifyBoard_dbName_talent_buy_posting modify = new ModifyBoard_dbName_talent_buy_posting(fm, viewFragment, tr, mode_buyTime_frag2_list_detail, layout, loading, getActivity());
            modify.execute(id, title, dayResult, writeDate, type, startHour, endHour, category, talent, contents, boardHash.get("boardNumber"), mlmgPath, link);
        }
        else
        {
            Toast.makeText(getActivity(),"정보를 다 입력해주세요.",Toast.LENGTH_LONG).show();
        }
    }
}