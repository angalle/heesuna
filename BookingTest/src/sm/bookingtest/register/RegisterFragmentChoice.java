package sm.bookingtest.register;

import sm.bookingtest.R;
import sm.bookingtest.purchase.PurchaseFragment;
import sm.bookingtest.util.RequestController;
import sm.bookingtest.util.SplashActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class RegisterFragmentChoice extends Fragment implements OnClickListener
{
	Button buy_btn, sell_btn;
	FragmentTransaction transaction;
	Fragment newFragment;
	SharedPreferences sh;
	String checkWhoStr;
	Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_register_choice,
				container, false);
		buy_btn = (Button) rootView.findViewById(R.id.register_buy_btn);
		sell_btn = (Button) rootView.findViewById(R.id.register_sell_btn);
		sh = getActivity().getSharedPreferences(SplashActivity.USERINFO, getActivity().MODE_PRIVATE);
		checkWhoStr = sh.getString(SplashActivity.ID, "아이디");
		buy_btn.setOnClickListener(this);
		sell_btn.setOnClickListener(this);
		context = getContext();
		return rootView;
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.register_buy_btn){
			checkWho(RegisterFragmentPurchase.class);
		}else if(v.getId() == R.id.register_sell_btn){
			checkWho(RegisterFragmentSell.class);
		}
	}
	
	private void checkWho(Class nextPage){
		if(!checkWhoStr.equals("아이디")){
			Intent next = new Intent(getActivity(),	nextPage);
			next.addFlags(0);
			startActivityForResult(next,0);
		}else{
			Toast.makeText(getContext(), "손님은 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String refreshData="default";
		if(data!=null)
			refreshData = data.getStringExtra("refresh");
		RequestController rc = new RequestController(context, PurchaseFragment.purchaseAdapter, PurchaseFragment.lv_purchaselist);
		if(refreshData.equals("purchase")){
			rc.updatePurchaseList();
		}else if(refreshData.equals("sell")){
			System.out.println("sell");
		}else{
			System.out.println("default");
		}
	}
	
	
	
}
