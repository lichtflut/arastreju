package org.arastreju.sge.config;

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
public class PhysicalDomain implements StoreIdentifier {

    private String store;

    // ----------------------------------------------------

    public PhysicalDomain(String store) {
        this.store = store;
    }

    // ----------------------------------------------------

    @Override
    public String getStorageName() {
        return store;
    }

}
