package org.arastreju.sge.config;

import org.arastreju.sge.context.VirtualDomain;

/**
 * <p>
 *  Interface for the mapping of a virtual domain name to a virtual domain.
 * </p>
 *
 * <p>
 *  Created Jul 3, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface VirtualDomainMapping {

    VirtualDomain getVirtualDomain(String virtualDomainName);

}
