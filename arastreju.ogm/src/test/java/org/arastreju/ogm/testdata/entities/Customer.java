/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.testdata.entities;

import org.arastreju.ogm.annotations.Attribute;
import org.arastreju.ogm.annotations.EntityNode;
import org.arastreju.ogm.annotations.NodeID;
import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 *  A sample entity.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
@EntityNode()
public class Customer {
	
	@NodeID
	private ResourceID id;
	
	@Attribute(predicate="http://l2r.info/forename")
	private String forename;
	
	@Attribute(predicate="http://l2r.info/surname")
	private String surname;

	// ----------------------------------------------------
	
	/**
	 * @return the id
	 */
	public ResourceID getId() {
		return id;
	}

	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * @param forename the forename to set
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}
