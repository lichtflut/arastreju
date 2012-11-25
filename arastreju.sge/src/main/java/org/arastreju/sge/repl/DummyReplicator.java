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
public class DummyReplicator extends ArasLiveReplicator {

	@Override
    protected void onNodeOp(boolean added, ResourceID node) {
    }

	@Override
    protected void onRelOp(boolean added, Statement stmt) {
    }

}
