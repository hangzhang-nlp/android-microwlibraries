package com.example.hasee.microwlibrary;

/**
 * Created by GeniusHe on 2017/3/16.
 */

public class Book {
    private String bookName;//书名
    private String bookAuthor;//作者
    private String bookDescription;//书籍简介
    private int readState; // 读书状态
    private double bookScore;//评分
    private String  planTime;//计划时间
    private String bookimageUrl; //书的封面Uri

 public Book(){

}

    public Book(String initBookName,String initbookAuthor,int initReadState, double initBookScore,String initBookimageUrl){
        this.bookName = initBookName;
        this.bookAuthor = initbookAuthor;
        this.readState = initReadState;
        this.bookScore = initBookScore;
        this.bookimageUrl = initBookimageUrl;

    }

    public Book(String initBookName,String initbookAuthor,int initReadState, String initPlanTime, String initBookimageUrl){
        this.bookName = initBookName;
        this.bookAuthor = initbookAuthor;
        this.readState = initReadState;
        this.planTime = initPlanTime;
        this.bookimageUrl = initBookimageUrl;
    }
    public Book(String initBookName,String initbookAuthor,int initReadState, double initBookScore){
        this.bookName = initBookName;
        this.bookAuthor = initbookAuthor;
        this.readState = initReadState;
        this.bookScore = initBookScore;

    }

    public Book(String initBookName,String initbookAuthor,int initReadState, String initPlanTime){
        this.bookName = initBookName;
        this.bookAuthor = initbookAuthor;
        this.readState = initReadState;
        this.planTime = initPlanTime;
    }


    public String getbookimageUrl() {
        return bookimageUrl;
    }

    public void setBookimageUrl(String bookimageUrl) {
        this.bookimageUrl = bookimageUrl;
    }

    public void setBookScore(double bookScore) {
        this.bookScore = bookScore;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public void setBookName(String initBookName){
        this.bookName = initBookName;
    }

    public void setBookAuthor(String initbookAuthor){
        this.bookAuthor = initbookAuthor;
    }

    public void setBookDescription(String initBookDescription){
        this.bookDescription = initBookDescription;
    }




    public double getBookScore() {
        return bookScore;
    }

    public String  getPlanTime() {
        return planTime;
    }

    public int getReadState() {
        return readState;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookName() {
        return bookName;
    }
}
