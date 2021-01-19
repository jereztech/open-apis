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
package com.jereztech.openapis.v1.ws;

import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.filterOutAllExcept;
import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.serializeAll;
import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.serializeAllExcept;
import static com.jereztech.openapis.support.Constants.COMMA_SEPARATOR;
import static com.jereztech.openapis.support.Constants.FIRST_PAGE;
import static com.jereztech.openapis.support.Constants.PAGE_SIZE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.strip;

import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jereztech.openapis.support.Constants;
import com.jereztech.openapis.v1.services.AbstractService;
import com.jereztech.openapis.v1.services.IFindAllDelegate;

/**
 * Defines the base end-point logic.
 * 
 * @author Joel Jerez
 *
 * @param <E> the Entity Class
 * @param <T> the Translation Class
 * @param <S> the concrete Service
 */
public abstract class AbstractRestController<E, T, S extends AbstractService<E, T>> implements IFindAllDelegate<E, T> {

	protected final S service;

	public AbstractRestController(S service) {
		this.service = service;
	}

	/**
	 * Defines the logic to find all entities that match the filter. The ResultSet
	 * can be paginated for performance reasons.
	 */
	@Override
	public List<E> findAll(Integer pageNumber, Integer pageSize, String filter) {
		return service.findAll(pageNumber, pageSize, filter);
	}

	/**
	 * Defines the logic to find all entity translations by locale that match the
	 * filter. The ResultSet can be paginated for performance reasons.
	 */
	@Override
	public List<T> findAllTranslations(Integer pageNumber, Integer pageSize, String filter, String locale) {
		return service.findAllTranslations(pageNumber, pageSize, filter, locale);
	}

	/**
	 * Find all entities that match the filter including or excluding properties.
	 * The ResultSet can be paginated for performance reasons.
	 *
	 * <pre>
	 * 
	 * * Pagination and Filter
	 * Request Example: GET /api/v1/countries?pageNumber=0&pageSize=1&filter=alpha3Code eq BRA
	 * Response Example:
		[
		  {
		    "name": "Brazil",
		    "capital": "Brasília",
		    "alpha2Code": "BR",
		    "alpha3Code": "BRA",
		    ...
		  }
		]
	 *
	 * * Including Properties
	 * Request Example: GET /api/v1/countries?includeProperties=name
	 * Response Example:
		[
		  {
		    "name": "Brazil"
		  },
		  {
		    "name": "British Indian Ocean Territory"
		  },
		  ...
		]
	 *
	 * * Ignoring Properties
	 * Request Example: GET /api/v1/countries?ignoreProperties=name
	 * Response Example:
		[
		  {
		    "capital": "Brasília",
		    "alpha2Code": "BR",
		    "alpha3Code": "BRA",
		    ...
		  },
		  {
		    "capital": "Diego Garcia",
		    "alpha2Code": "IO",
		    "alpha3Code": "IOT",
		    ...
		  }
		]
	 * </pre>
	 */
	@GetMapping
	public MappingJacksonValue findAllMapping(@RequestParam(defaultValue = FIRST_PAGE) Integer pageNumber, @RequestParam(defaultValue = PAGE_SIZE) Integer pageSize,
			@RequestParam(required = false) String filter, @RequestParam(required = false) String includeProperties,
			@RequestParam(required = false) String ignoreProperties) {
		return responseMapping(includeProperties, ignoreProperties, findAll(pageNumber, pageSize, filter));
	}

	/**
	 * Find all entity translations by locale that match the filter including or
	 * excluding properties. The ResultSet can be paginated for performance reasons.
	 *
	 * <pre>
	 * 
	 * * Pagination and Filter
	 * Request Example: GET /api/v1/countries/translations/en?pageNumber=0&pageSize=1&filter=alpha2Code eq US
	 * Response Example:
		[
		  {
		    "alpha2Code": "US",
		    "country": "United States"
		  }
		]
	 *
	 * * Including Properties
	 * Request Example: GET /api/v1/countries/translations/en?pageNumber=0&pageSize=1&filter=alpha2Code eq US&includeProperties=country
	 * Response Example:
		[
		  {
		    "country": "United States"
		  }
		]
	 *
	 * * Ignoring Properties
	 * Request Example: GET /api/v1/countries/translations/en?pageNumber=0&pageSize=1&filter=alpha2Code eq US&ignoreProperties=country
	 * Response Example:
		[
		  {
		    "alpha2Code": "US"
		  }
		]
	 * </pre>
	 */
	@GetMapping("/translations/{locale}")
	public MappingJacksonValue findAllTranslationsMapping(@PathVariable String locale, @RequestParam(defaultValue = FIRST_PAGE) Integer pageNumber,
			@RequestParam(defaultValue = PAGE_SIZE) Integer pageSize, @RequestParam(required = false) String filter,
			@RequestParam(required = false) String includeProperties, @RequestParam(required = false) String ignoreProperties) {
		return responseMapping(includeProperties, ignoreProperties, findAllTranslations(pageNumber, pageSize, filter, locale));
	}

	/**
	 * Apply the mapping to the ResultSet.
	 */
	private MappingJacksonValue responseMapping(String includeProperties, String ignoreProperties, List<?> response) {
		MappingJacksonValue mappingResponse = new MappingJacksonValue(response);
		SimpleBeanPropertyFilter propertiesFilter = isNotBlank(includeProperties) ? filterOutAllExcept(strip(includeProperties).split(COMMA_SEPARATOR))
				: isNotBlank(ignoreProperties) ? serializeAllExcept(strip(ignoreProperties).split(COMMA_SEPARATOR)) : serializeAll();
		mappingResponse.setFilters(new SimpleFilterProvider().addFilter(Constants.FILTER_NAME, propertiesFilter));
		return mappingResponse;
	}

}
