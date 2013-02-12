package org.arastreju.bindings.memory;

import org.arastreju.bindings.memory.conversation.MemConversation;
import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.bindings.memory.storage.MemStorage;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 *  Integration test case for MemConversation.
 * </p>

 * <p>
 * Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConversationTest {

    QualifiedName qnFlower = new QualifiedName("http://example.com/flower");

    MemConversation conversation;

    // ----------------------------------------------------

    @Before
    public void setUp() {
        MemStorage storage = new MemStorage();
        MemConnection connection = new MemConnection(storage, null);
        MemConversationContext context = new MemConversationContext(connection);
        conversation = new MemConversation(context);
    }

    // ----------------------------------------------------

    @Test
    public void testStoreAndFind() {
        ResourceNode flower = conversation.findResource(qnFlower);
        Assert.assertNull(flower);

        flower = new SNResource(qnFlower);
        conversation.attach(flower);

        ResourceNode found = conversation.findResource(qnFlower);
        Assert.assertNotNull(found);

        Assert.assertNotSame(flower, found);

    }

    @Test
    public void testResolving() {
        ResourceNode flower = conversation.resolve(new SimpleResourceID(qnFlower));
        Assert.assertNotNull(flower);
        Assert.assertEquals(flower.getQualifiedName(), qnFlower);
    }

    @Test
    public void testAttachAndDetach() {
        ResourceNode flower = new SNResource(qnFlower);
        conversation.attach(flower);

        flower.addAssociation(RDF.TYPE, RDFS.CLASS);

        ResourceNode found = conversation.findResource(qnFlower);
        Assert.assertNotNull(found);
        Assert.assertNotSame(flower, found);
        Assert.assertEquals(1, found.getAssociations().size());
        Assert.assertEquals(1, flower.getAssociations().size());

        conversation.detach(flower);

        flower.addAssociation(Aras.HAS_NAME, new SNText("Flower"));
        Assert.assertEquals(2, flower.getAssociations().size());
        Assert.assertEquals(1, found.getAssociations().size());

    }

}
