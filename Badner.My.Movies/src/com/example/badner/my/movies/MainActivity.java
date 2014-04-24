package com.example.badner.my.movies;

import java.util.ArrayList;
import java.util.Locale;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.view.animation.Animation;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	//משתנים לתפריט בצד
		// Within which the entire activity is enclosed
			private DrawerLayout mDrawerLayout;

			// ListView represents Navigation Drawer
			private ListView mDrawerList;

			// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
			private ActionBarDrawerToggle mDrawerToggle;

			// Title of the action bar
			private String mTitle = "";
	        
			//אוביקט לכתיבה לקובץ חיצוני
			private SharedPreferences pref;
			
			//אוביקט לשיתוף למעלה
			private ShareActionProvider myShareActionProvider;
		
			//משתנים לבסיס הנתונים ולרשימה
			private DBHandler dataBase;// אוביקט המקשר לבסיס הנתונים לקריאה ולכתיבה 
			
			//בשביל הרשימה שלוש הדברים שצריך לעשות
			private ArrayList<Movie> list;
			private ListView lv;
			private AllMovieAdapter iadapter;//המתאם ינהל את הרשימה
			
			 
			private String mid;// מספר מזהה של שורה בבסיס הנתונים
			
			private MediaPlayer mp;
			
			
	  @SuppressLint("NewApi")
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {

				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
	            
				//בשביל קליק על בחירה
				 mp = MediaPlayer.create(this, R.raw.beer_can_opening);
				
				
				
				/**יצרת הרשימה*/
				dataBase = new DBHandler(MainActivity.this);//יצרת אוביקט מקשר
				//בשביל הרשימה שלוש הדברים שצריך לעשות
				list  =   dataBase.getAllMoviesByArrayList();
				//String name="ggg";
				//list  =   dataBase.getAllMoviesByNAMEArrayList(name);
				
				lv = (ListView)findViewById(R.id.listViewAllMovie);
				iadapter = new AllMovieAdapter( list,MainActivity.this);
				
				lv.setAdapter(iadapter);
				
				
				//כאשר נלחץ על הרשימה
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						mp.start();//הפעלת מוזיקה
                      
						Movie movie = list.get(position);
						
						 mid=String.valueOf(movie.getId());//זה המספר האמיתי בתוך השורה שנבקש
						Intent intent1 = new Intent(MainActivity.this,EditingMovie.class);
						//שליחת מס שורה בטבלה
						intent1.putExtra("mid",mid);
						startActivity(intent1);
						//לאנמציה מעבר דף
						overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                       
						
					}
				});
				
				//לחיצה ארוכה ברשימה תפתח את התפריט מחקיה או עריכה
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
					
						mp.start();//הפעלת מוזיקה
						Movie movie = list.get(position);
						//important !!! position is not id !!!!!!!
						//Toast.makeText(MainActivity.this, position + " "+Contact.getId(), Toast.LENGTH_SHORT).show();	
					    
						 mid=String.valueOf(movie.getId());//זה המספר האמיתי בתוך השורה שנבקש
						 registerForContextMenu(lv);// הפעלת תפריט צף
						
						return false;
					}
				});			

		
					
				
				/** הוספת סרט ידני או מהרשת*/
				ImageView btnAdd = (ImageView)findViewById(R.id.imageViewaddmovie);
				btnAdd.setOnClickListener(new View.OnClickListener() {
					
					@Override
				public void onClick(View v) {
						mp.start();//הפעלת מוזיקה
			
						//יצרת אוביקט קלאס דיאלוג 
						MyDialogaddmovie idialogadd = new MyDialogaddmovie(MainActivity.this);
						//לאנמציה דיאלוג שימוש גם בסטיל
						idialogadd.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
						idialogadd.show();
					
				    	 
					}
				});			
				
				
				
				
	//////////************////////////////////////			
			/**קבלת שם משתמש כדי שיופיע בתפריט למעלה שימוש לקובץ חיצוני	*/
				//יצרת 
				pref = getSharedPreferences("login", MODE_PRIVATE);
				
				//פעם ראשונה יכתב אמת באופן אוטמטי במקרה שלא קיים המפתח
				boolean enter = pref.getBoolean("enter", true);
				
				if(enter)
				{
					// נכתוב לקובץ שכבר נכנסו כדי שלאיכנס בפעם הבאה
					//ונקרא לדיאלוג הכנס שם משתמש
					
					//נכתוב לקובץ 
					SharedPreferences.Editor edit = pref.edit();
					edit.putBoolean("enter", false);
					
					edit.commit();
					// לדיאלוג הכנס שם משתמש
					
					 
					
					MyDialogname dlgname = new MyDialogname(this);
					//לאנמציה דיאלוג שימוש גם בסטיל
					dlgname.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
					dlgname.show();
					
					
				}
				
				//קבלת שם המשתמש מהקובץ להצגת בבאר העליון
				String username =pref.getString("name", "").toString();
				
				/**כאשר התפריט סגור שם פרויקט ותמונה */
				//Eyal Film Library  
				//רק בכניסה ראשונה לא יופיע השם לבדוק מה ניתן לעשות
				mTitle = username+" Film Library";
				getActionBar().setTitle(mTitle);

				// Getting reference to the DrawerLayout
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

				mDrawerList = (ListView) findViewById(R.id.drawer_list);

				// Getting reference to the ActionBarDrawerToggle
				mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
						R.drawable.ic_drawer, R.string.drawer_open,
						R.string.drawer_close) {

	/** כאשר המשתמש בחר בתפריט  */
					
	  public void onDrawerClosed(View view) {
						
	     getActionBar().setTitle(mTitle);
	     invalidateOptionsMenu();

					}

	/** כאשר המשתמש פותח את התפריט */
	  public void onDrawerOpened(View drawerView) {
		getActionBar().setTitle("Select the options");
		invalidateOptionsMenu();
	   }

	};

				// Setting DrawerToggle on DrawerLayout
				mDrawerLayout.setDrawerListener(mDrawerToggle);

				// Creating an ArrayAdapter to add items to the listview mDrawerList
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), 
						R.layout.drawer_list_item, getResources().getStringArray(R.array.menus));

				// Setting the adapter on mDrawerList
				mDrawerList.setAdapter(adapter);

				// Enabling Home button
				getActionBar().setHomeButtonEnabled(true);

				// Enabling Up navigation
				getActionBar().setDisplayHomeAsUpEnabled(true);
	
				
				// Setting item click listener for the listview mDrawerList
				mDrawerList.setOnItemClickListener(new OnItemClickListener() {
	    
					
		@Override//לחיצה בתפריט בצד
		public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					// מקבל מערך של הרשומות שכתבנו בתפריט
					//String[] menuItems = getResources().getStringArray(R.array.menus);

					// Currently selected river
					//mTitle = menuItems[position];

			         //קריאה לפונקציה בחירה בתפריט ניתן לה מיקום הבחירה
					 menuSelection(position);
						
					
					// Getting reference to the FragmentManager
					FragmentManager fragmentManager = getFragmentManager();

					// Creating a fragment transaction
					FragmentTransaction ft = fragmentManager.beginTransaction();

					

						// Committing the transaction
						ft.commit();

						// Closing the drawer
						mDrawerLayout.closeDrawer(mDrawerList);

					}
				
				
				
		});
				
				
	  }
	  /** סוף יצרת הדף */

	  
	  
	  
	  
	  /** פונקציות לתפריט בצד*/
	  /** פונקציה בחירה בתפריט מה לעשות */
	    protected void menuSelection(int position) {
	  	//פונקציה בחירה בתפריט במקרה זה מציגה רק הודעה לפי בחירה
	  			
	  	switch (position) {
	  			case 0:
	  				
	  			// לדיאלוג הכנס שם משתמש
					MyDialogname dlgname = new MyDialogname(this);
					//לאנמציה דיאלוג שימוש גם בסטיל
					dlgname.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
					dlgname.show();
	  				
	  				break;
	  			case 1:
	  			   //דיאלוג לעברית
	  				
	  				//יצרת אוביקט קלאס דיאלוג 
	  				MyDialog idialog = new MyDialog(MainActivity.this);
	  				//לאנמציה דיאלוג שימוש גם בסטיל
	  				idialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
	  				idialog.show();
	  				 
	  				
	  				break;
	  			case 2:
	  				//הוספת סרט ידני
	  				Intent intent = new Intent(MainActivity.this,EditingMovie.class);
					
					//intent.putExtra("iName", Name );
					
					startActivity(intent); 
					//לאנמציה מעבר דף
					overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	  				
	  				Toast.makeText(MainActivity.this,"Add Your Movie" , Toast.LENGTH_SHORT).show();
	  				break;
	  			case 3:
	  				//הוספת סרט מהאינטרנט
                     Intent intentn = new Intent(MainActivity.this,MainMovieNetwork.class);
					
					//intent.putExtra("iName", Name );
					
					startActivity(intentn); 
					//לאנמציה מעבר דף
					overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	  				
	  				Toast.makeText(MainActivity.this," Add Web Movie" , Toast.LENGTH_SHORT).show();
	  				break;
	  			case 4:
	  				//מחיקת כל הסרטים
	  				
	  				 
								
								dataBase.deleteAll();
					  			//קריאה חדשה למנהל כדי לעדכן את הרשימה לאחר מחיקה
									list  =   dataBase.getAllMoviesByArrayList();
									lv = (ListView)findViewById(R.id.listViewAllMovie);
									iadapter = new AllMovieAdapter( list,MainActivity.this);
									
									lv.setAdapter(iadapter);
					  				Toast.makeText(MainActivity.this,"Deleted All Movies" , Toast.LENGTH_SHORT).show();
					  				
					  				//הפעלת ויברטור בזמן מחיקת כל הטבלה
					  				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					  			
					  				v.vibrate(200);
					  		
					  			   // v.cancel();
								
							
	

	  				break;
	  			case 5:
	  				
	  				MyDialogabot iMyDialogabot = new MyDialogabot(MainActivity.this);
	  			//לאנמציה דיאלוג שימוש גם בסטיל
	  				iMyDialogabot.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
	  				
	  				iMyDialogabot.show();
	  				Toast.makeText(MainActivity.this,"Thank you for using apps" , Toast.LENGTH_SHORT).show();

	  				break;
	  			case 6:
	  			    //יצאה
	  				Toast.makeText(MainActivity.this,"Goodbye" , Toast.LENGTH_SHORT).show();
	  				Intent intentout = new Intent(Intent.ACTION_MAIN);
	  				intentout.addCategory(Intent.CATEGORY_HOME);
	  				intentout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  				startActivity(intentout);
	  			//לאנמציה מעבר דף
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	  				

	  				break;
	  			default:
	  				break;
	  			}
	  		}
	  	 
	  @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	  	//נקרא לאחר onResume
	  	//במעגל החיים  
	  	//מחלקות נגזרות חייבות לקרוא ועד ליישומו של מעמד העל של שיטה זו. אם הם לא, למעט יושלכו
	  	//FragmentActivity
	  	super.onPostCreate(savedInstanceState);
	  			mDrawerToggle.syncState();
	  		}

	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	  	//נקרא בכל פעם שפריט בתפריט האפשרויות שלך נבחר	
	  	   if (mDrawerToggle.onOptionsItemSelected(item)) {
	  				return true;
	  			}
	  			//return super.onOptionsItemSelected(item);
	  			
	  			
	  				
	  				switch (item.getItemId()) {
	  				case R.id.action_settings:
	  					//פתיחת מגירה תפריט צד
	  					mDrawerLayout.openDrawer(mDrawerList);
	  					 
	  					Toast.makeText(MainActivity.this, "Menu open", Toast.LENGTH_SHORT).show();
	  					break;
	  				
	  		
	  				default:
	  					break;
	  				}
	  				
	  				return super.onOptionsItemSelected(item);
	  			
	  			
	  			
	  		}

	  /** נקרא בכל פעם שאנו קוראים  לתפריט בצד  */
	  		@Override
	   public boolean onPrepareOptionsMenu(Menu menu) {
	  			// אם המגירה פתוחה, להסתיר פריטי פעולה בקשר לתצוגת התוכן
	  			//אם התפריט פתוח אז מסתרים את האיקון פתיחה למעלה
	  			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	              //מגדירים אותו גלוי
	  			menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	  			return super.onPrepareOptionsMenu(menu);
	  		}

	  		
	  ///רענון הדף 		
	  void refresh(){
	  			Intent refresh = new Intent(this, MainActivity.class); 
	  			startActivity(refresh); 
	  		//לאנמציה מעבר דף
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	  };   
	  
	 /**לשיתוף איקון למעלה נשתמש בתפריט */
	  @SuppressLint("NewApi")
	@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.menu, menu);
		    MenuItem item = menu.findItem(R.id.menu_item_share);
		    myShareActionProvider = (ShareActionProvider)item.getActionProvider();
		    myShareActionProvider.setShareHistoryFileName(
		   ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		   myShareActionProvider.setShareIntent(createShareIntent());
		    return true;
		}
		
		private Intent createShareIntent() {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
	        shareIntent.setType("text/plain");
	                
	        shareIntent.putExtra(Intent.EXTRA_TEXT, 
	        		"https://www.google.co.il");
	        return shareIntent;
	    }

		
		
		
		
		
		
		/**   לתפריט כאשר לוחצים לחיצה ארוכה בשורה */	
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			getMenuInflater().inflate(R.menu.main, menu);
			super.onCreateContextMenu(menu, v, menuInfo);
		}
		
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
		
			
			switch (item.getItemId()) {
			
			case R.id.action_editmovie:
				Intent intent1 = new Intent(MainActivity.this,EditingMovie.class);
				//שליחת מס שורה בטבלה
				intent1.putExtra("mid",mid);
				startActivity(intent1);
				//FLAGContextMenu=1;
				Toast.makeText(MainActivity.this, "edit movie", Toast.LENGTH_SHORT).show();
				break;
			case R.id.action_deletemovie:
				 dataBase.deleteMovie(mid);	
				Toast.makeText(MainActivity.this, "deletemovie", Toast.LENGTH_SHORT).show();
				
				//קריאה חדשה למנהל כדי לעדכן את הרשימה לאחר מחיקה
				list  =   dataBase.getAllMoviesByArrayList();
				lv = (ListView)findViewById(R.id.listViewAllMovie);
				iadapter = new AllMovieAdapter( list,MainActivity.this);
				
				lv.setAdapter(iadapter);
				
				
				break;
			

			default:
				break;
			}
			return super.onContextItemSelected(item);
		}		
		
		
		/**קלאס דיאלוג על כתיבת האפלקציה */
		private class MyDialogabot extends Dialog{
		
		public MyDialogabot(Context context) {
				super(context);
				setContentView(R.layout.dialogabout);
				setTitle("About apps");
				ImageView ok=(ImageView)findViewById(R.id.imageViewok);
				ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
				
	        	}	
			}		
				
		
		/**קלאס דילוג הוספת סרט ידני או מהרשת*/   
		private class MyDialogaddmovie extends Dialog{
      
			public MyDialogaddmovie(Context context) {
				super(context);
				setContentView(R.layout.dialogaddmovie);
				setTitle("Choose how to add a movie");
				
				//מהרשת
				ImageView enter_net=(ImageView)findViewById(R.id.imageViewnet);
				enter_net.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//הפעלת מוזיקה
					Toast.makeText(MainActivity.this, "You have chosen to net add", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainActivity.this,MainMovieNetwork.class);
	
					startActivity(intent);  
					dismiss();
					//לאנמציה מעבר דף
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					
				}
			});
			
				
		   //ידני
			ImageView enter_manl=(ImageView)findViewById(R.id.imageViewmanl);
			enter_manl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//הפעלת מוזיקה
					Toast.makeText(MainActivity.this, "You have chosen to manually add", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainActivity.this,EditingMovie.class);
	
					startActivity(intent);  
					dismiss();
					//לאנמציה מעבר דף
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					
				}
			});
			}
		}	
		
		
		
		
			
		/**קלאס דילוג שם משתמש*/   
		private class MyDialogname extends Dialog{
        // נקבל שם משתמש ונכתוב אותו לקובץ
			public MyDialogname(Context context) {
				super(context);
				setContentView(R.layout.dialogname);
				setTitle("Enter a New Username");
				
				
				ImageView enter=(ImageView)findViewById(R.id.buttonenter);
				enter.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//הפעלת מוזיקה
					TextView newname=(TextView)findViewById(R.id.editTextusername);
					String name=newname.getText().toString();
					
					SharedPreferences.Editor edit = pref.edit();

					edit.putString("name", name);
					edit.commit();
					Toast.makeText(MainActivity.this, "Name Changed", Toast.LENGTH_LONG).show();
					
					 refresh();//רענון הדף כדי שיופיע השם החדש
					dismiss();
					
				}
			});
			
			}

				
		}
		
		
	   /** קלאס דיאלוג שפה אינר קלאס  */
	  	private class MyDialog extends Dialog{
	  		public String languagd = "";
	  		
	  		public MyDialog(Context context) {
	  			super(context);
	  			setContentView(R.layout.dialogln);
	  			setTitle("Menu Hebrew or English");
	  			//כפתור אנגלית
	  			ImageView buttonen1 = (ImageView)findViewById(R.id.buttonen);
	  			buttonen1.setOnClickListener(new View.OnClickListener() {

	  				@Override
	  				public void onClick(View v) {
	  					mp.start();//הפעלת מוזיקה
	  					//נחזיר ערך שפה אנגלית
	  					languagd="english";
	  					Toast.makeText(MainActivity.this, languagd, Toast.LENGTH_LONG).show();
	  					setLocale("en");
	  					//Intent intent = new Intent(MainActivity.this,MainActivity.class);
	  					//intent.putExtra("language","english");
	  					
	  					//startActivity(intent);	
	  					 dismiss();
	  				}	
	  			});
	  			
	  			
	  		   //כפתור לעברית
	  			ImageView buttonhb1 = (ImageView)findViewById(R.id.buttonhb);
	  			buttonhb1.setOnClickListener(new View.OnClickListener() {

	  				@Override
	  				public void onClick(View v) {
	  					mp.start();//הפעלת מוזיקה
	  					languagd="hebrew";
	  					Toast.makeText(MainActivity.this, languagd, Toast.LENGTH_LONG).show();
	  					setLocale("iw");
	  					//Intent intent = new Intent(MainActivity.this,MainActivity.class);
	  					//intent.putExtra("language","english");
	  					
	  					//startActivity(intent);	
	  					 dismiss();	
	              
	  				}	
	  			});
	  			
	  		
	  		}
	  		public String getLanguagd() {
	  			return languagd;
	  		}
	  		public void setLanguagd(String languagd) {
	  			this.languagd = languagd;
	  		
	        }
	  	}					
	  	//-------------Locale ----------
	  		 public void setLocale(String lang) { 
	  		    	Locale myLocale = new Locale(lang); 
	  		    	Resources res = getResources(); 
	  		    	DisplayMetrics dm = res.getDisplayMetrics(); 
	  		    	Configuration conf = res.getConfiguration(); 
	  		    	conf.locale = myLocale; 
	  		    	res.updateConfiguration(conf, dm); 
	  		    	Intent refresh = new Intent(this, MainActivity.class); 
	  		    	refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  		    	startActivity(refresh); 
	  		    	} 
	
	  		 
	  		 
	  			  		 
	  	

}
