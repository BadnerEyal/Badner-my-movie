package com.example.badner.my.movies;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class EditingMovie extends Activity{

	
	//������ ����� ������� �������
	private DBHandler dataBase;// ������ ����� ����� ������� ������ �������
	private String id="";//���� �����
	private String subject="";//�� ���
	private String body;//����� ���
	private String myrank ;//����� �� ������
	private String incomemanuallynetwork;//����� ���� �� �����
	private String urlimage;//����� ����� �������
	public String idmovie;//���� ���� ��� �����
	
	private String img ;//�����
	
	private EditText iEditTextmoviename;//�� ���
	private EditText iEditTextdescription;//����� ���
	private RatingBar ratingBar;	//����� ������
	
	public ImageView imageViewpic;//�����

	private ImageView btnupdating;//����� �����
	private ImageView btnSave;//����� �����
	public Movie movie;
	
	//������ ������ ���� ����� �����
	public MyTaskPic1 taskp ;//������ ����� ���� �����
	Bitmap bitmpform_net;//����� ����� ����� ����� 
	
	//����� ���� ������ �� ������ ��� ���� �����
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	public  Bitmap photo;//����� ������ �� ������ �������
	byte imgphoto[];//����� ���� ��� ������ ����� ������� �� ������
	
	MediaPlayer mp;//�������
	MediaPlayer mpclick;//���� �� �����
	
	//�������
	Typeface font ;
	Typeface fontbold;
	TextView textviewtitle;
	TextView textviewrating;
	TextView textviewdescription; 
	
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_movie);
		
		 mp = MediaPlayer.create(this, R.raw.wait);
		//����� ���� �� �����
		 mpclick = MediaPlayer.create(this, R.raw.beer_can_opening);
		
		/** ������ �������*/ 
		 font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Regular.ttf");
		 fontbold = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/CinzelDecorative-Black.ttf"); 
	    textviewtitle = (TextView) findViewById(R.id.textViewmovietitle);
		 textviewrating = (TextView) findViewById(R.id.textViewRating);
		 textviewdescription = (TextView) findViewById(R.id.textViewDescription);
		
		
		
		dataBase = new DBHandler(EditingMovie.this);//���� ������ ����
		
		iEditTextmoviename = (EditText)findViewById(R.id.editTextmoviename);
		iEditTextmoviename. setTypeface(font);
		iEditTextdescription = (EditText)findViewById(R.id.editTextdescription);
		iEditTextdescription. setTypeface(font);
		
		ratingBar = (RatingBar)findViewById(R.id.ratingBarmyratin);
		btnupdating=(ImageView)findViewById(R.id.button_updating);
		btnSave = (ImageView)findViewById(R.id.buttonok);
		
		imageViewpic=(ImageView)findViewById(R.id.imageViewButtonimg);
		
		
		
		//���� �� ���� ���� ����� ��� ������ �����
		//������ ����� ���� ������
		intent = getIntent();
		id=intent.getStringExtra("mid");
		//���� ������ ����
		subject  = intent.getStringExtra("itmpTitle");
		idmovie  = intent.getStringExtra("itmpIdMovie");
		urlimage  = intent.getStringExtra("itmpUrlImg");
		
		
		
		//protected void onResume() {
		/**��� ���� �� ��� ����� �� ��� ���� ��  */
		//����� ������ ��� ���� �� �� ���� ������
		//����� ��� ����� ��� ��������� �� ���� ��� �����
		// ��� ������ ���� �� ���� ��� ����� ��� ����
		
		
		/**����� ��� ����*/
		btnupdating.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if (id != null) {
				mpclick.start();//����� ������
				//id=id; 
				subject = iEditTextmoviename.getText().toString();
				body=iEditTextdescription.getText().toString();
				myrank=String.valueOf(ratingBar.getRating());
				//���� ��� �� ����� ������ �����
				if(photo!=null){ 
			
				//����� ����� ������ ����� ��� ����� ����� ������� �������
				//Bitmap yourImage = extras2.getParcelable("data");
				// convert bitmap to byte
	
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				//���� ������ ��� ������� ������ ���� ����� ������
				imgphoto = stream.toByteArray();
				
				 if(subject.equals("")){
					 mpclick.start();//����� ������
					 //������ �� ����� �� �� ���
					  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
					  iEditTextmoviename.setError("You must include a subject");
					   }else{
				//���� ��� ����� �������
		        //�� �����
				dataBase.updateMovie(id, subject, body, myrank, incomemanuallynetwork, urlimage, imgphoto);
				Intent intent = new Intent(EditingMovie.this, MainActivity.class);
				
				startActivity(intent); 
					   
					   }
				
				}else{
					mpclick.start();//����� ������

					// ���� �� ������ ����� ����� ��� ��� �����
					// ������ ���� �� ���� �� ����� �����		
					imgphoto=movie.getImg();
					// convert bitmap to byte
					 if(subject.equals("")){
						 //������ �� ����� �� �� ���
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						   }else{
							dataBase.updateMovie(id, subject, body, myrank, incomemanuallynetwork, urlimage, imgphoto);
							Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							
							startActivity(intent);
						   }
				     }
              
				
				}
			};
		});
	
		
		
		
		 //�� ��� ����� ����� �� ������
	    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	       
		/** ����� ����� ���� �� ������ */
		imageViewpic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mpclick.start();//����� ������
				MyDialogPic idialog = new MyDialogPic(EditingMovie.this);
				idialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;

				idialog.show();	
				
			
			}
		});
		
		
		
		
		/**����� ������ ������  */
		btnSave.setOnClickListener(new OnClickListener() {
         // ����� ����� ���� ����� 3.��� ����� 1.����� ������ �� �����
		//2.����� ����� 	
			@Override
			public void onClick(View v) {
				
				subject = iEditTextmoviename.getText().toString();
				body=iEditTextdescription.getText().toString();
				myrank=String.valueOf(ratingBar.getRating());
				//���� ��� �� ����� ������ �����
				if(photo!=null){ 
					mpclick.start();//����� ������
				//����� ����� ������ ����� ��� ����� ����� ������� �������
				//Bitmap yourImage = extras2.getParcelable("data");
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				//���� ������ ��� ������� ������ ���� ����� ������
				imgphoto = stream.toByteArray();
				
				  if(subject.equals("")){
						 //������ �� ����� �� �� ���
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						  
						   }else{
							   //���� ��� ����� �������
							   mpclick.start();//����� ������
							    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
							    Toast.makeText(EditingMovie.this," Movie add ty" , Toast.LENGTH_SHORT).show();	
							    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							
			                     startActivity(intent);  
			                   //������� ���� ��
			 					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
						   }
                    
				
				      }
			 else if (idmovie!=null) {
				   //�� �� ���� ��� ����� �� ��� ����� ����� �� ������ �����	
				 Bitmap image=bitmpform_net;
				
				 // convert bitmap to byte
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(Bitmap.CompressFormat.PNG, 100, stream);
					imgphoto = stream.toByteArray();
				 
					if(subject.equals("")){
						 //������ �� ����� �� �� ���
						  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
						  iEditTextmoviename.setError("You must include a subject");
						   }else{
							   //���� ��� ����� �������
							   mpclick.start();//����� ������
							    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
							    Toast.makeText(EditingMovie.this," Movie from net add ty" , Toast.LENGTH_SHORT).show();	
							    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
							    // ���� ��� ����� ����� ����
			                    
							    startActivity(intent); 
							  //������� ���� ��
								overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
						   }
				  }
				
				
				else{
					//������ �� ����� �����
					       
					
					      
					//����� ����� ������� ������
					        Bitmap image = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimagesm);

							// convert bitmap to byte
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							image.compress(Bitmap.CompressFormat.PNG, 100, stream);
							imgphoto = stream.toByteArray();
							//dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);

							
							  if(subject.equals("")){
								 //������ �� ����� �� �� ���
								  Toast.makeText(EditingMovie.this," You must include a subject" , Toast.LENGTH_SHORT).show();	
								  iEditTextmoviename.setError("You must include a subject");
								   }else{
									   //���� ��� ����� �������
									   mpclick.start();//����� ������
									    dataBase.addMovie(subject,body,myrank,incomemanuallynetwork,urlimage,imgphoto);
									    Toast.makeText(EditingMovie.this," Movie add ty" , Toast.LENGTH_SHORT).show();	
									    Intent intent = new Intent(EditingMovie.this, MainActivity.class);
									
					                     startActivity(intent);  
					                   //������� ���� ��
					 					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
								   }
							
				}
               
			}
		});
  
		/** ����� ����� ���� ��� ���� ��� ��� ����� ����*/
		ImageView ibuttoncacel=(ImageView)findViewById(R.id.buttoncacel);
		ibuttoncacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (idmovie!=null){
					mpclick.start();//����� ������
					//��� ����� ����� 
					 Intent intent = new Intent(EditingMovie.this, MainMovieNetwork.class);
						
	                 startActivity(intent);
	               //������� ���� ��
	                 overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
				}else {
					mpclick.start();//����� ������
					//��� �����
					 Intent intent = new Intent(EditingMovie.this, MainActivity.class);
						
	                 startActivity(intent); 
	               //������� ���� ��
						overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
				}
				
				
				
			}
			
		});
	}//��� ����� ���

//���� ���� �� ��� �����
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	//UI
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		
		
		
		super.onResume();
		//����� �������
		textviewtitle. setTypeface(fontbold);
		textviewrating. setTypeface(fontbold);
		textviewdescription. setTypeface(fontbold);
		
		
		/**��� ���� �� ��� ����� �� ��� ���� ��  */
		//����� ������ ��� ���� �� �� ���� ������
		//����� ��� ����� ��� ��������� �� ���� ��� �����
		// ��� ������ ���� �� ���� ��� ����� ��� ����
		
		
		if (id != null) {//�� ���� �� ���� ���� �� ����� �����
			//����� ��� ����
			//����� ����� �����
	        btnSave.setVisibility(ImageView.GONE);
			//����� ��� 
	        getActionBar().setTitle("Edit Movie");
	        
			//����� �������� ���� ���� ����� ������� ����� ������� ���
			 movie =dataBase.getMovie(id);
            //����� �� ���� �����
			iEditTextmoviename.setText(String.valueOf(movie.getSubject()));
			iEditTextdescription.setText(String.valueOf(movie.getBody())); 
			ratingBar.setRating(Float.valueOf(movie.getMyRank()));//���� �����
			
			 
			 
			//������ ���� ���� ������ �����
			byte[] theImage=movie.getImg();
	        ByteArrayInputStream imageStream = new ByteArrayInputStream(theImage);
	        Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);

	        imageViewpic.setImageBitmap(bitmapImage);
	       
	      
		
		}
		else if (idmovie!=null) {
			//�� ��� ��� ����� ��������
             // ����� ����� �����
		    
			btnupdating.setVisibility(ImageView.GONE);
			
			getActionBar().setTitle("Edit or Save Movie From Net");
			
			//������	
			
			iEditTextmoviename.setText(subject);
			//������
  
	     	 taskp = new MyTaskPic1(imageViewpic);
	   	     taskp.execute(urlimage);
			
	   	    //������
	 		//���� ������ ����� �������� ������ ���� ����� �� �� ��� ���
	 		MyTaskDescription task = new MyTaskDescription(this);
	 		//����� �� ���� ��� ����
	 		
	 		idmovie=idmovie+".json";
	 		String url1="http://api.rottentomatoes.com/api/public/v1.0/movies/"+idmovie+"?apikey=pe7kbs9aa2u3j46d2qkaavcv";

	 
	 		//����� �����
	 		task.execute(url1);
	 		    		
			
		} 
			else {
			//��� ���� ���� ������ ��� ��� ���� ����� ��� ���
		    //������
				getActionBar().setTitle("Add Your Own Movie");	
			// ����� ����� �����
		    
			btnupdating.setVisibility(ImageView.GONE);
		}

		
	
		
		
		
	}


	/** ���� ������ ������� �� ������*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case CAMERA_REQUEST:

			Bundle extras = data.getExtras();

			if (extras != null) {
			
			//����� ����� ������� ���
				  photo = (Bitmap) data.getExtras().get("data"); 
				  imageViewpic.setImageBitmap(photo);
				
				

			}
			break;
		case PICK_FROM_GALLERY:
			Bundle extras2 = data.getExtras();

			if (extras2 != null) {
				
				//����� ����� ������� ���
				  photo = (Bitmap) data.getExtras().get("data"); 
				  imageViewpic.setImageBitmap(photo);
						
				
			}

			break;
		}
	          
	   
	}

	 /** ���� ������ ����� �� ����� ���� ����  */
  	private class MyDialogPic extends Dialog{
  		
  		
  		public MyDialogPic(Context context) {
  			super(context);
  			setContentView(R.layout.dialogpicfrom);
  			setTitle("Select Gallery or Camera");
            //setCancelable(false);����� ����� ����
  			//����� ������
  			ImageView btncamera = (ImageView)findViewById(R.id.imageViewcamera);
  			btncamera.setOnClickListener(new View.OnClickListener() {

  				@Override
  				public void onClick(View v) {
  					callCamera();//���� ������� ������ �����	
  					 dismiss();
  				}	
  			});
  			
  			
  		   //����� ������
  			ImageView btngallery = (ImageView)findViewById(R.id.imageViewgallery);
  			btngallery.setOnClickListener(new View.OnClickListener() {

  				@Override
  				public void onClick(View v) {
  					callGallery();//����� ������	
  					 dismiss();	
              
  				}	
  			});

  		}		

     }
	
  	//������� ������
  	/**
	 * open camera method
	 */
	public void callCamera() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra("crop", "true");
		cameraIntent.putExtra("aspectX", 0);
		cameraIntent.putExtra("aspectY", 0);
		cameraIntent.putExtra("outputX", 150);
		cameraIntent.putExtra("outputY", 200);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);

	}

	//������ ������
	/**
	 * open gallery method
	 */

	public void callGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(
		Intent.createChooser(intent, "Complete action using")
		,PICK_FROM_GALLERY);

	}

	/**���� ����� ����� ���� */
public class MyTaskDescription extends AsyncTask<String, Void, String>{
		
	private MyDialogWait idialog;
	//private Activity activity;//���� ��� ������ ����
		
		public MyTaskDescription(Activity act ) {
			//activity = act;
		}
		
		@Override
		protected void onPreExecute() {
			//���� �� �����
		//	ProgressBar bar = (ProgressBar)activity.findViewById(R.id.progressBarwait);
		//	bar.setVisibility(ProgressBar.FOCUS_UP);
		
			 idialog = new MyDialogWait(EditingMovie.this);
				idialog.show();	
				//����� ������
				
				 mp.start();
				
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
			
			
			try {
				//����� ����� ������� ���� ���
				JSONObject json = new JSONObject(result);
			
				
				String strdetailed="";//����� ������ ���� �� ���� �� ����� �����
				
				

				if(json.has("synopsis"))//����� ��� ����
				strdetailed =json.getString("synopsis");//���� �� ����
				
				//����� ���� ����� ���� ��� �����
				if(strdetailed.equals("")){
					iEditTextdescription.setText("No description");
					}else{
				iEditTextdescription.setText(strdetailed);//���� ���� ���
				}
				idialog.dismiss();
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				

		}

		
	}

/**���� ����� ����� ���� ����� */
	 class MyTaskPic1 extends AsyncTask<String, Void, Bitmap> {
		    private final WeakReference<ImageView> imageViewReference;
		    private String data;

		    public MyTaskPic1(ImageView imageView) {
		        // Use a WeakReference to ensure the ImageView can be garbage
		        // collected
		        imageViewReference = new WeakReference<ImageView>(imageView);
		    }

		    // Decode image in background.
		    @Override
		    protected Bitmap doInBackground(String... urls) {
		    	Log.d("doInBackground", "starting download of image");
				//����� �������� ����� ����� ������ ���� ��� ����� ��� ����� ���
				Bitmap image = downloadImage(urls[0]);
				return image;
		    }

// Once complete, see if ImageView is still around and set bitmap.
		    @Override
		    protected void onPostExecute(Bitmap bitmap) {
		       
		         bitmpform_net=bitmap;
		    	if (imageViewReference != null && bitmap != null) {
		            final ImageView imageView = imageViewReference.get();
		            if (imageView != null) {
		                imageView.setImageBitmap(bitmap);
		            }
		        }
		    }
		    //������� ����� ����� ����� ����� ����� ��� ������� �����
	  		private Bitmap downloadImage(String urlString) {
	  			URL url;
	  			try {
	  				//����� ���� ����� ����� �����
	  				//���� ��� �� ��������
	  				url = new URL(urlString);
	  				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	  				InputStream is = httpCon.getInputStream();
	  				
	  				
	  				
	  				//���� �� ��� ����� ���� �� ������ �����
	  				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	  				int nRead;//����� ��� ��� ���� ���� �� ������ ������
	  			
	  				byte[] data = new byte[2048];//��� ����� �� ���
	  				
	  				
	  				// Read the image bytes in chunks of 2048 bytes
	  				
	  				// ����� ������ ������     
	  				//���� ����� ����� ���� �� ����� -1 ��� ���� �� �� ����
	  				while ((nRead = is.read(data, 0, data.length)) != -1) {
	  					buffer.write(data, 0, nRead);//����� ���
	  					
	  				}
	  				//����� ���� �� �� ������ �������
	  				buffer.flush();
	  				//���� ��� ����� ����� �� ������
	  				byte[] image = buffer.toByteArray();
	  				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
	  				return bitmap;
	  			
	  			
	  			} catch (Exception e) {//����� �� ������
	  				e.printStackTrace();
	  			}
	  			return null;
	  		} 
	 
	 }	
	
	 
		private class MyDialogWait extends Dialog{
	  		
	  		
	  		public MyDialogWait(Context context) {
				super(context);
				setContentView(R.layout.dialogpageloading);
	  			setTitle("Wait Page Loading");
	  		}		

	     }	 
}