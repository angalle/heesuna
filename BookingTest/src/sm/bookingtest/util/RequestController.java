package sm.bookingtest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import sm.bookingtest.data.ConLogin;
import sm.bookingtest.data.PurchaseData;
import sm.bookingtest.data.SellData;
import sm.bookingtest.mypagepurchase.MyPagePurchaseAdapter;
import sm.bookingtest.mypagepurchase.MyPagePurchaseListFragment;
import sm.bookingtest.mypagesell.MyPageSellAdapter;
import sm.bookingtest.purchase.PurchaseAdapter;
import sm.bookingtest.register.RegisterFragmentPurchase;
import sm.bookingtest.register.RegisterFragmentSell;
import sm.bookingtest.sell.SellAdapter;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RequestController {
	static Context context;
	static BaseAdapter mAdapter;
	static ListView listView;
	public RequestController(Context context,BaseAdapter mAdapter,ListView targetListView) {
		super();
		RequestController.context = context;
		RequestController.mAdapter = mAdapter;
		RequestController.listView = targetListView;
	}
	
	//데이터 업데이트 후 "팝니다" 리스트 강제 업데이트.
		public void updatePurchaseList(){
			Connecting.getInstance().purchase_listdata(context, new OnNetworkResultListener<ArrayList<PurchaseData>>() {
				@Override
				public void onResult(ArrayList<PurchaseData> result) {
					ArrayList<PurchaseData> data = new ArrayList<PurchaseData>();
					data.addAll(result);
					mAdapter = new PurchaseAdapter(context, data);
					listView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					Log.i("purchase list","ok");
				}
				@Override
				public void onFail(int code) {
					Toast.makeText(context, "데이터 받아오는데 실패 하였습니다.", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		//데이터 업데이트 후 "삽니다" 리스트 강제 업데이트.
		public void updateSellList(){
			Connecting.getInstance().sell_listdata(context, new OnNetworkResultListener<ArrayList<SellData>>() {
				@Override
				public void onResult(ArrayList<SellData> result) {
					ArrayList<SellData> data = new ArrayList<SellData>();
					data.addAll(result);
					mAdapter = new SellAdapter(context, data);
					listView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					Log.i("sell list","ok");
				}
				
				@Override
				public void onFail(int code) {
					Toast.makeText(context, "데이터 받아오는데 실패 하였습니다.", Toast.LENGTH_SHORT).show();
				}
			});
		}
	
	//데이터 업데이트 후 "팝니다" 마이 리스트 강제 업데이트.
	public void updatePurchaseMyList(String id){
		Connecting.getInstance().one_buy_listdata(context, id, new OnNetworkResultListener<ArrayList<PurchaseData>>() {
			@Override
			public void onResult(ArrayList<PurchaseData> result) {
				ArrayList<PurchaseData> data = new ArrayList<PurchaseData>();
				data.addAll(result);
				mAdapter = new MyPagePurchaseAdapter(context, data);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				Log.i("purchase Mylist","ok");
			}
			
			@Override
			public void onFail(int code) {
				Toast.makeText(context, "데이터 받아오는데 실패 하였습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//데이터 업데이트 후 "삽니다" 마이 리스트 강제 업데이트.
	public void updateSellMyList(String id){
		Connecting.getInstance().one_sell_listdata(context, id, new OnNetworkResultListener<ArrayList<SellData>>() {
			@Override
			public void onResult(ArrayList<SellData> result) {
				ArrayList<SellData> data = new ArrayList<SellData>();
				data.addAll(result);
				mAdapter = new MyPageSellAdapter(context, data);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				Log.i("sell Mylist","ok");
			}
			
			@Override
			public void onFail(int code) {
				Toast.makeText(context, "데이터 받아오는데 실패 하였습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void updatePurchaseState(final String id,String indx,String CurrentState,final String msg1,final String msg2){
		Connecting.getInstance().updatePurchaseState(context, id,indx,CurrentState, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				updatePurchaseMyList(id);
				Toast.makeText(context, msg1, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, msg2, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void updateSellState(final String id,String indx,String CurrentState,final String msg1,final String msg2){
		Connecting.getInstance().updateSellState(context, id,indx,SplashActivity.COMPLETE, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				updateSellMyList(id);
				Toast.makeText(context, "판매완료 처리 완료", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, "판매완료 처리 실패", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void modifyPurchaseData(final String id,String pw,String indx,String bookName,File file) {
		try {
			Connecting.getInstance().one_buy_modify(context, id,pw,indx,bookName,file, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updatePurchaseMyList(id);
						Toast.makeText(context, "수정 완료", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "수정 실패, 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "수정 실패, 데이터 오류 발생.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifyPurchaseData(final String id,String pw,String indx,String bookName) {
		try {
			Connecting.getInstance().one_buy_modify(context, id,pw,indx,bookName, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updatePurchaseMyList(id);
						Toast.makeText(context, "수정 완료", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "수정 실패, 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "수정 실패, 데이터 오류 발생.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifySellData(final String id,String pw,String indx,String bookName,String state,String hopePrice,String originPrice,File file) {
		try {
			Connecting.getInstance().one_sell_modify(context, id,pw,indx,bookName,state,hopePrice,originPrice,file, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updateSellMyList(id);
						Toast.makeText(context, "수정 완료", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "수정 실패, 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "수정 실패, 데이터 오류 발생.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifySellData(final String id,String pw,String indx,String bookName,String state,String hopePrice,String originPrice) {
		try {
			
			Connecting.getInstance().one_sell_modify(context, id,pw,indx,bookName,state,hopePrice,originPrice, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updateSellMyList(id);
						Toast.makeText(context, "수정 완료", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "수정 실패, 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "수정 실패, 데이터 오류 발생.", Toast.LENGTH_SHORT).show();
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deletePurchaseData(final String id,String indx,String passwd){
		Connecting.getInstance().one_buy_delete(context,id,passwd,indx, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				// 데이터를 삭제 후 자동으로 리스트 업데이트.
				Log.e("result ::::",result.getResult());
				if(result.getResult().equals("success")){
					updatePurchaseMyList(id);
					Toast.makeText(context, "구입 상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
				}else{
					//삭제 안된 메시지
					Toast.makeText(context, "삭제 실패 하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFail(int code) {
				//삭제 안된 메세지
				Toast.makeText(context, code+":: 삭제 실패!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void deleteSellData(final String id,String indx,String passwd){
		Connecting.getInstance().one_sell_delete(context,id,passwd,indx, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				// TODO Auto-generated method stub
				Log.e("result ::::",result.getResult());
				if(result.getResult().equals("success")){
					updateSellMyList(id);
					Toast.makeText(context, "판매 상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
				}else{
					//삭제 안된 메시지
					Toast.makeText(context, "삭제 실패 하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFail(int code) {
				//삭제 안된 메세지
				Toast.makeText(context, code+":: 삭제 실패!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void selectPurchaseData(String indx){
		Connecting.getInstance().one_buy_data(context,indx, new OnNetworkResultListener<ArrayList<PurchaseData>>() {
			@Override
			public void onResult(ArrayList<PurchaseData> result) {
				PurchaseData data = new PurchaseData();
				data = result.get(0);
				
				Intent next = new Intent(context,RegisterFragmentPurchase.class);
				next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				next.putExtra("indx", data.getIndx());
				next.putExtra("id", data.getId());
				next.putExtra("bookNm", data.getBook_name());
				next.putExtra("imgUrl", data.getImage_url());
				context.startActivity(next);
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, code+":: 수정 실패!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void selectSellData(String indx){
		Connecting.getInstance().one_sell_data(context,indx, new OnNetworkResultListener<ArrayList<SellData>>() {
			@Override
			public void onResult(ArrayList<SellData> result) {
				SellData data = new SellData();
				data = result.get(0);
				
				Intent next = new Intent(context,RegisterFragmentSell.class);
				next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				next.putExtra("indx", data.getIndx());
				next.putExtra("id", data.getId());
				next.putExtra("bookNm", data.getBook_name());
				next.putExtra("hopePrice", data.getHope_price());
				next.putExtra("originPrice", data.getOrigin_price());
				next.putExtra("state", data.getState());
				next.putExtra("imgUrl", data.getSell_picture());
				context.startActivity(next);
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, code+":: 수정 실패!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
