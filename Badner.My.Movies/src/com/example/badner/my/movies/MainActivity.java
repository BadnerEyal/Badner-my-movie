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
	//������ ������ ���
		// Within which the entire activity is enclosed
			private DrawerLayout mDrawerLayout;

			// ListView represents Navigation Drawer
			private ListView mDrawerList;

			// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
			private ActionBarDrawerToggle mDrawerToggle;

			// Title of the action bar
			private String mTitle = "";
	        
			//������ ������ ����� ������
			private SharedPreferences pref;
			
			//������ ������ �����
			private ShareActionProvider myShareActionProvider;
		
			//������ ����� ������� �������
			private DBHandler dataBase;// ������ ����� ����� ������� ������ ������� 
			
			//����� ������ ���� ������ ����� �����
			private ArrayList<Movie> list;
			private ListView lv;
			private AllMovieAdapter iadapter;//����� ���� �� ������
			
			 
			private String mid;// ���� ���� �� ���� ����� �������
			
			private MediaPlayer mp;
			
			
	  @SuppressLint("NewApi")
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {

				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
	            
				//����� ���� �� �����
				 mp = MediaPlayer.create(this, R.raw.beer_can_opening);
				
				
				
				/**���� ������*/
				dataBase = new DBHandler(MainActivity.this);//���� ������ ����
				//����� ������ ���� ������ ����� �����
				list  =   dataBase.getAllMoviesByArrayList();
				//String name="ggg";
				//list  =   dataBase.getAllMoviesByNAMEArrayList(name);
				
				lv = (ListView)findViewById(R.id.listViewAllMovie);
				iadapter = new AllMovieAdapter( list,MainActivity.this);
				
				lv.setAdapter(iadapter);
				
				
				//���� ���� �� ������
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						mp.start();//����� ������
                      
						Movie movie = list.get(position);
						
						 mid=String.valueOf(movie.getId());//�� ����� ������ ���� ����� �����
						Intent intent1 = new Intent(MainActivity.this,EditingMovie.class);
						//����� �� ���� �����
						intent1.putExtra("mid",mid);
						startActivity(intent1);
						//������� ���� ��
						overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                       
						
					}
				});
				
				//����� ����� ������ ���� �� ������ ����� �� �����
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
					
						mp.start();//����� ������
						Movie movie = list.get(position);
						//important !!! position is not id !!!!!!!
						//Toast.makeText(MainActivity.this, position + " "+Contact.getId(), Toast.LENGTH_SHORT).show();	
					    
						 mid=String.valueOf(movie.getId());//�� ����� ������ ���� ����� �����
						 registerForContextMenu(lv);// ����� ����� ��
						
						return false;
					}
				});			

		
					
				
				/** ����� ��� ���� �� �����*/
				ImageView btnAdd = (ImageView)findViewById(R.id.imageViewaddmovie);
				btnAdd.setOnClickListener(new View.OnClickListener() {
					
					@Override
				public void onClick(View v) {
						mp.start();//����� ������
			
						//���� ������ ���� ������ 
						MyDialogaddmovie idialogadd = new MyDialogaddmovie(MainActivity.this);
						//������� ������ ����� �� �����
						idialogadd.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
						idialogadd.show();
					
				    	 
					}
				});			
				
				
				
				
	//////////************////////////////////////			
			/**���� �� ����� ��� ������ ������ ����� ����� ����� ������	*/
				//���� 
				pref = getSharedPreferences("login", MODE_PRIVATE);
				
				//��� ������ ���� ��� ����� ������ ����� ��� ���� �����
				boolean enter = pref.getBoolean("enter", true);
				
				if(enter)
				{
					// ����� ����� ���� ����� ��� ������� ���� ����
					//����� ������� ���� �� �����
					
					//����� ����� 
					SharedPreferences.Editor edit = pref.edit();
					edit.putBoolean("enter", false);
					
					edit.commit();
					// ������� ���� �� �����
					
					 
					
					MyDialogname dlgname = new MyDialogname(this);
					//������� ������ ����� �� �����
					dlgname.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
					dlgname.show();
					
					
				}
				
				//���� �� ������ ������ ����� ���� ������
				String username =pref.getString("name", "").toString();
				
				/**���� ������ ���� �� ������ ������ */
				//Eyal Film Library  
				//�� ������ ������ �� ����� ��� ����� �� ���� �����
				mTitle = username+" Film Library";
				getActionBar().setTitle(mTitle);

				// Getting reference to the DrawerLayout
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

				mDrawerList = (ListView) findViewById(R.id.drawer_list);

				// Getting reference to the ActionBarDrawerToggle
				mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
						R.drawable.ic_drawer, R.string.drawer_open,
						R.string.drawer_close) {

	/** ���� ������ ��� ������  */
					
	  public void onDrawerClosed(View view) {
						
	     getActionBar().setTitle(mTitle);
	     invalidateOptionsMenu();

					}

	/** ���� ������ ���� �� ������ */
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
	    
					
		@Override//����� ������ ���
		public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					// ���� ���� �� ������� ������ ������
					//String[] menuItems = getResources().getStringArray(R.array.menus);

					// Currently selected river
					//mTitle = menuItems[position];

			         //����� �������� ����� ������ ���� �� ����� ������
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
	  /** ��� ���� ��� */

	  
	  
	  
	  
	  /** �������� ������ ���*/
	  /** ������� ����� ������ �� ����� */
	    protected void menuSelection(int position) {
	  	//������� ����� ������ ����� �� ����� �� ����� ��� �����
	  			
	  	switch (position) {
	  			case 0:
	  				
	  			// ������� ���� �� �����
					MyDialogname dlgname = new MyDialogname(this);
					//������� ������ ����� �� �����
					dlgname.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
					dlgname.show();
	  				
	  				break;
	  			case 1:
	  			   //������ ������
	  				
	  				//���� ������ ���� ������ 
	  				MyDialog idialog = new MyDialog(MainActivity.this);
	  				//������� ������ ����� �� �����
	  				idialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
	  				idialog.show();
	  				 
	  				
	  				break;
	  			case 2:
	  				//����� ��� ����
	  				Intent intent = new Intent(MainActivity.this,EditingMovie.class);
					
					//intent.putExtra("iName", Name );
					
					startActivity(intent); 
					//������� ���� ��
					overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	  				
	  				Toast.makeText(MainActivity.this,"Add Your Movie" , Toast.LENGTH_SHORT).show();
	  				break;
	  			case 3:
	  				//����� ��� ���������
                     Intent intentn = new Intent(MainActivity.this,MainMovieNetwork.class);
					
					//intent.putExtra("iName", Name );
					
					startActivity(intentn); 
					//������� ���� ��
					overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	  				
	  				Toast.makeText(MainActivity.this," Add Web Movie" , Toast.LENGTH_SHORT).show();
	  				break;
	  			case 4:
	  				//����� �� ������
	  				
	  				 
								
								dataBase.deleteAll();
					  			//����� ���� ����� ��� ����� �� ������ ���� �����
									list  =   dataBase.getAllMoviesByArrayList();
									lv = (ListView)findViewById(R.id.listViewAllMovie);
									iadapter = new AllMovieAdapter( list,MainActivity.this);
									
									lv.setAdapter(iadapter);
					  				Toast.makeText(MainActivity.this,"Deleted All Movies" , Toast.LENGTH_SHORT).show();
					  				
					  				//����� ������� ���� ����� �� �����
					  				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					  			
					  				v.vibrate(200);
					  		
					  			   // v.cancel();
								
							
	

	  				break;
	  			case 5:
	  				
	  				MyDialogabot iMyDialogabot = new MyDialogabot(MainActivity.this);
	  			//������� ������ ����� �� �����
	  				iMyDialogabot.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
	  				
	  				iMyDialogabot.show();
	  				Toast.makeText(MainActivity.this,"Thank you for using apps" , Toast.LENGTH_SHORT).show();

	  				break;
	  			case 6:
	  			    //����
	  				Toast.makeText(MainActivity.this,"Goodbye" , Toast.LENGTH_SHORT).show();
	  				Intent intentout = new Intent(Intent.ACTION_MAIN);
	  				intentout.addCategory(Intent.CATEGORY_HOME);
	  				intentout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  				startActivity(intentout);
	  			//������� ���� ��
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	  				

	  				break;
	  			default:
	  				break;
	  			}
	  		}
	  	 
	  @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	  	//���� ���� onResume
	  	//����� �����  
	  	//������ ������ ������ ����� ��� ������� �� ���� ��� �� ���� ��. �� �� ��, ���� ������
	  	//FragmentActivity
	  	super.onPostCreate(savedInstanceState);
	  			mDrawerToggle.syncState();
	  		}

	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	  	//���� ��� ��� ����� ������ ��������� ��� ����	
	  	   if (mDrawerToggle.onOptionsItemSelected(item)) {
	  				return true;
	  			}
	  			//return super.onOptionsItemSelected(item);
	  			
	  			
	  				
	  				switch (item.getItemId()) {
	  				case R.id.action_settings:
	  					//����� ����� ����� ��
	  					mDrawerLayout.openDrawer(mDrawerList);
	  					 
	  					Toast.makeText(MainActivity.this, "Menu open", Toast.LENGTH_SHORT).show();
	  					break;
	  				
	  		
	  				default:
	  					break;
	  				}
	  				
	  				return super.onOptionsItemSelected(item);
	  			
	  			
	  			
	  		}

	  /** ���� ��� ��� ���� ������  ������ ���  */
	  		@Override
	   public boolean onPrepareOptionsMenu(Menu menu) {
	  			// �� ������ �����, ������ ����� ����� ���� ������ �����
	  			//�� ������ ���� �� ������ �� ������ ����� �����
	  			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	              //������� ���� ����
	  			menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	  			return super.onPrepareOptionsMenu(menu);
	  		}

	  		
	  ///����� ��� 		
	  void refresh(){
	  			Intent refresh = new Intent(this, MainActivity.class); 
	  			startActivity(refresh); 
	  		//������� ���� ��
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	  };   
	  
	 /**������ ����� ����� ����� ������ */
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

		
		
		
		
		
		
		/**   ������ ���� ������ ����� ����� ����� */	
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
				//����� �� ���� �����
				intent1.putExtra("mid",mid);
				startActivity(intent1);
				//FLAGContextMenu=1;
				Toast.makeText(MainActivity.this, "edit movie", Toast.LENGTH_SHORT).show();
				break;
			case R.id.action_deletemovie:
				 dataBase.deleteMovie(mid);	
				Toast.makeText(MainActivity.this, "deletemovie", Toast.LENGTH_SHORT).show();
				
				//����� ���� ����� ��� ����� �� ������ ���� �����
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
		
		
		/**���� ������ �� ����� �������� */
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
				
		
		/**���� ����� ����� ��� ���� �� �����*/   
		private class MyDialogaddmovie extends Dialog{
      
			public MyDialogaddmovie(Context context) {
				super(context);
				setContentView(R.layout.dialogaddmovie);
				setTitle("Choose how to add a movie");
				
				//�����
				ImageView enter_net=(ImageView)findViewById(R.id.imageViewnet);
				enter_net.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//����� ������
					Toast.makeText(MainActivity.this, "You have chosen to net add", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainActivity.this,MainMovieNetwork.class);
	
					startActivity(intent);  
					dismiss();
					//������� ���� ��
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					
				}
			});
			
				
		   //����
			ImageView enter_manl=(ImageView)findViewById(R.id.imageViewmanl);
			enter_manl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//����� ������
					Toast.makeText(MainActivity.this, "You have chosen to manually add", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainActivity.this,EditingMovie.class);
	
					startActivity(intent);  
					dismiss();
					//������� ���� ��
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					
				}
			});
			}
		}	
		
		
		
		
			
		/**���� ����� �� �����*/   
		private class MyDialogname extends Dialog{
        // ���� �� ����� ������ ���� �����
			public MyDialogname(Context context) {
				super(context);
				setContentView(R.layout.dialogname);
				setTitle("Enter a New Username");
				
				
				ImageView enter=(ImageView)findViewById(R.id.buttonenter);
				enter.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();//����� ������
					TextView newname=(TextView)findViewById(R.id.editTextusername);
					String name=newname.getText().toString();
					
					SharedPreferences.Editor edit = pref.edit();

					edit.putString("name", name);
					edit.commit();
					Toast.makeText(MainActivity.this, "Name Changed", Toast.LENGTH_LONG).show();
					
					 refresh();//����� ��� ��� ������ ��� ����
					dismiss();
					
				}
			});
			
			}

				
		}
		
		
	   /** ���� ������ ��� ���� ����  */
	  	private class MyDialog extends Dialog{
	  		public String languagd = "";
	  		
	  		public MyDialog(Context context) {
	  			super(context);
	  			setContentView(R.layout.dialogln);
	  			setTitle("Menu Hebrew or English");
	  			//����� ������
	  			ImageView buttonen1 = (ImageView)findViewById(R.id.buttonen);
	  			buttonen1.setOnClickListener(new View.OnClickListener() {

	  				@Override
	  				public void onClick(View v) {
	  					mp.start();//����� ������
	  					//����� ��� ��� ������
	  					languagd="english";
	  					Toast.makeText(MainActivity.this, languagd, Toast.LENGTH_LONG).show();
	  					setLocale("en");
	  					//Intent intent = new Intent(MainActivity.this,MainActivity.class);
	  					//intent.putExtra("language","english");
	  					
	  					//startActivity(intent);	
	  					 dismiss();
	  				}	
	  			});
	  			
	  			
	  		   //����� ������
	  			ImageView buttonhb1 = (ImageView)findViewById(R.id.buttonhb);
	  			buttonhb1.setOnClickListener(new View.OnClickListener() {

	  				@Override
	  				public void onClick(View v) {
	  					mp.start();//����� ������
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
