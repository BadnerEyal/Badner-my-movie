package com.example.badner.my.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



@SuppressLint("NewApi")
public class MainMovieNetwork extends Activity {

	
	


		private String name="";//שם הסרט שנחפש
		private EditText namein;//השם שהמשתמש יכניס
		
		public ArrayList<Movie> list;//מערךליסט מסוג רשומה
		public ListView lv ;//אובייקט ListView יכיל את רשימת המערך
			//המנהל שאני יצרתי
		public SearchAdapter adapter;//המתאם ינהל את הרשימה
		
		final Context context = this;
		MediaPlayer mpclick;//קליק על כפתור
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main_network);
			//לפונט
			Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Black.ttf");
			namein=(EditText)findViewById(R.id.editTextname);
			namein. setTypeface(font);
			//בשביל קליק על בחירה
			 mpclick = MediaPlayer.create(this, R.raw.beer_can_opening);
			
			
			ImageView btmn = (ImageView)findViewById(R.id.imageButtonsearch);
			btmn.setOnClickListener(new View.OnClickListener() {
			
		
			@Override
			public void onClick(View arg0) {
				mpclick.start();//הפעלת מוזיקה
				name=namein.getText().toString();
				//תיקון השם שיוכל לקבל רווחים
				String replacedToSearchname = name.replaceAll("\\s+", "+");
				//יצרת אוביקט שמחבר לאינטרנט לשליפת מידע וניתן לו את הדף הזה
				MyTaskName task = new MyTaskName(MainMovieNetwork.this);
				// שליחה של דף תוצאות נקבל גנסון עם כל הסרטים באותו שם שניתן 
				//avatar
				String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+replacedToSearchname+"&page_limit=10&page=1&apikey=pe7kbs9aa2u3j46d2qkaavcv";

				task.execute(url);
			   
			   }
		
			});
			
			ListView lv = (ListView)findViewById(R.id.listViewAllmovie);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long id) {
					// TODO Auto-generated method stub
					//שמירת משתנים אשר נשלח אותם לדף עריכה כאשר המשתמש ילחץ על שורה מסוימת
					mpclick.start();//הפעלת מוזיקה
					//int tmpposition =position;//שמירת מס שורה למשתנה
					String tmpTitle=list.get(position).getSubject();//קבלת שם סרט 
					String tmpIdMovie=list.get(position).getMovieid();//קבלת מזהה סרט
					String tmpUrlImg=list.get(position).getUrlImage();//קבלת כתובת תמונה
					//String.valueOf(id);
					Intent intent = new Intent(MainMovieNetwork.this,EditingMovie.class);
					
					intent.putExtra("itmpTitle", tmpTitle);
					intent.putExtra("itmpIdMovie", tmpIdMovie);
					intent.putExtra("itmpUrlImg", tmpUrlImg);
					//startActivityForResult(intent,1);//הדף ימתין ולא ימחק מהמחסנית	
				    startActivity(intent);//לא נשתמש כי צריך את הדף הזה במצב המתנה 
				  //לאנמציה מעבר דף
	                 overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
					
					
				}
				
			});	
           
			//בקליק ארוך נציג את התמונה בגדול
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {
                
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					 final int tmpposition= position;
					 mpclick.start();//הפעלת מוזיקה
					 //דיאלוג פשוט להציג תמונה גדולה כן או לא
			         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			 
						// set title
						alertDialogBuilder.setTitle("View a larger image");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Click yes to larger image")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									String tmpurl=list.get(tmpposition).getUrlImagebig();// big קבלת כתובת תמונה
									String.valueOf(id);
									Intent intent = new Intent(MainMovieNetwork.this,ShowPic.class);
									
									intent.putExtra("tmpurl", tmpurl);
									
								    startActivity(intent);//לא נשתמש כי צריך את הדף הזה במצב המתנה
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
						
					
					return false;
				}
			});
			
			
			ImageView btmcan =(ImageView)findViewById(R.id.imageViewcansarc );
			
			btmcan.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mpclick.start();//הפעלת מוזיקה
					Intent intent = new Intent(MainMovieNetwork.this,MainActivity.class);					
					startActivity(intent);
					  //לאנמציה מעבר דף
	                 overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
				}
			});
			
			
		}

		@SuppressLint("NewApi")
		@Override
		protected void onResume() {
			
			super.onResume();
			getActionBar().setTitle("Search Movie Network");
		}

		
		public class MyTaskName extends AsyncTask<String, Void, String>{
			//הרשימה אותה נציג
			//private ArrayList<ClassMovieNetwork> list;//מערךליסט מסוג רשומה
			//private ListView lv ;//אובייקט ListView יכיל את רשימת המערך
			//המנהל שאני יצרתי
			//private SearchAdapter adapter;//המתאם ינהל את הרשימה
					
			//הרשימה אותה נציג
				
			
			
			private Activity activity;//מקשר לדף שקראנו ממנו
			
			public MyTaskName(Activity act ) {
				activity = act;
			}
			
			@Override
			protected void onPreExecute() {
				//הפעל את השעון
			//	ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
			//	bar.setVisibility(ProgressBar.FOCUS_UP);
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
			
				
				//קבלה לאחר העבודה 
				//כיבוי שעון
			//ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
			//	bar.setVisibility(ProgressBar.GONE);
			
				
				//ליסט מערך שנשמור את התוצאות
				//ArrayList<String> list = new ArrayList<String>();
				 //1
				//תחילה ניצר מערך ליסט של אוביקט רשומה	 
				list = new ArrayList<Movie>();
				
				
				try {
					//חיפוש בתוך גינסון לקבלת כותרות הסרטים
					//יחזיר את כל הסרטים במערך
					JSONObject json = new JSONObject(result);
					
					//קבלה של כל הסרטים למערך  גנסון משלנו
					JSONArray arr = json.getJSONArray("movies");
					//לעבור במערך הגנסון ולהוסיף אותם לרשימה שלנו
					for (int i = 0; i < arr.length(); i++) {
						//ניצר תחילה אוביקט חדש 
						//נשתול בתוכו ממה שקבלנו את שם השרת ומספר מזהה
						//כתובת לתמונה לפי הגנסון
						//וכך הרשימה תחזיק אוביקטים מלאים
						Movie iMovieNetwork= new Movie();
						
						//עבור מערך של גנסון בתוך הגנסון
						JSONObject js = (JSONObject)arr.get(i);
						//
						if(js.has("id"))
							iMovieNetwork.setMovieid(js.getString("id"));
						
						if(js.has("title"))
							iMovieNetwork.setSubject(js.getString("title"));
						
						
		                 //עבור תמונה
						JSONObject postersJson = js.getJSONObject("posters");//מפתח
						
						if(postersJson.has("detailed"))//תגית
							{iMovieNetwork.setUrlImage(postersJson.getString("thumbnail"));
						    iMovieNetwork.setUrlImagebig(postersJson.getString("detailed"));}
						  // big עבור תמונה
						//JSONObject postersJsonbig = js.getJSONObject("posters");//מפתח
						
						//if(postersJson.has("detailed"))//תגית
							
						
						
						list.add(iMovieNetwork);//הוספת האוביקט לרשימה
						
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//חיבור התוצאות לתןך רשימה
				//ListView lv = (ListView)activity.findViewById(R.id.list1);
				lv = (ListView)activity.findViewById(R.id.listViewAllmovie);
				
				//3
				// the adapter will manage the list
				//יצרת המנהל אומרים איך לבנות את השורה הקובץ אקמל שיצרנו 
				//עיצוב שורה בקמל
				
				
				adapter = new SearchAdapter(list, activity);
				
				//listView.setAdapter(adapter);
				//adapter = new ArrayAdapter<ClassNote>(this,R.layout.list_row,list);
				//adapter = new ArrayAdapter( this , מאיפה השורה ,  items  סוג האוביקט);
				
				
				//4
				// ListView נותנים למתאם לנהל לנו את אוביקט 
				lv.setAdapter(adapter);
				//המאזין ללחיצה על הרשומה
					

			}

			

		}
		
	}

