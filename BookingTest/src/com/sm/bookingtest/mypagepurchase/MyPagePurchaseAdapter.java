package com.sm.bookingtest.mypagepurchase;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sm.bookingtest.data.PurchaseData;

public class MyPagePurchaseAdapter extends BaseAdapter implements OnClickListener
{
	ArrayList<PurchaseData> purchase;
	private Context context;
	public MyPagePurchaseAdapter(Context context, ArrayList<PurchaseData> purchase2) 
	{
		super();
		this.context = context;
		purchase = new ArrayList<PurchaseData>();
		purchase.addAll(purchase2);
	}

	@Override
	public int getCount() 
	{
		return purchase.size();
	}

	@Override
	public Object getItem(int position)
	{
		return purchase.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		MypagePurchaseView itemView;
		if(convertView == null)
		{
			itemView = new MypagePurchaseView(context);
		}
		else
		{
			itemView = (MypagePurchaseView)convertView;
		}
		itemView.setTag(position);
		itemView.setData(purchase.get(position));
		return itemView;
	}

	@Override
	public void onClick(View v) 
	{
		int position = (Integer)v.getTag();

		PurchaseData data = (PurchaseData)getItem(position);
		AlertDialog.Builder dial = new AlertDialog.Builder(context);
		dial.setMessage("");
		dial.show();
	}

}
