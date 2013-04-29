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
package org.arastreju.sge.model.nodes.views;

import de.lichtflut.infra.logging.StopWatch;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.AreaAveragingScaleFilter;

/**
 * <p>
 *  Test case for InheritedDecorator.
 * </p>
 *
 * <p>
 *  Created 19.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class InheritedDecoratorTest {

    @Test
    public void shouldAddAllInherited() {
        ResourceNode a = new SNResource();
        ResourceNode b = new SNResource();
        ResourceNode c = new SNResource();

        ResourceNode o = new SNResource();

        a.addAssociation(Aras.HAS_FORENAME, new SNText("Fred"));
        b.addAssociation(Aras.HAS_FORENAME, new SNText("Ted"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M."));

        o.addAssociation(Aras.HAS_FORENAME, new SNText("Ed"));
        o.addAssociation(Aras.HAS_SURNAME, new SNText("Flintstone"));

        o.addAssociation(Aras.INHERITS_FROM, a);
        o.addAssociation(Aras.INHERITS_FROM, b);
        o.addAssociation(Aras.INHERITS_FROM, c);

        Assert.assertEquals(5, o.getAssociations().size());
        Assert.assertEquals("Ed", SNOPS.singleObject(o, Aras.HAS_FORENAME).asValue().getStringValue());

        ResourceNode inherited = new InheritedDecorator(o);

        Assert.assertEquals(8, inherited.getAssociations().size());
        Assert.assertEquals("Flintstone", SNOPS.singleObject(inherited, Aras.HAS_SURNAME).asValue().getStringValue());
        Assert.assertEquals("M.", SNOPS.singleObject(inherited, Aras.HAS_NAME_PART).asValue().getStringValue());

        Assert.assertEquals(3, SNOPS.associations(inherited, Aras.HAS_FORENAME).size());
    }

    @Test
    public void shouldNotAddRevoked() {
        ResourceNode a = new SNResource();
        ResourceNode b = new SNResource();
        ResourceNode c = new SNResource();

        ResourceNode o = new SNResource();

        ResourceNode revocations = new SNResource();

        a.addAssociation(Aras.HAS_FORENAME, new SNText("Fred"));
        b.addAssociation(Aras.HAS_FORENAME, new SNText("Ted"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M1"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M2"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M3"));

        o.addAssociation(Aras.HAS_SURNAME, new SNText("Flintstone"));

        o.addAssociation(Aras.INHERITS_FROM, a);
        o.addAssociation(Aras.INHERITS_FROM, b);
        o.addAssociation(Aras.INHERITS_FROM, c);

        o.addAssociation(Aras.REVOKES, revocations);

        revocations.addAssociation(Aras.HAS_FORENAME, new SNText("Ted"));
        revocations.addAssociation(Aras.HAS_NAME_PART, Aras.ANY);

        Assert.assertEquals(5, o.getAssociations().size());

        ResourceNode inherited = new InheritedDecorator(o);

        Assert.assertEquals(6, inherited.getAssociations().size());
        Assert.assertEquals("Flintstone", SNOPS.singleObject(inherited, Aras.HAS_SURNAME).asValue().getStringValue());
        Assert.assertEquals("Fred", SNOPS.singleObject(inherited, Aras.HAS_FORENAME).asValue().getStringValue());

        Assert.assertEquals(1, SNOPS.associations(inherited, Aras.HAS_FORENAME).size());
    }

    public static void main(String[] args) {

        ResourceNode a = new SNResource();
        ResourceNode b = new SNResource();
        ResourceNode c = new SNResource();
        ResourceNode d = new SNResource();

        ResourceNode o = new SNResource();

        ResourceNode revocations = new SNResource();

        a.addAssociation(Aras.HAS_FORENAME, new SNText("Fred"));
        b.addAssociation(Aras.HAS_FORENAME, new SNText("Ted"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M1"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M2"));
        c.addAssociation(Aras.HAS_NAME_PART, new SNText("M3"));
        d.addAssociation(Aras.HAS_NAME_PART, new SNText("M3"));

        o.addAssociation(Aras.HAS_SURNAME, new SNText("Flintstone"));

        o.addAssociation(Aras.INHERITS_FROM, a);
        o.addAssociation(Aras.INHERITS_FROM, b);
        o.addAssociation(Aras.INHERITS_FROM, c);
        o.addAssociation(Aras.INHERITS_FROM, d);

        o.addAssociation(Aras.REVOKES, revocations);

        revocations.addAssociation(Aras.HAS_FORENAME, new SNText("Ted"));
        revocations.addAssociation(Aras.HAS_NAME_PART, Aras.ANY);

        ResourceNode inherited = new InheritedDecorator(o);

        // warm up
        inherited.getAssociations();

        StopWatch sw = new StopWatch();
        for (int i = 0; i < 1000 * 1000; i++) {
            inherited.getAssociations();
        }
        sw.displayTime("needed");

    }

}
