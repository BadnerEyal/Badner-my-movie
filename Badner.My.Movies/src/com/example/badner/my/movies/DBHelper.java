package com.example.badner.my.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


/**    ����� ���� �� ���� ������� ����� ����� ��� ���� ���� ���� ��*/
public class DBHelper extends SQLiteOpenHelper{

	             //���� ���� �������    ��� �� ���� ������� �� ���
				//Activity Object ;  DB name      // null               ; DB version
	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		
	}

	//���� ����� �� ����� ����� ������� �� ���� ����� ���� �������
	// will run only once
	//BLOB ����� ����� ����� ����� ������
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//���� ���� ��� ����� 
		String cmd = "CREATE TABLE movies (_id INTEGER PRIMARY KEY , subject TEXT ," +
				" body TEXT ,myrank TEXT ,incomemanuallynetwork TEXT,urlimage TEXT,img BLOB);";
		
		try {
			db.execSQL(cmd);
		} catch (SQLiteException e) {
			e.getCause();
		}
		
		
	}

	
	// for case of changing table structure
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
