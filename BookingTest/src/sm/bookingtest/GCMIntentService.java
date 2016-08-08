package sm.bookingtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	
	
	public GCMIntentService() {
		super("99270619986");
		Log.i("GCM passing", "GCM passed");
		// TODO Auto-generated constructor stub
	}	
	
	@Override
	protected void onError(Context context, String intent) {
		// TODO Auto-generated method stub
		 Log.i("onError Call", "onError Call");		 
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("getmessage","getmessage:");
	}

	@Override
	protected void onRegistered(Context context, String reg_id) {
		// TODO Auto-generated method stub
		Log.e("onRegistered call",reg_id);
	}

	@Override
	protected void onUnregistered(Context arg0, String reg_id) {
		// TODO Auto-generated method stub
		Log.e("onUnregistered call","---");
	}

}
