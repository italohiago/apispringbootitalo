package com.compasso.api.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.compasso.api.model.Product;



public interface ProductRepository extends JpaRepository<Product, Long>{

	Product findById(String id); 
	
	void deleteById(String id); 
	
	List<Product> findByNameOrDescription(String name, String description);
	
	@Query("from Product WHERE (name = ?1 OR description = ?1) AND price >= ?2")
	List<Product> findByQandMinPrice(String q,Double min);
	
	@Query("from Product WHERE (name = ?1 OR description = ?1) AND price <= ?2")
	List<Product> findByQandMaxPrice(String q,Double max);
	
	@Query("from Product WHERE price >= ?1 AND price <= ?2")
	List<Product> findByPrices(Double min,Double max);

	@Query("from Product WHERE price >= ?1")
	List<Product> findByMinPrice(Double min);
	
	@Query("from Product WHERE price <= ?1")
	List<Product> findByMaxPrice(Double max);
	
	
	
	
	
	
	
}
