package com.example.badner.my.movies;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class EditingMovie extends Activity{

	
	//משתנים לבסיס הנתונים ולרשימה
	private DBHandler dataBase;// אוביקט המקשר לבסיס הנתונים לקריאה ולכתיבה
	private String id="";//מפתח בטבלה
	private String subject="";//שם סרט
	private String body;//תיאור סרט
	private String myrank ;//דירוג של המשתמש
	private String incomemanuallynetwork;//הוכנס ידני או מהאתר
	private String urlimage;//כתובת תמונה אינטרנט
	public String idmovie;//מספר מזהה סרט מהאתר
	
	private String img ;//תמונה
	
	private EditText iEditTextmoviename;//שם סרט
	private EditText iEditTextdescription;//תיאור סרט
	private RatingBar ratingBar;	//משתנה לדירוג
	
	public ImageView imageViewpic;//תמונה

	private ImageView btnupdating;//כפתור עדכון
	private ImageView btnSave;//כפתור שמירה
	public Movie movie;
	
	//משתנים נוספים לחלק תמונה מהרשת
	public MyTaskPic1 taskp ;//אוביקט לקלאס עובד תמונה
	Bitmap bitmpform_net;//משתנה תמונה מעובד תמונה 
	
	//דגלים חזרה ממצלמה או מגלריה ואז נפעל בהתאם
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	public  Bitmap photo;//משתנה שנשמור את התמונה שמקבלים
	byte imgphoto[];//משתנה מסוג ביט שנשצור בבסיס הנתונים את התמונה
	
	MediaPlayer mp;//למוזיקה
	MediaPlayer mpclick;//קליק על כפתור
	
	//לפונטים
	Typeface font ;
	Typeface fontbold;
	TextView textviewtitle;
	TextView textviewrating;
	TextView textviewdescription; 
	
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_movie);
		
		 mp = MediaPlayer.create(this, R.raw.wait);
		//בשביל קליק על בחירה
		 mpclick = MediaPlayer.create(this, R.raw.beer_can_opening);
		
		/** פונטים חיצונים*/ 
		 font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Regular.ttf");
		 fontbold = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Black.ttf"); 
	    textviewtitle = (TextView) findViewById(R.id.textViewmovietitle);
		 textviewrating = (TextView) findViewById(R.id.textViewRating);
		 textviewdescription = (TextView) findViewById(R.id.textViewDescription);
		
		
		
		dataBase = new DBHandler(EditingMovie.this);//יצרת אוביקט מקשר
		
		iEditTextmoviename = (EditText)findViewById(R.id.editTextmoviename);
		iEditTextmoviename. setTypeface(font);
		iEditTextdescription = (EditText)findViewById(R.id.editTextdescription);
		iEditTextdescription. setTypeface(font);
		
		ratingBar = (RatingBar)findViewById(R.id.ratingBarmyratin);
		btnupdating=(ImageView)findViewById(R.id.button_updating);
		btnSave = (ImageView)findViewById(R.id.buttonok);
		
		imageViewpic=(ImageView)findViewById(R.id.imageViewButtonimg);
		
		
		
		//קבלת מס שורה מהדף במקרה שיש ומילוי השדות
		//וקישור כפתור בסדר לעדכון
		intent = getIntent();
		id=intent.getStringExtra("mid");
		//קבלה ממילוי ברשת
		subject  = intent.getStringExtra("itmpTitle");
		idmovie  = intent.getStringExtra("itmpIdMovie");
		urlimage  = intent.getStringExtra("itmpUrlImg");
		
		
		
		//protected void onResume() {
		/**איך נעלה את הדף מאיזה דף בנו ולשם מה  */
		//שבאים לעדכון סרט קיים אז יש מזהה מהטבלה
		//שבאים מדף בחירה סרט מהאינטרנט יש מזהה סרט מהרשת
		// שלא מקבלים כלום אז באנו לשם הוספת סרט ידני
		
		
		/**עדכון סרט קיים*/
		btnupdating.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if (id != null) {
				mpclick.start();//הפעלת מוזיקה
				//id=id; 
				subject = iEditTextmoviename.getText().toString();
				body=iEditTextdescription.getText().toString();
				myrank=String.valueOf(ratingBar.getRating());
				//הוסף סרט עם תמונה ממצלמה גלריה
				if(photo!=null){ 
			
				//לקיחת תמונה מתצוגה והמרה כדי לשמור בבסיס הנתונים כמחרוזת
				//Bitmap yourImage = extras2.getParcelable("data");
				// convert bitmap to byte
	
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				//המרה לביטים אבל מכניסים מחרוזת באמת ההמרה מתמונה
				imgphoto = stream.toByteArray();
				
				 if(subject.equals("")){
					 mpclick.start();//הפעלת מוזיקה
					 //המשתמש לא הכניס שם של סרט
					  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
					  iEditTextmoviename.setError("You must include a subject");
					   }else{
				//הוסף סרט לבסיס הנתונים
		        //עם תמונה
				dataBase.updateMovie(id, subject, body, myrank, incomemanuallynetwork, urlimage, imgphoto);
				Intent intent = new Intent(EditingMovie.this, MainActivity.class);
				
				startActivity(intent); 
					   
					   }
				
				}else{
					mpclick.start();//הפעלת מוזיקה

					// ניקח את התמונה הישנה במקרה שלא היה שינוי
					// ונשמור אותו לא ניתן לא לשלוח תמונה		
					imgphoto=movie.getImg();
					// convert bitmap to byte
					 if(subject.equals("")){
						 //המשתמש לא הכניס שם של סרט
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						   }else{
							dataBase.updateMovie(id, subject, body, myrank, incomemanuallynetwork, urlimage, imgphoto);
							Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							
							startActivity(intent);
						   }
				     }
              
				
				}
			};
		});
	
		
		
		
		 //יש לתת הרשאה לשמור את התמונה
	    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	       
		/** הוספת תמונה קליק על התמונה */
		imageViewpic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mpclick.start();//הפעלת מוזיקה
				MyDialogPic idialog = new MyDialogPic(EditingMovie.this);
				idialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;

				idialog.show();	
				
			
			}
		});
		
		
		
		
		/**כפתור לשמירת נתונים  */
		btnSave.setOnClickListener(new OnClickListener() {
         // טיפול בשלוש מצבי תמונה 3.ללא תמונה 1.תמונה ממצלמה או גלריה
		//2.תמונה מהרשת 	
			@Override
			public void onClick(View v) {
				
				subject = iEditTextmoviename.getText().toString();
				body=iEditTextdescription.getText().toString();
				myrank=String.valueOf(ratingBar.getRating());
				//הוסף סרט עם תמונה ממצלמה גלריה
				if(photo!=null){ 
					mpclick.start();//הפעלת מוזיקה
				//לקיחת תמונה מתצוגה והמרה כדי לשמור בבסיס הנתונים כמחרוזת
				//Bitmap yourImage = extras2.getParcelable("data");
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				//המרה לביטים אבל מכניסים מחרוזת באמת ההמרה מתמונה
				imgphoto = stream.toByteArray();
				
				  if(subject.equals("")){
						 //המשתמש לא הכניס שם של סרט
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						  
						   }else{
							   //הוסף סרט לבסיס הנתונים
							   mpclick.start();//הפעלת מוזיקה
							    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
							    Toast.makeText(EditingMovie.this," Movie add ty" , Toast.LENGTH_SHORT).show();	
							    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							
			                     startActivity(intent);  
			                   //לאנמציה מעבר דף
			 					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
						   }
                    
				
				      }
			 else if (idmovie!=null) {
				   //אם יש מזהה סרט מהרשת אז אנו צרכים לשמור את התמונה מהרשת	
				 Bitmap image=bitmpform_net;
				
				 // convert bitmap to byte
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(Bitmap.CompressFormat.PNG, 100, stream);
					imgphoto = stream.toByteArray();
				 
					if(subject.equals("")){
						 //המשתמש לא הכניס שם של סרט
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						   }else{
							   //הוסף סרט לבסיס הנתונים
							   mpclick.start();//הפעלת מוזיקה
							    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
							    Toast.makeText(EditingMovie.this," Movie from net add ty" , Toast.LENGTH_SHORT).show();	
							    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							    // חזרה לדף חיפוש סרטים ברשת
			                    
							    startActivity(intent); 
							  //לאנמציה מעבר דף
								overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
						   }
				  }
				
				
				else{
					//המשתמש לא הכניס תמונה
					       
					
					      
					//לקיחת תמונה חילופית מהתקיה
					        Bitmap image = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimagesm);

							// convert bitmap to byte
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							image.compress(Bitmap.CompressFormat.PNG, 100, stream);
							imgphoto = stream.toByteArray();
							//dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);

							
							  if(subject.equals("")){
								 //המשתמש לא הכניס שם של סרט
								  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
								  iEditTextmoviename.setError("You must include a subject");
								   }else{
									   //הוסף סרט לבסיס הנתונים
									   mpclick.start();//הפעלת מוזיקה
									    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
									    Toast.makeText(EditingMovie.this," Movie add ty" , Toast.LENGTH_SHORT).show();	
									    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
									
					                     startActivity(intent);  
					                   //לאנמציה מעבר דף
					 					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
								   }
							
				}
               
			}
		});
  
		/** כפתור ביטול חזרה לדף ממנט בנו בלי לעשות כלום*/
		ImageView ibuttoncacel=(ImageView)findViewById(R.id.buttoncacel);
		ibuttoncacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (idmovie!=null){
					mpclick.start();//הפעלת מוזיקה
					//לדף חיפוש סרטים 
					 Intent intent = new Intent(EditingMovie.this, MainMovieNetwork.class);
						
	                 startActivity(intent);
	               //לאנמציה מעבר דף
	                 overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
				}else {
					mpclick.start();//הפעלת מוזיקה
					//לדף הראשי
					 Intent intent = new Intent(EditingMovie.this, MainActivity.class);
						
	                 startActivity(intent); 
	               //לאנמציה מעבר דף
						overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
				}
				
				
				
			}
			
		});
	}//סוף עליית הדף

//כאשר נקבל את טרד ההצגה
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	//UI
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		
		
		
		super.onResume();
		//בשביל הפונטים
		textviewtitle. setTypeface(fontbold);
		textviewrating. setTypeface(fontbold);
		textviewdescription. setTypeface(fontbold);
		
		
		/**איך נעלה את הדף מאיזה דף בנו ולשם מה  */
		//שבאים לעדכון סרט קיים אז יש מזהה מהטבלה
		//שבאים מדף בחירה סרט מהאינטרנט יש מזהה סרט מהרשת
		// שלא מקבלים כלום אז באנו לשם הוספת סרט ידני
		
		
		if (id != null) {//עם שווה או גדול לאפס אז אנחנו בטבלה
			//עדכון סרט קיים
			//נסתיר כפתור שמירה
	        btnSave.setVisibility(ImageView.GONE);
			//כותרת הדף 
	        getActionBar().setTitle("Edit Movie");
	        
			//קריאה לפונקציה קבלת שורה מבסיס הנתונים וקבלה לאוביקט סרט
			 movie =dataBase.getMovie(id);
            //נאתחל את שדות הטקסט
			iEditTextmoviename.setText(String.valueOf(movie.getSubject()));
			iEditTextdescription.setText(String.valueOf(movie.getBody())); 
			ratingBar.setRating(Float.valueOf(movie.getMyRank()));//המרה למספר
			
			 
			 
			//לתמונה נמיר מביט לביטמפ ונציג
			byte[] theImage=movie.getImg();
	        ByteArrayInputStream imageStream = new ByteArrayInputStream(theImage);
	        Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);

	        imageViewpic.setImageBitmap(bitmapImage);
	       
	      
		
		}
		else if (idmovie!=null) {
			//אם בנו מדף בחירה באינטרנט
             // נסתיר כפתור עדכון
		    
			btnupdating.setVisibility(ImageView.GONE);
			
			getActionBar().setTitle("Edit or Save Movie From Net");
			
			//לכותרת	
			
			iEditTextmoviename.setText(subject);
			//לתמונה
  
	     	 taskp = new MyTaskPic1(imageViewpic);
	   	     taskp.execute(urlimage);
			
	   	    //לתיאור
	 		//יצרת אוביקט שמחבר לאינטרנט לשליפת מידע וניתן לו את הדף הזה
	 		MyTaskDescription task = new MyTaskDescription(this);
	 		//שליחה של מזהה קוד הסרט
	 		
	 		idmovie=idmovie+".json";
	 		String url1="http://api.rottentomatoes.com/api/public/v1.0/movies/"+idmovie+"?apikey=pe7kbs9aa2u3j46d2qkaavcv";

	 
	 		//הפעלת העובד
	 		task.execute(url1);
	 		    		
			
		} 
			else {
			//אין מספר מזהה מהטבלה לכן אנו במצב שמירת סרט חדש
		    //לכותרת
				getActionBar().setTitle("Add Your Own Movie");	
			// נסתיר כפתור עדכון
		    
			btnupdating.setVisibility(ImageView.GONE);
		}

		
	
		
		
		
	}


	/** חזרה מתמונה מהגלריה או ממצלמה*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case CAMERA_REQUEST:

			Bundle extras = data.getExtras();

			if (extras != null) {
			
			//להציג תמונה מהמצלמה בדף
				  photo = (Bitmap) data.getExtras().get("data"); 
				  imageViewpic.setImageBitmap(photo);
				
				

			}
			break;
		case PICK_FROM_GALLERY:
			Bundle extras2 = data.getExtras();

			if (extras2 != null) {
				
				//להציג תמונה מהמצלמה בדף
				  photo = (Bitmap) data.getExtras().get("data"); 
				  imageViewpic.setImageBitmap(photo);
						
				
			}

			break;
		}
	          
	   
	}

	 /** קלאס דיאלוג מצלמה או גלריה אינר קלאס  */
  	private class MyDialogPic extends Dialog{
  		
  		
  		public MyDialogPic(Context context) {
  			super(context);
  			setContentView(R.layout.dialogpicfrom);
  			setTitle("Select Gallery or Camera");
            //setCancelable(false);ביטול כפתור חזרה
  			//כפתור למצלמה
  			ImageView btncamera = (ImageView)findViewById(R.id.imageViewcamera);
  			btncamera.setOnClickListener(new View.OnClickListener() {

  				@Override
  				public void onClick(View v) {
  					callCamera();//נקרא לפונציה שפותחת מצלמה	
  					 dismiss();
  				}	
  			});
  			
  			
  		   //כפתור לגלריה
  			ImageView btngallery = (ImageView)findViewById(R.id.imageViewgallery);
  			btngallery.setOnClickListener(new View.OnClickListener() {

  				@Override
  				public void onClick(View v) {
  					callGallery();//פתיחת הגלריה	
  					 dismiss();	
              
  				}	
  			});

  		}		

     }
	
  	//פונקציה למצלמה
  	/**
	 * open camera method
	 */
	public void callCamera() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra("crop", "true");
		cameraIntent.putExtra("aspectX", 0);
		cameraIntent.putExtra("aspectY", 0);
		cameraIntent.putExtra("outputX", 150);
		cameraIntent.putExtra("outputY", 200);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);

	}

	//פונציה לגלריה
	/**
	 * open gallery method
	 */

	public void callGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(
		Intent.createChooser(intent, "Complete action using")
		,PICK_FROM_GALLERY);

	}

	/**קלאס לקבלת תיאור הסרט */
public class MyTaskDescription extends AsyncTask<String, Void, String>{
		
	private MyDialogWait idialog;
	//private Activity activity;//מקשר לדף שקראנו ממנו
		
		public MyTaskDescription(Activity act ) {
			//activity = act;
		}
		
		@Override
		protected void onPreExecute() {
			//הפעל את השעון
		//	ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
		//	bar.setVisibility(ProgressBar.FOCUS_UP);
		
			 idialog = new MyDialogWait(EditingMovie.this);
				idialog.show();	
				//הפעלת מוזיקה
				
				 mp.start();
				
		}
		
	
		
		@Override
		protected String doInBackground(String... params) {
		
			
			//  חיבור לאתר ששלחנו במקרה זה תוצאות של סרט בשם
			
			BufferedReader input = null;
			HttpURLConnection httpCon = null;
			InputStream input_stream =null;
			InputStreamReader input_stream_reader = null;
			StringBuilder response = new StringBuilder();
			try{
				URL url = new URL(params[0]);
				httpCon = (HttpURLConnection)url.openConnection();
				if(httpCon.getResponseCode()!=HttpURLConnection.HTTP_OK){
					return null;
				}
				
				input_stream = httpCon.getInputStream();
				input_stream_reader = new InputStreamReader(input_stream);
				input = new BufferedReader(input_stream_reader);
				String line ;
				while ((line = input.readLine())!= null){
					response.append(line +"\n");
				}
				
				
				
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(input!=null){
					try {
						input_stream_reader.close();
						input_stream.close();
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(httpCon != null){
						httpCon.disconnect();
					}
				}
			}
			return response.toString();
		}

		
		@Override
		protected void onPostExecute(String result) {
			
			
			try {
				//חיפוש לקבלת מחרוזרת פרטי סרט
				JSONObject json = new JSONObject(result);
			
				
				String strdetailed="";//משתנה שנשמור אליו את הערך של המפתח שנחפש
				
				

				if(json.has("synopsis"))//במקרה שיש מפתח
				strdetailed =json.getString("synopsis");//הכנס את הערך
				
				//במקרה שאין תיאור נציג אין תיאור
				if(strdetailed.equals("")){
					iEditTextdescription.setText("No description");
					}else{
				iEditTextdescription.setText(strdetailed);//תיבת טקסט הצג
				}
				idialog.dismiss();
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				

		}

		
	}

/**קלאס לקבלת תמונת הסרט מהרשת */
	 class MyTaskPic1 extends AsyncTask<String, Void, Bitmap> {
		    private final WeakReference<ImageView> imageViewReference;
		    private String data;

		    public MyTaskPic1(ImageView imageView) {
		        // Use a WeakReference to ensure the ImageView can be garbage
		        // collected
		        imageViewReference = new WeakReference<ImageView>(imageView);
		    }

		    // Decode image in background.
		    @Override
		    protected Bitmap doInBackground(String... urls) {
		    	Log.d("doInBackground", "starting download of image");
				//קריאה לפונקציה הורדה שליחה הכתובת שלנו אשר במערך הוא במקום אפס
				Bitmap image = downloadImage(urls[0]);
				return image;
		    }

// Once complete, see if ImageView is still around and set bitmap.
		    @Override
		    protected void onPostExecute(Bitmap bitmap) {
		       
		         bitmpform_net=bitmap;
		    	if (imageViewReference != null && bitmap != null) {
		            final ImageView imageView = imageViewReference.get();
		            if (imageView != null) {
		                imageView.setImageBitmap(bitmap);
		            }
		        }
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
	  				
	  				
	  				
	  				//יצרת שק אשר נאסוף אליו את הביטים שנקרא
	  				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	  				int nRead;//משתנה שכל פעם נקרא אליו את הביטים בחלקים
	  			
	  				byte[] data = new byte[2048];//כמה נוריד כל פעם
	  				
	  				
	  				// Read the image bytes in chunks of 2048 bytes
	  				
	  				// הורדה ושמירה למשתנה     
	  				//בסוף קריאת הקובץ תמיד יש משתנה -1 לכן נעשה זו עד אליו
	  				while ((nRead = is.read(data, 0, data.length)) != -1) {
	  					buffer.write(data, 0, nRead);//שמירה לשק
	  					
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
	
	 
		private class MyDialogWait extends Dialog{
	  		
	  		
	  		public MyDialogWait(Context context) {
				super(context);
				setContentView(R.layout.dialogpageloading);
	  			setTitle("Wait Page Loading");
	  		}		

	     }	 
}