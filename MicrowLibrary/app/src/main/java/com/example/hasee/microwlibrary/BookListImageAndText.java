package com.example.hasee.microwlibrary;

/**
 * Created by HASEE on 2017/3/23.
 */
//相当于book类，就是将程序里面的书籍信息转换为一个对象。
public class BookListImageAndText {
    private String bookimageUrl;
    private String bookName;
    private String bookAuthor;
    private String bookScores;
    private String bookSummary;
    public BookListImageAndText(String bookimageUrl, String bookName, String bookAuthor, String bookScores, String bookSummary) {
        this.bookimageUrl = bookimageUrl;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookScores = bookScores;
        this.bookSummary=bookSummary;
    }
    public String getbookimageUrl() {
        return bookimageUrl;
    }
    public String getbookName() {
        return bookName;
    }
    public String getbookAuthor() {
        return bookAuthor;
    }
    public String getbookScores() {
        return bookScores;
    }
    public String getbookSummary() {
        return bookSummary;
    }
}

