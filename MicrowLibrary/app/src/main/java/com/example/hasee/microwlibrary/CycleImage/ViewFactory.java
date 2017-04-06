package com.example.hasee.microwlibrary.CycleImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.hasee.microwlibrary.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {



		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
                R.layout.view_banner,null);

        ImageLoader.getInstance().displayImage(url, imageView);
		Log.d("TAG",url);
		return imageView;
	}
}
