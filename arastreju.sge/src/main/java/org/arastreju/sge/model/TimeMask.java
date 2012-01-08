/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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


/**
 * Mask to be laid over {@link TimeSpec}, that defines the period described by the {@link TimeSpec}.
 * 
 * Created: 23.09.2008
 * 
 * @author Oliver Tigges
 */
public enum TimeMask {
	
	TIMESTAMP,
	DATE,
	TIME_OF_DAY;
	
// ------------------------------------------------------
	
	public static TimeMask getCorresponding(final ElementaryDataType datatype){
		switch(datatype){
		case DATE:
			return TimeMask.DATE;
		case TIME_OF_DAY:
			return TimeMask.TIME_OF_DAY;
		case TIMESTAMP:
			return TimeMask.TIMESTAMP;
		default:
			throw new IllegalArgumentException("illegal time mask type + mask");
		}
	}
	
}
