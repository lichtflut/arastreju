package org.arastreju.sge.eh.meta;

/**
 * <p>
 *  Exception thrown on not yet supported features.
 * </p>
 * <p>
 *  Created July 13, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NotYetSupportedException extends RuntimeException {

    public NotYetSupportedException() {
    }

    public NotYetSupportedException(String message) {
        super(message);
    }

    public NotYetSupportedException(Object obj) {
        super("Not yet supported: " + obj);
    }
}
