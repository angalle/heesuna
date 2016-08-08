package com.sm.bookingtest;

import com.sm.bookingtest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PushActivity extends Activity {

	TextView tv;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		tv = (TextView)findViewById(R.id.text);
		btn = (Button)findViewById(R.id.button1);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PushActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
					
		
		//PushWakeLock.releaseCpuLock();
		//Ïπ¥Ïπ¥?ò§?Ü° ?ã§?ù¥?ñºÎ°úÍ∑∏Ï≤òÎüº Íµ¨ÌòÑ?ïòÍ∏? http://arabiannight.tistory.com/287
	}
}
