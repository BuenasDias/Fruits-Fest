package com.fruits.vlk.fest.api.requests.newProducts;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CountriesItem{

	@SerializedName("country")
	private String country;

	@SerializedName("products")
	private List<ProductsItem> products;

	public String getCountry(){
		return country;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}
}