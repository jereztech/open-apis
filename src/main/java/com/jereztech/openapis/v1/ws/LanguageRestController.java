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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jereztech.openapis.v1.data.Language;
import com.jereztech.openapis.v1.data.LanguageTranslation;
import com.jereztech.openapis.v1.services.LanguageService;

/**
 * Defines the end-point for languages search.
 * 
 * @author Joel Jerez
 */
@RestController
@RequestMapping("/v1/languages")
public class LanguageRestController extends AbstractRestController<Language, LanguageTranslation, LanguageService> {

	public LanguageRestController(LanguageService service) {
		super(service);
	}

}
