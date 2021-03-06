package com.sm.bookingtest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sm.bookingtest.R;

public class ImageCacheDownloader extends AsyncTask<String, Integer, Bitmap>{
	
	private Context context = null;
	private ImageView imageview = null;
	private String imageURL = null;
	static ImageLoader imageloader;
	private WeakReference<ImageView> imageViewReference;
	
	public ImageCacheDownloader(Context _context, ImageView _imageview,
			String _imageURL) {
		super();
		this.context = _context;
		imageview = _imageview;
		this.imageURL = _imageURL;
		imageViewReference = new WeakReference<ImageView>(imageview);
	}
	/*
	private String urlToFiileFullPath(String _url){
		return context.getCacheDir().getAbsolutePath() + _url.substring(_url.lastIndexOf("/"));
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		String fileFullPath = urlToFiileFullPath(imageURL);
		if(new File(fileFullPath).exists()){
			Bitmap myBitmap = BitmapFactory.decodeFile(fileFullPath);
			imageview.setImageBitmap(myBitmap);
		}
	}

	*/
	
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		/*
		String fileFullPath = urlToFiileFullPath(imageURL);
		String tempFilePath = fileFullPath+"_temp";
		
		writeFile(result,new File(tempFilePath));
		File downTempFile = new File(tempFilePath);
		File newFile = new File(fileFullPath);
		if(new File(fileFullPath).exists()){
			Bitmap prevBitmap = BitmapFactory.decodeFile(fileFullPath);
			Bitmap downBitmap = BitmapFactory.decodeFile(downTempFile.getAbsolutePath());
		
			if(sameAs(prevBitmap,downBitmap)){
				
			}else{
				imageview.setImageBitmap(result);
				writeFile(result,newFile);
			}
		
		}else{
			writeFile(result,newFile);
			imageview.setImageBitmap(result);
		}
		downTempFile.delete();
		*/
		
		if(isCancelled()){
			bitmap = null;
		}
		
		if(imageViewReference != null){
			ImageView imageView = imageViewReference.get();
			if(imageView != null){
				if(bitmap != null){
					imageview.setImageBitmap(bitmap);
				}
				else
				{
					imageView.setImageDrawable(imageView.getContext()
							.getResources().getDrawable(R.drawable.beehive));
				}
			}
		
		}
	}
/*
	private boolean sameAs(Bitmap prevBitmap, Bitmap downBitmap) {
		// TODO Auto-generated method stub
		ByteBuffer buffer1 = ByteBuffer.allocate(prevBitmap.getHeight() * downBitmap.getRowBytes());
		prevBitmap.copyPixelsToBuffer(buffer1);
		
		ByteBuffer buffer2 = ByteBuffer.allocate(downBitmap.getHeight() * downBitmap.getRowBytes());
		downBitmap.copyPixelsToBuffer(buffer2);
		
		return Arrays.equals(buffer1.array(), buffer2.array());
	}

	private void writeFile(Bitmap bmp, File f) {
		// TODO Auto-generated method stub
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 50,	out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if(out!=null)				
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
		}
		
	}
*/
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		return downloadBitmap(imageURL,imageview);
	}

	private Bitmap downloadBitmap(String imageURL2,ImageView imageview) {
		// TODO Auto-generated method stub
/*		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheOnDisc(true).cacheInMemory(true)
		.resetViewBeforeLoading(true)
		.delayBeforeLoading(100)
		.showImageForEmptyUri(R.drawable.beehive)
		.showImageOnFail(R.drawable.beehive)
		//.imageScaleType(ImageScaleType.EXACTLY)
		//.displayer(new FadeInBitmapDisplayer(300))
		.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
		context).defaultDisplayImageOptions(defaultOptions)
		.memoryCache(new WeakMemoryCache())
		.discCacheSize(100 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
		imageloader = ImageLoader.getInstance();
*/
		
	
		imageloader = ImageLoader.getInstance();
		//imageloader.displayImage(imageURL2,imageview,defaultOptions);
		return imageloader.loadImageSync(imageURL2);
	}
}
