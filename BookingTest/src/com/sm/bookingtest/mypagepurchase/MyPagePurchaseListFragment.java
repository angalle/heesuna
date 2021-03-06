package com.sm.bookingtest.mypagepurchase;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.sm.bookingtest.MainActivity;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.PurchaseData;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.SplashActivity;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class MyPagePurchaseListFragment extends ActionBarActivity {
	ArrayList<PurchaseData> purchase;
	String id;
	public static ListView lv_purchaselist;
	public static MyPagePurchaseAdapter purchaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_purchase);
		//여기서 인텐트로 넘겨온 값을 더 해준다.
		purchase = new ArrayList<PurchaseData>();

		//MyPageSellAdapter purchaseAdapter = new MyPageSellAdapter(getApplicationContext(), sell);
		SharedPreferences idpw = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		id = idpw.getString(SplashActivity.ID,"");
		lv_purchaselist = (ListView)findViewById(R.id.purchase_list);
		Connecting.getInstance().one_buy_listdata(this, id, new OnNetworkResultListener<ArrayList<PurchaseData>>() {
			@Override
			public void onResult(ArrayList<PurchaseData> result) {
				// TODO Auto-generated method stub
				Log.i("test","ok");
				purchase.addAll(result);
				purchaseAdapter = new MyPagePurchaseAdapter(MyPagePurchaseListFragment.this,purchase);
				lv_purchaselist.setAdapter(purchaseAdapter);
			}
			
			@Override
			public void onFail(int code) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "데이터 받아오는데 실패 하였습니다.", 5000).show();
			}
		});
		purchaseAdapter = new MyPagePurchaseAdapter(MyPagePurchaseListFragment.this, purchase);
		
		lv_purchaselist.setAdapter(purchaseAdapter);
	}
}
