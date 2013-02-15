package org.arastreju.sge.index;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Interface for update, change and delete operations on Arastreju index.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexUpdator {

    /**
     * Index this node with all it's statements, regarding the current primary context.
     * If the node already has been indexed, it will be updated.
     * @param node The node to index.
     */
    void index(ResourceNode node);

    /**
     * Remove the resource identified by the qualified name form the index.
     * @param qn The qualified name.
     */
    void remove(QualifiedName qn);

}
