package com.suhruth.dcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.suhruth.dcc.model.CostResponse;
import com.suhruth.dcc.model.ProductRequest;
import com.suhruth.dcc.service.DeliveryCostCalcService;

@RestController
public class DeliveryCostCalcController {

	@Autowired
	DeliveryCostCalcService service;

	@PostMapping("/calculate-cost")
	ResponseEntity<CostResponse> calcDeliveryCost(@RequestBody ProductRequest productRequest) {
		return ResponseEntity
				.ok(new CostResponse(DeliveryCostCalcService.findMinimumCost(productRequest.getProducts())));
	}

}
