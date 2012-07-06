package org.arastreju.sge.config;

import org.arastreju.sge.context.VirtualDomain;
import org.arastreju.sge.model.SimpleResourceID;

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
public class DefaultVirtualDomainMapping implements VirtualDomainMapping {

    public static final String DEFAULT_STORAGE_NAME = "vd-storage";

    private final String contextBaseURI;

    // ----------------------------------------------------

    public DefaultVirtualDomainMapping(String contextBaseURI) {
        this.contextBaseURI = contextBaseURI;
    }

    // ----------------------------------------------------

    @Override
    public VirtualDomain getVirtualDomain(String virtualDomainName) {
        VirtualDomain vd = new VirtualDomain(virtualDomainName);
        return vd;
    }

}
