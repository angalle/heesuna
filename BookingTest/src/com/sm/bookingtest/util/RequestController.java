package com.sm.bookingtest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sm.bookingtest.data.ConLogin;
import com.sm.bookingtest.data.PurchaseData;
import com.sm.bookingtest.mypagepurchase.MyPagePurchaseAdapter;
import com.sm.bookingtest.mypagepurchase.MyPagePurchaseListFragment;
import com.sm.bookingtest.register.RegisterFragmentPurchase;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

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
	
	//������ ������Ʈ �� ����Ʈ ���� ������Ʈ.
	public void updateList(String id){
		Connecting.getInstance().one_buy_listdata(context, id, new OnNetworkResultListener<ArrayList<PurchaseData>>() {
			@Override
			public void onResult(ArrayList<PurchaseData> result) {
				Log.i("test","ok");
				ArrayList<PurchaseData> data = new ArrayList<PurchaseData>();
				data.addAll(result);
				mAdapter = new MyPagePurchaseAdapter(context, data);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFail(int code) {
				Toast.makeText(context, "������ �޾ƿ��µ� ���� �Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void updateState(final String id,String indx,String CurrentState,final String msg1,final String msg2){
		Connecting.getInstance().updatePurchaseState(context, id,indx,CurrentState, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				updateList(id);
				Toast.makeText(context, msg1, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFail(int code) {
				Toast.makeText(context, msg2, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void modifyData(final String id,String pw,String indx,String bookName,File file) {
		try {
			Connecting.getInstance().one_buy_modify(context, id,pw,indx,bookName,file, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					Log.e("result Check",result.getResult()+"suc");
					if(result.getResult().equals("success")){
						updateList(id);
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
	
	public void modifyData(final String id,String pw,String indx,String bookName) {
		try {
			Connecting.getInstance().one_buy_modify(context, id,pw,indx,bookName, new OnNetworkResultListener<ConLogin>() {
				public void onResult(ConLogin result) {
					if(result.getResult().equals("success")){
						updateList(id);
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
	
	public void deleteData(final String id,String indx,String passwd){
		Connecting.getInstance().one_buy_delete(context,id,passwd,indx, new OnNetworkResultListener<ConLogin>() {
			@Override
			public void onResult(ConLogin result) {
				// �����͸� ���� �� �ڵ����� ����Ʈ ������Ʈ.
				Log.e("result ::::",result.getResult());
				if(result.getResult().equals("success")){
					updateList(id);
					Toast.makeText(context, "�����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
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
}