package org.arastreju.sge.spi.impl;

import org.arastreju.sge.spi.PhysicalNodeID;

/**
 * <p>
 *  Standard implementation of physical node ID, where the ID is a number.
 * </p>
 *
 * <p>
 * Created 22.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class NumericPhysicalNodeID implements PhysicalNodeID {

    private final Number id;

    // ----------------------------------------------------

    public NumericPhysicalNodeID(Number id) {
        this.id = id;
    }

    // ----------------------------------------------------+

    public long asLong() {
        return id.longValue();
    }
}
