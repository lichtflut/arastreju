/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuProfile;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jun 10, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ProfileCloseListener {

	void onClosed(final ArastrejuProfile profile);
}
