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

import static com.jereztech.openapis.support.Constants.AND_SEPARATOR;
import static com.jereztech.openapis.support.Constants.EQ_SEPARATOR;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.strip;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.jereztech.openapis.support.JsonUtil;
import com.jereztech.openapis.support.ProxyAccessor;

/**
 * Defines the logic to find all entities.
 * 
 * @author Joel Jerez
 *
 * @param <E> the Entity Class
 * @param <T> the Translation Class
 */
public abstract class AbstractService<E, T> implements IFindAllDelegate<E, T> {

	@Autowired
	private JsonUtil jsonUtil;

	private List<E> entities;

	protected abstract Class<E> getEntityClass();

	protected abstract String getEntitiesPath();

	protected abstract Class<T> getTranslationClass();

	protected abstract String getTranslationsPath();

	/**
	 * Defines the logic for reading source files.
	 */
	@PostConstruct
	@SuppressWarnings("unchecked")
	private void init() {
		try {
			if (isNotBlank(getEntitiesPath())) {
				entities = (List<E>) jsonUtil.fromJson(getEntitiesPath(),
						jsonUtil.getObjectMapper().getTypeFactory().constructCollectionType(List.class, getEntityClass()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Defines the logic to find all entities that match the filter. The ResultSet
	 * can be paginated for performance reasons.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<E> findAll(Integer pageNumber, Integer pageSize, String filter) {
		return (List<E>) filter(pageNumber, pageSize, filter, entities);
	}

	/**
	 * Defines the logic to find all entity translations by locale that match the
	 * filter. The ResultSet can be paginated for performance reasons.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllTranslations(Integer pageNumber, Integer pageSize, String filter, String locale) {
		if (isBlank(getTranslationsPath())) {
			throw new UnsupportedOperationException();
		}
		try {
			List<T> translations = (List<T>) jsonUtil.fromJson(String.format("%s/%s.json", getTranslationsPath(), locale),
					jsonUtil.getObjectMapper().getTypeFactory().constructCollectionType(List.class, getTranslationClass()));
			return (List<T>) filter(pageNumber, pageSize, filter, translations);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid locale.");
		}
	}

	/**
	 * Apply filter to the ResultSet.
	 */
	private List<?> filter(Integer pageNumber, Integer pageSize, String filter, List<?> source) {
		return source.stream().filter(item -> {
			if (isBlank(filter)) {
				return true;
			}
			Map<String, String> expressions = Arrays.stream(strip(filter).split(AND_SEPARATOR)).map(exp -> exp.split(EQ_SEPARATOR))
					.collect(toMap(key -> key[0], value -> value[1]));
			return expressions.entrySet().stream().allMatch(entry -> entry.getValue().equals(ProxyAccessor.proxify(item, entry.getKey())));
		}).skip(pageSize * pageNumber).limit(pageSize).collect(toList());
	}

}
