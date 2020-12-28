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

/**
 * Created by junkikim on 2016-09-21.
 */
public class Mode_SellTime_Frag2_list_detail_modify extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int PICK_FROM_ALBUM = 1004;
    String mlmgPath = null;
    String mlmgTitle = null;
    int idValue, countValue;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";


    String id;
    String title, writeDate, type, category, talent, startHour, endHour, contents;  //데이터베이스에 입력해야하는 문자열


    EditText titleText, startHourText, endHourText, contentsText;
    Spinner categorySpinner, talentSpinner;
    RadioGroup radioGroup;
    RadioButton rb1, rb2;
    Button write;

    RadioGroup dayGroup;
    RadioButton weekdayButton, weekendButton, directSelectButton;
    LinearLayout directSeletPage;
    String dayResult;

    CheckBox cb[];
    int[] dayBit = new int[]{64, 32, 16, 8, 4, 2, 1};
    int tmp = 0;

    int cPosition, tPosition;
    ImageView[] groundImageView;
    //groundImageView2,groundImageView3;

    int s_hour, s_minute, f_hour, f_minute;
    int TimeFlag; //위의 변수 시작 시간,분과 종료 시간,분으로 나누어 저장하기위한 변수

    ProgressDialog dialog;

    Bitmap bmImg;

    HashMap<String, String> sendHash;

    HashMap<String, String> boardHash;

    public Mode_SellTime_Frag2_list_detail_modify(HashMap<String, String> boardHash, Bitmap bmImg) {
        this.boardHash = boardHash;
        this.bmImg = bmImg;
    }

    public Mode_SellTime_Frag2_list_detail_modify() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2_board_list_detail_modify, container, false);
        radioGroup = (RadioGroup) root.findViewById(R.id.type);
        titleText = (EditText) root.findViewById(R.id.title);
        titleText.setText(boardHash.get("title"));
        startHourText = (EditText) root.findViewById(R.id.startHour);
        startHourText.setText(boardHash.get("startHour"));
        endHourText = (EditText) root.findViewById(R.id.endHour);
        endHourText.setText(boardHash.get("endHour"));
        categorySpinner = (Spinner) root.findViewById(R.id.category);
        String[] tmp = getResources().getStringArray(R.array.category);
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals(boardHash.get("category"))) {
                cPosition = i;
            }
        }
        talentSpinner = (Spinner) root.findViewById(R.id.talent);
        contentsText = (EditText) root.findViewById(R.id.contents);
        contentsText.setText(boardHash.get("contents"));
        write = (Button) root.findViewById(R.id.upload);
        rb1 = (RadioButton) root.findViewById(R.id.nomal);
        rb2 = (RadioButton) root.findViewById(R.id.present);
        dayGroup = (RadioGroup) root.findViewById(R.id.dayGroup);
        type = boardHash.get("type");
        //Toast.makeText(getActivity(), tmp, Toast.LENGTH_SHORT).show();
        if (type.equals("일반")) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }
        weekdayButton = (RadioButton) root.findViewById(R.id.weekday);
        weekendButton = (RadioButton) root.findViewById(R.id.weekend);
        directSelectButton = (RadioButton) root.findViewById(R.id.directSelectButton);
        directSeletPage = (LinearLayout) root.findViewById(R.id.directSelectPage);
        cb = new CheckBox[]
                {(CheckBox) root.findViewById(R.id.sun),
                        (CheckBox) root.findViewById(R.id.mon),
                        (CheckBox) root.findViewById(R.id.tue),
                        (CheckBox) root.findViewById(R.id.wen),
                        (CheckBox) root.findViewById(R.id.thu),
                        (CheckBox) root.findViewById(R.id.fri),
                        (CheckBox) root.findViewById(R.id.sat),
                };
        dayResult = boardHash.get("dayResult");
        int dayValue = Integer.parseInt(dayResult);
        //Toast.makeText(getActivity(), ""+dayValue, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < dayBit.length; i++) {
            int tmp2 = dayBit[i] & dayValue;
            switch (tmp2) {
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
        directSelectButton.setChecked(true);
        directSeletPage.setVisibility(View.VISIBLE);
        dayGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.weekday:
                        directSeletPage.setVisibility(View.GONE);
                        dayResult = "62";
                        break;
                    case R.id.weekend:
                        directSeletPage.setVisibility(View.GONE);
                        dayResult = "65";
                        break;
                    case R.id.directSelectButton:
                        directSeletPage.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        write.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post(v);
            }
        });

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

        groundImageView = new ImageView[]{
                (ImageView) root.findViewById(R.id.mod_ground), (ImageView) root.findViewById(R.id.mod_ground2), (ImageView) root.findViewById(R.id.mod_ground3)};
        for (int i = 0; i < groundImageView.length; i++) {
            groundImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idValue = v.getId();
                    switch (v.getId()) {
                        case R.id.mod_ground:
                            countValue = 0;
                            break;
                        case R.id.mod_ground2:
                            countValue = 1;
                            break;
                        case R.id.mod_ground3:
                            countValue = 2;
                            break;
                    }
                    Intent intent = new Intent(Intent.ACTION_PICK); // ACTION_PICK 이라는 이름을 가지고 있는 Intent를 호출해라. -> 이미지 선택하는 인탠트를 가져와라
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            });
        }
        if (bmImg != null) {
            groundImageView[0].setImageBitmap(bmImg);
        }

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
                    groundImageView[countValue] = (ImageView) getActivity().findViewById(idValue);
                    groundImageView[countValue].setImageBitmap(src);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void getImageNameToUri(Uri data) {
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE
        };
        //Data는 이미지 파일의 스트림 데이터 경로를 의미, 어떤 column을 빼낼지 정보

        Cursor cursor = getActivity().getContentResolver().query(data, proj, null, null, null);
        cursor.moveToFirst(); //Cursor를 제일 첫번째 행(Row)으로 이동 시킨다.

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);

        mlmgPath = cursor.getString(column_data);
        mlmgTitle = cursor.getString(column_title);
        //Log.d(mlmgPath,"path");
        //Log.d(mlmgTitle,"title");


        //String imgPath = cursor.getString(column_data);
        //lastIndexOf = 문자열에서 탐색하는 문자열이 마지막으로 등장하는 위치에 대한 index를 반환
        //substring = 해당하는 문자열부터 추출
        //String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

    }


    public void addcategorySpinner(View root) {
        //데이터 준비
        String[] categoryList = getResources().getStringArray(R.array.category);
        //어댑터 준비 현재 컨텍스트(액티비티),표시할 레이아웃형식(기본 안드로이드 dropdown이용),데이터 원본)
        ArrayAdapter<CharSequence> categoryAdpater = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        //스피너와 어댑터 연결
        categorySpinner = (Spinner) root.findViewById(R.id.category);
        categorySpinner.setAdapter(categoryAdpater); //어댑터 객체와  뷰를 연결
        categorySpinner.setOnItemSelectedListener(this); //아래의 onItemSeleted의 이벤트를 리스너로 지정
        categorySpinner.setSelection(cPosition);
    }

    public void addSegmentSpinner(String subCategory) {
        talentSpinner = (Spinner) getActivity().findViewById(R.id.talent);
        ArrayAdapter talentadpater;
        String[] tmp;

        HashMap<String, Integer> checkTalet1 = new HashMap<String, Integer>();
        checkTalet1.put("카테고리선택", R.array.select);
        checkTalet1.put("디자인", R.array.Design);
        checkTalet1.put("미디어", R.array.Media);
        checkTalet1.put("사무", R.array.office);
        checkTalet1.put("IT", R.array.IT);
        checkTalet1.put("어학", R.array.language);
        checkTalet1.put("생활", R.array.life);
        checkTalet1.put("기타", R.array.etc);

        talentadpater = ArrayAdapter.createFromResource(getActivity(), checkTalet1.get(subCategory), android.R.layout.simple_spinner_item);
        talentSpinner.setAdapter(talentadpater);
        talentSpinner.setOnItemSelectedListener(this);
        tmp = getResources().getStringArray(checkTalet1.get(subCategory));
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals(boardHash.get("talent"))) {
                tPosition = i;
            }
        }
        talentSpinner.setSelection(tPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //int id = parent.getId();
        switch (parent.getId()) {
            case R.id.category:
                addSegmentSpinner(parent.getItemAtPosition(position).toString());
                category = parent.getItemAtPosition(position).toString();
                break;
            case R.id.talent:
                talent = (String) talentSpinner.getSelectedItem();
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
            if (TimeFlag == 0) {
                startHourText.setText(msg);
            } else {
                endHourText.setText(msg);
            }
        }
    };

    private void Post(View view) {

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);
        //Toast.makeText(getActivity(),formatDate,Toast.LENGTH_SHORT).show();
        id = LoginActivity.id;
        title = titleText.getText().toString();
        writeDate = formatDate;
        if (radioGroup.getCheckedRadioButtonId() == R.id.nomal)
            type = rb1.getText().toString();
        else if (radioGroup.getCheckedRadioButtonId() == R.id.present)
            type = rb2.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        talent = talentSpinner.getSelectedItem().toString();
        startHour = startHourText.getText().toString();
        endHour = endHourText.getText().toString();
        contents = contentsText.getText().toString();

        if (directSelectButton.isChecked()) {
            tmp = 0;
            for (int i = 0; i < cb.length; i++) {
                if (cb[i].isChecked()) {
                    tmp = tmp | dayBit[i];
                }
            }
            dayResult = Integer.toString(tmp);
        } else {
        }


        //Toast.makeText(getActivity(),id+"\n"+title+"\n"+writeDate+"\n"+type+"\n"+startHour+"\n"+endHour+"\n"+category+"\n"+talent+"\n"+contents+"\n"+boardHash.get("boardNumber")+mlmgPath,Toast.LENGTH_LONG).show();
        if (id != null || title != null || writeDate != null || type != null || startHour != null || endHour != null || talent != null || contents != null) {
            dialog = ProgressDialog.show(getActivity(), "Please Wait", "올리는 중입니다.", true, true);
            sendHash = new HashMap<String, String>();
            sendHash.put("boardNumber", boardHash.get("boardNumber"));
            sendHash.put("id", id);
            sendHash.put("title", title);
            sendHash.put("dayResult", dayResult);
            sendHash.put("gender", boardHash.get("gender"));
            sendHash.put("writeDate", writeDate);
            sendHash.put("type", type);
            sendHash.put("startHour", startHour);
            sendHash.put("endHour", endHour);
            sendHash.put("category", category);
            sendHash.put("talent", talent);
            sendHash.put("contents", contents);

            String link = "http://1.243.135.179:8080/modify_talent_buy_posting.php";
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

            Mode_SellTime_Frag2_list_detail modeSellTimeFrag2_list_detail = new Mode_SellTime_Frag2_list_detail(sendHash);
            ModifyBoard_dbName_talent_buy_posting modify = new ModifyBoard_dbName_talent_buy_posting(fm, viewFragment, tr, modeSellTimeFrag2_list_detail, layout, dialog, getActivity());
            modify.execute(id, title, dayResult, writeDate, type, startHour, endHour, category, talent, contents, boardHash.get("boardNumber"), mlmgPath, link);
            //insertToBoard(id, title, dayResult, writeDate, type, startHour, endHour, category, talent, contents, boardHash.get("boardNumber"));
        } else {
            Toast.makeText(getActivity(), "정보를 다 입력해주세요.", Toast.LENGTH_LONG).show();

        }

    }
}