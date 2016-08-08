package sm.bookingtest.mypagesell;

import java.util.ArrayList;

import sm.bookingtest.data.SellData;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyPageSellAdapter extends BaseAdapter implements OnClickListener
{
	ArrayList<SellData> sell;
	private Context context;
	
	public MyPageSellAdapter(Context context, ArrayList<SellData> list) 
	{
		//super();
		this.context = context;
		sell = new ArrayList<SellData>();
		sell.addAll(list);
	}

	@Override
	public int getCount() 
	{
		
		return sell.size();
	}

	@Override
	public Object getItem(int position) 
	{
		
		return sell.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		MypageSellView itemView;
		
		if(convertView == null)
		{
			itemView = new MypageSellView(context);
		}
		else
		{
			itemView = (MypageSellView)convertView;
		}		
		
		itemView.setTag(position);
		
		itemView.setData(sell.get(position));

		return itemView;
	}

	@Override
	public void onClick(View v) 
	{
		int position = (Integer)v.getTag();
		SellData data = (SellData)getItem(position);

	}
}
