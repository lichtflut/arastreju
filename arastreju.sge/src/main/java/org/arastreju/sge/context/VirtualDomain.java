package org.arastreju.sge.context;

import org.arastreju.sge.naming.Namespace;

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
public class VirtualDomain extends DomainIdentifier {

    public static final String DEFAULT_STORAGE_NAME = "virtuals";

    // ----------------------------------------------------

    private String physicalStore;

    private Context context;

    // ----------------------------------------------------

    public VirtualDomain(String name) {
        super(name);
        this.physicalStore = DEFAULT_STORAGE_NAME;
        this.context = new SimpleContextID(Namespace.LOCAL_CONTEXTS, name);
    }

    // ----------------------------------------------------


    @Override
    public String getStorage() {
        return physicalStore;
    }

    @Override
    public Context getInitialContext() {
        return context;
    }


}
