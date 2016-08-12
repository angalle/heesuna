package com.sm.bookingtest.purchase;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.LoginData;
import com.sm.bookingtest.data.PurchaseData;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.SplashActivity;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class PurchaseView extends FrameLayout {
	Context context;

	public PurchaseView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	ImageLoader imageLoader;
	ImageView iv_bookimage,state;
	TextView tv_title;
	ImageButton imbtn_sale;
	DisplayImageOptions defaultOptions;

	private void init() {
		defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.beehive)
				.showImageOnFail(R.drawable.beehive)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();

		LayoutInflater.from(getContext()).inflate(R.layout.list_purchase, this);

		iv_bookimage = (ImageView) findViewById(R.id.iv_purchase_bookimage);
		tv_title = (TextView) findViewById(R.id.tv_purchase_title);
		imbtn_sale = (ImageButton) findViewById(R.id.btn_purchase_purchasebtn);
		state = (ImageView)findViewById(R.id.status_img);
	}
	
	static String ph ="";
	public void setData(final PurchaseData data) {
		imageLoader.displayImage(data.getImage_url(), iv_bookimage, defaultOptions);
		tv_title.setText(data.getBook_name());
		
		if(data.getPurchase_state() == SplashActivity.PURST){
			state.setVisibility(VISIBLE);
			imbtn_sale.setEnabled(false);
		}else{
			state.setVisibility(GONE);
			imbtn_sale.setEnabled(true);
		}
		
		imbtn_sale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle extras = new Bundle();
				extras.putString("indx", data.getIndx());
				extras.putString("id", data.getId());
				extras.putString("bookNm", data.getBook_name());
				extras.putString("im_url", data.getImage_url());
				extras.putString("ph", ph);
				
				Intent intent = new Intent(context,	PurchaseItemClickActivity.class);
				intent.putExtra("PurchaseItem", extras);
				context.startActivity(intent);
			}
		});
	}
}
