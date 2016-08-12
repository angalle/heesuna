package com.sm.bookingtest.sell;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sm.bookingtest.R;
import com.sm.bookingtest.data.SellData;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class SellFragment extends Fragment
{
	
	static ArrayList<SellData> sale;
	ListView lv_salelist;
	SellAdapter saleAdapter;
	Intent data;
	String str = null;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_sale, container, false);
	
		sale = new ArrayList<SellData>();
	
		data = getActivity().getIntent();
		str = data.getStringExtra("book_title");
		
		
		lv_salelist = (ListView)rootView.findViewById(R.id.sale_list);
		Connecting.getInstance().sell_listdata(getActivity().getApplicationContext(), new OnNetworkResultListener<ArrayList<SellData>>() {
			
			@Override
			public void onResult(ArrayList<SellData> result) {
				// TODO Auto-generated method stub
				sale.addAll(result);
				saleAdapter = new SellAdapter(getActivity(), sale);
				
				lv_salelist.setAdapter(saleAdapter);
			}
			
			@Override
			public void onFail(int code) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "인터넷연결 또는 받아올 리스트가 없습니다.", 5000).show();
			}
		});
		
		saleAdapter = new SellAdapter(getActivity(), sale);
		
		lv_salelist.setAdapter(saleAdapter);
		
		if(str != null)
		{
			Toast.makeText(getActivity(), str, 5000).show();
			//purchase.add();
			saleAdapter.add(new SellData("1","ID","BOOKNAME","15000","20000","B","https://graph.facebook.com/100003724406178/picture?type=large"));
			saleAdapter.notifyDataSetChanged();//갱신해주는 함수
		}
		
		return rootView;
	}
}
