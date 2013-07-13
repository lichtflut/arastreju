/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <p>
 * 	Class with static utility methods.
 * </p>
 * 
 * <p>
 * 	Created: 06.09.2008
 * </p>
 * 
 * @author Oliver Tigges
 */
public class Infra {
	
	/**
	 * returns true if both are null or a.equals(b).
	 */
	public static boolean equals(Object a, Object b){
		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null) {
			return a.equals(b);
		}
		return false;
	}

	/**
	 * returns true if a is null or a equals b
	 */
	public static boolean matches(Object a, Object b){
		if (a == null || a == b){
			return true;
		} else {
			return a.equals(b);
		}
	}
	
	/**
	 * returns true if given arr contains all given values 
	 */
	public static boolean containsAll(Object[] arr, Object[] values){
		return false;
	}
	
	public static boolean different(Object a, Object b) {
		return !equals(a, b);
	}
	

	/**
	 * returns true if both are null or a.equals(b).
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(Comparable a, Comparable b){
		if (a != null && b != null) {
			return a.compareTo(b);
		} else if (a != null){
			return -1;
		} else if (b != null){
			return 1;
		}
		return 0;
	}

	
	//-----------------------------------------------------
	
	public static boolean appendToBuffer(StringBuffer sb, String field, Object value, boolean first) {
		if (value != null) {
			if (first) {
				first = false;
			} else {
				sb.append(" , ");
			}
			sb.append(" " + field + "= '" + value + "'");
		}
		return first;
	}
	
	public static boolean appendToQuery(StringBuffer sb, String stmt, Object value, boolean first) {
		if (value != null) {
			if (first) {
				first = false;
			} else {
				sb.append(" AND ");
			}
			sb.append(" " + stmt);
		}
		return first;
	}
	
	/**
	 * returns the first object not null.
	 * @param values The values
	 * @return
	 */
	public static <T> T coalesce(T... values){
		for (T current : values) {
			if (current != null){
				return current;
			}
		}
		return null;
	}
	
	// -- String Operations -------------------------------

	public static String normalize(final String orig) {
		if (orig != null){
			return orig.toLowerCase();
		} else {
			return null;
		}
	}
	
	public static String firstUpper(final String orig) {
		if (orig == null || orig.length() < 2){
			return orig;
		}
		return orig.substring(0, 1).toUpperCase() + orig.substring(1);
	}
	
	public static String firstLower(final String orig) {
		if (orig == null || orig.length() < 2){
			return orig;
		}
		return orig.substring(0, 1).toLowerCase() + orig.substring(1);
	}
	
	public static String toCamelCaseName(final String orig){
		StringBuffer sb = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(orig);
		while (tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			sb.append(Infra.firstUpper(token));
		}
		return sb.toString();
	}
	
	// ------------------------------------------------------

	public static void wait(int miliseconds){
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
