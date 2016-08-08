package sm.bookingtest;

import sm.bookingtest.data.LoginData;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import sm.bookingtest.util.SplashActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener
{
	Button btn_guest, btn_signup, btn_login;
	EditText identification_edittx, password_edittx;
	String id="", pw="",ph="",regId="";
	
	SharedPreferences idpw;
	SharedPreferences.Editor editor;
	//GCM 구현시 registrationIds 의 값을 SharedPrefrence 에 넣어줘야한다.
	// 손님 로그인에 대한 권한 설정이 필요함
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		idpw = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);		
		editor = idpw.edit();
		
		btn_guest = (Button) findViewById(R.id.btn_guest);
		identification_edittx = (EditText) findViewById(R.id.edit_idfication);
		password_edittx = (EditText) findViewById(R.id.edit_password);
		btn_signup = (Button) findViewById(R.id.btn_signup);
		btn_login = (Button) findViewById(R.id.btn_login);

		// 가입하는 페이지로 이동하는 리스너
		btn_signup.setOnClickListener(this);
		// 손님입장 텍스트 뷰를 눌렀을때		
		btn_guest.setOnClickListener(this);
		// 아이디,비밀번호를 입력하고 눌렀을때
		btn_login.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_signup){
			// 가입하는 페이지로 이동하는 리스너
			Intent next = new Intent(Login.this, Join.class);
			startActivity(next);
		}
		else if(v.getId() == R.id.btn_guest){
			// 손님입장 텍스트 뷰를 눌렀을때	
			editor.putString(SplashActivity.ID, "아이디");
			editor.putString(SplashActivity.PW, "비밀번호");
			editor.putString(SplashActivity.PH, "전화번호");
			editor.putString(SplashActivity.AUTOLOGIN,"false");
			editor.commit();
			Intent next = new Intent(Login.this, MainActivity.class);
			startActivity(next);
			finish();//손님입장시 바로 메인리스트로 넘어감. 게스트에 관한 권한설정 필요.
		}else if(v.getId() == R.id.btn_login){
			// 아이디,비밀번호를 입력하고 눌렀을때
			id = identification_edittx.getText().toString();
			pw = password_edittx.getText().toString();
			regId = idpw.getString(SplashActivity.REGID, "fail");

			Connecting.getInstance().login(Login.this, id, pw, regId,
					new OnNetworkResultListener<LoginData>() {
						@Override
						public void onResult(LoginData result) {
							if (result != null) 
							{
								Intent next = new Intent(Login.this,MainActivity.class);
								next.putExtra("guest", true);//로그인
								editor.putString(SplashActivity.ID, result.ID);
								editor.putString(SplashActivity.PW, result.PW);
								editor.putString(SplashActivity.PH, result.PH);
								//registrationIds값을 넣어줘야한다.
								//GCM도입시 여기에 넣는다.
								editor.putString(SplashActivity.AUTOLOGIN, "true");
								editor.commit();

								startActivity(next);
								finish();
								Toast.makeText(Login.this,"로그인 되셨습니다.", 5000).show();
							} 	else 	{
								Toast.makeText(Login.this,	"로그인 실패.", 5000).show();
								editor.putString("autoLogin", "false");
								editor.commit();
							}
						}
						@Override
						public void onFail(int code) {
							Toast.makeText(Login.this, "로그인 실패하셨습니다.\n다시 시도하세요.", 5000).show();
							editor.putString("autoLogin", "false");
							editor.commit();
						}
					});
		}
	}
}
