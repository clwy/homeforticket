package com.homeforticket.parser;

import com.homeforticket.model.BaseType;

public interface Parser<T extends BaseType> {

	public abstract T parse(String parser) throws Exception;

}