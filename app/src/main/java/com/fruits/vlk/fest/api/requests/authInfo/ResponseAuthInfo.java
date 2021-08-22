package com.fruits.vlk.fest.api.requests.authInfo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAuthInfo {

	@SerializedName("bg_picture")
	private String urlPicture;

	@SerializedName("county_default")
	private String countryDefault;

	@SerializedName("counties")
	private List<String> countiesCode;

	public String getUrlPicture(){
		return urlPicture;
	}

	public String getCountryDefault() {
		return countryDefault;
	}

	public List<String> getCountiesCode(){
		return countiesCode;
	}
}