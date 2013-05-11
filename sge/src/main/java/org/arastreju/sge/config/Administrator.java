package org.arastreju.sge.config;

/**
 * <p>
 *  Administration interface.
 * </p>
 *
 * <p>
 *  Created 10.05.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Administrator {

    void reIndex();

    void reInference();

    void backup();

    void restore();

}
