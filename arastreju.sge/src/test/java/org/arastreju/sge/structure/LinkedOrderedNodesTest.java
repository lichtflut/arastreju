/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNScalar;
import org.junit.Test;

/**
 * <p>
 *  Test case for {@link LinkedOrderedNodes}.
 * </p>
 *
 * <p>
 * 	Created Jan 24, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class LinkedOrderedNodesTest {
	
	private static final ResourceID ID = new SimpleResourceID("http://l2r.info#ID");
	
	private static final int SIZE = 100;
	
	// ----------------------------------------------------

	@Test
	public void testPredecessorSort() {
		final Set<ResourceNode> all = new HashSet<ResourceNode>();
		ResourceNode current = create(0);
		all.add(current);
		for(int i=1; i <= SIZE; i++) {
			ResourceNode next = create(i);
			all.add(next);
			current.addAssociation(Aras.IS_PREDECESSOR_OF, next);
			current = next;
		}
		
		List<ResourceNode> sorted = LinkedOrderedNodes.sortResources(all);
		for(int i=0; i < SIZE; i++) {
			Assert.assertEquals(i, getID(sorted.get(i)));
		}
	}
	
	@Test
	public void testSuccessorSort() {
		final Set<ResourceNode> all = new HashSet<ResourceNode>();
		ResourceNode current = create(0);
		all.add(current);
		for(int i=1; i <= SIZE; i++) {
			ResourceNode next = create(i);
			all.add(next);
			next.addAssociation(Aras.IS_SUCCESSOR_OF, current);
			current = next;
		}
		
		List<ResourceNode> sorted = LinkedOrderedNodes.sortResources(all);
		
		for(int i=0; i < SIZE; i++) {
			Assert.assertEquals(i, getID(sorted.get(i)));
		}
		
	}
	// ----------------------------------------------------
	
	private ResourceNode create(int id) {
		final ResourceNode node = new SNResource();
		node.addAssociation(ID, new SNScalar(id));
		return node;
	}
	
	private int getID(ResourceNode node) {
		SemanticNode sn = SNOPS.singleObject(node, ID);
		if (sn != null) {
			return sn.asValue().getIntegerValue().intValue();
		} else {
			return 0;
		}
		
	}
	
}
