package com.song.openapi;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideExtension;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SpecificPrfActivity extends MainActivity{

    TextView prfNm;
    TextView prfAge;
    TextView stDate;
    TextView edDate;
    TextView runTime;
    TextView tickets;
    TextView states;
    TextView prfTime;
    TextView prfCast;
    TextView prfCrew;

    ViewPager2 pager;
    MyPagerAdapter pagerAdapter;

    ArrayList<String> imgs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_prf);

        //TextView findView
        prfNm=findViewById(R.id.spTv_prfNm);
        stDate=findViewById(R.id.spTv_stDate);
        edDate=findViewById(R.id.spTv_edDate);
        runTime=findViewById(R.id.spTv_runTime);
        tickets=findViewById(R.id.spTv_ticket);
        states=findViewById(R.id.spTv_state);
        prfTime=findViewById(R.id.spTv_prfTime);
        prfAge=findViewById(R.id.spTv_prfAge);
        prfCast=findViewById(R.id.spTv_prfCast);
        prfCrew=findViewById(R.id.spTv_prfCrew);

        //ViewPager
        pager=findViewById(R.id.sp_pager);
        //pagerAdapter
        pagerAdapter=new MyPagerAdapter(this,imgs);
        //set pager's adapter
        pager.setAdapter(pagerAdapter);

        MyThread thread=new MyThread();
        thread.start();

    }

    String prfnm=null, stdate = null, eddate=null,cast=null, crew=null,
            age=null,runtime= null,ticket= null,state= null,prftime= null;

    class MyThread extends Thread{
        @Override
        public void run() {


            String urlAddress="http://www.kopis.or.kr/openApi/restful/pblprfr/"
                    +getIntent().getStringExtra("prfId")
                    +"?service="+apiKey;

            tickets.setVisibility(View.VISIBLE);
            prfTime.setVisibility(View.VISIBLE);
            runTime.setVisibility(View.VISIBLE);
            prfCrew.setVisibility(View.VISIBLE);
            prfCast.setVisibility(View.VISIBLE);

            try {
                URL url = new URL(urlAddress);

                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(isr);

                int eventType = xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT) {

                    switch (eventType) {

                        case XmlPullParser.START_TAG:
                            String tagName = xpp.getName();
                            if (tagName.equals("prfnm")) {
                                xpp.next();
                                prfnm= xpp.getText();
                            } else if (tagName.equals("prfpdfrom")) {
                                xpp.next();
                                stdate = xpp.getText();
                            } else if (tagName.equals("prfpdto")) {
                                xpp.next();
                                eddate = xpp.getText();
                            } else if (tagName.equals("prfcast")) {
                                xpp.next();
                                cast = xpp.getText();
                            }else if (tagName.equals("prfcrew")) {
                                xpp.next();
                                crew = xpp.getText();
                            } else if (tagName.equals("prfruntime")) {
                                xpp.next();
                                runtime = xpp.getText();
                            } else if (tagName.equals("prfage")) {
                                xpp.next();
                                age = xpp.getText();
                            }else if (tagName.equals("pcseguidance")) {
                                xpp.next();
                                ticket = xpp.getText();
                            } else if (tagName.equals("poster")) {
                                xpp.next();
                                imgs.add(xpp.getText());
                            } else if (tagName.equals("prfstate")) {
                                xpp.next();
                                state = xpp.getText();
                            }
                            else if(tagName.equals("styurl")) {
                                xpp.next();
                                imgs.add(xpp.getText());
                            }else if (tagName.equals("dtguidance")) {
                                xpp.next();
                                prftime = xpp.getText();
                            }
                            break;
                    }//switch
                    eventType=xpp.next();
                }//while

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prfNm.setText(prfnm);
                        stDate.setText(stdate);
                        edDate.setText(" ~ "+eddate);
                        states.setText(state);
                        prfAge.setText(age);
                        if(ticket==null||ticket==" ")tickets.setVisibility(View.GONE);
                        else tickets.setText(ticket);
                        if(prftime==null||prftime==" ") prfTime.setVisibility(View.GONE);
                        else prfTime.setText("\n"+prftime);
                        if(runtime==null||runtime==" ") runTime.setVisibility(View.GONE);
                        else runTime.setText("\n공연 런타임: "+runtime);
                        if(cast==null||cast==" ")prfCast.setVisibility(View.GONE);
                        else  prfCast.setText("\n출연진 : "+cast);
                        if(crew==null||crew==" ") prfCrew.setVisibility(View.GONE);
                        else prfCrew.setText("\n제작진 : "+crew);
                        prfNm.setSelected(true);
                        pagerAdapter.notifyDataSetChanged();
                    }
                });
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (XmlPullParserException xmlPullParserException) {
                xmlPullParserException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

}

/*
사진 옆으로 넘기는거 java에서 버튼눌리면 current그걸 빼고 더하고 해서 하기
이랑 목록 더보기
중복되는 이미지 없애기
*/





