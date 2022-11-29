package com.song.openapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //service key
    String apiKey="0bfc88f87e4d47389bedb016c1a9f79d";
    String genre="",local="",prfState="";

    ArrayList<PrfItems> prfItems=new ArrayList<PrfItems>();;
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    //spinner and its arrayadapter
    Spinner spinnerLocal,spinnerGenre,spinnerState;
    boolean thdCt=false;
    ArrayAdapter[] arrayAdapter=new ArrayAdapter[3];
    //spinner items
    int spinnerItems[];

    //genre code value
    String[] genreCode=new String[]{"","AAAA","AAAB","BBBA","CCCA","CCCB","CCCC","EEEA"};
    //local code value
    String[] localCode=new String[]{"","11","26","27","28","29","30","31","36","41","42","43","44","45","46","47","48","50"};
    //prfState code value
    String[] prfStateCode=new String[]{"","01","02","03"};

    //when Prf of selected case doesn't exist
    TextView tvPrfNone;
    //loading
    ProgressBar progressBar;

    Date date=new Date();
    TextView tvDate;
    CalendarView calendarView;
    boolean dateOn=true;
    String day,endDay;

    //View More
    TextView viewMore;
    int rowCt=10;
    int pgCt=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerview);
        myAdapter=new MyAdapter(this,prfItems);
        recyclerView.setAdapter(myAdapter);

        tvPrfNone=findViewById(R.id.tv_prfNone);
        progressBar=findViewById(R.id.progressbar);

        tvDate=findViewById(R.id.tv_date);
        calendarView=findViewById(R.id.calendar);
        tvDate.setOnClickListener(view -> {
            if(dateOn)
               calView();
            else{
                calendarView.setVisibility(View.GONE);
                dateOn=true;
            }

        });
        calView();

        viewMore=findViewById(R.id.tv_viewMore);
        viewMore.setOnClickListener(view -> vMore());

        //spinner (select date,genre,state)
        spinner();

        // MyThread class
        MyThread myThread=new MyThread();
        //start thread
        myThread.start();

    }//onCreate method

    //view more method
    void vMore(){
        rowCt+=10;
        pgCt++;
        thdCt=true;
        MyThread myThread=new MyThread();
        myThread.start();
    }

    //calendar
    void calView(){
        calendarView.setVisibility(View.VISIBLE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date.setYear(i-1900);
                date.setMonth(i1);
                date.setDate(i2);

                rowCt=10;

                calendarView.setVisibility(View.GONE);

                MyThread myThread=new MyThread();
                //start thread
                myThread.start();
            }
        });//listener
        dateOn=false;
    }//calView

    void spinner(){

        spinnerItems= new int[]{R.array.local, R.array.genre, R.array.prfstate};
        spinnerLocal=findViewById(R.id.spinner_local);
        spinnerGenre=findViewById(R.id.spinner_genre);
        spinnerState=findViewById(R.id.spinner_prfstate);
        for(int i=0;i<3;i++) {
            arrayAdapter[i] = ArrayAdapter.createFromResource(this, spinnerItems[i], R.layout.spinner_selected_item);
            arrayAdapter[i].setDropDownViewResource(R.layout.spinner_dropdown_items);
        }
        spinnerLocal.setAdapter(arrayAdapter[0]);
        spinnerGenre.setAdapter(arrayAdapter[1]);
        spinnerState.setAdapter(arrayAdapter[2]);

        spinnerLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) local="";
                else local = "&signgucode=" + localCode[i];
                new MyThread().start();
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                local="";
            }
        });// local listener

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) genre="";
                else genre = "&shcate=" + genreCode[i];
                new MyThread().start();
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });//genre listener

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) prfState="";
                else prfState = "&prfstate=" + prfStateCode[i];
                new MyThread().start();
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });//state listener

    } //spinner method

    // Thread inner class
    class MyThread extends Thread{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    viewMore.setVisibility(View.INVISIBLE);
                    prfItems.clear();
                    myAdapter.notifyDataSetChanged();

                }
            });
            //Date (start and end date)
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
            day=simpleDateFormat.format(date);
            //day+1year== endday
            date.setTime(date.getTime()+1000*60*60*24*365);
            endDay=simpleDateFormat.format(date);


            //URL address
            String urlAddress="http://www.kopis.or.kr/openApi/restful/pblprfr"
                    +"?service="+apiKey
                    +"&stdate="+day
                    +"&eddate="+endDay
                    +"&cpage="+pgCt
                    +"&rows="+rowCt
                    +genre
                    +local
                    +prfState;

            try {
                // read url
                URL url=new URL(urlAddress);
                InputStream inStream=url.openStream();
                InputStreamReader insReader=new InputStreamReader(inStream);

                //xmlpullparser
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();

                //
                xpp.setInput(insReader);

                int eventType=xpp.getEventType();

                PrfItems prfItem=null;

                while(eventType!=XmlPullParser.END_DOCUMENT){
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            String tagName=xpp.getName();
                            if(tagName.equals("db")){
                                prfItem=new PrfItems();
                            }
                            else if(tagName.equals("mt20id")){
                                xpp.next();
                                prfItem.prfId =xpp.getText();
                            }
                            else if(tagName.equals("prfnm")){
                                xpp.next();
                                prfItem.prfNm=xpp.getText();
                            }
                            else if(tagName.equals("prfpdfrom")){
                                xpp.next();
                                prfItem.stDate=xpp.getText();
                            }
                            else if(tagName.equals("prfpdto")){
                                xpp.next();
                                prfItem.edDate=xpp.getText();
                            }
                            else if(tagName.equals("fcltynm")){
                                xpp.next();
                                prfItem.fcltyNm=xpp.getText();
                            }
                            else if(tagName.equals("poster")){
                                xpp.next();
                                prfItem.poster=xpp.getText();
                            }
                            else if(tagName.equals("genrenm")){
                                xpp.next();
                                prfItem.genreNm=xpp.getText();
                            }
                            else if(tagName.equals("prfstate")){
                                xpp.next();
                                prfItem.state=xpp.getText();
                            }
                            else if(tagName.equals("festival")){
                                xpp.next();
                                prfItem.festYN=xpp.getText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(xpp.getName().equals("db")){
                                prfItems.add(prfItem);

                            }
                            break;
                    }//switch

                    eventType=xpp.next();

                }//while method

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(prfItems.size()==0) {
                            tvPrfNone.setVisibility(View.VISIBLE);
                            viewMore.setVisibility(View.INVISIBLE);
                        }
                        else {
                            tvPrfNone.setVisibility(View.INVISIBLE);
                            viewMore.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.GONE);
                        myAdapter.notifyDataSetChanged();
                    }
                }); //runOnUiThread method

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }//run method

    };// MyThread class

}//MainActivity class










