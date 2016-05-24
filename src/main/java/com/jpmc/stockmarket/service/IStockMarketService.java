package com.jpmc.stockmarket.service;

import com.jpmc.stockmarket.exceptions.StockMarketException;
import com.jpmc.stockmarket.formula.FormulaRequest;
import com.jpmc.stockmarket.model.Trade;

public interface IStockMarketService {
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.DividendYield and PERatio
	 * @param StockSymbol
	 * @param stock price
	 * @return formula result 
	 */
	Double executeFormulaRequest(FormulaRequest request, String stockSymbol, double price) throws StockMarketException;
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.VolumeWeightedStockPrice
	 * @param StockSymbol
	 * @return formula result 
	 */
	Double executeFormulaRequest(FormulaRequest request, String stockSymbol) throws StockMarketException;
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.GeometricMean
	 * @return formula result 
	 */
	Double executeFormulaRequest(FormulaRequest request) throws StockMarketException;
	
	/*
	 * @param @Trade.
	 * @return true and false.
	 */
	boolean recordTrade(Trade trade);
	
	/*
	 * @param stockSymbol
	 * @param tradedPrice
	 * @param shareQuantity
	 * @return true and false.
	 */
	boolean buyStock(String stockSymbol, double tradedPrice, long shareQuantity) throws StockMarketException;
	
	/*
	 * @param stockSymbol
	 * @param tradedPrice
	 * @param shareQuantity
	 * @return true and false.
	 */
	boolean sellStock(String stockSymbol, double tradedPrice, long shareQuantity) throws StockMarketException;

}
