package com.jpmc.stockmarket.exchange;

import java.util.concurrent.ConcurrentHashMap;

import com.jpmc.stockmarket.model.Stock;


public interface IExchangeService {
	
	/**
     * @param @ExchangeCode Stock exchange code
     * @return In-Memory data of Stock Exchange
     */
	ConcurrentHashMap<String, Stock> getDataFromExchange(ExchangeCode exchange);
	
	/*
	 * /**
     * @param @Stock ticker price to be updated
     * @return boolean type result true of false
     */
	boolean updateStockTickerPrice(Stock buyStock);
}
