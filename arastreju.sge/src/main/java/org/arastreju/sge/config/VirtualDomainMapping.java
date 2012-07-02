package org.arastreju.sge.config;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  Mapping from a virtual domain to the physical store and logical context.
 * </p>
 * 
 * <p>
 * Created Jul 2, 2012
 * </p>
 * 
 * @author Oliver Tigges
 */
public class VirtualDomainMapping {

    public VirtualDomain getVirtualDomain(String virtualDomainName) {
        VirtualDomain vd = new VirtualDomain(virtualDomainName);
        throw new NotYetImplementedException();
    }


}
