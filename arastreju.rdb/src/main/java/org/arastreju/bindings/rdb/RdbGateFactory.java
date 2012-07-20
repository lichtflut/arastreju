package org.arastreju.bindings.rdb;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateInitializationException;

public class RdbGateFactory extends ArastrejuGateFactory {

	public RdbGateFactory(ArastrejuProfile profile) {
		super(profile);
	}

	@Override
	public ArastrejuGate create(DomainIdentifier identifier) throws GateInitializationException {
		
		String storageName = identifier.getStorage();
		
		// TODO: check is storage with given name already exists.
		
		getProfile().getProperty("jdbc.url");
		
		RdbConnectionProvider provider = new RdbConnectionProvider();
		
		// TODO: iniitalize provider with JDBC stuff
		
		return new RdbGate(provider, identifier);
	}

}
