package com.example.badner.my.movies;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;




public class AllMovieAdapter extends BaseAdapter {

	//הצרה על משתנה מסוג מערךליסט מסוג של האובקיט שבנינו 
	//בקלאס סרט מאינטרנט
	final ArrayList<Movie> ListObject;
		//יצרת אוביקט מקשר
	final Activity context;
	
 
	
		 
		// public AllMovieAdapter() {
		//		super();
		//	}	 

		 //זה מה שצריך
		 public AllMovieAdapter(ArrayList<Movie> listObject,
					Activity context) {
				super();
				ListObject = listObject;
				this.context = context;
				
			}	 
		 
		 
	public AllMovieAdapter(ArrayList<Movie> listObject,
				Activity context, int layoutResourceId) {
			super();
			ListObject = listObject;
			this.context = context;
			
		}




	public ArrayList<Movie> getListObject() {
			return ListObject;
		}




	//	public void setListObject(ArrayList<Movie> listObject) {
	//		ListObject = listObject;
	//	}




		public Activity getContext() {
			return context;
		}




	//	public void setContext(Activity context) {
	//		this.context = context;
	//	}



	
	@Override
	public int getCount() {
		// תחזיר גודל מערךליסט
		return ListObject.size();
	}

	@Override
	public Object getItem(int position) {
		// מחזיר מיקום איבר במערךליסט
		
		
		return ListObject.get(position);
	}

	@Override
	public long getItemId(int position) {
		// מחזיר מיקום לפי ID
		return position;
	}

	
	//הפונקציה מקבלת מיקום ברשימה 
			//ההשקפה הישנה
			//ההשקפה מאפשרת לנו יכולת ליצור שורות לא דומות הומגניות אך זה לא במקרה שלנו
			
			//מי ההורה יותר נכון מסגרת הרשימה
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View row = convertView;
        ImageHolder holder = null;//מחזיק שורה
       
        if(row == null)
        {
           //מקשרים לתצוגה
        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
           //קישור לשורה בקמל
            row = inflater.inflate(R.layout.row_movie, parent, false);
            
           
            //המחזיק אליו פונים לשני המקומות
            holder = new ImageHolder();
           //בשביל הטקסט
            holder.Objectsubject = (TextView)row.findViewById(R.id.textViewnamemovie);
            holder.ObjectratingBar=(RatingBar)row.findViewById(R.id.ratingBarmyrat);
            
            //בשביל הפונוט
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/CinzelDecorative-Regular.ttf");
            holder.Objectsubject. setTypeface(font);
            
            // Objectliner בשביל צבע שורה
            holder.Objectliner=(LinearLayout)row.findViewById(R.id.LinearLayoutallmovie);
            //כל שורה זוגית נצבע בלבן
            if(position%2==0){
            	holder.Objectliner.setBackgroundColor(0xffffffff);
            	
            };
            
            
            //בשביל התמונה
            holder.Objectimage = (ImageView)row.findViewById(R.id.imageViewpic);
            row.setTag(holder);
            
           
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
       
        //קישור לתוכן 
        Movie movie = ListObject.get(position);
        holder.Objectsubject.setText(movie.getSubject());
       holder.ObjectratingBar.setRating(Float.valueOf(movie.getMyRank()));
        
       
    
      
       
        //convert byte to bitmap take from contact class
        //לתמונה המרה
       byte[] outImage=movie.getImg();
       ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
       Bitmap theImage = BitmapFactory.decodeStream(imageStream);
       holder.Objectimage.setImageBitmap(theImage);
      
     //לאנמציה
       Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
       row.startAnimation(animation);
		
       
       return row;
       
    }
   
    static class ImageHolder
    {
       
    	public TextView Objectsubject;	
    	public ImageView Objectimage;
    	private RatingBar ObjectratingBar;	//משתנה לדירוג
    	LinearLayout Objectliner;
    }
	
		

}
