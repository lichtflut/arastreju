/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * The Arastreju-Neo4j binding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arastreju.sge.spi;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Accessor for {@link org.arastreju.sge.model.associations.AssociationKeeper} of a Resource Node.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AssocKeeperAccess {

	private final Field assocKeeperField;

	private static final AssocKeeperAccess INSTANCE = new AssocKeeperAccess();

	// -----------------------------------------------------

    public static AssocKeeperAccess getInstance() {
        return INSTANCE;
    }

    // ----------------------------------------------------

    /**
	 * Get the association keeper of given node.
	 */
	public AssociationKeeper getAssociationKeeper(final ResourceNode node){
		final SNResource resource = findSNResource(node);
		try {
			return (AssociationKeeper) assocKeeperField.get(resource);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get AssociationKeeper", e);
		}
	}

	/**
	 * Set the given association keeper to the resource node.
	 * @param node Must be an instance of {@link org.arastreju.sge.model.nodes.SNResource}.
	 * @param ak The association keeper to be set.
	 */
	public void setAssociationKeeper(final ResourceNode node, final AssociationKeeper ak) {
		final SNResource resource = findSNResource(node);
		try {
			assocKeeperField.set(resource, ak);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get AssociationKeeper", e);
		}
	}

    // ----------------------------------------------------

    /**
     * Merge the status of source to target. Replicate added and removed associations.
     * @param target The target keeper.
     * @param source The source keeper.
     */
    public void merge(final AssociationKeeper target, final AssociationKeeper source) {
        final Set<Statement> existingAssocs = new HashSet<Statement>(target.getAssociations());
        for (Statement toBeRemoved : source.getAssociationsForRemoval()) {
            target.removeAssociation(toBeRemoved);
        }
        for(Statement assoc : source.getAssociations()){
            if (!existingAssocs.contains(assoc)){
                target.addAssociation(assoc);
            }
        }
    }

	// -----------------------------------------------------

	private SNResource findSNResource(ResourceNode node) {
		ResourceNode resource = node.asResource();
		int depth = 100;
		while (!(resource instanceof SNResource)){
			if (resource == resource.asResource() || depth <= 0) {
				throw new IllegalArgumentException("Cannot get AssociationKeeper for class: " + node.getClass());
			}
			resource = resource.asResource();
			depth--;
		}
		return (SNResource) resource;
	}

	/**
	 * Private constructor.
	 */
	private AssocKeeperAccess() {
		try {
			assocKeeperField = SNResource.class
					.getDeclaredField("associationKeeper");
			assocKeeperField.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
}

}
