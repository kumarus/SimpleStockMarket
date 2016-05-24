package com.jpmc.stockmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jpmc.stockmarket.entity.manager.StockTradeEntityManager;
import com.jpmc.stockmarket.exchange.ExchangeService;
import com.jpmc.stockmarket.formula.CommonDividendYield;
import com.jpmc.stockmarket.formula.GeometricMean;
import com.jpmc.stockmarket.formula.PERatio;
import com.jpmc.stockmarket.formula.PreferredDividendYield;
import com.jpmc.stockmarket.formula.VolumeWeightedStockPrice;
import com.jpmc.stockmarket.service.StockMarketService;

@Configuration
public class StockMarketJunitConfig {
	
	@Bean
	public CommonDividendYield commonDividendYield(){
		return new CommonDividendYield(0.0, 0.0);
	}
	
	@Bean
	public PreferredDividendYield referredDividendYield(){
		return new PreferredDividendYield(0, 0, 0);
	}
	
	@Bean
	public PERatio peRatio(){
		return new PERatio(0, 0);
	}
	
	@Bean
	public GeometricMean geometricMean(){
		return new GeometricMean(null, 0);
	}
	
	@Bean
	public VolumeWeightedStockPrice volumeWeightedStockPrice(){
		return new VolumeWeightedStockPrice(0, 0);
	}
	
	@Bean
	public StockTradeEntityManager stockEntityManager(){
		return StockTradeEntityManager.getInstance();
	}
	
	@Bean
	public ExchangeService exchangeService(){
		return new ExchangeService();
	}
	
	@Bean
	public StockMarketService stockMarketService(){
		return new StockMarketService(exchangeService());
	}
	

}
