package com.jpmc.stockmarket.exceptions;

public class StockMarketException extends Exception{
	
	/**
	 * Stock Market exception.
	 */
	private static final long serialVersionUID = 1L;

	public StockMarketException(String message){
		super(message);
	}
}
