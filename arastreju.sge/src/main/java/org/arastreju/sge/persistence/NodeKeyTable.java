package org.arastreju.sge.persistence;

import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.PhysicalNodeID;

/**
 * <p>
 *  Key table mapping qualified names to physical node IDs.
 * </p>
 *
 * <p>
 *  Created 27.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface NodeKeyTable {

    PhysicalNodeID lookup(QualifiedName qualifiedName);

    void put(QualifiedName qn, PhysicalNodeID physicalID);

}
