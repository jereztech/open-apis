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
package com.jereztech.openapis.support;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * A helper for Java objects projections to avoid NullPointerException. Inspired
 * on Elvis null-coalescing operator.
 * 
 * <pre>
 * 
 * Example: 
 * {@code String countryName = proxify(globe, 'continent.regionNull.country.name'); //expected: null}
 * </pre>
 * 
 * @author Joel Jerez
 */
public class ProxyAccessor {

	private static final int ONE = BigInteger.ONE.intValue();
	private static final String DOT = ".";
	private static final String DOT_REGEX = "\\.";

	@SuppressWarnings("unchecked")
	public static <T> T proxify(Object entity, String attributePath) {
		if (entity != null && isNotBlank(attributePath)) {
			String[] attributePaths = attributePath.split(DOT_REGEX);
			final String attribute = Arrays.stream(attributePaths).findFirst().orElse(null);
			if (isNotBlank(attribute)) {
				Optional<Field> fieldOptional = FieldUtils.getAllFieldsList(entity.getClass()).stream().filter((Field field) -> matchedField(field, attribute))
						.findFirst();
				if (fieldOptional.isPresent()) {
					try {
						Field field = fieldOptional.get();
						field.setAccessible(true);
						Object value = field.get(entity);
						if (value != null) {
							if (attributePaths.length > ONE) {
								attributePaths = Arrays.copyOfRange(attributePaths, ONE, attributePaths.length);
								return (ClassUtils.isPrimitiveOrWrapper(value.getClass()) || value instanceof String) ? null : proxify(value, join(attributePaths, DOT));
							}
							return (T) value;
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return null;
	}

	private static boolean matchedField(Field field, String attribute) {
		return !Modifier.isFinal(field.getModifiers()) && field.getName().equalsIgnoreCase(attribute);
	}

}
