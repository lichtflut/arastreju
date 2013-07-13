package org.arastreju.sge.eh.meta;

/**
 * <p>
 *  Exception thrown on not yet implemented features.
 * </p>
 * <p>
 *  Created July 13, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NotYetImplementedException extends RuntimeException {

    public NotYetImplementedException() {
    }

    public NotYetImplementedException(String message) {
        super(message);
    }
}
