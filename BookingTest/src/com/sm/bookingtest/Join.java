package com.sm.bookingtest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sm.bookingtest.R;
import com.sm.bookingtest.R.id;
import com.sm.bookingtest.R.layout;
import com.sm.bookingtest.data.ConLogin;
import com.sm.bookingtest.util.Connecting;
import com.sm.bookingtest.util.SplashActivity;
import com.sm.bookingtest.util.Connecting.OnNetworkResultListener;

public class Join extends Activity implements OnClickListener
{
	
	ImageView main_img;
	EditText id, passwd, passwd_check, ph;
	Button btn;
	String str_id, str_passwd, str_passwd_check, str_ph;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join);
		// networkInit();
		id = (EditText) findViewById(R.id.signup_id);
		passwd = (EditText) findViewById(R.id.signup_password);
		passwd_check = (EditText) findViewById(R.id.signup_password_checking);
		ph = (EditText) findViewById(R.id.signup_ph);
		btn = (Button) findViewById(R.id.signup_btn);
		btn.setOnClickListener(this);
		// 가입하기 버튼을 누를때.
	}
	boolean flag = false;
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.signup_btn){
			// 아이디 스트링값을 가져와서 서버와 커넥팅 하는 부분.
			SharedPreferences regId = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
			str_id = id.getText().toString();
			str_passwd = passwd.getText().toString();
			str_passwd_check = passwd_check.getText().toString();
			str_ph = ph.getText().toString();

			if(!flag){
				if( str_id.length()<5 && str_id.length()<20 ){
					Toast.makeText(getApplicationContext(), "아이디를 5자 이상, 20자 이하로 만들어주세요.", Toast.LENGTH_SHORT).show();
				}
				else if(!str_id.matches("[0-9|a-z|A-Z]*" )){
					Toast.makeText(getApplicationContext(), "아이디는 영문 대소문자, 숫자로만 가능합니다.", Toast.LENGTH_SHORT).show();
				}else{
					if(!str_passwd.equals(str_passwd_check)){
						Toast.makeText(getApplicationContext(), "확인 비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
					}else{
						Pattern p  = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
						Matcher m = p.matcher(str_passwd);
						if(!m.find()){
							Toast.makeText(getApplicationContext(), "비밀번호는 영문 대소문자, 숫자, 특수문자 조합으로만 가능합니다.", Toast.LENGTH_SHORT).show();
						}else if( str_passwd.length()<5 && str_passwd.length()<20 ){
							Toast.makeText(getApplicationContext(), "비밀번호를 5자 이상, 20자 이하로 만들어주세요.", Toast.LENGTH_SHORT).show();
						}else{
							if( !( str_ph.length() == 11) ){
								Toast.makeText(getApplicationContext(), "전화번호를 11자리로 입력해주세요.", Toast.LENGTH_SHORT).show();
							}else if(!str_ph.matches("[0-9]*" )){
								Toast.makeText(getApplicationContext(), "'-', 특수문자를 제외해주세요.", Toast.LENGTH_SHORT).show();
							}else{
								flag = true;
							}
						}
					}
				}
			}
			if(flag){
			String regid = regId.getString(SplashActivity.REGID, "");
			Log.d("commit::", regid+"123456");
			Connecting.getInstance().join(Join.this, str_id,str_passwd, str_ph,regid,
					new OnNetworkResultListener<ConLogin>() {
						@Override
						public void onResult(ConLogin result) {
							// TODO Auto-generated method stub
							if (result.getResult().equals("success")) {
								Toast.makeText(Join.this, "가입 되셨습니다.", 5000).show();
								Intent next = new Intent(Join.this, Login.class);
								startActivity(next);
								finish();
							} else {
								Toast.makeText(Join.this, result + "가입실패.", 5000).show();
							}
						}
						@Override
						public void onFail(int code) {
							// TODO Auto-generated method stub
							Toast.makeText(Join.this, "가입 실패하셨습니다.\n다시 시도하세요.", 5000).show();
						}
					});
			}
		}
	}
	
	public void dataErrChk(String inputData){
		Toast.makeText(getApplicationContext(), str_passwd + "비밀번호가 다릅니다." + str_passwd_check, 5000).show();
	}
}
