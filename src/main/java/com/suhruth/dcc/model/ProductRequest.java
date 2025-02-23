package com.suhruth.dcc.model;

import java.util.Map;

public class ProductRequest {
	
	private Map<String, Integer> products;

	public ProductRequest(Map<String, Integer> products) {
		super();
		this.products = products;
	}

	public Map<String, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<String, Integer> products) {
		this.products = products;
	}
	
	
	
}
