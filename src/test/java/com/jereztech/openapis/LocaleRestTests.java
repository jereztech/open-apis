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
import com.jereztech.openapis.v1.data.LocaleTranslation;
import com.jereztech.openapis.v1.ws.LocaleRestController;

/**
 * @author Joel Jerez
 */
@SpringBootTest
class LocaleRestTests {

	@Autowired
	private LocaleRestController localeRestController;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testPagination() {
		List<LocaleTranslation> locales = localeRestController.findAllTranslations(FIRST_PAGE_INT, PAGE_SIZE_3, null, "en");
		assertEquals(locales.size(), PAGE_SIZE_3);
		assertEquals(locales.get(0).getLocale(), "Afrikaans");
		locales = localeRestController.findAllTranslations(PAGE_NUMBER_2, PAGE_SIZE_3, null, "en");
		assertEquals(locales.get(0).getLocale(), "Albanian (Albania)");
	}

	@Test
	void testFilter() {
		List<LocaleTranslation> locales = localeRestController.findAllTranslations(FIRST_PAGE_INT, PAGE_SIZE_3, "code eq ru", "ru");
		assertEquals(locales.get(0).getLocale(), "русский");
	}

	@Test
	void testTranslations() {
		List<LocaleTranslation> locales = localeRestController.findAllTranslations(FIRST_PAGE_INT, PAGE_SIZE_3, "code eq es", "en");
		assertEquals(locales.get(0).getLocale(), "Spanish");
		locales = localeRestController.findAllTranslations(FIRST_PAGE_INT, PAGE_SIZE_3, "code eq es", "es");
		assertEquals(locales.get(0).getLocale(), "español");
	}

	@Test
	@SuppressWarnings("unchecked")
	void testIncludeProperties() throws IOException {
		MappingJacksonValue jacksonValue = localeRestController.findAllTranslationsMapping("en", FIRST_PAGE_INT, PAGE_SIZE_3, null, "locale", null);
		String _response = objectMapper.writer(jacksonValue.getFilters()).writeValueAsString(((List<Currency>) jacksonValue.getValue()).get(0));
		assertTrue(_response.contains("\"locale\""));
		assertFalse(_response.contains("\"code\""));
	}

	@Test
	@SuppressWarnings("unchecked")
	void testIgnoreProperties() throws IOException {
		MappingJacksonValue jacksonValue = localeRestController.findAllTranslationsMapping("en", FIRST_PAGE_INT, PAGE_SIZE_3, null, null, "locale");
		String _response = objectMapper.writer(jacksonValue.getFilters()).writeValueAsString(((List<Currency>) jacksonValue.getValue()).get(0));
		assertFalse(_response.contains("\"locale\""));
		assertTrue(_response.contains("\"code\""));
	}

}
