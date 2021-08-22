package com.fruits.vlk.fest.api.requests.newProducts;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProducts{

	@SerializedName("countries")
	private List<CountriesItem> countries;

	public List<CountriesItem> getCountries(){
		return countries;
	}
}