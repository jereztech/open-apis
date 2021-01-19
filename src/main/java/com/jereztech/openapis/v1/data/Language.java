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
package com.jereztech.openapis.v1.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.jereztech.openapis.support.Constants;

/**
 * @author Joel Jerez
 */
@JsonFilter(Constants.FILTER_NAME)
public class Language {

	private Integer number;
	private String name;
	private String nativeName;
	private String countryName;
	private String iso6391;
	private String iso6392;
	private String localeAlpha2;
	private String localeAlpha3;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNativeName() {
		return nativeName;
	}

	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIso6391() {
		return iso6391;
	}

	public void setIso6391(String iso6391) {
		this.iso6391 = iso6391;
	}

	public String getIso6392() {
		return iso6392;
	}

	public void setIso6392(String iso6392) {
		this.iso6392 = iso6392;
	}

	public String getLocaleAlpha2() {
		return localeAlpha2;
	}

	public void setLocaleAlpha2(String localeAlpha2) {
		this.localeAlpha2 = localeAlpha2;
	}

	public String getLocaleAlpha3() {
		return localeAlpha3;
	}

	public void setLocaleAlpha3(String localeAlpha3) {
		this.localeAlpha3 = localeAlpha3;
	}

}
