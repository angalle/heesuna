package com.sm.bookingtest.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sm.bookingtest.MainActivity;
import com.sm.bookingtest.R;
import com.sm.bookingtest.util.SplashActivity;

public class RegisterFragmentChoice extends Fragment implements OnClickListener
{
	Button buy_btn, sell_btn;
	FragmentTransaction transaction;
	Fragment newFragment;
	SharedPreferences sh;
	String checkWhoStr;
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
			startActivity(next);
		}else{
			Toast.makeText(getContext(), "손님은 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.print("오빠가 무식해서그래");
	}
	
	
	
}
