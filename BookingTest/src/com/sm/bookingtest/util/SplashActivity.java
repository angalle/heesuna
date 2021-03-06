package com.sm.bookingtest.util;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sm.bookingtest.Login;
import com.sm.bookingtest.MainActivity;
import com.sm.bookingtest.R;

public class SplashActivity extends Activity
{
	//스플레쉬 기간 정의
	private final int SPLASH_DISPLAY_LENGTH = 2000;
	
	//SharedPreference 데이터 영역
	private String autoLoginState = "false";
	public static final String USERINFO = "pref";
	public static final String AUTOLOGIN = "autologin";
	public static final String REGID = "regid";
	public static final String ID = "id";
	public static final String PW = "pw";
	public static final String PH = "ph";
	public static final String COMPLETE = "3";
	public static final String PURST = "2";
	public static final String SELLST = "1";
	public static final String NORST = "0";
	
	
	SharedPreferences idpn;
	
	//여긴 뭐지 ?
	public static final String EXTRA_MESSAGE = "message";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	//gcm관련 변수
	String SENDER_ID = "484409779151";
	static final String TAG = "GCM Demo";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	String regid;
	
	
	/* Called when the activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		// 쉐어드 프리퍼런스로 구현...
		idpn = getSharedPreferences(USERINFO, MODE_PRIVATE);
		autoLoginState = idpn.getString(AUTOLOGIN, "false");
		isNetWork();
		Log.e("TEST:::", autoLoginState);
//		일단 GCM 키값을 받아온다.
		context = getApplicationContext();
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
			regid = getRegistrationId(context);
			// registration id send server !
			if (regid.isEmpty()) {
				registerInBackground();
			}
			sendRegistrationIdToBackend();
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
//		그 후에 오토로그인에 대한 세션을 검사 한다.
		if (autoLoginState.equals("true"))
		{
			//메인 액티비티로 넘어간다. 데이터는 sharedpreference에 모두 저장 되어있다.
			pageIntent(MainActivity.class);
		} 
		else 
		{
			//기본 디폴트는 false값이다. String 형태이기때문에 잘 이해하길.
			//로그인 액티비티로 넘어간다. 데이터는 sharedpreference에 모두 저장 되어있다.
			pageIntent(Login.class);
		}
	}
	
	
	
	void pageIntent(final Class page){
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Intent mainIntent = new Intent(SplashActivity.this, page);
				startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkPlayServices();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void storeRegistrationId(Context context, String regId) {
//		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = idpn.edit();
		editor.putString(USERINFO, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
//		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = idpn.getString(USERINFO, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = idpn.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					Log.e("Reg ID", regid);
					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();
					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.
					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Errors :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.e("msg!",msg + "\n");
			}
		}.execute(null, null, null);
	}
	
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
		//regid로 데이터 들어오는 것 확인.
		SharedPreferences.Editor loginData = idpn.edit();
		loginData.putString(REGID, regid);
		loginData.commit();
	}
	
	
	private void isNetWork(){
		ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G나 LTE등 데이터 네트워크에 연결된 상태
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 와이파이에 연결된 상태
		 
		if (wifi.isConnected()) { // 와이파이에 연결된 경우
		// ...
		} else if (mobile.isConnected()) { // 데이터 네트워크에 연결된 경우
		// ...
		} else { // 인터넷에 연결되지 않은 경우
			autoLoginState = "false";
			Toast.makeText(getApplicationContext(), "인터넷 연결을 확인하세요.", Toast.LENGTH_LONG).show();
		}
	}
}
