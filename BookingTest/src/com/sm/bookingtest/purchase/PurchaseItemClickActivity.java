package com.sm.bookingtest.purchase;

import java.util.ArrayList;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.ConLogin;
import com.sm.bookingtest.data.LoginData;
import com.sm.bookingtest.sell.SellItemClickActivity.Messager;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;
import com.sm.bookingtest.util.SplashActivity;

public class PurchaseItemClickActivity extends Activity {
	ImageLoader imageLoader;
	DisplayImageOptions defaultOptions;
	
	Button purchasBtn;
	String msgPh="",messge="";
	String boookNm="",content_im_url=""; //�ش� ��Ƽ��Ƽ�� å�̸� , �̹��� url
	String targetUser = ""; //Ÿ�� ���� ���̵�
	String indx="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_purchase);
		purchasBtn = (Button)findViewById(R.id.purchase_btn);
		
		Intent intent = getIntent();

		Bundle extra = intent.getBundleExtra("PurchaseItem");
		targetUser = extra.getString("id"); 
		boookNm = extra.getString("bookNm");
		content_im_url = extra.getString("im_url");
		msgPh = extra.getString("ph");
		indx = extra.getString("indx");
		
		SharedPreferences sp = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		String usingUser = sp.getString(SplashActivity.ID, "���̵�");		
		messge = usingUser+"����\n"+targetUser+"���� "+boookNm+"�� �Ȱ�;� �մϴ�.\n�����ּ���!";
		
		
		defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.beehive)
				.showImageOnFail(R.drawable.beehive)
				// .imageScaleType(ImageScaleType.EXACTLY) xml���� ������.
				.displayer(new FadeInBitmapDisplayer(300))
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(boookNm);
		ImageView im_bookImage = (ImageView) findViewById(R.id.purchase_imageview);
		imageLoader.displayImage(content_im_url, im_bookImage, defaultOptions);
		
		if(!usingUser.equals("���̵�")){
			//�մ��� �ƴ� ���
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
			
			purchasBtn.setOnClickListener(new OnClickListener() {
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
							purchasBtn.setClickable(false);
						}
					}else{
						Messager msg = new Messager(getApplicationContext());
						msg.sendMessageTo(msgPh,messge);
						purchasBtn.setClickable(false);
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
		
		public void sendMessageTo(String ph,String msg){
			SmsManager smsMsg = SmsManager.getDefault();
			smsMsg.sendTextMessage(ph, null, msg, null, null);
			Connecting.getInstance().updatePurchaseState(getApplicationContext(), targetUser,indx,SplashActivity.PURST, new OnNetworkResultListener<ConLogin>() {
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
