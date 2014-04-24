package com.example.badner.my.movies;

public class Movie {

	private int id ;//חובה זה המקשר שלנו לטבלה לפי זה נדע לאיזה שורה לפנות נבנה אוטמטי בבסיס הנתונים
	private String subject;//שם סרט
	private String body;//תיאור הסרט
	private String myRank ;//הדירוג של המשתמש
	private String incomeManuallyNetwork;//הכנסה ידנית או מהרשת
	private String urlImage;//כתובת תמונה מהרשת
	private String urlImagebig;//כתובת תמונה מהרשת big
	private String movieid;//מזהה סרט לפי זה ניקח את פרטי הסרט	

	private byte[] img ;//תמונה
	
	
	
	
	//בנאי ריק
	public Movie() {
		super();
	}
	
	//בנאי שמקבל נתונים
	public Movie(int id, String subject, String body, String myRank,
			String incomeManuallyNetwork, String urlImage, byte[] img) {
		super();
		this.id = id;
		this.subject = subject;
		this.body = body;
		this.myRank = myRank;
		this.incomeManuallyNetwork = incomeManuallyNetwork;
		this.urlImage = urlImage;
		this.img = img;
	}
	
	

	public Movie(int id, String subject, String body, String myRank,
			String incomeManuallyNetwork, String urlImage, String movieid,
			byte[] img) {
		super();
		this.id = id;
		this.subject = subject;
		this.body = body;
		this.myRank = myRank;
		this.incomeManuallyNetwork = incomeManuallyNetwork;
		this.urlImage = urlImage;
		this.movieid = movieid;
		this.img = img;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getMyRank() {
		return myRank;
	}
	public void setMyRank(String myRank) {
		this.myRank = myRank;
	}
	public String getIncomeManuallyNetwork() {
		return incomeManuallyNetwork;
	}
	public void setIncomeManuallyNetwork(String incomeManuallyNetwork) {
		this.incomeManuallyNetwork = incomeManuallyNetwork;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	
	
	public String getUrlImagebig() {
		return urlImagebig;
	}

	public void setUrlImagebig(String urlImagebig) {
		this.urlImagebig = urlImagebig;
	}

	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	
	public String getMovieid() {
		return movieid;
	}

	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}			
	
	
	
}
