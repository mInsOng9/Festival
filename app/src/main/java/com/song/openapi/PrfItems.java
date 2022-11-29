package com.song.openapi;

import java.net.URL;

public class PrfItems {
    String prfId;
    String prfNm;
    String stDate;
    String edDate;
    String fcltyNm;
    String poster;
    String genreNm;
    String state;
    String festYN;

    public PrfItems(String prfId,String prfNm, String stDate, String edDate, String fcltyNm, String poster,String genreNm, String state, String festYN) {
        this.prfId=prfId;
        this.prfNm=prfNm;
        this.stDate = stDate;
        this.edDate = edDate;
        this.fcltyNm = fcltyNm;
        this.poster = poster;
        this.genreNm = genreNm;
        this.state = state;
        this.festYN = festYN;
    }

    public PrfItems(){

    }
}




