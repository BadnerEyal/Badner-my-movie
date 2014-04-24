package com.example.badner.my.movies;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

//����� ����� ������ ��� ���� �������
class MyTaskPic extends AsyncTask<String, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	   

	    public MyTaskPic(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage
	        // collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(String... urls) {
	    	Log.d("doInBackground", "starting download of image");
			//����� �������� ����� ����� ������ ���� ��� ����� ��� ����� ���
			Bitmap image = downloadImage(urls[0]);
			return image;
	    	
	 
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	       
	    	
	    	if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	    
	  //������� ����� ����� ����� ����� ����� ��� ������� �����
	  		private Bitmap downloadImage(String urlString) {
	  			URL url;
	  			try {
	  				//����� ���� ����� ����� �����
	  				//���� ��� �� ��������
	  				url = new URL(urlString);
	  				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	  				InputStream is = httpCon.getInputStream();
	  				
	  				
	  				
	  				//���� �� ��� ����� ���� �� ������ �����
	  				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	  				int nRead;//����� ��� ��� ���� ���� �� ������ ������
	  			
	  				byte[] data = new byte[2048];//��� ����� �� ���
	  				
	  				
	  				// Read the image bytes in chunks of 2048 bytes
	  				
	  				// ����� ������ ������     
	  				//���� ����� ����� ���� �� ����� -1 ��� ���� �� �� ����
	  				while ((nRead = is.read(data, 0, data.length)) != -1) {
	  					buffer.write(data, 0, nRead);//����� ���
	  					
	  				}
	  				//����� ���� �� �� ������ �������
	  				buffer.flush();
	  				//���� ��� ����� ����� �� ������
	  				byte[] image = buffer.toByteArray();
	  				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
	  				return bitmap;
	  			
	  			
	  			} catch (Exception e) {//����� �� ������
	  				e.printStackTrace();
	  			}
	  			return null;
	  		} 
	    
	}
