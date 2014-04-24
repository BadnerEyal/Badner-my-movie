package com.example.badner.my.movies;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/**  יצרת בסיס הנתונים ופונקציות לקריאה כתיבה*/   

public class DBHandler {
	//אוביקט שמאפשר לנו לפנות לבסיס הנתונים
	// give us the ability to open db for read or write
	private DBHelper helper;  
	
	//בנאי הוא בעצם מיצר לנו את בסיס הנתונים בשם  גרסה 1
	public DBHandler(Context con) {
		helper = new DBHelper(con, "movies_131db.db", null, 1);
	}

	//movies  הטבלה שלנו נקראת
	//פונקציה להוספת סרט
	public void addMovie(String subject,String body,String myrank,
			String incomemanuallynetwork,
			String urlimage,byte[] img){
		//פתיחת הטבלה לכתיבה
		SQLiteDatabase db = helper.getWritableDatabase();
		
		
		try {
			//אוביקט שיחזיק לנו את שם השדות
			//tag is column name , value is value
			ContentValues cv = new ContentValues();
			cv.put("subject", subject);
			cv.put("body", body);
			cv.put("myrank", myrank);
			cv.put("incomemanuallynetwork", incomemanuallynetwork);
			cv.put("urlimage", urlimage);
			cv.put("img", img);
			 // הכנס לטבלה אוביקט מסוג 
			//CV שהוא השדות שלנו
			//func for insert query
			db.insertOrThrow("movies", null, cv);
			
			
		} catch (SQLiteException e) {
			// TODO: handle exception
		}finally{
			if(db.isOpen())
				db.close();//סגירת הטבלה לכתיבה
		}
		
	}
	
	
		//פונקציה להוספת סרט ללא תמונה
		public void addMovieNoPic(String subject,String body,String myrank,
				String incomemanuallynetwork,
				String urlimage){
			//פתיחת הטבלה לכתיבה
			SQLiteDatabase db = helper.getWritableDatabase();
			
			
			try {
				//אוביקט שיחזיק לנו את שם השדות
				//tag is column name , value is value
				ContentValues cv = new ContentValues();
				cv.put("subject", subject);
				cv.put("body", body);
				cv.put("myrank", myrank);
				cv.put("incomemanuallynetwork", incomemanuallynetwork);
				cv.put("urlimage", urlimage);
				
				 // הכנס לטבלה אוביקט מסוג 
				//CV שהוא השדות שלנו
				//func for insert query
				db.insertOrThrow("movies", null, cv);
				
				
			} catch (SQLiteException e) {
				// TODO: handle exception
			}finally{
				if(db.isOpen())
					db.close();//סגירת הטבלה לכתיבה
			}
			
		}
	
	
	
	//מחיקת כל הטבלה של 
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
	
	//מחיקת סרט לפי 
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

	
	// עדכון כל השדות לפי מספר מזהה 
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
	

	
	//מחזיר סרט  אחד בתוך אוביקט סרט
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
	    return movie;//מחזיר אוביקט סרט
	}
	
	
	
	// קבלת כל הטבלה אלינו לתוך 
	//cursor מחזיר אוביקט
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
	
	
	
	
	
	//מחזיר לנו את כל הטבלה לתוך מערך ליסט במקום לאוביקט 
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
		// אנו בעצם עוברים על מטריצה מערך דו מימדי שורה שורה ומחזרים את האיברים לפי עמודות
		//עבור שורה שורה  עד סוף השורות
		while(cursor.moveToNext()){
		    // עבור עמודה עמודה 
			//העמודות הם מספרים מאפס עד סוף העמודות 
			int id = cursor.getInt(0);
			String subject = cursor.getString(1);
			String body = cursor.getString(2);
			String myrank = cursor.getString(3);
			String incomemanuallynetwork = cursor.getString(4);
			String urlimage = cursor.getString(5);
			
			byte[] img = cursor.getBlob(6);
			
			
			list.add(new Movie(id, subject, body, myrank,incomemanuallynetwork,urlimage,img));
		
		}

		return list;//מחזיר את הרשימה מלאה 
		
	}
	
	

	
	
}
