package org.arastreju.sge.context;

import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 *  Direct identifier of a store based on the store's name.
 * </p>
 *
 * <p>
 * Created ${Date}
 * </p>
 *
 * @author Oliver Tigges
 */
public class PhysicalDomain extends DomainIdentifier {

    public PhysicalDomain(String domainName) {
        super(domainName);
    }

    // ----------------------------------------------------

    @Override
    public String getStorage() {
        return getDomainName();
    }

    @Override
    public Context getInitialContext() {
        return null;
    }
}
