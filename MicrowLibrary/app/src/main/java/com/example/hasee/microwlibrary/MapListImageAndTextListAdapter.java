package com.example.hasee.microwlibrary;

/**
 * Created by HASEE on 2017/3/23.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
//自定义适配器，作用是设置控件里面的信息，输入是BookListViewCache，将里面的信息与view链接
public class MapListImageAndTextListAdapter extends ArrayAdapter<Book> {
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;
    public MapListImageAndTextListAdapter(Activity activity, List<Book> imageAndTexts, ListView listView) {
        super(activity, 0, imageAndTexts);
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        // Inflate the views from XML
        View rowView = convertView;
        BookListViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null);
            viewCache = new BookListViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (BookListViewCache) rowView.getTag();
        }
        Book book = getItem(position);
        // Load the image and set it on the ImageView
        String imageUrl = book.getbookimageUrl();
        ImageView imageView = viewCache.getImageView();
        imageView.setTag(imageUrl);
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.drawable.icon_error);
        }else{
            imageView.setImageDrawable(cachedImage);
        }
        // Set the text on the TextView
        TextView bookName = viewCache.getBookName();
        bookName.setText(book.getBookName());
        TextView bookAuthor = viewCache.getBookAuthor();
        bookAuthor.setText(book.getBookAuthor());
        TextView bookGrade = viewCache.getBookScores();
        bookGrade.setText(book.getBookScore()+"");
        TextView bookSummary = viewCache.getBookSummary();
        bookSummary.setText(book.getBookDescription());
        return rowView;
    }
}

