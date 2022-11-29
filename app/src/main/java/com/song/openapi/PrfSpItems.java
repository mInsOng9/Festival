package com.song.openapi;

import androidx.viewpager2.widget.ViewPager2;

//prf specific information items
public class PrfSpItems {
    String prfNm;
    String stDate;
    String edDate;
    String runTime;
    String ticket;
    String img;
    String state;
    String img1;
    String img2;
    String img3;
    String prfTime;

    public PrfSpItems(String prfNm, String stDate, String edDate, String runTime, String ticket, String img, String state, String img1, String img2, String img3, String prfTime) {
        this.prfNm = prfNm;
        this.stDate = stDate;
        this.edDate = edDate;
        this.runTime = runTime;
        this.ticket = ticket;
        this.img = img;
        this.state = state;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.prfTime = prfTime;
    }

    public PrfSpItems() {
    }
}



