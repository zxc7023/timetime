package com.example.junkikim.donate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    public static Activity loginActivity; //로그인이 되었을때 액티비티 수명을 죽이기위하여 사용하였음.


    EditText userIdText,passwordText;
    String password;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2;


    public static String id;
    private int dotsCount=5;    // circle indicator의 갯수
    private ImageView[] dots;   // circle indicator를 담을 뷰.
    LinearLayout dotLayout;     // 위의 이미지를 배치할 레이아웃


    @Override  //생명주기 Oncreate 어플리케이션 시작시 할 작업들
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity=LoginActivity.this;
        startActivity(new Intent(LoginActivity.this, SplashActivity.class)); // SplashActivity로 이동
        initialize();

        //text입력값을 받기위하여 id를 통하여 edittext값을 받음음
        userIdText=(EditText)findViewById(R.id.usernameInput);
        passwordText=(EditText)findViewById(R.id.passwordInput);
        radioGroup=(RadioGroup)findViewById(R.id.mode);
        radioButton1=(RadioButton)findViewById(R.id.radioButton);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);


        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyAdapter adapter = new MyAdapter(); // inner class로 작성한것을 객체화
        pager.setAdapter(adapter); // 리스트에 사용할 객체를 넘겨줌
        drawPageSelectionIndicators(0); //첫번째 circle indicator를 checked 하게함

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //페이지 이벤트 리스너
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    private void initialize() { // Splash 표시와 초기화를 동시에 진행하기 위한 쓰레드
        InitializationRunnable init = new InitializationRunnable();
        new Thread(init).start();
    }

    class InitializationRunnable implements Runnable { // 초기화 작업 처리
        public void run() { // null 값 == 초기화 작업 처리

        }
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {

            return dotsCount;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //뷰페이져의 getCount()에서 얻어온 Count갯수의 position별로 pager에 등록할 item을 처리하는 메서드
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = new LinearLayout(getApplicationContext()); // 레이아웃을 xml이 아닌 직접 만들어 사용
            layout.setOrientation(LinearLayout.VERTICAL); //위레이아웃의 Orientation을 vertical로 사용

            TextView view = new TextView(getApplicationContext());
            switch (position)
            {
                case 0:
                    view.setBackgroundResource(R.drawable.p5);
                    break;
                case 1:
                    view.setBackgroundResource(R.drawable.p2);
                    break;
                case 2:
                    view.setBackgroundResource(R.drawable.p3);
                    break;
                case 3:
                    view.setBackgroundResource(R.drawable.p4);
                    break;
                case 4:
                    view.setBackgroundResource(R.drawable.p1);
                    break;
            }
            layout.addView(view);
            container.addView(layout);

            return layout;
        }
    }


    private void drawPageSelectionIndicators(int mPosition){
        if(dotLayout!=null) {
            dotLayout.removeAllViews();
        }
        dotLayout=(LinearLayout)findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getApplicationContext());
            if(i==mPosition)
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.item_selected));
            else
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.item_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(10, 0, 10, 0);
            dotLayout.addView(dots[i], params);
        }
    }

    // 회원버튼을 눌렀을때 새로운 액티비트를 띄어서 회원가입정보를 입력하게 하기 위한 메소드.
    public void onButton2Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),JoinActivity.class);
        //JoinActivity 는 activity_join.xml을 사용하기 위한 자바 소스
        startActivity(intent); //Activity 시작.
    }

    public void login(View view) {
        id=userIdText.getText().toString().trim();
        password=passwordText.getText().toString().trim();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("token",token);

        if (id == null || id.equals("")||password == null || password.equals("")){
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();

        }
        else{
            userLogin(id,password,token);
        }

    }
    private void userLogin(final String id, String password,String token ) {

        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    if(radioButton1.isChecked()){
                        Intent intent = new Intent(LoginActivity.this, Mode_SellTime_MainActivity.class);
                        //intent.putExtra("id",id);
                        startActivity(intent);
                    }
                    else if(radioButton2.isChecked()){
                        Intent intent2 = new Intent(LoginActivity.this, Mode_BuyTime_MainActivity.class);
                        //intent.putExtra(USER_NAME,username);
                        startActivity(intent2);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String id = (String) params[0];
                    String password = (String) params[1];
                    String token =(String)params[2];


                    String link = "http://1.243.135.179:8080/login.php/";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(id, password,token);
    }
}