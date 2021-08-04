package com.compasso.api.controllers.exceptions;


import com.compasso.api.model.Product;
import com.compasso.api.model.exceptions.Exceptions;

public class ExceptionProductController {
	
	private Product product;
	
	public ExceptionProductController(Product product) {
		super();
		this.product = product;
	}

	public Exceptions checkingProductParameters(){
		String msg = "";
		if(this.product.getDescription() == null) {
			msg = "The field description is required. ";
		}
		if(this.product.getName() == null) {
			msg = msg + "The field name is required. ";
		}
		if(this.product.getPrice() == null) {
			msg = msg + "The field price is required. ";
		}
		else if(this.product.getPrice() < 0){
			msg = msg + "The field price must be a positive number. ";
		}
		msg = msg.substring(0, msg.length()-1); //Apagando o último caractere (Que sempre será vazio)
		
		return new Exceptions(400, msg);
	}
}
