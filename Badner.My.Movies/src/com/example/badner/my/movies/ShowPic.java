package com.example.badner.my.movies;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ShowPic  extends Activity{
	
	 private Intent intent;
	 private String URL;
	 private ImageView imageView;
	 private MediaPlayer mp;
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pic);
		 imageView = (ImageView)findViewById(R.id.imageView);
		//קבלת המשתנים מהדף הראשון לתוך משתנים שלנו
				intent = getIntent();
				URL= intent.getStringExtra("tmpurl");//קבלת כותרת
				//DownloadImageTask1 t = new DownloadImageTask1(this);
		    	//t.execute(URL);
				
				 mp = MediaPlayer.create(this, R.raw.beer_can_opening);
				
				ImageView btnAdd = (ImageView)findViewById(R.id.content);
				btnAdd.setOnClickListener(new View.OnClickListener() {
					
					@Override
				public void onClick(View v) {
						mp.start();//הפעלת מוזיקה
						//לדף חיפוש סרטים 
						 Intent intent = new Intent(ShowPic.this, MainMovieNetwork.class);
							
		                 startActivity(intent);
		               //לאנמציה מעבר דף
		                 overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
						
					
				    	 
					}
				});					
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		
		  getActionBar().setTitle("Larger Image");
		
		DownloadImageTask1 t = new DownloadImageTask1(this);
    	t.execute(URL);
		
	
	}
	
	
		
		
	
}
