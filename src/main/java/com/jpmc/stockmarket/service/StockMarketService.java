package com.jpmc.stockmarket.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmc.stockmarket.entity.manager.IStockTradeEntityManager;
import com.jpmc.stockmarket.entity.manager.StockTradeEntityManager;
import com.jpmc.stockmarket.exceptions.StockMarketException;
import com.jpmc.stockmarket.exchange.ExchangeCode;
import com.jpmc.stockmarket.exchange.IExchangeService;
import com.jpmc.stockmarket.formula.Formula;
import com.jpmc.stockmarket.formula.FormulaRequest;
import com.jpmc.stockmarket.formula.factory.FormulaFactory;
import com.jpmc.stockmarket.model.Stock;
import com.jpmc.stockmarket.model.Trade;
import com.jpmc.stockmarket.model.TradeIndicator;

@Service
public class StockMarketService implements IStockMarketService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StockMarketService.class);
	private static final IStockTradeEntityManager STOCK_ENTITY_MANAGER = StockTradeEntityManager.getInstance();
	private static final double LEAST_STOCK_PRICE = 0.0;
	
	private IExchangeService exchangeService;
	private final ConcurrentHashMap<String, Stock> EXCHANGE_DATA;
	
	@Autowired
	public StockMarketService(IExchangeService exchangeService) {
		this.exchangeService = exchangeService;
		EXCHANGE_DATA = this.exchangeService.getDataFromExchange(ExchangeCode.GBCE);
	}
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.DividendYield and PERatio
	 * @param StockSymbol
	 * @param stock price
	 * @return formula result 
	 */
	@Override
	public Double executeFormulaRequest(FormulaRequest request, String stockSymbol, double price) throws StockMarketException {
		
		LOGGER.debug("Stock Market Calculating formula {} for Stock {} with price {}", request.name(), stockSymbol, price);
		
		Formula<Double> formula ;
		Stock stock = EXCHANGE_DATA.get(stockSymbol);
		if (stock == null || !(price > LEAST_STOCK_PRICE)){
			throw new StockMarketException("Stock ["+stockSymbol+"] is not listed on the exchange.");
			
		}else {
			formula = FormulaFactory.newFormula(request, stock, price);
		} 
			
		LOGGER.debug("Formula created {} ", formula);
		Double result = (Double) formula.execute();
		
		LOGGER.debug("Formula {} return the result {} ", request.name(), result);
		return result;
	}
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.VolumeWeightedStockPrice
	 * @param StockSymbol
	 * @return formula result 
	 */
	public Double executeFormulaRequest(FormulaRequest request, String stockSymbol) throws StockMarketException {
			
		LOGGER.debug("Stock Market Calculating formula {} for Stock {} ", request.name(), stockSymbol);
		
		Formula<Double> formula ;
		Stock stock = EXCHANGE_DATA.get(stockSymbol);
		if (stock == null){
			throw new StockMarketException("Stock ["+stockSymbol+"] is not listed on the exchange.");
			
		}else {
			formula = FormulaFactory.newFormula(request, stock);
		} 
		
		LOGGER.debug("Formula created {} ", formula);
		Double result = (Double) formula.execute();
		
		LOGGER.debug("Formula {} return the result {} ", request.name(), result);
		return result;
	}
	
	/*
	 * @param @FormulaRequest this method only supports request type @FormulaRequest.GeometricMean
	 * @return formula result 
	 */
	public Double executeFormulaRequest(FormulaRequest request) throws StockMarketException {
		
		LOGGER.debug("Stock Market Calculating formula {} ", request.name());
		
		Formula<Double> formula = FormulaFactory.newFormula(request, EXCHANGE_DATA);
		
		LOGGER.debug("Formula created {} ", formula);
		Double result = (Double) formula.execute();
		
		LOGGER.debug("Formula {} return the result {} ", request.name(), result);
		return result;
	}
	
	/*
	 * @param @Trade 
	 * @return True of False
	 * This method record trade in in-memory trade store.
	 * @see com.jpmc.stockmarket.service.IStockMarketService#recordTrade(com.jpmc.stockmarket.model.Trade)
	 */
	@Override
	public boolean recordTrade(Trade trade) {
		LOGGER.debug("Stock Market storing Trade {} in trade store", trade);
		boolean complete = STOCK_ENTITY_MANAGER.recordTrade(trade);
		if (complete) {
			LOGGER.debug("Trade {} with {} Indicator COMPLETE",trade, trade.getTradeIndicator().name());
		}else {
			LOGGER.debug("Trade {} with {} Indicator NOT-COMPLETE",trade, trade.getTradeIndicator().name());
		}
		return complete;
	}

	
	/*
	 * This method perform Trade with BUY Indication
	 * @see com.jpmc.stockmarket.service.IStockMarketService#buyStock(java.lang.String, double, long)
	 */
	@Override
	public boolean buyStock(String stockSymbol, double tradedPrice, long shareQuantity) throws StockMarketException {
		LOGGER.debug("Buy Stock {} with Price {} in ShareQuantity {} ", stockSymbol, tradedPrice, shareQuantity);
		
		Stock buyStock = EXCHANGE_DATA.get(stockSymbol);
		boolean tradeComplete = false;
		if (buyStock != null && tradedPrice > LEAST_STOCK_PRICE){
			buyStock.setTickerPrice(tradedPrice);
			Trade trade = new Trade(buyStock, new Date(), tradedPrice, shareQuantity, TradeIndicator.BUY);
			tradeComplete = this.recordTrade(trade);
		}else {
			throw new StockMarketException("Stock ["+stockSymbol+"] is not listed on the exchange.");
		}
		
		if (tradeComplete){
			exchangeService.updateStockTickerPrice(buyStock);
		}
		return tradeComplete;
	}

	/*
	 * This method perform Trade with SELL Indication
	 * @see com.jpmc.stockmarket.service.IStockMarketService#sellStock(java.lang.String, double, long)
	 */
	@Override
	public boolean sellStock(String stockSymbol, double tradedPrice, long shareQuantity) throws StockMarketException {
		LOGGER.debug("Sell Stock {} with Price {} in ShareQuantity {} ", stockSymbol, tradedPrice, shareQuantity);
		
		Stock sellStock = EXCHANGE_DATA.get(stockSymbol);
		boolean tradeComplete = false;
		if (sellStock != null && tradedPrice > LEAST_STOCK_PRICE) {
			sellStock.setTickerPrice(tradedPrice);
			Trade trade = new Trade(sellStock, new Date(), tradedPrice, shareQuantity, TradeIndicator.SELL);
			tradeComplete = this.recordTrade(trade);
		}else {
			throw new StockMarketException("Stock ["+stockSymbol+"] is not listed on the exchange.");
		}
		if (tradeComplete){
			exchangeService.updateStockTickerPrice(sellStock);
		}
		return tradeComplete;
	}

}
