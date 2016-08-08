package sm.bookingtest.register;

import java.io.File;
import java.io.FileNotFoundException;

import sm.bookingtest.R;
import sm.bookingtest.data.ConLogin;
import sm.bookingtest.mypagepurchase.MyPagePurchaseListFragment;
import sm.bookingtest.util.Connecting;
import sm.bookingtest.util.Connecting.OnNetworkResultListener;
import sm.bookingtest.util.RequestController;
import sm.bookingtest.util.SplashActivity;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class RegisterFragmentPurchase extends ActionBarActivity implements OnClickListener{
	Button purchaseregi_btn;

	File mSavedFile;
	Connecting client;
	EditText book_title;
	String title, picturl_url;
	ImageView img;
	public static final int REQUEST_CODE_CROP = 0; // �̰� ������?
	public static final int REQ_CODE_PICK_IMAGE = 0;
	public static final String TAG = "outputFormat";
	private static final String TEMP_PHOTO_FILE = "temp.jpg"; // �ӽ� ��������
	ArrayAdapter<CharSequence> adapter;
	boolean sucess = true;
	ImageLoader imageLoader = null;
	DisplayImageOptions defaultOptions;
	int intentFlag = 0;
	String indx="",id="",bookNm="",imgUrl="";
	RequestController rc;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register_purchase);

		book_title = (EditText) findViewById(R.id.purchase_edit_text);
		purchaseregi_btn = (Button) findViewById(R.id.register_purchaseregi_btn);
		img = (ImageView) findViewById(R.id.register_purchase_img);
		// ��ϴ� ��ư�� ������ ������ ���۵Ǿ����. �׸��� View�� ���ش�.
		Intent next = getIntent();
		intentFlag = next.getFlags();
		
		if(intentFlag == Intent.FLAG_ACTIVITY_NEW_TASK){ // ���� flag
			indx = next.getStringExtra("indx");
			id = next.getStringExtra("id");
			bookNm = next.getStringExtra("bookNm");
			imgUrl = next.getStringExtra("imgUrl");
			book_title.setText(bookNm);
			
			defaultOptions = new DisplayImageOptions.Builder()
			.cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
			.showImageForEmptyUri(R.drawable.beehive)
			.showImageOnFail(R.drawable.beehive)
			//.imageScaleType(ImageScaleType.EXACTLY) xml���� ������.
			.displayer(new FadeInBitmapDisplayer(300)).build();
	
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
					.memoryCache(new WeakMemoryCache())
					.discCacheSize(100 * 1024 * 1024).build();
			
			ImageLoader.getInstance().init(config);
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(imgUrl, img, defaultOptions);
		}
		
		img.setOnClickListener(this);
		purchaseregi_btn.setOnClickListener(this);
		rc = new RequestController(getApplicationContext(), MyPagePurchaseListFragment.purchaseAdapter, MyPagePurchaseListFragment.lv_purchaselist);
	}
	private Uri getTempUri() {
		// TODO Auto-generated method stub
		mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000+".jpg");
		return Uri.fromFile(mSavedFile);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CROP
				&& resultCode == Activity.RESULT_OK) {
			sucess = true;		
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath(),option);
			img.setImageBitmap(bitmap);
		}
	}

	private void registerPurchaseImage(String id, String title) {
		final String upfile = mSavedFile.getAbsolutePath();
		if(mSavedFile == null)
			Toast.makeText(getApplicationContext(),"������ �����ϼ���",Toast.LENGTH_SHORT).show();
		RequestParams params = new RequestParams();
		try {
			params.add("id", id);
			params.add("book_name", title);
			params.put("purchase_picture", new File(upfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Connecting.getInstance().buy_senddata(getApplicationContext(), params, new OnNetworkResultListener<ConLogin>() {
					@Override
					public void onResult(ConLogin result) {
						if (result.getResult().equals("success")){
							Toast.makeText(getApplicationContext(), "��ϴ� ��� ����",5000).show();
							Intent next = getIntent();
							next.putExtra("refresh", "purchase");
							setResult(RESULT_OK,next);
							finish();
						}else{
							Toast.makeText(getApplicationContext(),	"��ϴ� ��� ����", 5000).show();
						}
					}
					@Override
					public void onFail(int code) {
						Toast.makeText(getApplicationContext(), "��Ʈ��ũ ���ۻ��� �̻�",5000).show();
					}
				});

	}

	@TargetApi(23)
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.register_purchaseregi_btn){
			if(intentFlag == 0){ // ��� flag
				title = book_title.getText().toString();
				SharedPreferences idpn = getSharedPreferences(SplashActivity.USERINFO,MODE_PRIVATE);
				String id = idpn.getString("id", "fail");
				try{
					if ((mSavedFile != null) && !title.equals("") ){
						registerPurchaseImage(id, title);
					}else{
						Toast.makeText(getApplicationContext(), "����/å ������ �Է��ϼ���", Toast.LENGTH_SHORT).show();	
					}
				}catch(NullPointerException e){
					Toast.makeText(getApplicationContext(), "������ �־��ּ���", Toast.LENGTH_SHORT).show();
				}
			}else if(intentFlag == Intent.FLAG_ACTIVITY_NEW_TASK){ //  ���� flag
				title = book_title.getText().toString();
				//update query apply
				AlertDialog.Builder dial = new AlertDialog.Builder(RegisterFragmentPurchase.this);
				dial.setTitle("����");
				LayoutInflater inflater = (LayoutInflater)RegisterFragmentPurchase.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.delete_dialog,	null);
				final EditText pwfield = (EditText) view.findViewById(R.id.password);
				dial.setView(view);
				dial.setTitle("��й�ȣ Ȯ��");
				dial.setPositiveButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String passwd = pwfield.getText().toString();
						File file = null;
						if(mSavedFile == null){
							rc.modifyPurchaseData(id, passwd, indx, title);
							purchaseregi_btn.setClickable(false);
							//�����Ҷ� ��ư ����.
						}else if(title.equals("")){
							Toast.makeText(getApplicationContext(), "��й�ȣ�� Ʋ�Ȱų�, ���������� �����ϴ�.", Toast.LENGTH_SHORT).show();
						}
						else{
							final String upfile = mSavedFile.getAbsolutePath();
							file = new File(upfile);
							rc.modifyPurchaseData(id, passwd, indx, title, file);
							purchaseregi_btn.setClickable(false);
						}
						dialog.cancel();
						finish();
					}
				});
				dial.show();
			}
		}
		else if(v.getId() == R.id.register_purchase_img){
			if(Build.VERSION.SDK_INT >= 23){
				if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
			            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
					requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
			                0);
					//���� �ڸ��� �� �⺻ �����ϴ� �޼ҵ�.
					setPictureInfo();
				}
			}else{
				//���� �ڸ��� �� �⺻ �����ϴ� �޼ҵ�.
				setPictureInfo();
			}
		}
	}
	
	private void setPictureInfo(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		photoPickerIntent.putExtra("crop", "true");
		photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,	getTempUri());				
		photoPickerIntent.putExtra(TAG,	Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
	}
	
}
