package com.jpmc.stockmarket.exchange;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.jpmc.stockmarket.config.StockMarketJunitConfig;
import com.jpmc.stockmarket.exchange.ExchangeCode;
import com.jpmc.stockmarket.exchange.ExchangeService;
import com.jpmc.stockmarket.model.Stock;
import com.jpmc.stockmarket.model.StockType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StockMarketJunitConfig.class, loader=AnnotationConfigContextLoader.class)
public class ExchangeServiceTest {

	@Autowired
	private ExchangeService exchangeService;

	@Test
	public void testExchangeService() {
		assertEquals( 
                "class com.jpmc.stockmarket.exchange.ExchangeService", 
                this.exchangeService.getClass().toString());
	}
	
	@Test
	public void testFetchDataFromExchange(){
		Assert.assertNotNull(exchangeService.getDataFromExchange(ExchangeCode.GBCE));
	}
	
	@Test
	public void testUpdateStockTickerPrice(){
		Stock stock = new Stock("POP", StockType.COMMON, 8.0, 0.0, 100.0);
		assertEquals(Boolean.TRUE, exchangeService.updateStockTickerPrice(stock));
	}
}
