package com.fruits.vlk.fest.api.requests.newProducts;

import com.google.gson.annotations.SerializedName;

public class ProductsItem{

	@SerializedName("image")
	private String image;

	@SerializedName("url")
	private String url;

	public String getImage(){
		return image;
	}

	public String getUrl(){
		return url;
	}
}