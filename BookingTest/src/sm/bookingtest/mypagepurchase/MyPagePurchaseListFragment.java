package sm.bookingtest.mypagepurchase;

import java.util.ArrayList;

import sm.bookingtest.R;
import sm.bookingtest.data.PurchaseData;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import sm.bookingtest.util.SplashActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MyPagePurchaseListFragment extends ActionBarActivity {
	ArrayList<PurchaseData> purchase;
	String id;
	public static ListView lv_purchaselist;
	public static MyPagePurchaseAdapter purchaseAdapter;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_purchase);
		//���⼭ ����Ʈ�� �Ѱܿ� ���� �� ���ش�.
		purchase = new ArrayList<PurchaseData>();
		context = MyPagePurchaseListFragment.this;
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
				Toast.makeText(getApplicationContext(), "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", 5000).show();
			}
		});
		purchaseAdapter = new MyPagePurchaseAdapter(MyPagePurchaseListFragment.this, purchase);
		
		lv_purchaselist.setAdapter(purchaseAdapter);
	}
}
