package org.arastreju.sge.config;

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
public class VirtualDomain {

    private String name;

    private String storageDomain;

    private String context;

    // ----------------------------------------------------

    public VirtualDomain(String name) {
        this.name = name;
    }

    // ----------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageDomain() {
        return storageDomain;
    }

    public void setStorageDomain(String storageDomain) {
        this.storageDomain = storageDomain;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
