/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
/**
 * 
 */
package org.arastreju.sge.model.nodes.views;

import java.text.DateFormat;
import java.util.Date;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.TimeMask;
import org.arastreju.sge.model.nodes.SNValue;

import de.lichtflut.infra.exceptions.NotYetSupportedException;

/**
 * Semantic node representing a time specification.
 * 
 * Created: 31.01.2008 
 *
 * @author Oliver Tigges
 */
public class SNTimeSpec extends ValueView {
	
	// -----------------------------------------------------
	
	/**
	 * Creates a new transient time specification, without 'mask'. The default TimeMask.TIMESTAMP
	 * will be used.
	 * @param time The time.
	 */
	public SNTimeSpec(final Date time){
		this(time, TimeMask.TIMESTAMP);
	}

	/**
	 * Creates a new transient time specification.
	 * @param time The time.
	 * @param mask The mask.
	 */
	public SNTimeSpec(final Date time, final TimeMask mask){
		super(ElementaryDataType.getCorresponding(mask), time);
	}
	
	/**
	 * Creates a new time specification view for given value.
	 * @param value The value to be wrapped.
	 */
	public SNTimeSpec(final SNValue value) {
		super(value);
	}
	
	// -----------------------------------------------------
	
	/**
	 * Get the time mask.
	 */
	public TimeMask getTimeMask() {
		return TimeMask.getCorresponding(getDataType());
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.views.ValueView#toString()
	 */
	@Override
	public String toString() {
		switch(getTimeMask()) {
		case DATE:
			return DateFormat.getDateInstance().format(getTimeValue());
		case TIME_OF_DAY:
			return DateFormat.getTimeInstance().format(getTimeValue());
		case TIMESTAMP:
			return DateFormat.getDateTimeInstance().format(getTimeValue());
		default:
			throw new NotYetSupportedException(getTimeMask());
		}
	}

}
