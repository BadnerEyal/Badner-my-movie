package com.example.badner.my.movies;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


//                          ����� ����������� .���� ����� ������� .������� ������
public class DownloadImageTask1 extends AsyncTask<String, Integer,Bitmap>{
	
		
		private Activity mActivity;//����� ����� ��������� ��  �� ����� ��
		private ProgressDialog mDialog;//����� ���� ��������
		
		//���� ������ �� ��� �� ����� ���� �������
		DownloadImageTask1(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}
		
		//���� ����� ����� ����� ����� ���������
		protected void onPreExecute() {
			
			ImageView imageView = (ImageView) mActivity.findViewById(R.id.imageView);
			imageView.setImageBitmap(null);//����� ����� ����
			// Reset the progress bar
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setProgress(0);
			TextView errorMsg = (TextView) mActivity.findViewById(R.id.errorMsg);
			errorMsg.setVisibility(View.INVISIBLE);
		}
		
		
		
		//����� ����� ����� ����� �������� ����� �� ����� �������� ������ ��
		protected Bitmap doInBackground(String... urls) {
			Log.d("doInBackground", "starting download of image");
			//����� �������� ����� ����� ������ ���� ��� ����� ��� ����� ���
			Bitmap image = downloadImage(urls[0]);
			return image;
		}
		
		
		//���� ����� ������ ����� �� ���� ��������
		protected void onProgressUpdate(Integer... progress) {
			mDialog.show();
			mDialog.setProgress(progress[0]);
		}
		
		//���� ���� ����� ����� ����� �� ������� ������ ������� �����
		protected void onPostExecute(Bitmap result) {
			//���� ������ �� return bitmap;
			if (result != null) {
				ImageView imageView = (ImageView)mActivity.findViewById(R.id.imageView);
				imageView.setImageBitmap(result);
			}
			else {
				TextView errorMsg = (TextView)mActivity.findViewById(R.id.errorMsg);
				errorMsg.setVisibility(View.VISIBLE);//����� ����� ����� �����
				errorMsg.setText("Problem downloading image. Try again later");
			}
			// Close the progress dialog
				mDialog.dismiss();
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
				
				//���� ����� �� ������
				int fileLength = httpCon.getContentLength();
				
				//���� �� ��� ����� ���� �� ������ �����
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;//����� ��� ��� ���� ���� �� ������ ������
				int totalBytesRead = 0;//��� ����� �� �����
				byte[] data = new byte[2048];//��� ����� �� ���
				
				//������ ����� ������� �� ��������
				mDialog.setMax(fileLength);
				// Read the image bytes in chunks of 2048 bytes
				
				// ����� ������ ������     
				//���� ����� ����� ���� �� ����� -1 ��� ���� �� �� ����
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);//����� ���
					
					//����� �������
					totalBytesRead += nRead;//����� ���� ������
					
		//����� ���� ����� �� ����� ����� �� ���� ���� ���� ��� ������ �� �������� ��� ��� �������
					publishProgress(totalBytesRead);//����� ���� ��������
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