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

/**
 * Defines the Constants.
 * 
 * @author Joel Jerez
 */
public interface Constants {

	String FIRST_PAGE = "0";
	Integer FIRST_PAGE_INT = Integer.valueOf(FIRST_PAGE);
	String PAGE_SIZE = "1000";
	Integer PAGE_NUMBER_2 = 2;
	Integer PAGE_SIZE_3 = 3;

	String EQ_SEPARATOR = " eq ";
	String COMMA_SEPARATOR = ",";
	String AND_SEPARATOR = " and ";

	String FILTER_NAME = "MappingFilter";

}
