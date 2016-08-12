package com.sm.bookingtest.purchase;

import java.util.ArrayList;

import com.sm.bookingtest.data.PurchaseData;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PurchaseAdapter extends BaseAdapter implements OnClickListener
{
	ArrayList<PurchaseData> purchase;
	private Context context;
	
	public PurchaseAdapter(Context context, ArrayList<PurchaseData> list) 
	{
		super();
		this.context = context;
		purchase = new ArrayList<PurchaseData>();
		purchase.addAll(list);
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
		PurchaseView itemView;
		
		if(convertView == null)
		{
			itemView = new PurchaseView(context);
		}
		else
		{
			itemView = (PurchaseView)convertView;
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

	}

	public void add(PurchaseData purchaseData) 
	{
		// TODO Auto-generated method stub
		purchase.add(purchaseData);
	}

}
