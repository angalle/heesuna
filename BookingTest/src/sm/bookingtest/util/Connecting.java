package sm.bookingtest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sm.bookingtest.data.ConLogin;
import sm.bookingtest.data.LoginData;
import sm.bookingtest.data.PurchaseData;
import sm.bookingtest.data.SellData;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Connecting {
	public static final String MainActivity = null;
	AsyncHttpClient client;
	private static Connecting instance;

	public static String PORT = ":40002";
	public static String SERVER_URL = "http://52.79.137.125" + PORT;
	

	public static String LOGIN = "/login";
	public static String JOIN = "/join";
	//�Ǹ�, ���� ���
	public static String SELLREG = "/sellregister";
	public static String BUYREG = "/buyregister";
	//�Ǹ�, ���� ����Ʈ �޾ƿ���
	public static String SELLLIST = "/selllist";
	public static String BUYLIST = "/buylist";
	//���� �Ǹ�, ���� ����Ʈ �޾ƿ���
	public static String USERSELLIST = "/userselllist";
	public static String USERBUYLIST = "/userbuylist";

	public static String USERBUYDELETE= "/userbuydelete";
	public static String USERSELLDELETE = "/userselldelete";
	
	public static String USERPURCHASEMODIFIY= "/userbuyupdate";
	public static String USERSELLMODIFIY = "/usersellupdate";
	public static String USERPURCHASEBOOKMODIFIY= "/userbuybookupdate";
	public static String USERSELLBOOKMODIFIY = "/usersellbookupdate";
	
	public static String USERBUYDATA= "/userbookbuydata";
	public static String USERSELLDATA = "/userbookselldata";
	
	public static String ONEUSERDATA = "/oneuserdata";
	
	// �������� ����
	public static String ONEUSERMODIFICATION = "/userphchange";
	
	public static String UPDATEPURCHASESTATE = "/updatepurchasestate";
	public static String UPDATESELLSTATE = "/updatesellstate";

	// �̱��� �г����μ� ��ü�� �ѹ��� �����ϰ� ����� �޼ҵ��̴�.
	public static Connecting getInstance() {
		if (instance == null) {
			instance = new Connecting();
		}
		return instance;

	}

	// �����ڷ� ��ü ������ �Ѵ�.
	private Connecting() {
		client = new AsyncHttpClient();
		client.setTimeout(3000);
	}

	// ��ҿ�û ������Ʈ�̴�.
	public void cancelRequests(Context context) {
		client.cancelRequests(context, false);
	}

	// �������̽��� �����Ǿ��ִ�. ������� T���·� �޾ƿ���, ���и� �޾ƿ´�.
	public interface OnNetworkResultListener<String> {
		public void onResult(String result);
		public void onFail(int code);
	}

	// �����͸� ������ �Լ�(�α��ν� ���.)
	// ������ ���뼺�� Ȯ���Ϸ��� �迭�� ����ؼ� �޴� ����� ���� �غ���.
	// �迭���� ���������Ͱ� ���������� �ݺ������� �����͸� params�� put�� ���� ������ �ִ� ������� �����Ѵ�.
	public void login(Context context, String id, String pw, String regId,
			final OnNetworkResultListener<LoginData> listener) {
		String url = SERVER_URL+LOGIN;
		
		RequestParams params = new RequestParams();
		
		params.add("id", id);
		params.add("pw", pw);
		params.add("regId",regId);
		
		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					// TODO Auto-generated method stub
					String content = new String(responseBody);
					Log.i("test", "" + content);
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("login");
					object = array.getJSONObject(0);
					LoginData login = new LoginData();
					login.ID = object.getString("ID");
					login.PW = object.getString("PW");
					login.PH = object.getString("PH");
					login.REGID = object.getString("REGID");
					
					//ConLogin result = gson.fromJson(content, ConLogin.class);
					listener.onResult(login);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				listener.onFail(statusCode);
				Log.d("fail", "���� ����" + statusCode + "\n" + headers + "\n"
						+ error);
			}
		});
	}

	// �����͸� �������� �Լ�
	// �� ���� ���뼺�� ���ϼ� �ִ� �Լ��� ¥�����Ѵ�.
	// ������ ����� �� �ֵ��� ���ǵ� Ŭ������ ���� �� �ֵ��� �Ѵ�.
	public void join(Context context, String id, String pw, String ph, String regId,
			final OnNetworkResultListener<ConLogin> listener) {
		String url = SERVER_URL + JOIN;
		
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("pw", pw);
		params.put("ph", ph);
		params.put("regId", regId);

		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Log.d("test", content);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(content, ConLogin.class);
				listener.onResult(result);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}
	//��ȭ��ȣ �����ϴ� �޼ҵ�
	public void pn_modification(Context context, String uid, String pn,
			final OnNetworkResultListener<ConLogin> onNetworkResultListener) {
		String url = SERVER_URL + ONEUSERMODIFICATION;
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("pn", pn);

		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Log.d("test", content);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(content, ConLogin.class);
				onNetworkResultListener.onResult(result);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	// �����Ϸ��� å ������ ������ �޼ҵ�
	public void buy_senddata(Context context, RequestParams params,
			final OnNetworkResultListener<ConLogin> onNetworkResultListener) {
		String url = SERVER_URL + BUYREG;

		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Gson gson = new Gson();
				ConLogin login = gson.fromJson(content, ConLogin.class);
				onNetworkResultListener.onResult(login);
				Log.i("Sever", "Img insert to Success");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				Log.i("Server", "Img insert to fail");
			}

		});
	}

	// �Ǹ������� �����ϴ� �޼ҵ�
	public void sell_senddata(Context context, RequestParams params,
			final OnNetworkResultListener<ConLogin> listener) {
		String url = SERVER_URL + SELLREG;

		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Log.d("tt", content);
				Gson gson = new Gson();
				// gson���� ���� IsSuccess Ŭ������ ������ Ŭ�����̴�.
				// ������ Ŭ������ �����ͺ��̽��� ������� ���� ���ƾ� �ϸ�
				// �׷������� �ùٸ� �޼����� ���޵ȴ�.
				ConLogin result = gson.fromJson(content, ConLogin.class);
				listener.onResult(result);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	// �˴ϴ� �������� ������ �޾ƿ��� �κ�
	// ������� �ѷ��ִ� �����͸� �����´�.
	// key ���� id, book_name, purchase_picture 3������ ���еȴ�.
	public void sell_listdata(
			Context context,
			final OnNetworkResultListener<ArrayList<SellData>> onNetworkResultListener) {
		String url = SERVER_URL + SELLLIST;

		client.get(context, url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("sell");

					ArrayList<SellData> saledata = new ArrayList<SellData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							SellData s = new SellData();
							s.setIndx(object.getString("indx"));
							s.setId(object.getString("id"));
							s.setBook_name(object.getString("book_name"));
							s.setHope_price(object.getString("hope_price"));
							s.setOrigin_price(object.getString("origin_price"));
							s.setState(object.getString("state"));
							s.setSell_picture(object.getString("sell_picture"));
							s.setSell_state(object.getString("sell_state"));
							saledata.add(s);
						}

					}
					onNetworkResultListener.onResult(saledata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	// ��ϴ� �������� ������ �޾ƿ��� �κ�
	// ������� �ѷ��ִ� �����͸� �����´�.
	// key ���� id, book_name, purchase_picture 3������ ���еȴ�.
	public void purchase_listdata(
			Context context,
			final OnNetworkResultListener<ArrayList<PurchaseData>> onNetworkResultListener) {
		String url = SERVER_URL + BUYLIST;

		client.get(context, url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("purchase");

					ArrayList<PurchaseData> saledata = new ArrayList<PurchaseData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							PurchaseData s = new PurchaseData();
							s.setIndx(object.getString("indx"));
							s.setId(object.getString("id"));
							s.setBook_name(object.getString("book_name"));
							s.setImage_url(object.getString("purchase_picture"));
							s.setPurchase_state(object.getString("purchase_state"));
							saledata.add(s);
						}

					}
					Log.e("data","success");
					onNetworkResultListener.onResult(saledata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void one_sell_listdata(
			Context context,
			String id,
			final OnNetworkResultListener<ArrayList<SellData>> onNetworkResultListener) {
		String url = SERVER_URL + USERSELLIST;

		RequestParams params = new RequestParams();
		params.add(SplashActivity.ID, id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("sell");

					ArrayList<SellData> saledata = new ArrayList<SellData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							SellData s = new SellData();
							s.setIndx(String.valueOf(object.getInt("INDX")));
							s.setId(object.getString("ID"));
							s.setBook_name(object.getString("BOOK_NAME"));
							s.setSell_picture(object.getString("SELL_PICTURE"));
							s.setState(object.getString("STATE"));
							s.setHope_price(object.getString("HOPE_PRICE"));
							s.setOrigin_price(object.getString("ORIGIN_PRICE"));
							s.setSell_state(object.getString("SELL_STATE"));

							saledata.add(s);
						}

					}
					onNetworkResultListener.onResult(saledata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void one_buy_listdata(Context context,	String id,
			final OnNetworkResultListener<ArrayList<PurchaseData>> onNetworkResultListener) {
		String url = SERVER_URL + USERBUYLIST ;
		RequestParams parmas = new RequestParams();
		parmas.add("id", id);
		client.post(context, url, parmas, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Log.e("JsonDataCheck", content);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("purchase");

					ArrayList<PurchaseData> saledata = new ArrayList<PurchaseData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							PurchaseData s = new PurchaseData();
							s.setIndx(String.valueOf(object.getInt("INDX")));
							s.setId(object.getString("ID"));
							s.setBook_name(object.getString("BOOK_NAME"));
							s.setImage_url(object.getString("PURCHASE_PICTURE"));
							s.setPurchase_state(object.getString("PURCHASE_STATE"));
							saledata.add(s);
						}

					}
					onNetworkResultListener.onResult(saledata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void one_buy_delete(Context context,String id,String pw,String indx, final OnNetworkResultListener<ConLogin> onNetworkResultListener) {
		String url = SERVER_URL + USERBUYDELETE;
		RequestParams params = new RequestParams();
		params.add("indx",indx);
		params.add("id",id);
		params.add("pw",pw);
		
		client.post(context,url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Log.e("tt", str);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkResultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void one_sell_delete(Context context,String id,String pw,String indx, final OnNetworkResultListener<ConLogin> onNetworkReultListener){
		String url=SERVER_URL+USERSELLDELETE;
		RequestParams params = new RequestParams();
		params.add("indx", indx);
		params.add("id", id);
		params.add("pw", pw);
		
		client.post(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkReultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void one_buy_modify(Context context,String id,String pw,String indx,String bookName,File file, final OnNetworkResultListener<ConLogin> onNetworkResultListener) throws FileNotFoundException {
		String url = SERVER_URL + USERPURCHASEMODIFIY;
		RequestParams params = new RequestParams();
		params.add("indx",indx);
		params.add("id",id);
		params.add("book_name", bookName);
		params.put("purchase_pictrue", file);
		params.add("pw",pw);
		
		client.post(context,url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Log.e("tt", str);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkResultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
		});
	}
	
	public void one_sell_modify(Context context,String id,String pw,String indx,String bookName,String hopePrice,String originPrice,String state,File file, final OnNetworkResultListener<ConLogin> onNetworkReultListener) throws FileNotFoundException{
		String url=SERVER_URL+USERSELLMODIFIY;
		RequestParams params = new RequestParams();
		params.add("indx", indx);
		params.add("id", id);
		params.add("pw", pw);
		params.add("book_name", bookName);
		params.add("hope_price", hopePrice);
		params.add("origin_price", originPrice);
		params.add("state", state);
		
		params.put("sell_picture", file);
		client.post(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkReultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
	public void one_buy_modify(Context context,String id,String pw,String indx,String bookName, final OnNetworkResultListener<ConLogin> onNetworkResultListener) throws FileNotFoundException {
		String url = SERVER_URL + USERPURCHASEBOOKMODIFIY;
		RequestParams params = new RequestParams();
		params.add("indx",indx);
		params.add("id",id);
		params.add("book_name", bookName);
		params.add("pw",pw);
		
		client.post(context,url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Log.e("tt", str);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkResultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
		});
	}
	
	public void one_sell_modify(Context context,String id,String pw,String indx,String bookName,String hopePrice,String originPrice,String state,final OnNetworkResultListener<ConLogin> onNetworkReultListener) throws FileNotFoundException{
		String url=SERVER_URL+USERSELLBOOKMODIFIY;
		RequestParams params = new RequestParams();
		params.add("indx", indx);
		params.add("id", id);
		params.add("pw", pw);
		params.add("book_name", bookName);
		params.add("hope_price", hopePrice);
		params.add("origin_price", originPrice);
		params.add("state", state);
		client.post(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkReultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void one_buy_data(Context context,String indx, final OnNetworkResultListener<ArrayList<PurchaseData>> onNetworkResultListener) {
		String url = SERVER_URL + USERBUYDATA;
		RequestParams params = new RequestParams();
		params.add("indx",indx);
		
		client.post(context,url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("purchase");

					ArrayList<PurchaseData> buydata = new ArrayList<PurchaseData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							PurchaseData s = new PurchaseData();
							s.setId(object.getString("ID"));
							s.setIndx(String.valueOf(object.getInt("INDX")));
							s.setBook_name(object.getString("BOOK_NAME"));
							s.setImage_url(object.getString("PURCHASE_PICTURE"));
							buydata.add(s);
						}

					}
					onNetworkResultListener.onResult(buydata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void one_sell_data(Context context,String indx,final OnNetworkResultListener<ArrayList<SellData>> onNetworkReultListener){
		String url=SERVER_URL+USERSELLDATA;
		RequestParams params = new RequestParams();
		params.add("indx", indx);

		client.post(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("sell");

					ArrayList<SellData> buydata = new ArrayList<SellData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							SellData s = new SellData();
							s.setIndx(String.valueOf(object.getInt("INDX")));
							s.setId(String.valueOf(object.getInt("ID")));
							s.setBook_name(object.getString("BOOK_NAME"));
							s.setHope_price(object.getString("HOPE_PRICE"));
							s.setOrigin_price(object.getString("ORIGIN_PRICE"));
							s.setSell_picture(object.getString("SELL_PICTURE"));
							s.setState(object.getString("STATE"));
							buydata.add(s);
						}
					}
					onNetworkReultListener.onResult(buydata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void one_user_data(Context context,	String id,
			final OnNetworkResultListener<ArrayList<LoginData>> onNetworkResultListener) {
		String url = SERVER_URL + ONEUSERDATA ;
		RequestParams parmas = new RequestParams();
		parmas.add("id", id);
		client.post(context, url, parmas, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				String content = new String(responseBody);
				Log.e("JsonDataCheck", content);
				try {
					JSONObject object = new JSONObject(content);
					JSONArray array = object.getJSONArray("user");

					ArrayList<LoginData> saledata = new ArrayList<LoginData>();
					for (int i = 0; i < array.length(); i++) {
						object = array.getJSONObject(i);
						if (object != null) {
							LoginData s = new LoginData();
							s.setPh(object.getString("ph"));
							saledata.add(s);
						}

					}
					onNetworkResultListener.onResult(saledata);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	public void updatePurchaseState(Context context,String id,String indx,String state, final OnNetworkResultListener<ConLogin> onNetworkReultListener){
		String url=SERVER_URL+UPDATEPURCHASESTATE;
		RequestParams params = new RequestParams();
		params.add("indx", indx);
		params.add("id", id);
		params.add("state", state);
		
		client.post(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkReultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void updateSellState(Context context,String id,String indx,String state, final OnNetworkResultListener<ConLogin> onNetworkReultListener){
		String url=SERVER_URL+UPDATESELLSTATE;
		RequestParams params = new RequestParams();
		params.add("indx", indx);
		params.add("id", id);
		params.add("state", state);
		client.post(context, url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				String str = new String(responseBody);
				Gson gson = new Gson();
				ConLogin result = gson.fromJson(str, ConLogin.class);
				onNetworkReultListener.onResult(result);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}
		});
	}
}
