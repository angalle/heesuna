package com.sm.bookingtest.mypagesell;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sm.bookingtest.R;
import com.sm.bookingtest.data.ConLogin;
import com.sm.bookingtest.data.SellData;
import com.sm.bookingtest.register.RegisterFragmentSell;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.SplashActivity;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class MypageSellView extends FrameLayout implements OnClickListener
{
	Context context;
	String indx, id, passwd;
	ImageLoader imageLoader;
	ImageView iv_bookimage,status_img;
	TextView tv_title,tv_hopePrice,tv_originPrice,tv_state;
	ImageButton btn_sale_modify,btn_sale_delete;
	
	public MypageSellView(Context context) 
	{
		super(context);
		this.context = context;
		init();
	}
	
	private void init() 
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
		ImageLoader.getInstance().init(config);
		
		LayoutInflater.from(getContext()).inflate(R.layout.my_list_sale, this);
		iv_bookimage = (ImageView)findViewById(R.id.iv_sale_bookimage);
		status_img = (ImageView)findViewById(R.id.purchase_status_img);
		tv_title = (TextView)findViewById(R.id.tv_sale_title);
		tv_hopePrice = (TextView)findViewById(R.id.tv_sale_price);
		tv_originPrice = (TextView)findViewById(R.id.tv_sale_cost);
		tv_state = (TextView)findViewById(R.id.tv_sale_condition);
		btn_sale_modify = (ImageButton)findViewById(R.id.my_sale_modify);
		btn_sale_delete = (ImageButton)findViewById(R.id.my_sale_delete);
		imageLoader = ImageLoader.getInstance();
	}

	public void setData(SellData myPageSellData)
	{
		imageLoader.displayImage(myPageSellData.getSell_picture(), iv_bookimage);
		tv_title.setText(myPageSellData.getBook_name());
		tv_hopePrice.setText(myPageSellData.getHope_price());
		tv_originPrice.setText(myPageSellData.getOrigin_price());
		tv_state.setText(myPageSellData.getState());
		indx = myPageSellData.getIndx();
		id = myPageSellData.getId();
		
		btn_sale_modify.setOnClickListener(this);
		btn_sale_delete.setOnClickListener(this);
		status_img.setOnClickListener(this);
		String status = myPageSellData.getState();
		if (status.equals("A급"))
			status_img.setImageResource(R.drawable.grade_a);
		else if (status.equals("B급"))
			status_img.setImageResource(R.drawable.grade_b);
		else if (status.equals("C급"))
			status_img.setImageResource(R.drawable.grade_c);
		else if (status.equals("D급"))
			status_img.setImageResource(R.drawable.grade_d);

		if(myPageSellData.getSell_state() == SplashActivity.SELLST){
			//판매중인 이미지를 제어. 판매중이면 여기서 이미지 제어
			status_img.setImageResource(R.drawable.grade_aplus);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.my_sale_modify){
				AlertDialog.Builder dial = new AlertDialog.Builder(context);
				dial.setTitle("수정");
				//다이얼로그 오류 ㅠㅠㅠㅠㅠ
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.delete_dialog,	null);
				dial.setTitle("수정하시겠습니까 ?");
				dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int which) {
						//쿼리문을 조회하고
						//인텐트로 데이터를 넘긴다.
						Connecting.getInstance().one_sell_data(context, indx, new OnNetworkResultListener<ArrayList<SellData>>() {
							@Override
							public void onResult(ArrayList<SellData> result) {
								// TODO Auto-generated method stub
								SellData data = new SellData();
								data = result.get(0);
								
								Intent next = new Intent(context,RegisterFragmentSell.class);
								next.addFlags(1);
								next.putExtra("indx", data.getIndx());
								next.putExtra("id", data.getId());
								next.putExtra("bookNm", data.getBook_name());
								next.putExtra("hopePrice", data.getHope_price());
								next.putExtra("originPrice", data.getOrigin_price());
								next.putExtra("state", data.getState());
								next.putExtra("imgUrl", data.getSell_picture());
								
								context.startActivity(next);
								dialog.cancel();
							}

							@Override
							public void onFail(int code) {
								dialog.cancel();
							}
						});
						
					}
				});
				dial.show();
		}else if(v.getId() == R.id.my_sale_delete){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("삭제");
			//다이얼로그 오류 ㅠㅠㅠㅠㅠ
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog, null);
			final EditText pw = (EditText) view.findViewById(R.id.password);
			dial.setView(view);
			dial.setTitle("삭제하시겠습니까?");
			
			dial.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			dial.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					passwd = pw.getText().toString();
					Connecting.getInstance().one_sell_delete(context,id,passwd,indx, new OnNetworkResultListener<ConLogin>() {
						@Override
						public void onResult(ConLogin result) {
							// TODO Auto-generated method stub
							Log.e("result ::::",result.getResult());
							if(result.getResult().equals("success")){
								Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
							}else{
								//삭제 안된 메시지
								Toast.makeText(context, "삭제 실패!!!", Toast.LENGTH_SHORT).show();
							}
						}
						@Override
						public void onFail(int code) {
							//삭제 안된 메세지
							Toast.makeText(context, code+":: 삭제 실패!!!", Toast.LENGTH_SHORT).show();
						}
					});
					dialog.cancel();
				}
			});
			dial.show();
		}else if(v.getId() == R.id.purchase_status_img){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("판매완료/판매재개");
			//다이얼로그 오류 ㅠㅠㅠㅠㅠ
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			dial.setTitle("판매완료/판매재개 하시겠습니까 ?");
			dial.setPositiveButton("판매재개", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//쿼리문을 조회하고
					//인텐트로 데이터를 넘긴다.
					Connecting.getInstance().updateSellState(context, id,indx,SplashActivity.NORST, new OnNetworkResultListener<ConLogin>() {
						@Override
						public void onResult(ConLogin result) {
							Toast.makeText(context, "판매재개 처리 완료", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFail(int code) {
							Toast.makeText(context, "판매재개 처리 실패", Toast.LENGTH_SHORT).show();
						}
					});
					dialog.cancel();
				}
			});
			dial.setNegativeButton("판매완료", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//쿼리문을 조회하고
					//인텐트로 데이터를 넘긴다.
					Connecting.getInstance().updateSellState(context, id,indx,SplashActivity.COMPLETE, new OnNetworkResultListener<ConLogin>() {
						@Override
						public void onResult(ConLogin result) {
							Toast.makeText(context, "판매완료 처리 완료", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFail(int code) {
							Toast.makeText(context, "판매완료 처리 실패", Toast.LENGTH_SHORT).show();
						}
					});
					dialog.cancel();
				}
			});
			dial.show();
		}
	}
}
