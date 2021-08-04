package com.compasso.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.api.controllers.exceptions.ExceptionProductController;
import com.compasso.api.model.Product;
import com.compasso.api.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
		
	@PostMapping("")
	public ResponseEntity<Object> newProduct(@RequestBody Product product) {
		try {
			Product obj = productRepository.save(product);
			return ResponseEntity.status(201).body(obj);
		}
		catch (Exception e) {
			ExceptionProductController exceptionProductController = new ExceptionProductController(product);
			return ResponseEntity.badRequest().body(exceptionProductController.checkingProductParameters());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProducttById(@PathVariable String id, @RequestBody Product oldProduct) {
		Product newProduct = null;
		try {
			newProduct = productRepository.findById(id);
			
			newProduct.setDescription(oldProduct.getDescription());
			newProduct.setName(oldProduct.getName());
			newProduct.setPrice(oldProduct.getPrice());
			productRepository.save(newProduct);
			
			return ResponseEntity.ok().body(newProduct);
		}
		catch (Exception e) {
			if(newProduct == null) {
				return ResponseEntity.notFound().build();
			}
			ExceptionProductController exceptionProductController = new ExceptionProductController(oldProduct);
			return ResponseEntity.badRequest().body(exceptionProductController.checkingProductParameters());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable String id) {
		try {
			Product obj = productRepository.findById(id);
			if(obj == null)
				return ResponseEntity.notFound().build();
			return ResponseEntity.ok().body(obj);
		}
		catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("")
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Product>> getProductByParams(String q, Double min_price, Double max_price) {
		
		try {
			List<Product> productList = new ArrayList<>();
			
			//Poderia ser mais interessante montar a query baseada nos parâmetros que vinheram do que
			//criar várias querys, porém não encontrei uma forma de fazer isso no Spring Boot
			
			if(q != null && min_price == null && max_price == null) { //Only q
				productList.addAll(productRepository.findByNameOrDescription(q, q));
			}
			else if (q != null && min_price != null && max_price == null) { //q AND min_price
				productList.addAll(productRepository.findByQandMinPrice(q, min_price));
			}
			else if (q != null && min_price == null && max_price != null) { //q AND max_price
				productList.addAll(productRepository.findByQandMaxPrice(q, max_price));
			}
			else if (q == null && min_price != null && max_price != null) { //min_price AND max_price
				productList.addAll(productRepository.findByPrices(min_price, max_price));
			}
			else if(q == null && min_price != null && max_price == null) { //Only min_price
				productList.addAll(productRepository.findByMinPrice(min_price));
			}
			else if(q == null && min_price == null && max_price != null) { //Only max_price
				productList.addAll(productRepository.findByMaxPrice(max_price));
			}
			
			//productList.addAll(productRepository.findByPrices(min_price, max_price));
			return ResponseEntity.ok().body(productList);
		}
		catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable String id) {
		try {
			Product obj = productRepository.findById(id);
			if(obj == null)
				return ResponseEntity.notFound().build();
			productRepository.delete(obj);
			return ResponseEntity.ok().body(null);
		}
		catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}	
}
