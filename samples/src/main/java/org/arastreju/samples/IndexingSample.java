package org.arastreju.samples;

import static org.arastreju.sge.SNOPS.associate;
import static org.arastreju.sge.SNOPS.id;
import static org.arastreju.sge.SNOPS.resource;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

/**
 * <p>
 *  Simple sample to demonstrate attaching and detaching a statement.
 * </p>
 *
 * <p>
 * 	Created Feb 22, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class IndexingSample {
	private QualifiedName RESOURCE_URI_ALICE = new QualifiedName("http://example.org/alice");
	private QualifiedName RESOURCE_URI_BOB = new QualifiedName("http://example.org/bob");
	private QualifiedName RESOURCE_URI_EVE = new QualifiedName("http://example.org/eve");
	private QualifiedName RESOURCE_URI_KNOWS = new QualifiedName("http://example.org/knows");

	private final Arastreju aras;
	private final ArastrejuGate gate;
	private final Conversation conv;

	public IndexingSample() {
		aras = Arastreju.getInstance();
		gate = aras.openMasterGate();
		conv = gate.startConversation();
	}

	public void sample() {
		createAssociation(resource(id(RESOURCE_URI_ALICE)), id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_BOB));

		/* search for everyone who knows bob */
		Query query = conv.createQuery().addField(RESOURCE_URI_KNOWS.toURI(), RESOURCE_URI_BOB.toURI());
		QueryResult result = query.getResult();

		/* in this case we know it's only alice, so we can use .getSingleNode() */
		System.out.println("found in index: " + result.getSingleNode().toURI());

		/* introduce bob to eve, too */
		createAssociation(resource(id(RESOURCE_URI_EVE)), id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_BOB));

		/* search again */
		result = query.getResult();

		/* now we should find two, alice and eve */
		for (ResourceNode node : result) {
			System.out.println("found in index: " + node.toURI());
		}
	}

	private void createAssociation(ResourceNode subject, ResourceID predicate, ResourceID object) {
		Statement stmt = associate(subject, predicate, object);
		conv.addStatement(stmt);
	}

	private void tearDown() {
		conv.close();
		gate.close();
	}

	public static void main(String[] args) {
		IndexingSample smp = new IndexingSample();
		smp.sample();
		smp.tearDown();
	}
}
