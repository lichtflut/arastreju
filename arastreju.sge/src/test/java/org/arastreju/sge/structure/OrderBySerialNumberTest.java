/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNScalar;
import org.junit.Test;

/**
 * <p>
 *  Test case for {@link OrderBySerialNumber}.
 * </p>
 *
 * <p>
 * 	Created Jan 24, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class OrderBySerialNumberTest {

	@Test
	public void testSorting() {
		final List<ResourceNode> list = new ArrayList<ResourceNode>();
		list.add(create(4));
		list.add(create(13));
		list.add(new SNResource());
		list.add(create(45));
		list.add(create(1));
		list.add(new SNResource());
		list.add(create(7));
		list.add(new SNResource());
		list.add(create(3));

		Collections.sort(list, new OrderBySerialNumber());
		
		Assert.assertEquals(9, list.size());
		
		Assert.assertEquals(1, getSerialNumber(list.get(0)));
		Assert.assertEquals(3, getSerialNumber(list.get(1)));
		Assert.assertEquals(4, getSerialNumber(list.get(2)));
		Assert.assertEquals(7, getSerialNumber(list.get(3)));
		Assert.assertEquals(13, getSerialNumber(list.get(4)));
		Assert.assertEquals(45, getSerialNumber(list.get(5)));
		Assert.assertEquals(0, getSerialNumber(list.get(6)));
		Assert.assertEquals(0, getSerialNumber(list.get(7)));
		Assert.assertEquals(0, getSerialNumber(list.get(8)));
		
	}
	
	// ----------------------------------------------------
	
	private ResourceNode create(int serialNumber) {
		final ResourceNode node = new SNResource();
		node.addAssociation(Aras.HAS_SERIAL_NUMBER, new SNScalar(serialNumber));
		return node;
	}
	
	private int getSerialNumber(ResourceNode node) {
		SemanticNode sn = SNOPS.singleObject(node, Aras.HAS_SERIAL_NUMBER);
		if (sn != null) {
			return sn.asValue().getIntegerValue().intValue();
		} else {
			return 0;
		}
	}
}
