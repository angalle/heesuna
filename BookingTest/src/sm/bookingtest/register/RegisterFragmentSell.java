package sm.bookingtest.register;

import java.io.File;
import java.io.FileNotFoundException;

import sm.bookingtest.R;
import sm.bookingtest.data.ConLogin;
import sm.bookingtest.mypagesell.MyPageSellListFragment;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class RegisterFragmentSell extends ActionBarActivity implements OnClickListener{

	Connecting client;
	EditText hope_price, title;
	TextView orginal_price;
	Spinner state;
	CheckBox give;
	File mSavedFile;
	ImageLoader imageLoader;
	DisplayImageOptions defaultOptions;
	ImageView img;
	public static final String TAG = "outputFormat";
	public static final int REQUEST_CODE_CROP = 0; // 이게 무슨뜻?
	public static final int REQ_CODE_PICK_IMAGE = 0;
	private static final String TEMP_PHOTO_FILE = "temp.jpg"; // 임시 저장파일
	ArrayAdapter<CharSequence> adapter;

	Button sellregi_btn;
	int intentFlag = 0;
	String indx ="",id="",bookName="", hopePrice="", originePrice="", stateStr="",imgUrl="";
	RequestParams params = new RequestParams();
	RequestController rc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register_sell);
		initUI(); 
		
		Intent next = getIntent();
		intentFlag = next.getFlags();
		if(intentFlag == Intent.FLAG_ACTIVITY_NEW_TASK){
			indx = next.getStringExtra("indx");
			id = next.getStringExtra("id");
			bookName = next.getStringExtra("bookNm");
			hopePrice = next.getStringExtra("hopePrice");
			originePrice = next.getStringExtra("originPrice");
			stateStr = next.getStringExtra("state");
			imgUrl = next.getStringExtra("imgUrl");
			
			title.setText(bookName);
			hope_price.setText(hopePrice);
			orginal_price.setText(originePrice);
			stateSetData(stateStr); //state value setting function
			
			defaultOptions = new DisplayImageOptions
									.Builder()
									.cacheOnDisc(true).cacheInMemory(true).resetViewBeforeLoading(true)
									.showImageForEmptyUri(R.drawable.beehive)
									.showImageOnFail(R.drawable.beehive)
									.displayer(new FadeInBitmapDisplayer(300)).build();
	
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
													.defaultDisplayImageOptions(defaultOptions)
													.memoryCache(new WeakMemoryCache())
													.discCacheSize(100 * 1024 * 1024).build();
			
			ImageLoader.getInstance().init(config);
			imageLoader = ImageLoader.getInstance();
			
			imageLoader.displayImage(imgUrl, img, defaultOptions);
		}
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
		
		//체크박스에 intent에서 값을 판단해서 표시하도록 하기.
		//give로 가져와야 함.
		
		img.setOnClickListener(this);
		sellregi_btn.setOnClickListener(this);
		give.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					hope_price.setText("0");
					hope_price.setHint("기 부 하 기");
					hope_price.setEnabled(false);
					originePrice = "0";
				} else {
					hope_price.setHint("희 망 가 격");
					hope_price.setEnabled(true);
				}
			}
		});
		rc = new RequestController(getApplicationContext(), MyPageSellListFragment.sellAdapter, MyPageSellListFragment.lv_selllist);
	}

	private void stateSetData(String stateData) {
		int level =0;
		     if(stateData.equals("A급")){level =0;}
		else if(stateData.equals("B급")){level =1;}
		else if(stateData.equals("C급")){level =2;}
		else if(stateData.equals("D급")){level =3;}
		state.setSelection(level);
	}
	
	private Uri getTempUri() {
		mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis() / 1000 + ".jpg");
		return Uri.fromFile(mSavedFile);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
			Bitmap bitmap = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
			img.setImageBitmap(bitmap);
		}
	}

	private void sendData(String book_name, String state, String hope_price, String origin_price) {
		final String upfile = mSavedFile.getAbsolutePath();
		
		SharedPreferences idpn = getSharedPreferences(SplashActivity.USERINFO, MODE_PRIVATE);
		String id = "";
		id = idpn.getString(SplashActivity.ID, "");

		try {
			params.put("sell_picture", new File(upfile));
			params.put("id", id);
			params.put("book_name", book_name);
			params.put("state", state);
			params.put("hope_price", hope_price);
			params.put("origin_price", origin_price);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Connecting.getInstance().sell_senddata(RegisterFragmentSell.this,
				params, new OnNetworkResultListener<ConLogin>() {
					@Override
					public void onResult(ConLogin result) {
						if (result.getResult().equals("success")) { 
							Toast.makeText(RegisterFragmentSell.this,"도서 등록 되었습니다.", 5000).show();
							Intent next = new Intent();
							next.putExtra("refresh", "sell");
							setResult(RESULT_OK, next);
							finish();
						} else {
							Toast.makeText(RegisterFragmentSell.this,"도서 등록 실패하였습니다.", 5000).show();
							sellregi_btn.setClickable(true);
						}
					}
					@Override
					public void onFail(int code) {
						// TODO Auto-generated method stub
						Toast.makeText(RegisterFragmentSell.this,
								"네트워크 전송상태 이상", 5000).show();
					}
				});
	}

	public void initUI() {
		title = (EditText) findViewById(R.id.register_sell_title);
		hope_price = (EditText) findViewById(R.id.register_sell_hopeprice);
		orginal_price = (TextView) findViewById(R.id.register_sell_original_price);
		state = (Spinner) findViewById(R.id.register_sell_state);
		give = (CheckBox) findViewById(R.id.register_sell_check_give);

		sellregi_btn = (Button) findViewById(R.id.register_sell_regi_btn);
		img = (ImageView) findViewById(R.id.register_sell_img);
		// 팝니다 버튼을 누를시 서버로 전송되어야함. 그리고 View를 없앤다.
		adapter = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_list_item_checked);
		state.setAdapter(adapter);
	}

	@TargetApi(23)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		bookName = title.getText().toString();
		hopePrice = hope_price.getText().toString();
		originePrice = orginal_price.getText().toString();
		stateStr = state.getSelectedItem().toString();
		if(intentFlag == 0){
			if(v.getId() == R.id.register_sell_regi_btn){
				try{
					if ((mSavedFile != null) && !bookName.equals("")	&& !hopePrice.equals("") &&
							!stateStr.equals("") && !originePrice.equals("")){
						sendData(bookName, stateStr, hopePrice, originePrice);
						sellregi_btn.setClickable(false);
					}
					else 
						Toast.makeText(getApplicationContext(), "빠짐없이 입력해주세요.", Toast.LENGTH_SHORT)	.show();
				}catch(NullPointerException e){
					Toast.makeText(getApplicationContext(), "사진을 넣어주세요.", Toast.LENGTH_SHORT).show();
				}
			}else if (v.getId() == R.id.register_sell_img){
				if(Build.VERSION.SDK_INT >= 23){
					if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
				                0);
						Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						photoPickerIntent.setType("image/*");
						photoPickerIntent.putExtra("crop", "true");
						photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,	getTempUri());				
						photoPickerIntent.putExtra(TAG,	Bitmap.CompressFormat.JPEG.toString());
						startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
					}
				}else{
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					photoPickerIntent.setType("image/*");
					photoPickerIntent.putExtra("crop", "true");
					photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,	getTempUri());				
					photoPickerIntent.putExtra(TAG,	Bitmap.CompressFormat.JPEG.toString());
					startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
				}
			}
		} else if(intentFlag == Intent.FLAG_ACTIVITY_NEW_TASK){
			bookName = title.getText().toString();
			hopePrice= hope_price.getText().toString();
			originePrice = orginal_price.getText().toString();
			stateStr = state.getSelectedItem().toString();
			
			AlertDialog.Builder dial = new AlertDialog.Builder(RegisterFragmentSell.this);
			dial.setTitle("수정");
			LayoutInflater inflater = (LayoutInflater)RegisterFragmentSell.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.delete_dialog,	null);
			final EditText pwfield = (EditText) view.findViewById(R.id.password);
			dial.setView(view);
			dial.setTitle("비밀번호 확인");
			dial.setPositiveButton("수정", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String passwd = pwfield.getText().toString();
					File file = null;
					if ((mSavedFile != null) && (bookName != null)	&& (hopePrice != null) &&
							(stateStr != null) && (originePrice != null))
						{
							final String upfile = mSavedFile.getAbsolutePath();
							file = new File(upfile);
							rc.modifySellData(id, passwd, indx, bookName,stateStr,hopePrice,originePrice, file);
							sellregi_btn.setClickable(false);
						}
					else if ((mSavedFile == null)){
						rc.modifySellData(id, passwd, indx, bookName,hopePrice,originePrice,stateStr);
					}else{
						Toast.makeText(getApplicationContext(), "비밀번호가 틀렸거나, 수정내용이 없습니다.", Toast.LENGTH_SHORT).show();
					}
					dialog.cancel();
					finish();
				}
			});
			dial.show();
		}
	}
}
