package com.fruits.vlk.fest.api.requests.products;

import com.google.gson.annotations.SerializedName;

public class ListItems {

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