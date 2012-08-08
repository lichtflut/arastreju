/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

import java.util.HashMap;
import java.util.Map;

import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;


/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 25.07.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public class Cache {
	
	private Map<QualifiedName, AssociationKeeper> data;
	
	public Cache(){
		data = new HashMap<QualifiedName, AssociationKeeper>();
	}
	
	public void add(QualifiedName id, AssociationKeeper keeper){
		if(!data.containsKey(id))
			data.put(id, keeper);
	}
	
	public boolean contains(QualifiedName id){
		return data.containsKey(id);
	}
	
	public void remove(AssociationKeeper keeper){
		data.remove(keeper);
	}
	
	public AssociationKeeper get(QualifiedName qn){
		return data.get(qn);
	}
	
}
