package com.example.hasee.microwlibrary;

/**
 * Created by HASEE on 2017/3/23.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
//相当于缓存，设置控件里面的信息
public class BookListViewCache {
    private View baseView;
    private TextView bookName;
    private TextView bookAuthor;
    private TextView bookScores;
    private TextView bookSummary;
    private ImageView imageView;
    public BookListViewCache(View baseView) {
        this.baseView = baseView;
    }
    public TextView getBookName() {
        if (bookName == null) {
            bookName = (TextView) baseView.findViewById(R.id.textBookName);
        }
        return bookName;
    }
    public TextView getBookAuthor() {
        if (bookAuthor == null) {
            bookAuthor = (TextView) baseView.findViewById(R.id.textBookAuthor);
        }
        return bookAuthor;
    }
    public TextView getBookScores() {
        if (bookScores == null) {
            bookScores = (TextView) baseView.findViewById(R.id.textBookGrade);
        }
        return bookScores;
    }
    public TextView getBookSummary() {
        if (bookSummary == null) {
            bookSummary = (TextView) baseView.findViewById(R.id.textBookSummary);
        }
        return bookSummary;
    }
    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.buttonBookImage);
        }
        return imageView;
    }

}

