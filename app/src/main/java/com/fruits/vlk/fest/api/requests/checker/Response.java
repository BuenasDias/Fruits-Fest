package com.fruits.vlk.fest.api.requests.checker;

import com.google.gson.annotations.SerializedName;

public class Response {

	@SerializedName("content")
	private int content;

	public int getContent(){
		return content;
	}
}