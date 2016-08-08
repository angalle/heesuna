package sm.bookingtest.mypagesell;

import sm.bookingtest.R;
import sm.bookingtest.data.SellData;
import sm.bookingtest.util.RequestController;
import sm.bookingtest.util.SplashActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MypageSellView extends FrameLayout implements OnClickListener
{
	Context context;
	String indx, id, passwd;
	ImageLoader imageLoader;
	ImageView iv_bookimage,status_img;
	TextView tv_title,tv_hopePrice,tv_originPrice,tv_state;
	ImageButton btn_sale_modify,btn_sale_delete;
	RequestController rc;
	public MypageSellView(Context context) 
	{
		super(context);
		this.context = MyPageSellListFragment.context;
		this.rc = new RequestController(context, MyPageSellListFragment.sellAdapter, MyPageSellListFragment.lv_selllist);
		init();
	}
	
	private void init() 
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
		ImageLoader.getInstance().init(config);
		
		LayoutInflater.from(getContext()).inflate(R.layout.my_list_sale, this);
		iv_bookimage = (ImageView)findViewById(R.id.iv_sale_bookimage);
		status_img = (ImageView)findViewById(R.id.purchase_status_img);
		tv_title = (TextView)findViewById(R.id.tv_sale_title);
		tv_hopePrice = (TextView)findViewById(R.id.tv_sale_price);
		tv_originPrice = (TextView)findViewById(R.id.tv_sale_cost);
		tv_state = (TextView)findViewById(R.id.tv_sale_condition);
		btn_sale_modify = (ImageButton)findViewById(R.id.my_sale_modify);
		btn_sale_delete = (ImageButton)findViewById(R.id.my_sale_delete);
		imageLoader = ImageLoader.getInstance();
	}

	public void setData(SellData myPageSellData)
	{
		imageLoader.displayImage(myPageSellData.getSell_picture(), iv_bookimage);
		tv_title.setText(myPageSellData.getBook_name());
		tv_hopePrice.setText(myPageSellData.getHope_price());
		tv_originPrice.setText(myPageSellData.getOrigin_price());
		tv_state.setText(myPageSellData.getState());
		indx = myPageSellData.getIndx();
		id = myPageSellData.getId();
		
		btn_sale_modify.setOnClickListener(this);
		btn_sale_delete.setOnClickListener(this);
		status_img.setOnClickListener(this);
		String status = myPageSellData.getState();
		if (status.equals("A厭"))
			status_img.setImageResource(R.drawable.a);
		else if (status.equals("B厭"))
			status_img.setImageResource(R.drawable.b);
		else if (status.equals("C厭"))
			status_img.setImageResource(R.drawable.c);
		else if (status.equals("D厭"))
			status_img.setImageResource(R.drawable.d);

		if(myPageSellData.getSell_state() == SplashActivity.SELLST){
			//毒古掻昔 戚耕走研 薦嬢. 毒古掻戚檎 食奄辞 戚耕走 薦嬢
			status_img.setImageResource(R.drawable.aplus);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.my_sale_modify){
				AlertDialog.Builder dial = new AlertDialog.Builder(context);
				dial.setTitle("呪舛");
				//陥戚杖稽益 神嫌 ばばばばば
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.delete_dialog,	null);
				dial.setTitle("呪舛馬獣畏柔艦猿 ?");
				dial.setPositiveButton("溌昔", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int which) {
						//汀軒庚聖 繕噺馬壱
						//昔度闘稽 汽戚斗研 角延陥.
						rc.selectSellData(indx);
						dialog.cancel();
					}
				});
				dial.show();
		}else if(v.getId() == R.id.my_sale_delete){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("肢薦");
			//陥戚杖稽益 神嫌 ばばばばば
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog, null);
			final EditText pw = (EditText) view.findViewById(R.id.password);
			dial.setView(view);
			dial.setTitle("肢薦馬獣畏柔艦猿?");
			
			dial.setNegativeButton("焼艦神", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			dial.setPositiveButton("肢薦", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					passwd = pw.getText().toString();
					rc.deleteSellData(id, indx, passwd);
					dialog.cancel();
				}
			});
			dial.show();
		}else if(v.getId() == R.id.purchase_status_img){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("毒古刃戟/毒古仙鯵");
			//陥戚杖稽益 神嫌 ばばばばば
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			dial.setTitle("毒古刃戟/毒古仙鯵 馬獣畏柔艦猿 ?");
			dial.setPositiveButton("毒古仙鯵", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//汀軒庚聖 繕噺馬壱
					//昔度闘稽 汽戚斗研 角延陥.
					rc.updateSellState(id, indx, SplashActivity.NORST, "毒古仙鯵 坦軒 刃戟","毒古仙鯵 坦軒 叔鳶");

					dialog.cancel();
				}
			});
			dial.setNegativeButton("毒古刃戟", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//汀軒庚聖 繕噺馬壱
					//昔度闘稽 汽戚斗研 角延陥.
					rc.updateSellState(id, indx, SplashActivity.COMPLETE, "毒古刃戟 坦軒 刃戟","毒古刃戟 坦軒 叔鳶");
					dialog.cancel();
				}
			});
			dial.show();
		}
	}
}
