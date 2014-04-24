package com.example.badner.my.movies;


import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import android.app.Activity;

import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	//הצרה על משתנה מסוג מערךליסט מסוג של האובקיט שבנינו 
	//בקלאס סרט מאינטרנט
		private ArrayList<Movie> ListObject;
		//יצרת אוביקט מקשר
		private Activity context;
	
		
	
	
	public ArrayList<Movie> getListObject() {
			return ListObject;
		}

		public void setListObject(ArrayList<Movie> listObject) {
			ListObject = listObject;
		}

		public Activity getContext() {
			return context;
		}

		public void setContext(Activity context) {
			this.context = context;
		}

	public SearchAdapter() {
			super();
		}

	public SearchAdapter(ArrayList<Movie> listObject,
				Activity context) {
			super();
			ListObject = listObject;
			this.context = context;
		}

	
	
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

	
	
	
	  @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        
	    	View row = convertView;
	        ImageHolder holder = null;//מחזיק שורה
	       
	        if(row == null)
	        {
	           //מקשרים לתצוגה
	        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	           //קישור לשורה בקמל
	            row = inflater.inflate(R.layout.row_movie_search, parent, false);
	           
	            //המחזיק אליו פונים לשני המקומות
	            holder = new ImageHolder();
	           //בשביל הטקסט
	            holder.ObjectName = (TextView)row.findViewById(R.id.textViewnamemovie);
	            holder.ObjectId=(TextView)row.findViewById(R.id.textViewidmovie);
	            
	            
	            //בשביל הפונוט
	            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/CinzelDecorative-Regular.ttf");
	            holder.ObjectName. setTypeface(font);
	            
	            // Objectliner בשביל צבע שורה
	            holder.Objectliner=(LinearLayout)row.findViewById(R.id.LinearLayoutrow);
	            //כל שורה זוגית נצבע בלבן
	            if(position%2==0){
	            	holder.Objectliner.setBackgroundColor(0xffffffff);
	            }
	            
	         
	            
	            //בשביל התמונה
	            holder.Objectimage = (ImageView)row.findViewById(R.id.imageViewpic);
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ImageHolder)row.getTag();
	        }
	       //לתמונה מהקלאס
	        ImageLoaderConfiguration config= new ImageLoaderConfiguration.Builder(context).build();
	        ImageLoader.getInstance().init(config);
	        DisplayImageOptions option=new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
	        
	        //קישור לתוכן 
	        Movie movie = ListObject.get(position);
	        holder.ObjectName.setText(movie.getSubject());
	       holder.ObjectId.setText(movie.getMovieid());
	        
	       //בשביל התמונה
	       ImageLoader.getInstance().displayImage(movie.getUrlImage(), holder.Objectimage,option);
	       
	       
	       //לאנמציה
           Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
           row.startAnimation(animation);
	   			
	       
	       return row;
	       
	    }
	   
	    static class ImageHolder
	    {
	       
	    	public TextView ObjectName;	
			public TextView ObjectId;//להוסיף ךקךאס הגדול	
			ImageView Objectimage;
			LinearLayout Objectliner;
	    	
	    }
		
	    

}
