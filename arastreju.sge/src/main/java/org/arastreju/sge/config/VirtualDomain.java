package org.arastreju.sge.config;

import org.arastreju.sge.model.ResourceID;

/**
 **
 * <p>
 *  A virtual domain is a phyisically non isolated domain. The separation is based on a Context.
 * </p>
 *
 * <p>
 * 	Created Jul 1, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class VirtualDomain implements StoreIdentifier {

    public static final String DEFAULT_STORAGE_NAME = "virtuals";

    // ----------------------------------------------------

    private String name;

    private String physicalStore;

    private ResourceID context;

    // ----------------------------------------------------

    public VirtualDomain(String name) {
        this.name = name;
        this.physicalStore = DEFAULT_STORAGE_NAME;
    }

    // ----------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getStorageName() {
        return physicalStore;
    }

    public void setStorageName(String storageDomain) {
        this.physicalStore = storageDomain;
    }

    public ResourceID getContext() {
        return context;
    }

    public void setContext(ResourceID context) {
        this.context = context;
    }

}
