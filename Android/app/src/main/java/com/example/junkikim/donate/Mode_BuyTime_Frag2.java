package com.example.junkikim.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Mode_BuyTime_Frag2 extends Fragment
{
    Animation translateUpAnim;
    Animation translateDownAnim;
    Button button1,button2,button3,button4,button5,button6,button7;
    boolean[] isPageOpenCheck=new boolean[]{false,false,false,false,false,false,false};


    LinearLayout slidingPage01,slidingPage02,slidingPage03,slidingPage04,slidingPage05,slidingPage06,slidingPage07;
    Button categoryButton;
    TextView talentTextView;

    Mode_BuyTime_Frag2_list modeBuyTimeFrag2_list;
    String category;
    Button media,design,international,common,science,medical,etc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_buytime_2,container,false); //카테고리 화면 인플레이터해줌
        slidingPage01 = (LinearLayout) root.findViewById(R.id.sliding_design);
        slidingPage02 =(LinearLayout)root.findViewById(R.id.sliding_media);
        slidingPage03 =(LinearLayout)root.findViewById(R.id.sliding_office);
        slidingPage04 =(LinearLayout)root.findViewById(R.id.sliding_it);
        slidingPage05 =(LinearLayout)root.findViewById(R.id.sliding_language);
        slidingPage06 =(LinearLayout)root.findViewById(R.id.sliding_life);
        slidingPage07 =(LinearLayout)root.findViewById(R.id.sliding_etc);

        button1=(Button)root.findViewById(R.id.button1);
        button2=(Button)root.findViewById(R.id.button2);
        button3=(Button)root.findViewById(R.id.button3);
        button4=(Button)root.findViewById(R.id.button4);
        button5=(Button)root.findViewById(R.id.button5);
        button6=(Button)root.findViewById(R.id.button6);
        button7=(Button)root.findViewById(R.id.button7);
        translateDownAnim= AnimationUtils.loadAnimation(getActivity(),R.anim.translate_down);
        translateUpAnim=AnimationUtils.loadAnimation(getActivity(),R.anim.translate_up);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateDownAnim.setAnimationListener(animListener);
        translateUpAnim.setAnimationListener(animListener);
        return root;
    }

    public void onClickedCategory(View v) {
        switch (v.getId()){
            case R.id.button1:
                if(isPageOpenCheck[0]){
                    slidingPage01.setVisibility(View.GONE);
                    slidingPage01.startAnimation(translateUpAnim);
                    isPageOpenCheck[0]=false;
                }
                else{
                    slidingPage01.setVisibility(View.VISIBLE);
                    slidingPage01.startAnimation(translateDownAnim);
                    isPageOpenCheck[0]=true;
                }
                break;
            case R.id.button2:
                if(isPageOpenCheck[1]){
                    slidingPage02.setVisibility(View.GONE);
                    slidingPage02.startAnimation(translateUpAnim);
                    isPageOpenCheck[1]=false;
                }
                else{
                    slidingPage02.setVisibility(View.VISIBLE);
                    slidingPage02.startAnimation(translateDownAnim);
                    isPageOpenCheck[1]=true;
                }
                break;
            case R.id.button3:
                if(isPageOpenCheck[2]){
                    slidingPage03.setVisibility(View.GONE);
                    slidingPage03.startAnimation(translateUpAnim);
                    isPageOpenCheck[2]=false;
                }
                else{
                    slidingPage03.setVisibility(View.VISIBLE);
                    slidingPage03.startAnimation(translateDownAnim);
                    isPageOpenCheck[2]=true;
                }
                break;
            case R.id.button4:
                if(isPageOpenCheck[3]){
                    slidingPage04.setVisibility(View.GONE);
                    slidingPage04.startAnimation(translateUpAnim);
                    isPageOpenCheck[3]=false;
                }
                else{
                    slidingPage04.setVisibility(View.VISIBLE);
                    slidingPage04.startAnimation(translateDownAnim);
                    isPageOpenCheck[3]=true;
                }
                break;
            case R.id.button5:
                if(isPageOpenCheck[4]){
                    slidingPage05.setVisibility(View.GONE);
                    slidingPage05.startAnimation(translateUpAnim);
                    isPageOpenCheck[4]=false;
                }
                else{
                    slidingPage05.setVisibility(View.VISIBLE);
                    slidingPage05.startAnimation(translateDownAnim);
                    isPageOpenCheck[4]=true;
                }
                break;
            case R.id.button6:
                if(isPageOpenCheck[5]){
                    slidingPage06.setVisibility(View.GONE);
                    slidingPage06.startAnimation(translateUpAnim);
                    isPageOpenCheck[5]=false;
                }
                else{
                    slidingPage06.setVisibility(View.VISIBLE);
                    slidingPage06.startAnimation(translateDownAnim);
                    isPageOpenCheck[5]=true;
                }
                break;
            case R.id.button7:
                if(isPageOpenCheck[6]){
                    slidingPage07.setVisibility(View.GONE);
                    slidingPage07.startAnimation(translateUpAnim);
                    isPageOpenCheck[6]=false;
                }
                else{
                    slidingPage07.setVisibility(View.VISIBLE);
                    slidingPage07.startAnimation(translateDownAnim);
                    isPageOpenCheck[6]=true;
                }
                break;
        }
        categoryButton=(Button) v.findViewById(v.getId());
        category=categoryButton.getText().toString();
        //Toast.makeText(getActivity(),""+category, Toast.LENGTH_SHORT).show();
    }
    public void onClickedTalent(View v) {
        talentTextView= (TextView) v.findViewById(v.getId());
        String talent=talentTextView.getText().toString();
        //Toast.makeText(getActivity(),""+talent, Toast.LENGTH_SHORT).show();

        for(int i=0;i<isPageOpenCheck.length;i++)
            isPageOpenCheck[i]=false;
        FragmentManager fm =getFragmentManager();
        Fragment fragment =fm.findFragmentById(R.id.maincontainer2);
        FragmentTransaction tr =fm.beginTransaction();
        modeBuyTimeFrag2_list = Mode_BuyTime_Frag2_list.newInstance(category,talent);
        tr.replace(R.id.maincontainer2, modeBuyTimeFrag2_list,"modeBuyTimeFrag2_list");
        //tr.addToBackStack(null);
        // tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tr.commit();
        }
    }
class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }
        @Override
        public void onAnimationEnd(Animation animation) {
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
}




