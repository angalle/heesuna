package sm.bookingtest.mypagepurchase;

import sm.bookingtest.R;
import sm.bookingtest.mypagesell.MyPageSellListFragment;
import sm.bookingtest.util.SplashActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyPageFragment extends Fragment implements OnClickListener{
	String id, ph;
	SharedPreferences shared;
	String checkWho;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mypage, container,
				false);

		TextView id_text = (TextView) rootView.findViewById(R.id.mypage_id);
		TextView ph_text = (TextView) rootView.findViewById(R.id.mypage_ph);

		// �����ͺ��̽����� �޾ƿ� ���̵�� ��ȭ��ȣ�� �ִ´�.
		// ������� ���̵�� ��ȭ��ȣ�� ������ �ȴ�.
		SharedPreferences idpn = getActivity().getSharedPreferences(SplashActivity.USERINFO,getActivity().MODE_PRIVATE);
		id = idpn.getString(SplashActivity.ID, "booking-id");
		ph = idpn.getString(SplashActivity.PH, "booking-ph");

		id_text.setText(id);
		ph_text.setText(ph);

		Button purchase_btn = (Button) rootView	.findViewById(R.id.mypage_purchase_btn);
		purchase_btn.setOnClickListener(this);

		Button sell_btn = (Button) rootView.findViewById(R.id.mypage_sale_btn);
		sell_btn.setOnClickListener(this);

		
		shared = getActivity().getSharedPreferences(SplashActivity.USERINFO, getActivity().MODE_PRIVATE);
		checkWho = shared.getString(SplashActivity.ID, "���̵�");
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if(!checkWho.equals("���̵�")){
			if(v.getId() == R.id.mypage_purchase_btn){
				Intent next = new Intent(getActivity(),	MyPagePurchaseListFragment.class);
				startActivity(next);
			}else if(v.getId() == R.id.mypage_sale_btn){
				Intent next = new Intent(getActivity(), MyPageSellListFragment.class);
				startActivity(next);
			}
		}else{
			Toast.makeText(getContext(), "�մ��� �̿��ϽǼ� �����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	}
}