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
package org.arastreju.sge.model.nodes.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Helper-Class to measure runtime.
 * 
 * Created: 16.05.2005 
 * 
 * @author Oliver Tigges
 */
public class StopWatch {

    private final static String STOP_WATCH = "StopWatch";

    private static final Logger LOGGER = LoggerFactory.getLogger(STOP_WATCH);

	private final static NumberFormat format = new DecimalFormat();
	
	private long lastTime = System.nanoTime();
	
	//-----------------------------------------------------
	
	public static void pause(long milisec){
		try {
			Thread.sleep(milisec);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void reset(){
		lastTime = System.nanoTime();
	}
	
	public long getTime(){
		long currentTime = System.nanoTime();
		return (currentTime - lastTime) / 1000;
	}
	
	public long fetchTime(){
		long currentTime = System.nanoTime();
		long time = (currentTime - lastTime) / 1000;
		reset();
		return time;
	}
	
	public void displayTime(String msg){
		LOGGER.info ("{} : {} micros", msg, format.format(getTime()));
		lastTime = System.nanoTime();		
	}
	
	public void displayNanoTime(String msg){
		long currentTime = System.nanoTime();
		System.out.println(msg + ": " + (format.format((currentTime - lastTime))) + " ns");
		lastTime = System.nanoTime();		
	}
	
}
