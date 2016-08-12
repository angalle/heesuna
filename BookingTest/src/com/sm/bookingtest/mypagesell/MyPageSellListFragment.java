package com.sm.bookingtest.mypagesell;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.sm.bookingtest.R;
import com.sm.bookingtest.data.SellData;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;
import com.sm.bookingtest.util.SplashActivity;

public class MyPageSellListFragment extends ActionBarActivity {
	ArrayList<SellData> sell;
	String id, pw;
	ListView lv_purchaselist;
	MyPageSellAdapter purchaseAdapter;
	static public Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sale);
		context = this;
		sell = new ArrayList<SellData>();
		lv_purchaselist = (ListView) findViewById(R.id.sale_list);
		
		SharedPreferences idpw = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		id = idpw.getString(SplashActivity.ID, "");		

		Connecting.getInstance().one_sell_listdata(this,id,new OnNetworkResultListener<ArrayList<SellData>>() {
			@Override
			public void onResult(ArrayList<SellData> result) {
				// TODO Auto-generated method stub
				sell.addAll(result);
				purchaseAdapter = new MyPageSellAdapter(MyPageSellListFragment.this, sell);
				lv_purchaselist.setAdapter(purchaseAdapter);
			}
			@Override
			public void onFail(int code) {
				// TODO Auto-generated method stub
				Toast.makeText(MyPageSellListFragment.this, "개인 판매 목록 로드 실패", Toast.LENGTH_SHORT).show();
			}
		});
		purchaseAdapter = new MyPageSellAdapter(MyPageSellListFragment.this, sell);
		lv_purchaselist.setAdapter(purchaseAdapter);
	}
}
