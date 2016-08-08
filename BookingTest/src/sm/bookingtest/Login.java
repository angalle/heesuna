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
	//GCM ������ registrationIds �� ���� SharedPrefrence �� �־�����Ѵ�.
	// �մ� �α��ο� ���� ���� ������ �ʿ���
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

		// �����ϴ� �������� �̵��ϴ� ������
		btn_signup.setOnClickListener(this);
		// �մ����� �ؽ�Ʈ �並 ��������		
		btn_guest.setOnClickListener(this);
		// ���̵�,��й�ȣ�� �Է��ϰ� ��������
		btn_login.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_signup){
			// �����ϴ� �������� �̵��ϴ� ������
			Intent next = new Intent(Login.this, Join.class);
			startActivity(next);
		}
		else if(v.getId() == R.id.btn_guest){
			// �մ����� �ؽ�Ʈ �並 ��������	
			editor.putString(SplashActivity.ID, "���̵�");
			editor.putString(SplashActivity.PW, "��й�ȣ");
			editor.putString(SplashActivity.PH, "��ȭ��ȣ");
			editor.putString(SplashActivity.AUTOLOGIN,"false");
			editor.commit();
			Intent next = new Intent(Login.this, MainActivity.class);
			startActivity(next);
			finish();//�մ������ �ٷ� ���θ���Ʈ�� �Ѿ. �Խ�Ʈ�� ���� ���Ѽ��� �ʿ�.
		}else if(v.getId() == R.id.btn_login){
			// ���̵�,��й�ȣ�� �Է��ϰ� ��������
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
								next.putExtra("guest", true);//�α���
								editor.putString(SplashActivity.ID, result.ID);
								editor.putString(SplashActivity.PW, result.PW);
								editor.putString(SplashActivity.PH, result.PH);
								//registrationIds���� �־�����Ѵ�.
								//GCM���Խ� ���⿡ �ִ´�.
								editor.putString(SplashActivity.AUTOLOGIN, "true");
								editor.commit();

								startActivity(next);
								finish();
								Toast.makeText(Login.this,"�α��� �Ǽ̽��ϴ�.", 5000).show();
							} 	else 	{
								Toast.makeText(Login.this,	"�α��� ����.", 5000).show();
								editor.putString("autoLogin", "false");
								editor.commit();
							}
						}
						@Override
						public void onFail(int code) {
							Toast.makeText(Login.this, "�α��� �����ϼ̽��ϴ�.\n�ٽ� �õ��ϼ���.", 5000).show();
							editor.putString("autoLogin", "false");
							editor.commit();
						}
					});
		}
	}
}
