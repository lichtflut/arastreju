package org.arastreju.sge.context;


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
	public PhysicalDomain(final String domainName) {
		super(domainName);
	}

	// ----------------------------------------------------

	@Override
	public String getStorage() {
		return getDomainName();
	}

}
