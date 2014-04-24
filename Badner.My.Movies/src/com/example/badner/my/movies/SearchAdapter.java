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

	//���� �� ����� ���� �������� ���� �� ������� ������ 
	//����� ��� ��������
		private ArrayList<Movie> ListObject;
		//���� ������ ����
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
		// ����� ���� ��������
		return ListObject.size();
	}

	@Override
	public Object getItem(int position) {
		// ����� ����� ���� ���������
		
		
		return ListObject.get(position);
	}

	@Override
	public long getItemId(int position) {
		// ����� ����� ��� ID
		return position;
	}

	
	
	
	  @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        
	    	View row = convertView;
	        ImageHolder holder = null;//����� ����
	       
	        if(row == null)
	        {
	           //������ ������
	        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	           //����� ����� ����
	            row = inflater.inflate(R.layout.row_movie_search, parent, false);
	           
	            //������ ���� ����� ���� �������
	            holder = new ImageHolder();
	           //����� �����
	            holder.ObjectName = (TextView)row.findViewById(R.id.textViewnamemovie);
	            holder.ObjectId=(TextView)row.findViewById(R.id.textViewidmovie);
	            
	            
	            //����� ������
	            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/CinzelDecorative-Regular.ttf");
	            holder.ObjectName. setTypeface(font);
	            
	            // Objectliner ����� ��� ����
	            holder.Objectliner=(LinearLayout)row.findViewById(R.id.LinearLayoutrow);
	            //�� ���� ����� ���� ����
	            if(position%2==0){
	            	holder.Objectliner.setBackgroundColor(0xffffffff);
	            }
	            
	         
	            
	            //����� ������
	            holder.Objectimage = (ImageView)row.findViewById(R.id.imageViewpic);
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ImageHolder)row.getTag();
	        }
	       //������ ������
	        ImageLoaderConfiguration config= new ImageLoaderConfiguration.Builder(context).build();
	        ImageLoader.getInstance().init(config);
	        DisplayImageOptions option=new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
	        
	        //����� ����� 
	        Movie movie = ListObject.get(position);
	        holder.ObjectName.setText(movie.getSubject());
	       holder.ObjectId.setText(movie.getMovieid());
	        
	       //����� ������
	       ImageLoader.getInstance().displayImage(movie.getUrlImage(), holder.Objectimage,option);
	       
	       
	       //�������
           Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
           row.startAnimation(animation);
	   			
	       
	       return row;
	       
	    }
	   
	    static class ImageHolder
	    {
	       
	    	public TextView ObjectName;	
			public TextView ObjectId;//������ ����� �����	
			ImageView Objectimage;
			LinearLayout Objectliner;
	    	
	    }
		
	    

}
