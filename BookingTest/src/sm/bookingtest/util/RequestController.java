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
	
	//������ ������Ʈ �� "�˴ϴ�" ����Ʈ ���� ������Ʈ.
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
					Toast.makeText(context, "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		//������ ������Ʈ �� "��ϴ�" ����Ʈ ���� ������Ʈ.
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
					Toast.makeText(context, "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
				}
			});
		}
	
	//������ ������Ʈ �� "�˴ϴ�" ���� ����Ʈ ���� ������Ʈ.
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
				Toast.makeText(context, "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//������ ������Ʈ �� "��ϴ�" ���� ����Ʈ ���� ������Ʈ.
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
				Toast.makeText(context, "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, "�ǸſϷ� ó�� �Ϸ�", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, "�ǸſϷ� ó�� ����", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void modifyPurchaseData(final String id,String pw,String indx,String bookName,File file) {
		try {
			Connecting.getInstance().one_buy_modify(context, id,pw,indx,bookName,file, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updatePurchaseMyList(id);
						Toast.makeText(context, "���� �Ϸ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "���� ����, ��й�ȣ�� Ȯ���ϼ���.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "���� ����, ������ ���� �߻�.", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "���� �Ϸ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "���� ����, ��й�ȣ�� Ȯ���ϼ���.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "���� ����, ������ ���� �߻�.", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "���� �Ϸ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "���� ����, ��й�ȣ�� Ȯ���ϼ���.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "���� ����, ������ ���� �߻�.", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "���� �Ϸ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "���� ����, ��й�ȣ�� Ȯ���ϼ���.", Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onFail(int code) {
					Log.e("result Check",code+"err");
					Toast.makeText(context, "���� ����, ������ ���� �߻�.", Toast.LENGTH_SHORT).show();
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
				// �����͸� ���� �� �ڵ����� ����Ʈ ������Ʈ.
				Log.e("result ::::",result.getResult());
				if(result.getResult().equals("success")){
					updatePurchaseMyList(id);
					Toast.makeText(context, "���� ��ǰ�� �����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
				}else{
					//���� �ȵ� �޽���
					Toast.makeText(context, "���� ���� �Ͽ����ϴ�. �ٽ� �õ��� �ּ���.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFail(int code) {
				//���� �ȵ� �޼���
				Toast.makeText(context, code+":: ���� ����!!!", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(context, "�Ǹ� ��ǰ�� �����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
				}else{
					//���� �ȵ� �޽���
					Toast.makeText(context, "���� ���� �Ͽ����ϴ�. �ٽ� �õ��� �ּ���.", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFail(int code) {
				//���� �ȵ� �޼���
				Toast.makeText(context, code+":: ���� ����!!!", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, code+":: ���� ����!!!", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, code+":: ���� ����!!!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
