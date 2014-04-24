package com.example.badner.my.movies;

public class Movie {

	private int id ;//���� �� ����� ���� ����� ��� �� ��� ����� ���� ����� ���� ������ ����� �������
	private String subject;//�� ���
	private String body;//����� ����
	private String myRank ;//������ �� ������
	private String incomeManuallyNetwork;//����� ����� �� �����
	private String urlImage;//����� ����� �����
	private String urlImagebig;//����� ����� ����� big
	private String movieid;//���� ��� ��� �� ���� �� ���� ����	

	private byte[] img ;//�����
	
	
	
	
	//���� ���
	public Movie() {
		super();
	}
	
	//���� ����� ������
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
