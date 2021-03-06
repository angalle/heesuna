package com.sm.bookingtest.sell;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.SellData;
import com.sm.bookingtest.util.ImageCacheDownloader;
import com.sm.bookingtest.util.SplashActivity;

public class SellView extends FrameLayout

{
	Context context;

	public SellView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	ImageLoader imageLoader;
	ImageView iv_bookimage;
	ImageView status_img;
	TextView tv_title;
	TextView tv_price;
	TextView selling_price;
	TextView tv_condition;
	ImageButton imbtn_buy;
	String status;
	DisplayImageOptions defaultOptions;

	private void init() {
		defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.resetViewBeforeLoading(true)
				.delayBeforeLoading(100)
				.showImageForEmptyUri(R.drawable.beehive)
				.showImageOnFail(R.drawable.beehive)
				//.imageScaleType(ImageScaleType.EXACTLY)
				//.displayer(new FadeInBitmapDisplayer(300))
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
		

		LayoutInflater.from(getContext()).inflate(R.layout.list_sale, this);

		iv_bookimage = (ImageView) findViewById(R.id.iv_sale_bookimage);
		tv_title = (TextView) findViewById(R.id.tv_sale_title);
		tv_price = (TextView) findViewById(R.id.tv_sale_price);
		selling_price = (TextView) findViewById(R.id.tv_sale_cost);
		tv_condition = (TextView) findViewById(R.id.tv_sale_condition);
		imbtn_buy = (ImageButton) findViewById(R.id.btn_sale_salebtn);
		status_img = (ImageView) findViewById(R.id.status_img);

	}

	public void setData(final SellData data) {
		imageLoader.displayImage(data.getSell_picture(), iv_bookimage, defaultOptions);
		
		tv_title.setText(data.getBook_name());
		tv_price.setText(data.getOrigin_price());
		selling_price.setText(data.getHope_price());
		
		

		status = data.getState();
		if (status.equals("A��"))
			status_img.setImageResource(R.drawable.grade_a);
		else if (status.equals("B��"))
			status_img.setImageResource(R.drawable.grade_b);
		else if (status.equals("C��"))
			status_img.setImageResource(R.drawable.grade_c);
		else if (status.equals("D��"))
			status_img.setImageResource(R.drawable.grade_d);

		if(data.getSell_state() == SplashActivity.SELLST){
			status_img.setImageResource(R.drawable.grade_aplus);
			imbtn_buy.setEnabled(false);
		}else{
			imbtn_buy.setEnabled(true);
		}
		
		tv_condition.setText(status);

		imbtn_buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle extras = new Bundle();
				extras.putString("indx", data.getIndx());
				extras.putString("bookNm", data.getBook_name());
				extras.putString("origin_price", data.getOrigin_price());
				extras.putString("hope_price", data.getHope_price());
				extras.putString("condition", data.getState());
				extras.putString("im_url", data.getSell_picture());
				extras.putString("id", data.getId());

				Intent intent = new Intent(context, SellItemClickActivity.class);
				intent.putExtra("SaleItem", extras);
				context.startActivity(intent);
			}
		});
	}

}
