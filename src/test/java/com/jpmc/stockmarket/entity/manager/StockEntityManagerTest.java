package com.jpmc.stockmarket.entity.manager;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.jpmc.stockmarket.config.StockMarketJunitConfig;
import com.jpmc.stockmarket.entity.manager.StockTradeEntityManager;
import com.jpmc.stockmarket.model.Stock;
import com.jpmc.stockmarket.model.StockType;
import com.jpmc.stockmarket.model.Trade;
import com.jpmc.stockmarket.model.TradeIndicator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StockMarketJunitConfig.class, loader=AnnotationConfigContextLoader.class)
public class StockEntityManagerTest {
	
	@Autowired
	private StockTradeEntityManager stockTradeEntityManager;

	@Test
	public void testStockEntityManager() {
		assertEquals( 
                "class com.jpmc.stockmarket.entity.manager.StockTradeEntityManager", 
                this.stockTradeEntityManager.getClass().toString());
	}
	
	@Test
	public void testRecordTrade(){
		Stock stock = new Stock("POP", StockType.COMMON, 8.0, 0.0, 100.0);
		Trade trade = new Trade(stock, new Date(), 100.0, 1000L, TradeIndicator.BUY);
		assertEquals(Boolean.TRUE, stockTradeEntityManager.recordTrade(trade));
	}
	
	@Test
	public void testGetTrades(){
		Assert.assertNotNull(stockTradeEntityManager.getTrades());
	}
}
