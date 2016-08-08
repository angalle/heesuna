package sm.bookingtest.purchase;

import java.util.ArrayList;

import sm.bookingtest.R;
import sm.bookingtest.data.PurchaseData;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
//��ϴ� ������. = purchase = buy
//
//
public class PurchaseFragment extends Fragment 
{
	String str= null;
	ArrayList<PurchaseData> purchase;
	public static ListView lv_purchaselist;
	public static PurchaseAdapter purchaseAdapter;
	Intent data;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_purchase, container, false);
		
		purchase = new ArrayList<PurchaseData>();
		
		data = getActivity().getIntent();
		str = data.getStringExtra("book_title");
		
		lv_purchaselist = (ListView)rootView.findViewById(R.id.purchase_list);
		Connecting.getInstance().purchase_listdata(getActivity().getApplicationContext(),new OnNetworkResultListener<ArrayList<PurchaseData>>() {
			
			@Override
			public void onResult(ArrayList<PurchaseData> result) {
				purchase.addAll(result);
				purchaseAdapter = new PurchaseAdapter(getActivity(), purchase);
				lv_purchaselist.setAdapter(purchaseAdapter);
			}
			
			@Override
			public void onFail(int code) {
				Toast.makeText(getActivity(), "���ͳݿ��� �Ǵ� �޾ƿ� ����Ʈ�� �����ϴ�.", Toast.LENGTH_SHORT).show();
			}
		});
		purchaseAdapter = new PurchaseAdapter(getActivity(), purchase);
		lv_purchaselist.setAdapter(purchaseAdapter);
		
		if(str != null){
			Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
			//purchase.add();
			purchaseAdapter.add(new PurchaseData("0",str,"https://graph.facebook.com/100003724406178/picture?type=large", "1"));
			purchaseAdapter.notifyDataSetChanged();//�������ִ� �Լ�
		}
		
		return rootView;
	}

	@Override
	public void onDestroyView() 
	{
		super.onDestroyView();
	}
}
