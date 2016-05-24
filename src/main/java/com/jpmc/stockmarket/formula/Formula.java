package com.jpmc.stockmarket.formula;

public interface Formula<T> {
	
	/*
	 * @return T type result of formula execution.
	 */
	T execute();
}
