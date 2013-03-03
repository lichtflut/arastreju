/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.traverse;

import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 *  Test cases for Walkers.
 * </p>
 *
 * <p>
 * 	Created Jun 26, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class WalkerTest {

    @Test
    public void shouldWalkRightWay() {

        SimpleResourceID pred1 = new SimpleResourceID("http://lichtflut.de/pred1");
        SimpleResourceID pred2 = new SimpleResourceID("http://lichtflut.de/pred2");

        ResourceNode node0 = new SNResource();
        ResourceNode node1 = new SNResource();
        ResourceNode node2 = new SNResource();

        node0.addAssociation(pred1, node1);
        node0.addAssociation(pred2, node2);

        Walker walker = Walker.start(node0).walk(pred1);

        Assert.assertFalse(walker.isEmpty());
        Assert.assertEquals(1, walker.size());

        Assert.assertTrue(walker.matches(node1));
        Assert.assertFalse(walker.matches(node2));
        Assert.assertFalse(walker.matches("hello"));
    }

    @Test
    public void shouldHandleMissingNodesWithoutException() {
        ResourceNode node = new SNResource();

        Walker walker = Walker.start(node).walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(walker.isEmpty());
        Assert.assertEquals(0, walker.size());

        Assert.assertFalse(walker.matches(new SimpleResourceID()));
        Assert.assertFalse(walker.matches("hello"));
    }

    @Test
    public void valueNodesCanBeTestedAgainstResources() {
        ValueNode node = new SNText("anything");

        Walker walker = Walker.start(node).walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(walker.isEmpty());
        Assert.assertEquals(0, walker.size());

        Assert.assertFalse(walker.matches(new SimpleResourceID()));
        Assert.assertFalse(walker.matches("hello"));
    }

    @Test
    public void resourceNodesCanBeTestedAgainstValues() {
        ResourceNode resource = new SNResource();

        Walker walker = Walker.start(resource).walk(new SimpleResourceID("http://does.not.exist/xyz"));

        Assert.assertTrue(walker.isEmpty());
        Assert.assertEquals(0, walker.size());

        Assert.assertFalse(walker.matches(new SimpleResourceID()));
        Assert.assertFalse(walker.matches("hello"));
    }

}
