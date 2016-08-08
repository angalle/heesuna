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
		if (status.equals("A��"))
			status_img.setImageResource(R.drawable.a);
		else if (status.equals("B��"))
			status_img.setImageResource(R.drawable.b);
		else if (status.equals("C��"))
			status_img.setImageResource(R.drawable.c);
		else if (status.equals("D��"))
			status_img.setImageResource(R.drawable.d);

		if(myPageSellData.getSell_state() == SplashActivity.SELLST){
			//�Ǹ����� �̹����� ����. �Ǹ����̸� ���⼭ �̹��� ����
			status_img.setImageResource(R.drawable.aplus);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.my_sale_modify){
				AlertDialog.Builder dial = new AlertDialog.Builder(context);
				dial.setTitle("����");
				//���̾�α� ���� �ФФФФ�
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.delete_dialog,	null);
				dial.setTitle("�����Ͻðڽ��ϱ� ?");
				dial.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int which) {
						//�������� ��ȸ�ϰ�
						//����Ʈ�� �����͸� �ѱ��.
						rc.selectSellData(indx);
						dialog.cancel();
					}
				});
				dial.show();
		}else if(v.getId() == R.id.my_sale_delete){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("����");
			//���̾�α� ���� �ФФФФ�
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog, null);
			final EditText pw = (EditText) view.findViewById(R.id.password);
			dial.setView(view);
			dial.setTitle("�����Ͻðڽ��ϱ�?");
			
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
					rc.deleteSellData(id, indx, passwd);
					dialog.cancel();
				}
			});
			dial.show();
		}else if(v.getId() == R.id.purchase_status_img){
			AlertDialog.Builder dial = new AlertDialog.Builder(context);
			dial.setTitle("�ǸſϷ�/�Ǹ��簳");
			//���̾�α� ���� �ФФФФ�
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			dial.setTitle("�ǸſϷ�/�Ǹ��簳 �Ͻðڽ��ϱ� ?");
			dial.setPositiveButton("�Ǹ��簳", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//�������� ��ȸ�ϰ�
					//����Ʈ�� �����͸� �ѱ��.
					rc.updateSellState(id, indx, SplashActivity.NORST, "�Ǹ��簳 ó�� �Ϸ�","�Ǹ��簳 ó�� ����");

					dialog.cancel();
				}
			});
			dial.setNegativeButton("�ǸſϷ�", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					//�������� ��ȸ�ϰ�
					//����Ʈ�� �����͸� �ѱ��.
					rc.updateSellState(id, indx, SplashActivity.COMPLETE, "�ǸſϷ� ó�� �Ϸ�","�ǸſϷ� ó�� ����");
					dialog.cancel();
				}
			});
			dial.show();
		}
	}
}
