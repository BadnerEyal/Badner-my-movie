package com.example.badner.my.movies;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/**  ���� ���� ������� ��������� ������ �����*/   

public class DBHandler {
	//������ ������ ��� ����� ����� �������
	// give us the ability to open db for read or write
	private DBHelper helper;  
	
	//���� ��� ���� ���� ��� �� ���� ������� ���  ���� 1
	public DBHandler(Context con) {
		helper = new DBHelper(con, "movies_131db.db", null, 1);
	}

	//movies  ����� ���� �����
	//������� ������ ���
	public void addMovie(String subject,String body,String myrank,
			String incomemanuallynetwork,
			String urlimage,byte[] img){
		//����� ����� ������
		SQLiteDatabase db = helper.getWritableDatabase();
		
		
		try {
			//������ ������ ��� �� �� �����
			//tag is column name , value is value
			ContentValues cv = new ContentValues();
			cv.put("subject", subject);
			cv.put("body", body);
			cv.put("myrank", myrank);
			cv.put("incomemanuallynetwork", incomemanuallynetwork);
			cv.put("urlimage", urlimage);
			cv.put("img", img);
			 // ���� ����� ������ ���� 
			//CV ���� ����� ����
			//func for insert query
			db.insertOrThrow("movies", null, cv);
			
			
		} catch (SQLiteException e) {
			// TODO: handle exception
		}finally{
			if(db.isOpen())
				db.close();//����� ����� ������
		}
		
	}
	
	
		//������� ������ ��� ��� �����
		public void addMovieNoPic(String subject,String body,String myrank,
				String incomemanuallynetwork,
				String urlimage){
			//����� ����� ������
			SQLiteDatabase db = helper.getWritableDatabase();
			
			
			try {
				//������ ������ ��� �� �� �����
				//tag is column name , value is value
				ContentValues cv = new ContentValues();
				cv.put("subject", subject);
				cv.put("body", body);
				cv.put("myrank", myrank);
				cv.put("incomemanuallynetwork", incomemanuallynetwork);
				cv.put("urlimage", urlimage);
				
				 // ���� ����� ������ ���� 
				//CV ���� ����� ����
				//func for insert query
				db.insertOrThrow("movies", null, cv);
				
				
			} catch (SQLiteException e) {
				// TODO: handle exception
			}finally{
				if(db.isOpen())
					db.close();//����� ����� ������
			}
			
		}
	
	
	
	//����� �� ����� �� 
	public void deleteAll(){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			
			db.delete("movies", null, null);
			
		} catch (SQLiteException e) {
			// TODO: handle exception
		}finally{
			if(db.isOpen())
				db.close();
		}
		
		
		
	}
	
	//����� ��� ��� 
	//id 
	public void deleteMovie(String id){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			String [] arg  = {id};  // arg is String array with one element (id)
			db.delete("movies", "_id=?", arg);
			
		} catch (SQLiteException e) {
			e.getCause();
		}finally{
			if(db.isOpen())
				db.close();
		}
		
		
		
	}

	
	// ����� �� ����� ��� ���� ���� 
		public void updateMovie(String id,String subject,String body,String myrank,
				String incomemanuallynetwork,
				String urlimage,byte[] img){
			
			
			SQLiteDatabase db = helper.getWritableDatabase();
			
			try {
				
				ContentValues cv = new ContentValues();
				cv.put("subject", subject);
				cv.put("body", body);
				cv.put("myrank", myrank);
				cv.put("incomemanuallynetwork", incomemanuallynetwork);
				cv.put("urlimage", urlimage);
				cv.put("img", img);
				
				db.update("movies", cv, "_id=?", new String [] {id});
				
			} catch (SQLiteException e) {
				e.getCause(); // can use us for logs ..... 
			}finally{
				if(db.isOpen())
					db.close();
			}
		}	
	

	
	//����� ���  ��� ���� ������ ���
	public Movie getMovie(String id){
		 
		
		Movie movie=null;
		
	    // 1. get reference to readable DB
		SQLiteDatabase db = helper.getReadableDatabase();
	 
	    // 2. build query
		String [] arg  = {id};
		Cursor cursor = db.query("movies",null , "_id=?", arg, null, null, null);
		
	    // 3. if we got results get the first one
	        cursor.moveToFirst();
	 
	        
	    // 4. build contacts object
	        movie = new Movie(
	    cursor.getInt(0),
	    cursor.getString(1),
	    cursor.getString(2),
	    cursor.getString(3),
	    cursor.getString(4),
	    cursor.getString(5),
	    cursor.getBlob(6));
	    		
	    // 5. return contacts
	    return movie;//����� ������ ���
	}
	
	
	
	// ���� �� ����� ����� ���� 
	//cursor ����� ������
	public Cursor getAllMovies(){
		Cursor cursor = null;
	
		SQLiteDatabase db = helper.getReadableDatabase();
		
		try {
			cursor = db.query("movies", null, null, null, null, null, null);
		} catch (SQLiteException e) {
			e.getCause();
		}
				
		return cursor;
		
	}
	
	
	
	
	
	//����� ��� �� �� ����� ���� ���� ���� ����� ������� 
	//cursor
	
	
	public ArrayList<Movie> getAllMoviesByArrayList(){
		Cursor cursor = null;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		
		try {
			cursor = db.query("movies", null, null, null, null, null, null);
		} catch (SQLiteException e) {
			e.getCause();
		}
				
		
		// loop to get rows from cursor and add them to  array list <Contacts> type 
		ArrayList<Movie> list = new ArrayList<Movie>();
		// ��� ���� ������ �� ������ ���� �� ����� ���� ���� ������� �� ������� ��� ������
		//���� ���� ����  �� ��� ������
		while(cursor.moveToNext()){
		    // ���� ����� ����� 
			//������� �� ������ ���� �� ��� ������� 
			int id = cursor.getInt(0);
			String subject = cursor.getString(1);
			String body = cursor.getString(2);
			String myrank = cursor.getString(3);
			String incomemanuallynetwork = cursor.getString(4);
			String urlimage = cursor.getString(5);
			
			byte[] img = cursor.getBlob(6);
			
			
			list.add(new Movie(id, subject, body, myrank,incomemanuallynetwork,urlimage,img));
		
		}

		return list;//����� �� ������ ���� 
		
	}
	
	

	
	
}
