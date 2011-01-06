/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import junit.framework.Assert;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.junit.Test;


/**
 * <p>
 *  Test cases for initialization of Arastreju.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuInitializationTest {

	@Test
	public void testRootGate(){
		final ArastrejuGate rootGate = Arastreju.getInstance().rootContext();
		
		Assert.assertNotNull(rootGate);
		
		Assert.assertNotNull(rootGate.startConversation());
		
	}
	
}
