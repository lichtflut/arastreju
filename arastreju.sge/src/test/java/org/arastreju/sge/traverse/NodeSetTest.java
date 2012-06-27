package org.arastreju.sge.traverse;

import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 *  Test cases for NodeSet.
 * </p>
 *
 * <p>
 * 	Created Jun 26, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeSetTest {

    @Test
    public void shouldHandleMissingNodesWithoutException() {
        ResourceNode node = new SNResource();

        NodeSet start = new ResourceNodeSet(node);
        NodeSet nodeSet = start.walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(nodeSet.isEmpty());
        Assert.assertEquals(0, nodeSet.size());

        Assert.assertFalse(nodeSet.matches(new SimpleResourceID()));
        Assert.assertFalse(nodeSet.matches("hello"));
    }

    @Test
    public void valueNodesCanBeTestedAgainstResources() {
        ValueNode node = new SNText("anything");

        NodeSet start = new ValueNodeSet(node);
        NodeSet nodeSet = start.walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(nodeSet.isEmpty());
        Assert.assertEquals(0, nodeSet.size());

        Assert.assertFalse(nodeSet.matches(new SimpleResourceID()));
        Assert.assertFalse(nodeSet.matches("hello"));
    }

    @Test
    public void resourceNodesCanBeTestedAgainstValues() {
        ResourceNode resource = new SNResource();

        NodeSet start = new ResourceNodeSet(resource);
        NodeSet nodeSet = start.walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(start.asValue().isEmpty());
        Assert.assertEquals(0, start.asValue().size());

        Assert.assertFalse(start.asValue().matches(new SimpleResourceID()));
        Assert.assertFalse(nodeSet.matches("hello"));
    }

}
