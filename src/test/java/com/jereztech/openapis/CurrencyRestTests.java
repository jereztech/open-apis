/*******************************************************************************
 * Copyright (C) 2021 Joel Jerez
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jereztech.openapis;

import static com.jereztech.openapis.support.Constants.FIRST_PAGE_INT;
import static com.jereztech.openapis.support.Constants.PAGE_NUMBER_2;
import static com.jereztech.openapis.support.Constants.PAGE_SIZE_3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jereztech.openapis.v1.data.Currency;
import com.jereztech.openapis.v1.ws.CurrencyRestController;

/**
 * @author Joel Jerez
 */
@SpringBootTest
class CurrencyRestTests {

	@Autowired
	private CurrencyRestController currencyRestController;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testPagination() {
		List<Currency> currencies = currencyRestController.findAll(FIRST_PAGE_INT, PAGE_SIZE_3, null);
		assertEquals(currencies.size(), PAGE_SIZE_3);
		assertEquals(currencies.get(0).getName(), "Afghani");
		currencies = currencyRestController.findAll(PAGE_NUMBER_2, PAGE_SIZE_3, null);
		assertEquals(currencies.get(0).getName(), "Kwanza");
	}

	@Test
	void testFilter() {
		List<Currency> currencies = currencyRestController.findAll(FIRST_PAGE_INT, PAGE_SIZE_3, "code eq EUR");
		assertEquals(currencies.get(0).getName(), "Euro");
	}

	@Test
	@SuppressWarnings("unchecked")
	void testIncludeProperties() throws IOException {
		MappingJacksonValue jacksonValue = currencyRestController.findAllMapping(FIRST_PAGE_INT, PAGE_SIZE_3, null, "code,countryName", null);
		String _response = objectMapper.writer(jacksonValue.getFilters()).writeValueAsString(((List<Currency>) jacksonValue.getValue()).get(0));
		assertTrue(_response.contains("\"code\""));
		assertTrue(_response.contains("\"countryName\""));
		assertFalse(_response.contains("\"countryAlpha3Code\""));
	}

	@Test
	@SuppressWarnings("unchecked")
	void testIgnoreProperties() throws IOException {
		MappingJacksonValue jacksonValue = currencyRestController.findAllMapping(FIRST_PAGE_INT, PAGE_SIZE_3, null, null, "code,countryName");
		String _response = objectMapper.writer(jacksonValue.getFilters()).writeValueAsString(((List<Currency>) jacksonValue.getValue()).get(0));
		assertFalse(_response.contains("\"code\""));
		assertFalse(_response.contains("\"countryName\""));
		assertTrue(_response.contains("\"countryAlpha3Code\""));
	}

}
