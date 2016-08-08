package sm.bookingtest.sell;

import java.util.ArrayList;

import sm.bookingtest.data.SellData;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SellAdapter<T> extends BaseAdapter implements OnClickListener 
{

	ArrayList<SellData> sale;
	private Context context;

	public SellAdapter(Context context, ArrayList<SellData> list) 
	{
		super();
		this.context = context;
		sale = new ArrayList<SellData>();
		sale.addAll(list);
	}

	@Override
	public int getCount() 
	{
		return sale.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return sale.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		SellView itemView;

		if (convertView == null) 
		{
			itemView = new SellView(context);
		} 
		else 
		{
			itemView = (SellView) convertView;
		}
		itemView.setTag(position);
		
		itemView.setData(sale.get(position));
		
		return itemView;
	}

	@Override
	public void onClick(View v) 
	{
		int position = (Integer)v.getTag();
		
		SellData data = (SellData)getItem(position);

	}
	
	public void add(SellData saledata)
	{
		sale.add(saledata);	
	}
}
