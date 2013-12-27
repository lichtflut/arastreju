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
package org.arastreju.tck;

import org.arastreju.sge.Conversation;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.Accessibility;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.ContextID;
import org.arastreju.sge.io.RdfXmlBinding;
import org.arastreju.sge.io.SemanticGraphIO;
import org.arastreju.sge.io.SemanticIOException;
import org.arastreju.sge.model.*;
import org.arastreju.sge.model.associations.DefaultStatementMetaInfo;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.impl.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.arastreju.sge.SNOPS.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * <p>
 *  Abstract base for compatibility test of Arastreju bindings.
 * </p>
 *
 * <p>
 *  Created 22.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractConversationTest {

    protected static final QualifiedName qnVehicle = QualifiedName.from("http://q#", "Verhicle");
    protected static final QualifiedName qnCar = QualifiedName.from("http://q#", "Car");
    protected static final QualifiedName qnBike = QualifiedName.from("http://q#", "Bike");
    protected static final QualifiedName qnEmployedBy = QualifiedName.from("http://q#", "employedBy");
    protected static final QualifiedName qnHasEmployees = QualifiedName.from("http://q#", "hasEmployees");
    protected static final QualifiedName qnKnows = QualifiedName.from("http://q#", "knows");

    protected static final SNContext accessContext =
            new SNContext(QualifiedName.from(Context.LOCAL_CONTEXTS_NAMESPACE, "Access"));
    protected static final SNContext sourceContext =
            new SNContext(QualifiedName.from(Context.LOCAL_CONTEXTS_NAMESPACE, "Source"));
    protected static final SNContext publicContext = new SNContext(QualifiedName.from(Context.LOCAL_CONTEXTS_NAMESPACE, "Public"));
    protected static final SNContext privateContext = new SNContext(QualifiedName.from(Context.LOCAL_CONTEXTS_NAMESPACE, "Private"));

    static {
        sourceContext.setAccessContext(accessContext);
        publicContext.setVisibility(Accessibility.PUBLIC);
        privateContext.setVisibility(Accessibility.PRIVATE);

    }

    // ----------------------------------------------------

    private GraphDataStore store;
    private GraphDataConnection connection;
    private Conversation conversation;

    // ----------------------------------------------------

    /**
     * TO be overridden by concrete tests.
     *
     * @return A new store instance.
     */
    protected abstract GraphDataStore createStore() throws Exception;

    // ----------------------------------------------------

    @Before
    public void setUp() throws Exception {
        store = createStore();
        connection = new GraphDataConnectionImpl(store);
        ConversationContextImpl context = new ConversationContextImpl();
        ConversationControllerImpl controller = new ConversationControllerImpl(connection, context);
        conversation = new ConversationImpl(controller);
        context.setContextResolver(new ContextResolverImpl(controller));
    }

    @After
    public void tearDown() throws Exception {
        conversation.close();
        connection.close();
        store.close();
    }

    // ----------------------------------------------------

    @Test
    public void testInstantiation() throws IOException {
        ResourceNode node = new SNResource(QualifiedName.from("http://q#", "N1"));
        conversation.attach(node);
    }

    @Test
    public void testFind() throws IOException {
        QualifiedName qn = QualifiedName.from("http://q#", "N1");
        ResourceNode node = new SNResource(qn);
        conversation.attach(node);

        ResourceNode node2 = conversation.findResource(qn);

        Assert.assertNotNull(node2);
    }

    @Test
    public void testResolveAndFind() throws IOException {
        ResourceNode found = conversation.findResource(qnVehicle);
        assertNull(found);

        ResourceNode resolved = conversation.resolve(SNOPS.id(qnVehicle));
        Assert.assertNotNull(resolved);

        found = conversation.findResource(qnVehicle);
        Assert.assertNotNull(found);
    }

    @Test
    public void testMerge() throws IOException {
        QualifiedName qn = QualifiedName.from("http://q#", "N1");
        ResourceNode node = new SNResource(qn);
        conversation.attach(node);

        conversation.attach(node);

        ResourceNode node2 = conversation.findResource(qn);

        Assert.assertNotNull(node2);
    }

    @Test
    public void testDetaching() throws IOException {
        final ResourceNode car = new SNResource(qnCar);
        conversation.attach(car);

        final ResourceNode car3 = conversation.findResource(qnCar);
        assertEquals(car, car3);

        conversation.detach(car);

        final ResourceNode car4 = conversation.findResource(qnCar);
        Assert.assertNotSame(car, car4);
    }

    @Test
    public void testMerging() throws IOException {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car1 = new SNResource(qnCar);

        conversation.attach(car1);

        SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));

        // detach
        conversation.detach(car1);
        conversation.detach(vehicle);

        SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
        SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

        // attach again
        conversation.attach(car1);

        // detach and find again
        conversation.detach(car1);
        final ResourceNode car2 = conversation.findResource(qnCar);
        Assert.assertNotNull(car2);
        Assert.assertNotSame(car1, car2);

        final ResourceNode subClasss = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();
        assertEquals(vehicle, subClasss);

        final ValueNode brandname = SNOPS.singleObject(car2, Aras.HAS_BRAND_NAME).asValue();
        assertEquals(brandname.getStringValue(), "BMW");

        final ValueNode propername = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
        assertEquals(propername.getStringValue(), "Knut");
    }

    @Test
    public void testPersisting() throws IOException {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car = new SNResource(qnCar);

        SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
        SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));

        conversation.attach(car);

        // detach and find again
        conversation.detach(car);
        final ResourceNode car2 = conversation.findResource(qnCar);
        Assert.assertNotSame(car, car2);

        final ResourceNode res = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();
        assertEquals(vehicle, res);

        final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
        assertEquals(value.getStringValue(), "BMW");
    }

    @Test
    public void testRemove() {
        final SNClass vehicle = SNClass.from(new SNResource(qnVehicle));
        final SNClass car = SNClass.from(new SNResource(qnCar));
        final SNClass bike = SNClass.from(new SNResource(qnBike));

        final ResourceNode car1 = car.createInstance();

        SNOPS.associate(vehicle, RDFS.SUB_CLASS_OF, RDF.TYPE);
        SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
        SNOPS.associate(bike, RDFS.SUB_CLASS_OF, vehicle);

        conversation.attach(vehicle);
        conversation.attach(bike);

        SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
        SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

        conversation.attach(car1);

        conversation.remove(car);
        assertNull(conversation.findResource(qnCar));
        conversation.remove(car1);

        Assert.assertFalse(car1.isAttached());
        Assert.assertFalse(car.isAttached());

        Assert.assertTrue(vehicle.isAttached());

        conversation.detach(vehicle);

        ResourceNode found = conversation.findResource(qnVehicle);
        Assert.assertNotNull(found);
        assertEquals(RDF.TYPE, SNOPS.singleObject(found, RDFS.SUB_CLASS_OF));
    }

    @Test
    public void testDirectRemoval() {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car1 = new SNResource(qnCar);

        final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
        SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
        SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

        conversation.attach(car1);

        final Statement stored = SNOPS.singleAssociation(car1, Aras.HAS_BRAND_NAME);
        assertEquals(association.hashCode(), stored.hashCode());

        assertEquals(3, car1.getAssociations().size());
        Assert.assertFalse(associations(car1, Aras.HAS_BRAND_NAME).isEmpty());
        Assert.assertTrue("Association not present", car1.getAssociations().contains(association));

        final boolean removedFlag = car1.removeAssociation(association);
        Assert.assertTrue(removedFlag);

        assertEquals(2, car1.getAssociations().size());
        Assert.assertTrue(associations(car1, Aras.HAS_BRAND_NAME).isEmpty());

    }

    @Test
    public void testAttachingRemoval() {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car1 = new SNResource(qnCar);

        conversation.attach(car1);

        final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
        SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
        SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

        // detach
        conversation.detach(car1);

        assertEquals(3, car1.getAssociations().size());
        Assert.assertFalse(associations(car1, Aras.HAS_BRAND_NAME).isEmpty());
        Assert.assertTrue("Association not present", car1.getAssociations().contains(association));

        final boolean removedFlag = car1.removeAssociation(association);
        Assert.assertTrue(removedFlag);

        conversation.attach(car1);

        final ResourceNode car2 = conversation.findResource(qnCar);
        Assert.assertNotSame(car1, car2);

        assertEquals(2, car2.getAssociations().size());
        Assert.assertTrue(associations(car2, Aras.HAS_BRAND_NAME).isEmpty());

    }

    @Test
    public void testBidirectionalAssociations() {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car = new SNResource(qnCar);

        final ResourceID pred1 = new SimpleResourceID("http://arastreju.org/stuff#", "P1");
        final ResourceID pred2 = new SimpleResourceID("http://arastreju.org/stuff#", "P2");

        SNOPS.associate(vehicle, pred1, car);
        SNOPS.associate(car, pred2, vehicle);
        conversation.attach(vehicle);

        final ResourceNode vehicleLoaded = conversation.findResource(qnVehicle);
        final ResourceNode carLoaded = conversation.findResource(qnCar);

        Assert.assertFalse(vehicleLoaded.getAssociations().isEmpty());
        Assert.assertFalse(carLoaded.getAssociations().isEmpty());

        Assert.assertFalse(associations(vehicleLoaded, pred1).isEmpty());
        Assert.assertFalse(associations(carLoaded, pred2).isEmpty());
    }

    @Test
    public void testValueIndexing() throws IOException {
        final ResourceNode car = new SNResource(qnCar);
        SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));

        conversation.attach(car);

        Query query = conversation.createQuery().addField(Aras.HAS_PROPER_NAME.toURI(), "BMW");
        QueryResult result = query.getResult();

        Assert.assertNotNull(result);

        Assert.assertFalse(result.isEmpty());

        assertEquals(qnCar, result.getSingleNode().getQualifiedName());
    }

    @Test
    public void testDataTypes() throws IOException {
        final ResourceNode car = new SNResource(qnCar);

        conversation.attach(car);

        SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
        conversation.detach(car);

        final ResourceNode car2 = conversation.findResource(qnCar);
        Assert.assertNotSame(car, car2);
        final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();

        assertEquals(value.getStringValue(), "BMW");
    }

    @Test
    public void testSNViews() throws IOException {
        final QualifiedName qnVehicle = QualifiedName.from("http://q#", "Verhicle");
        ResourceNode vehicle = new SNResource(qnVehicle);
        conversation.attach(vehicle);

        final QualifiedName qnCar = QualifiedName.from("http://q#", "Car");
        ResourceNode car = new SNResource(qnCar);
        conversation.attach(car);

        SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);

        conversation.getConversationContext().clear();

        car = conversation.findResource(qnCar);
        vehicle = conversation.findResource(qnVehicle);

        Assert.assertTrue(SNClass.from(car).isSpecializationOf(vehicle));
    }

    @Test
    public void testGraphImport() throws IOException, SemanticIOException {
        final SemanticGraphIO io = new RdfXmlBinding();
        final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("test-statements.rdf.xml"));

        conversation.attach(graph);

        final QualifiedName qn = QualifiedName.from("http://test.arastreju.org/common#Person");
        final ResourceNode node = conversation.findResource(qn);
        Assert.assertNotNull(node);

        final ResourceNode hasChild = conversation.findResource(SNOPS.qualify("http://test.arastreju.org/common#hasChild"));
        Assert.assertNotNull(hasChild);
        assertEquals(new SimpleResourceID("http://test.arastreju.org/common#hasParent"), objects(hasChild, Aras.INVERSE_OF).iterator().next());

        final ResourceNode marriedTo = conversation.findResource(SNOPS.qualify("http://test.arastreju.org/common#isMarriedTo"));
        Assert.assertNotNull(marriedTo);
        assertEquals(marriedTo, objects(marriedTo, Aras.INVERSE_OF).iterator().next());
    }

    @Test
    public void testSerialization() throws IOException, SemanticIOException, ClassNotFoundException {
        final SemanticGraphIO io = new RdfXmlBinding();
        final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("test-statements.rdf.xml"));
        conversation.attach(graph);

        final QualifiedName qn = QualifiedName.from("http://test.arastreju.org/common#Person");
        final ResourceNode node = conversation.findResource(qn);

        Assert.assertTrue(node.isAttached());

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(node);

        byte[] bytes = out.toByteArray();
        out.close();

        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));

        final ResourceNode read = (ResourceNode) in.readObject();
        Assert.assertFalse(read.isAttached());
    }

    @Test
    public void testInferencingInverseOfBidirectional() {
        final ResourceNode knows = new SNResource(qnKnows);

        associate(knows, Aras.INVERSE_OF, knows);

        conversation.attach(knows);

        Assert.assertTrue(objects(knows, Aras.INVERSE_OF).contains(knows));

        conversation.detach(knows);

        // preparation done.

        ResourceNode mike = new SNResource(qualify("http://q#Mike"));
        ResourceNode kent = new SNResource(qualify("http://q#Kent"));

        conversation.attach(mike);

        associate(mike, knows, kent);

        conversation.detach(kent);

        kent = conversation.findResource(kent.getQualifiedName());

        Assert.assertTrue(objects(kent, knows).contains(mike));

    }

    @Test
    public void testInferencingInverseOf() {
        final ResourceNode hasEmployees = new SNResource(qnHasEmployees);
        final ResourceNode isEmployedBy = new SNResource(qnEmployedBy);

        associate(hasEmployees, Aras.INVERSE_OF, isEmployedBy);
        associate(isEmployedBy, Aras.INVERSE_OF, hasEmployees);

        conversation.attach(hasEmployees);

        Assert.assertTrue(hasEmployees.isAttached());
        Assert.assertTrue(isEmployedBy.isAttached());

        // preparation done.

        ResourceNode mike = new SNResource(qualify("http://q#Mike"));
        ResourceNode corp = new SNResource(qualify("http://q#Corp"));

        conversation.attach(mike);
        Assert.assertTrue(mike.isAttached());

        associate(mike, isEmployedBy, corp);
        Assert.assertTrue(corp.isAttached());

        conversation.detach(corp);

        corp = conversation.findResource(corp.getQualifiedName());

        Assert.assertTrue(objects(corp, hasEmployees).contains(mike));
        Assert.assertTrue(objects(mike, isEmployedBy).contains(corp));

        remove(corp, hasEmployees);
        Assert.assertFalse(objects(corp, hasEmployees).contains(mike));

        mike = conversation.findResource(mike.getQualifiedName());
        Assert.assertFalse(objects(mike, isEmployedBy).contains(corp));

    }

    @Test
    public void testInferencingSubClasses() {
        final SNClass vehicleClass = SNClass.from(new SNResource(qnVehicle));
        final SNClass carClass = SNClass.from(new SNResource(qnCar));
        SNOPS.associate(carClass, RDFS.SUB_CLASS_OF, vehicleClass);

        final SNEntity car = carClass.createInstance();
        final SNEntity vehicle = vehicleClass.createInstance();

        conversation.attach(vehicle);
        conversation.attach(car);

        QueryResult res = conversation.createQuery().addField(RDF.TYPE.toURI(), qnVehicle).getResult();
        Assert.assertNotNull(res);

        assertEquals(2, res.size());

        remove(car, RDF.TYPE);

        res = conversation.createQuery().addField(RDF.TYPE.toURI(), qnVehicle).getResult();
        Assert.assertNotNull(res);

        assertEquals(1, res.size());

    }

    @Test
    public void testMultipleContexts() {
        final ResourceNode vehicle = new SNResource(qnVehicle);
        final ResourceNode car1 = new SNResource(qnCar);

        final String ctxNamepsace = "http://lf.de/ctx#";
        final Context ctx1 = ContextID.forContext(ctxNamepsace, "ctx1");
        final Context ctx2 = ContextID.forContext(ctxNamepsace, "ctx2");
        final Context ctx3 = ContextID.forContext(ctxNamepsace, "ctx3");

        final Context convCtx1 = ContextID.forContext(ctxNamepsace, "convCtx1");
        final Context convCtx2 = ContextID.forContext(ctxNamepsace, "convCtx1");

        conversation.attach(car1);

        ConversationContext ctx = conversation.getConversationContext();

        ctx.setReadContexts(ctx1, ctx2, ctx3, convCtx1, convCtx2);

        car1.addAssociation(Aras.HAS_BRAND_NAME, new SNText("BMW"), DefaultStatementMetaInfo.from(ctx1));
        ctx.setPrimaryContext(convCtx1);
        car1.addAssociation(RDFS.SUB_CLASS_OF, vehicle, DefaultStatementMetaInfo.from(ctx1, ctx2));
        ctx.setPrimaryContext(convCtx2);
        car1.addAssociation(Aras.HAS_PROPER_NAME, new SNText("Knut"), DefaultStatementMetaInfo.from(ctx1, ctx2, ctx3));

        // detach
        conversation.detach(car1);

        final ResourceNode car2 = conversation.findResource(qnCar);
        Assert.assertNotSame(car1, car2);

        final Context[] cl1 = SNOPS.singleAssociation(car2, Aras.HAS_BRAND_NAME).getContexts();
        final Context[] cl2 = SNOPS.singleAssociation(car2, RDFS.SUB_CLASS_OF).getContexts();
        final Context[] cl3 = SNOPS.singleAssociation(car2, Aras.HAS_PROPER_NAME).getContexts();

        Assert.assertArrayEquals(new Context[]{ctx1}, cl1);
        Assert.assertArrayEquals(new Context[]{convCtx1, ctx1, ctx2}, cl2);
        Assert.assertArrayEquals(new Context[]{convCtx2, ctx1, ctx2, ctx3}, cl3);
    }

    @Test
    public void testNonCommitNodeIndexing() throws IOException {
        final ResourceNode car = new SNResource(qnCar);
        SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));

        TransactionControl tx = conversation.beginTransaction();

        conversation.attach(car);

        /* no commit here */

        Query query = conversation.createQuery().addField(Aras.HAS_PROPER_NAME.toURI(), "BMW");
        QueryResult result = query.getResult();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        assertEquals(qnCar, result.getSingleNode().getQualifiedName());

        conversation.remove(car);

        /* commit after remove operations statements */
        tx.finish();

        query = conversation.createQuery().addField(Aras.HAS_PROPER_NAME.toURI(), "BMW");
        result = query.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());

    }

    @Test
    public void testNonCommitStmtIndexing() throws IOException {
        final ResourceNode car = new SNResource(qnCar);
        final Statement stmt1 = SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));

        TransactionControl tx = conversation.beginTransaction();
        conversation.attach(car);

        /* no commit here */

        Query query = conversation.createQuery().addField(Aras.HAS_PROPER_NAME.toURI(), "BMW");
        QueryResult result = query.getResult();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        assertEquals(qnCar, result.getSingleNode().getQualifiedName());

        conversation.removeStatement(stmt1);

        /* for removing we need a commit first */
        tx.finish();

        query = conversation.createQuery().addField(Aras.HAS_PROPER_NAME.toURI(), "BMW");
        result = query.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue("Result should be empty: " + result.toList(), result.isEmpty());
    }

    @Test
    public void testAccessContextsDuringIndexing() {
        final SNClass car = SNClass.from(new SNResource(qnCar));
        final SNEntity aCar = car.createInstance();

        SNContext accessContext = new SNContext(QualifiedName.generate());
        SNContext sourceContext = new SNContext(QualifiedName.generate());
        sourceContext.setAccessContext(accessContext);
        conversation.attach(sourceContext);
        Assert.assertTrue(accessContext.isAttached());

        conversation.getConversationContext().setPrimaryContext(sourceContext);
        conversation.attach(aCar);

        // Check with independent context
        SNContext otherContext = new SNContext(QualifiedName.generate());
        conversation.getConversationContext().setPrimaryContext(otherContext);
        Query query1 = conversation.createQuery().addField(RDF.TYPE, qnCar);
        assertEquals(0, query1.getResult().size());

        // Check with access context
        conversation.getConversationContext().setPrimaryContext(accessContext);
        Query query2 = conversation.createQuery().addField(RDF.TYPE, qnCar);
        assertEquals(1, query2.getResult().size());

        // Check with source context
        conversation.getConversationContext().setPrimaryContext(sourceContext);
        Query query3 = conversation.createQuery().addField(RDF.TYPE, qnCar);
        assertEquals(1, query3.getResult().size());
    }

    @Test
    public void testContextRegarding() {
        final SNClass car = SNClass.from(new SNResource(qnCar));
        final SNEntity aCar = car.createInstance();

        conversation.attach(sourceContext);
        conversation.attach(publicContext);
        conversation.attach(privateContext);

        conversation.attach(aCar);

        aCar.addAssociation(Aras.HAS_BRAND_NAME, new SNText("BMW"), DefaultStatementMetaInfo.from(sourceContext));
        aCar.addAssociation(RDFS.LABEL, new SNText("A BMW car"), DefaultStatementMetaInfo.from(publicContext));
        aCar.addAssociation(Aras.HAS_PROPER_NAME, new SNText("Knut"), DefaultStatementMetaInfo.from(privateContext));

        conversation.detach(aCar);
        conversation.getConversationContext().setPrimaryContext(sourceContext);

        final ResourceNode car2 = conversation.findResource(aCar.getQualifiedName());
        Assert.assertNotSame(aCar, car2);

        // primary context
        assertEquals(new SNText("BMW"), fetchObject(car2, Aras.HAS_BRAND_NAME));
        // public
        assertEquals(new SNText("A BMW car"), fetchObject(car2, RDFS.LABEL));
        // private
        assertNull("Expected not to see a proper name.", fetchObject(car2, Aras.HAS_PROPER_NAME));

    }

    @Test
    public void testAccessContextRegarding() {
        final SNClass car = SNClass.from(new SNResource(qnCar));
        final SNEntity aCar = car.createInstance();

        conversation.attach(sourceContext);
        conversation.attach(publicContext);
        conversation.attach(privateContext);

        conversation.attach(aCar);

        aCar.addAssociation(Aras.HAS_BRAND_NAME, new SNText("BMW"), DefaultStatementMetaInfo.from(sourceContext));
        aCar.addAssociation(RDFS.LABEL, new SNText("A BMW car"), DefaultStatementMetaInfo.from(publicContext));
        aCar.addAssociation(Aras.HAS_PROPER_NAME, new SNText("Knut"), DefaultStatementMetaInfo.from(privateContext));

        conversation.detach(aCar);
        conversation.getConversationContext().setPrimaryContext(accessContext);

        final ResourceNode car2 = conversation.findResource(aCar.getQualifiedName());
        Assert.assertNotSame(aCar, car2);

        // primary context
        assertEquals(new SNText("BMW"), fetchObject(car2, Aras.HAS_BRAND_NAME));
        // public
        assertEquals(new SNText("A BMW car"), fetchObject(car2, RDFS.LABEL));
        // private
        assertNull(fetchObject(car2, Aras.HAS_PROPER_NAME));
    }

    @Test
    public void testStrictContextRegarding() {
        final SNClass car = SNClass.from(new SNResource(qnCar));
        final SNEntity aCar = car.createInstance();

        conversation.attach(sourceContext);
        conversation.attach(publicContext);
        conversation.attach(privateContext);

        conversation.attach(aCar);

        aCar.addAssociation(Aras.HAS_BRAND_NAME, new SNText("BMW"), DefaultStatementMetaInfo.from(sourceContext));
        aCar.addAssociation(Aras.HAS_NAME_PART, new SNText("M3"), DefaultStatementMetaInfo.from(accessContext));
        aCar.addAssociation(RDFS.LABEL, new SNText("A BMW car"), DefaultStatementMetaInfo.from(publicContext));
        aCar.addAssociation(Aras.HAS_PROPER_NAME, new SNText("Knut"), DefaultStatementMetaInfo.from(privateContext));

        conversation.detach(aCar);

        // Set to strict
        conversation.getConversationContext().setPrimaryContext(sourceContext).setStrictContextRegarding(true);

        final ResourceNode car2 = conversation.findResource(aCar.getQualifiedName());
        Assert.assertNotSame(aCar, car2);

        // primary context
        assertEquals(new SNText("BMW"), fetchObject(car2, Aras.HAS_BRAND_NAME));
        // access context
        assertNull(fetchObject(car2, Aras.HAS_NAME_PART));
        // public
        assertNull(fetchObject(car2, RDFS.LABEL));
        // private
        assertNull(fetchObject(car2, Aras.HAS_PROPER_NAME));
    }

    @Test
    public void testTimeConstraints() {
        final ResourceNode thing = new SNResource();

        conversation.attach(thing);

        Statement stmt1 = Assertor.start(thing, Aras.HAS_PROPER_NAME, new SNText("ShortTimeValid"))
                .validFrom(1000 * 1000L)
                .validUntil(1000 * 2000L)
                .build();
        conversation.addStatement(stmt1);

        Statement stmt2 = Assertor.start(thing, RDFS.LABEL, new SNText("AllTimeValid"))
                .build();
        conversation.addStatement(stmt2);

        conversation.detach(thing);

        final ResourceNode thing2 = conversation.findResource(thing.getQualifiedName());
        Assert.assertNotSame(thing, thing2);

        Statement s1 = fetchAssociation(thing2, Aras.HAS_PROPER_NAME);
        assertEquals(1000 * 1000L, s1.getMetaInfo().getValidFrom().getTime());
        assertEquals(1000 * 2000L, s1.getMetaInfo().getValidUntil().getTime());

        Statement s2 = fetchAssociation(thing2, RDFS.LABEL);
        assertNull(s2.getMetaInfo().getValidFrom());
        assertNull(s2.getMetaInfo().getValidUntil());

    }

}
