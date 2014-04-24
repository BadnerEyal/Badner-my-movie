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

	
	


		private String name="";//�� ���� �����
		private EditText namein;//��� ������� �����
		
		public ArrayList<Movie> list;//�������� ���� �����
		public ListView lv ;//������� ListView ���� �� ����� �����
			//����� ���� �����
		public SearchAdapter adapter;//����� ���� �� ������
		
		final Context context = this;
		MediaPlayer mpclick;//���� �� �����
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main_network);
			//�����
			Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Black.ttf");
			namein=(EditText)findViewById(R.id.editTextname);
			namein. setTypeface(font);
			//����� ���� �� �����
			 mpclick = MediaPlayer.create(this, R.raw.beer_can_opening);
			
			
			ImageView btmn = (ImageView)findViewById(R.id.imageButtonsearch);
			btmn.setOnClickListener(new View.OnClickListener() {
			
		
			@Override
			public void onClick(View arg0) {
				mpclick.start();//����� ������
				name=namein.getText().toString();
				//����� ��� ����� ���� ������
				String replacedToSearchname = name.replaceAll("\\s+", "+");
				//���� ������ ����� �������� ������ ���� ����� �� �� ��� ���
				MyTaskName task = new MyTaskName(MainMovieNetwork.this);
				// ����� �� �� ������ ���� ����� �� �� ������ ����� �� ����� 
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
					//����� ������ ��� ���� ���� ��� ����� ���� ������ ���� �� ���� ������
					mpclick.start();//����� ������
					//int tmpposition =position;//����� �� ���� ������
					String tmpTitle=list.get(position).getSubject();//���� �� ��� 
					String tmpIdMovie=list.get(position).getMovieid();//���� ���� ���
					String tmpUrlImg=list.get(position).getUrlImage();//���� ����� �����
					//String.valueOf(id);
					Intent intent = new Intent(MainMovieNetwork.this,EditingMovie.class);
					
					intent.putExtra("itmpTitle", tmpTitle);
					intent.putExtra("itmpIdMovie", tmpIdMovie);
					intent.putExtra("itmpUrlImg", tmpUrlImg);
					//startActivityForResult(intent,1);//��� ����� ��� ���� ��������	
				    startActivity(intent);//�� ����� �� ���� �� ��� ��� ���� ����� 
				  //������� ���� ��
	                 overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
					
					
				}
				
			});	
           
			//����� ���� ���� �� ������ �����
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {
                
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					 final int tmpposition= position;
					 mpclick.start();//����� ������
					 //������ ���� ����� ����� ����� �� �� ��
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
									String tmpurl=list.get(tmpposition).getUrlImagebig();// big ���� ����� �����
									String.valueOf(id);
									Intent intent = new Intent(MainMovieNetwork.this,ShowPic.class);
									
									intent.putExtra("tmpurl", tmpurl);
									
								    startActivity(intent);//�� ����� �� ���� �� ��� ��� ���� �����
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
					mpclick.start();//����� ������
					Intent intent = new Intent(MainMovieNetwork.this,MainActivity.class);					
					startActivity(intent);
					  //������� ���� ��
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
			//������ ���� ����
			//private ArrayList<ClassMovieNetwork> list;//�������� ���� �����
			//private ListView lv ;//������� ListView ���� �� ����� �����
			//����� ���� �����
			//private SearchAdapter adapter;//����� ���� �� ������
					
			//������ ���� ����
				
			
			
			private Activity activity;//���� ��� ������ ����
			
			public MyTaskName(Activity act ) {
				activity = act;
			}
			
			@Override
			protected void onPreExecute() {
				//���� �� �����
			//	ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
			//	bar.setVisibility(ProgressBar.FOCUS_UP);
			}
			
			
			
			
			@Override
			protected String doInBackground(String... params) {
			
						
				
				
				//  ����� ���� ������ ����� �� ������ �� ��� ���
				
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
			
				
				//���� ���� ������ 
				//����� ����
			//ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
			//	bar.setVisibility(ProgressBar.GONE);
			
				
				//���� ���� ������ �� �������
				//ArrayList<String> list = new ArrayList<String>();
				 //1
				//����� ���� ���� ���� �� ������ �����	 
				list = new ArrayList<Movie>();
				
				
				try {
					//����� ���� ������ ����� ������ ������
					//����� �� �� ������ �����
					JSONObject json = new JSONObject(result);
					
					//���� �� �� ������ �����  ����� �����
					JSONArray arr = json.getJSONArray("movies");
					//����� ����� ������ ������� ���� ������ ����
					for (int i = 0; i < arr.length(); i++) {
						//���� ����� ������ ��� 
						//����� ����� ��� ������ �� �� ���� ����� ����
						//����� ������ ��� ������
						//��� ������ ����� �������� �����
						Movie iMovieNetwork= new Movie();
						
						//���� ���� �� ����� ���� ������
						JSONObject js = (JSONObject)arr.get(i);
						//
						if(js.has("id"))
							iMovieNetwork.setMovieid(js.getString("id"));
						
						if(js.has("title"))
							iMovieNetwork.setSubject(js.getString("title"));
						
						
		                 //���� �����
						JSONObject postersJson = js.getJSONObject("posters");//����
						
						if(postersJson.has("detailed"))//����
							{iMovieNetwork.setUrlImage(postersJson.getString("thumbnail"));
						    iMovieNetwork.setUrlImagebig(postersJson.getString("detailed"));}
						  // big ���� �����
						//JSONObject postersJsonbig = js.getJSONObject("posters");//����
						
						//if(postersJson.has("detailed"))//����
							
						
						
						list.add(iMovieNetwork);//����� ������� ������
						
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//����� ������� ���� �����
				//ListView lv = (ListView)activity.findViewById(R.id.list1);
				lv = (ListView)activity.findViewById(R.id.listViewAllmovie);
				
				//3
				// the adapter will manage the list
				//���� ����� ������ ��� ����� �� ����� ����� ���� ������ 
				//����� ���� ����
				
				
				adapter = new SearchAdapter(list, activity);
				
				//listView.setAdapter(adapter);
				//adapter = new ArrayAdapter<ClassNote>(this,R.layout.list_row,list);
				//adapter = new ArrayAdapter( this , ����� ����� ,  items  ��� �������);
				
				
				//4
				// ListView ������ ����� ���� ��� �� ������ 
				lv.setAdapter(adapter);
				//������ ������ �� ������
					

			}

			

		}
		
	}

