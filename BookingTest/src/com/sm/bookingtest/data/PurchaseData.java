package com.sm.bookingtest.data;

public class PurchaseData 
{
	String indx="";
	String id="";
	String book_name="";
	String image_url="";
	String purchase_state="";
	

	public String getPurchase_state() {
		return purchase_state;
	}

	public void setPurchase_state(String purchase_state) {
		this.purchase_state = purchase_state;
	}

	public PurchaseData(){
		
	}
	
	public PurchaseData(String indx, String id, String book_name,
			String image_url) {
		super();
		this.indx = indx;
		this.id = id;
		this.book_name = book_name;
		this.image_url = image_url;
	}
	
	
	public String getIndx() {
		return indx;
	}
	public void setIndx(String indx) {
		this.indx = indx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	




}
