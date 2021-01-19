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

import java.util.List;

/**
 * @author Joel Jerez
 */
public class RegionalBloc {

	private String acronym;
	private String name;
	private List<Object> otherAcronyms;
	private List<Object> otherNames;

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getOtherAcronyms() {
		return otherAcronyms;
	}

	public void setOtherAcronyms(List<Object> otherAcronyms) {
		this.otherAcronyms = otherAcronyms;
	}

	public List<Object> getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(List<Object> otherNames) {
		this.otherNames = otherNames;
	}

}
