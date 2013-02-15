package org.arastreju.sge.spi.uow;

/**
 * <p>
 *  Represents a unit of work to be done during a transaction.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface UnitOfWork {

    void finish();
}
