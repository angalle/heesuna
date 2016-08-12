package com.sm.bookingtest.data;

public class SellData
{	
	String indx;
	String id;
	String book_name; // 책제목
	String hope_price; // 원가
	String origin_price; // 판매가격
	String state; // 상태설명
	String sell_picture; // 사진
	String sell_state;
	
	public String getSell_state() {
		return sell_state;
	}


	public void setSell_state(String sell_state) {
		this.sell_state = sell_state;
	}


	public SellData(){
		
	}
	
	
	public SellData(String indx, String id, String book_name,
			String hope_price, String origin_price, String state,
			String sell_picture) {
		super();
		this.indx = indx;
		this.id = id;
		this.book_name = book_name;
		this.hope_price = hope_price;
		this.origin_price = origin_price;
		this.state = state;
		this.sell_picture = sell_picture;
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
	public String getHope_price() {
		return hope_price;
	}
	public void setHope_price(String hope_price) {
		this.hope_price = hope_price;
	}
	public String getOrigin_price() {
		return origin_price;
	}
	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSell_picture() {
		return sell_picture;
	}
	public void setSell_picture(String sell_picture) {
		this.sell_picture = sell_picture;
	}

	
}
