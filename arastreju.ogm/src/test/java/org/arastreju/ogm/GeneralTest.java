/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm;

import junit.framework.Assert;

import org.arastreju.ogm.testdata.entities.Customer;
import org.junit.Test;


/**
 * <p>
 *  Just some general test cases.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class GeneralTest {

	@Test
	public void testAnnotations() {
		Customer customer = new Customer();
		
		Assert.assertNotNull(customer);
	}
}
