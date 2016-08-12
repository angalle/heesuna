package com.sm.bookingtest.sell;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.crypto.spec.GCMParameterSpec;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.ConLogin;
import com.sm.bookingtest.data.LoginData;
import com.sm.bookingtest.purchase.PurchaseItemClickActivity.Messager;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.SplashActivity;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class SellItemClickActivity extends Activity {
	ImageLoader imageLoader;
	DisplayImageOptions defaultOptions;
	
	String msgPh="",messge="";
	String boookNm="",content_im_url=""; //�ش� ��Ƽ��Ƽ�� å�̸� , �̹��� url
	String targetUser = ""; //Ÿ�� ���� ���̵�
	Button saleBtn;
	String indx="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_sale);

		defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.beehive)
				.showImageOnFail(R.drawable.beehive)
				// .imageScaleType(ImageScaleType.EXACTLY) xml���� ������.
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();

		Intent intent = getIntent();

		Bundle extra = intent.getBundleExtra("SaleItem");

		targetUser = extra.getString("id");
		String boookNm = extra.getString("bookNm");
		String content_origin_price = extra.getString("origin_price");
		String content_hope_price = extra.getString("hope_price");
		String content_condition = extra.getString("condition");
		content_im_url = extra.getString("im_url");
		indx = extra.getString("indx");

		SharedPreferences sp = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		String usingUser = sp.getString(SplashActivity.ID, "���̵�");		
		
		
		messge = usingUser+"����\n"+targetUser+"���� "+boookNm+"�� ���;� �մϴ�.\n�����ּ���!";
		
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(boookNm);

		TextView tv_cost = (TextView) findViewById(R.id.tv_cost);
		tv_cost.setText(content_origin_price);

		TextView tv_price = (TextView) findViewById(R.id.tv_price);
		tv_price.setText(content_hope_price);

		TextView tv_condition = (TextView) findViewById(R.id.tv_condition);
		tv_condition.setText(content_condition);

		ImageView img_url = (ImageView) findViewById(R.id.im_url1);

		imageLoader.getInstance().displayImage(content_im_url, img_url,defaultOptions);

		saleBtn = (Button) findViewById(R.id.btn_sale);
		
		if(!usingUser.equals("���̵�")){
			
			Connecting.getInstance().one_user_data(getApplicationContext(), targetUser, new OnNetworkResultListener<ArrayList<LoginData>>() {
				@Override
				public void onResult(ArrayList<LoginData> result) {
					msgPh = result.get(0).getPh();
				}
				
				@Override
				public void onFail(int code) {
					Toast.makeText(getApplicationContext(), "��ȭ��ȣ �޾ƿ��� ����", Toast.LENGTH_SHORT).show();
				}
			});
			
			saleBtn.setOnClickListener(new OnClickListener() {
				@TargetApi(23)
				@Override
				public void onClick(View v) {
					if(Build.VERSION.SDK_INT >= 23){
						if(checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
					            || checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
							requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS},  0);
							Toast.makeText(getApplicationContext(), "���ڰ� �Ⱥ����� ��� ������ ���� �� �ٽ� ���ּ���.", Toast.LENGTH_SHORT).show();
							Messager msg = new Messager(getApplicationContext());
							msg.sendMessageTo(msgPh,messge);
							saleBtn.setClickable(false);
						}
					}else{
						Messager msg = new Messager(getApplicationContext());
						msg.sendMessageTo(msgPh,messge);
						saleBtn.setClickable(false);
					}
				}
			});
		}else{
			//�մ��� ���
			Toast.makeText(getApplicationContext(), "�մ��� ������ �Ұ��� �մϴ�.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public class Messager{
		private Context context;
		String ph = "";
		public Messager(Context context){
			this.context = context;
		}
		
		public void sendMessageTo(String ph,String msg) {
			SmsManager smsMsg = SmsManager.getDefault();
			smsMsg.sendTextMessage(ph, null, msg, null, null);
			
			
			Connecting.getInstance().updateSellState(getApplicationContext(), targetUser,indx,SplashActivity.SELLST, new OnNetworkResultListener<ConLogin>() {
				@Override
				public void onResult(ConLogin result) {
					Toast.makeText(context, "�޼��� ���� �Ϸ� \n�޼��� ���� Ȯ���ϼ���!", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFail(int code) {
					Toast.makeText(context, "�޼��� ���� ����!", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
