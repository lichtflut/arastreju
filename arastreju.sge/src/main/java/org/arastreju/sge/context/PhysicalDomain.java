package org.arastreju.sge.context;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.naming.Namespace;

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

    /**
     * Constructor.
     * @param domainName The domain name.
     */
    public PhysicalDomain(String domainName) {
        super(domainName);
    }

    // ----------------------------------------------------

    @Override
    public String getStorage() {
        return getDomainName();
    }

}
