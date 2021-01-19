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
package com.jereztech.openapis.v1.services;

import org.springframework.stereotype.Service;

import com.jereztech.openapis.v1.data.Country;
import com.jereztech.openapis.v1.data.CountryTranslation;

/**
 * Defines the logic for countries search.
 * 
 * @author Joel Jerez
 */
@Service
public class CountryService extends AbstractService<Country, CountryTranslation> {

	@Override
	protected Class<Country> getEntityClass() {
		return Country.class;
	}

	@Override
	protected String getEntitiesPath() {
		return "src/main/resources/v1/countries/countries.json";
	}

	@Override
	protected Class<CountryTranslation> getTranslationClass() {
		return CountryTranslation.class;
	}

	@Override
	protected String getTranslationsPath() {
		return "src/main/resources/v1/countries/translations";
	}

}
