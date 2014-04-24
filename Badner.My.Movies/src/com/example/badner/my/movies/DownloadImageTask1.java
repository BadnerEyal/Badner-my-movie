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


//                          מחזיר ביטמפלתמונה .מספר לשעון התקדמות .ומחרוזת לכתובת
public class DownloadImageTask1 extends AsyncTask<String, Integer,Bitmap>{
	
		
		private Activity mActivity;//בשביל לפנות לאוביקטים של  מי שקורא לן
		private ProgressDialog mDialog;//בשביל שעון ההתקדמות
		
		//בנאי שמאתחל מי קרא לו ויוצר שעון התקדמות
		DownloadImageTask1(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}
		
		//לפני תחילת עבודת העובד קישור לאוביקטים
		protected void onPreExecute() {
			
			ImageView imageView = (ImageView) mActivity.findViewById(R.id.imageView);
			imageView.setImageBitmap(null);//ניקוי תמונה ישנה
			// Reset the progress bar
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setProgress(0);
			TextView errorMsg = (TextView) mActivity.findViewById(R.id.errorMsg);
			errorMsg.setVisibility(View.INVISIBLE);
		}
		
		
		
		//העובד מתחיל לעבוד קריאה לפונקציה הורדה עם כתובת האינטרנט ששלחנו לו
		protected Bitmap doInBackground(String... urls) {
			Log.d("doInBackground", "starting download of image");
			//קריאה לפונקציה הורדה שליחה הכתובת שלנו אשר במערך הוא במקום אפס
			Bitmap image = downloadImage(urls[0]);
			return image;
		}
		
		
		//בזמן עדכון התמונה נפעיל את שעון ההתקדמות
		protected void onProgressUpdate(Integer... progress) {
			mDialog.show();
			mDialog.setProgress(progress[0]);
		}
		
		//בזמן סיום עבודת העובד לקיחת מה שהורדנו וחיבור לאוביקט תמונה
		protected void onPostExecute(Bitmap result) {
			//קבלת מהעובד את return bitmap;
			if (result != null) {
				ImageView imageView = (ImageView)mActivity.findViewById(R.id.imageView);
				imageView.setImageBitmap(result);
			}
			else {
				TextView errorMsg = (TextView)mActivity.findViewById(R.id.errorMsg);
				errorMsg.setVisibility(View.VISIBLE);//הודעת שגיאה שתהיה גלויה
				errorMsg.setText("Problem downloading image. Try again later");
			}
			// Close the progress dialog
				mDialog.dismiss();
			}
		
		
		//פונקציה הורדת תמונה מהרשת מקבלת כתובת רשת ומחזירה תמונה
		private Bitmap downloadImage(String urlString) {
			URL url;
			try {
				//כתובת האתר שממנו נוריד תמונה
				//יצרת קשר אם האינטרנט
				url = new URL(urlString);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				InputStream is = httpCon.getInputStream();
				
				//אורך הקובץ של התמונה
				int fileLength = httpCon.getContentLength();
				
				//יצרת שק אשר נאסוף אליו את הביטים שנקרא
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;//משתנה שכל פעם נקרא אליו את הביטים בחלקים
				int totalBytesRead = 0;//כמה קראנו עד עכשיו
				byte[] data = new byte[2048];//כמה נוריד כל פעם
				
				//אומרים לשעון התקדמות מה המקסימום
				mDialog.setMax(fileLength);
				// Read the image bytes in chunks of 2048 bytes
				
				// הורדה ושמירה למשתנה     
				//בסוף קריאת הקובץ תמיד יש משתנה -1 לכן נעשה זו עד אליו
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);//שמירה לשק
					
					//לשעון התקדמות
					totalBytesRead += nRead;//הוספת כמות הביטים
					
		//אנחנו בתוך העובד רק בזכות פקודה זו אפשר לצאת מידי פעם ולעדכן את ההתקדמות שלו בבר התקדמות
					publishProgress(totalBytesRead);//עדכון שעון ההתקדמות
				}
				//חיבור מחדש את כל החלקים שהורדנו
				buffer.flush();
				//המרה כדי שנוכל להציג את התמונה
				byte[] image = buffer.toByteArray();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				return bitmap;
			
			
			} catch (Exception e) {//במקרה של כישלון
				e.printStackTrace();
			}
			return null;
		}

	}