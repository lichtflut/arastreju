package org.arastreju.sge.repl;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  A dummy replicator which doesn't replicate anything
 * </p>
 *
 * Created: 23.11.2012
 *
 * @author Timo Buhrmester
 */
public class NoOpReplicator extends ArasLiveReplicator {

	@Override
	protected void onRelOp(int txSeq, boolean added, Statement stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNodeOp(int txSeq, boolean added, ResourceID node) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEndOfTx(int txSeq, boolean success) {
		// TODO Auto-generated method stub

	}

}
