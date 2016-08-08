package sm.bookingtest;

import sm.bookingtest.data.ConLogin;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import sm.bookingtest.util.SplashActivity;
import sm.bookingtest.util.TabPagerAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements TabListener {
	static public ViewPager viewPager;
	static public TabPagerAdapter mAdapter;
	private android.support.v7.app.ActionBar actionBar;
	static TabHost host;
	private String[] tabs = { "팝니다", "삽니다", "등록", "내 정보" };
	static public String id, pwd;
	String ph="";
	static public Context context = null;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		SharedPreferences share = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		String ids = share.getString("id", "아이디");
		context = this;
		if(!ids.equals("아이디"))
			inflater.inflate(R.menu.member_menu, menu);
		else
			inflater.inflate(R.menu.guest_menu, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.PH_Modification:
			AlertDialog.Builder dial = new AlertDialog.Builder(	MainActivity.this);
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();

			View v = inflater.inflate(R.layout.fragment_mypage_ph_modification,null);

			final EditText pn = (EditText) v.findViewById(R.id.fragment_mypage_ph_modification_edittext);

			dial.setView(v);
			dial.setTitle("전화번호 수정");
			dial.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			dial.setPositiveButton("수정", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ph = pn.getText().toString();
					Connecting.getInstance().pn_modification(
							getApplicationContext(), id, ph,
							new OnNetworkResultListener<ConLogin>() {
								@Override
								public void onResult(ConLogin result) {
									// TODO Auto-generated method stub
									if (result.getResult().equals("ok")) {
										// 선언
										SharedPreferences idpw = getSharedPreferences("pref", MODE_PRIVATE);
										SharedPreferences.Editor editor = idpw.edit();
										// 입력한 전화번호로 수정
										editor.putString("pn", ph);
										editor.commit();
										Toast.makeText(getApplicationContext(),"수정 완료", 5000).show();
									} else {
										Toast.makeText(getApplicationContext(),"로그인을 다시해주시기 바랍니다.", 5000).show();
									}
								}

								@Override
								public void onFail(int code) {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(),"인터넷 상태 또는 서버 불안정상태", 5000).show();
								}
							});
				}
			});

			dial.show();
			break;
		// action with ID action_settings was selected
			case R.id.Logout:
				// Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
				SharedPreferences next = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
				SharedPreferences.Editor editor = next.edit();
				editor.putString(SplashActivity.AUTOLOGIN, "false");
				editor.commit();
				Intent back1 = new Intent(this, Login.class);
				startActivity(back1);
				finish();
				break;
			case R.id.login:
				Intent back2 = new Intent(this, Login.class);
				startActivity(back2);
				finish();
				break;
		default:
			break;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		// Adding Tabs
		for (String tab_name : tabs) {
				actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.gray_mini_logo, getTheme())));
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
			
			
		});
		
		

	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
}
