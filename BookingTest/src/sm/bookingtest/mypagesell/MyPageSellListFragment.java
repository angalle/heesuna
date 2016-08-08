package sm.bookingtest.mypagesell;

import java.util.ArrayList;

import sm.bookingtest.R;
import sm.bookingtest.data.SellData;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import sm.bookingtest.util.SplashActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

public class MyPageSellListFragment extends ActionBarActivity {
	ArrayList<SellData> sell;
	String id, pw;
	public static ListView lv_selllist;
	public static MyPageSellAdapter sellAdapter;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_sale);
		context = MyPageSellListFragment.this;
		sell = new ArrayList<SellData>();
		lv_selllist = (ListView) findViewById(R.id.sale_list);
		
		SharedPreferences idpw = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		id = idpw.getString(SplashActivity.ID, "");		

		Connecting.getInstance().one_sell_listdata(this,id,new OnNetworkResultListener<ArrayList<SellData>>() {
			@Override
			public void onResult(ArrayList<SellData> result) {
				// TODO Auto-generated method stub
				sell.addAll(result);
				sellAdapter = new MyPageSellAdapter(MyPageSellListFragment.this, sell);
				lv_selllist.setAdapter(sellAdapter);
			}
			@Override
			public void onFail(int code) {
				// TODO Auto-generated method stub
				Toast.makeText(MyPageSellListFragment.this, "개인 판매 목록 로드 실패", Toast.LENGTH_SHORT).show();
			}
		});
		sellAdapter = new MyPageSellAdapter(MyPageSellListFragment.this, sell);
		lv_selllist.setAdapter(sellAdapter);
	}
}
