package com.baidu.paddle.lite.demo.image_classification.Bean;

public class Product_Bean {
	private int id;
	private String typename, price;
	private int icon;

	public Product_Bean(String typename, int icon,String price) {
		super();
		this.typename = typename;
		this.icon = icon;
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}
