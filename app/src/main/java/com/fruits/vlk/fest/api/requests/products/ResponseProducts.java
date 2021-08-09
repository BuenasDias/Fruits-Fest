package com.fruits.vlk.fest.api.requests.products;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProducts {

	@SerializedName("list_items")
	private List<ListItems> listItems;

	public List<ListItems> getListItems(){
		return listItems;
	}
}