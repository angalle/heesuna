package sm.bookingtest.mypagepurchase;

import sm.bookingtest.R;
import sm.bookingtest.data.PurchaseData;
import sm.bookingtest.util.RequestController;
import sm.bookingtest.util.SplashActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MypagePurchaseView extends FrameLayout implements View.OnClickListener {
	Context context;
	int index;
	String indx, id, passwd;
	RequestController rc;
	ImageLoader imageLoader;
	ImageView iv_bookimage,state_img;
	TextView tv_title;
	ImageButton btn_sale_modify, btn_sale_delete;
	
	
	public MypagePurchaseView(Context context) {
		super(context);
		this.context = MyPagePurchaseListFragment.context;
		this.rc = new RequestController(context, MyPagePurchaseListFragment.purchaseAdapter, MyPagePurchaseListFragment.lv_purchaselist);
		init();
	}

	private void init() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getContext()).build();
		ImageLoader.getInstance().init(config);

		LayoutInflater.from(getContext()).inflate(R.layout.my_list_purchase,
				this);
		iv_bookimage = (ImageView) findViewById(R.id.iv_purchase_bookimage);
		state_img = (ImageView)findViewById(R.id.state_img);
		tv_title = (TextView) findViewById(R.id.tv_purchase_title);
		btn_sale_modify = (ImageButton) findViewById(R.id.my_purchase_modify);
		btn_sale_delete = (ImageButton) findViewById(R.id.my_purchase_delete);
		tv_title.setTextColor(Color.BLACK);
		
		imageLoader = ImageLoader.getInstance();
	}

	public void setData(PurchaseData purchaseData) {
		imageLoader.displayImage(purchaseData.getImage_url(), iv_bookimage);
		tv_title.setText(purchaseData.getBook_name());
		indx = purchaseData.getIndx();
		id = purchaseData.getId();
		passwd = "";

		btn_sale_modify.setOnClickListener(this);
		btn_sale_delete.setOnClickListener(this);
		state_img.setOnClickListener(this);
		if(purchaseData.getPurchase_state() == SplashActivity.PURST){
			//�Ǹ����̸� ���⼭ ����
			state_img.setVisibility(VISIBLE);
		}else{
			state_img.setVisibility(GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.my_purchase_modify){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("����");
			//���̾�α� ���� �ФФФФ�
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			dial.setTitle("�����Ͻðڽ��ϱ� ?");
			dial.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//������ �����ͼ� ������������ ���� ��Ų��.
					rc.selectPurchaseData(indx);
					dialog.cancel();
				}
			});
			dial.show();
		}else if(v.getId() == R.id.my_purchase_delete){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("����");
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			final EditText pw = (EditText) view.findViewById(R.id.password);
			dial.setView(view);
			dial.setTitle("���� �����Ͻðڽ��ϱ�?");
			
			dial.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			dial.setPositiveButton("����", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					passwd = pw.getText().toString();
					//���� ��û
					rc.deletePurchaseData(id, indx, passwd);
					dialog.cancel();
				}
			});
			dial.show();
		}else if(v.getId() == R.id.state_img){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("���ԿϷ�/�����簳");
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			dial.setTitle("���ԿϷ�/�����簳 �Ͻðڽ��ϱ� ?");
			
			dial.setPositiveButton("�����簳", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					rc.updatePurchaseState(id,indx,SplashActivity.NORST,"�����簳 ó�� �Ϸ�","�����簳 ó�� ����");
					dialog.cancel();
				}
			});
			
			dial.setNegativeButton("���ԿϷ�", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					rc.updatePurchaseState(id,indx,SplashActivity.COMPLETE,"���ԿϷ� ó�� �Ϸ�","���ԿϷ� ó�� ����");
					dialog.cancel();
				}
			});
			dial.show();
		}
	}
}