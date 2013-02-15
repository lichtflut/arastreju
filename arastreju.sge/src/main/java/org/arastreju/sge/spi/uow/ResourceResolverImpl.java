package org.arastreju.sge.spi.uow;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AttachedResourceNode;
import org.arastreju.sge.spi.abstracts.WorkingContext;

import java.util.Set;

/**
 * <p>
 *  Standard implementation of ResourceResolver.
 * </p>
 *
 * <p>
 *  Created Feb. 15, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceResolverImpl implements ResourceResolver {

    private WorkingContext context;

    // ----------------------------------------------------

    public ResourceResolverImpl(WorkingContext context) {
        this.context = context;
    }

    // ----------------------------------------------------

    @Override
    public ResourceNode findResource(QualifiedName qn) {
        final AssociationKeeper keeper =  context.find(qn);
        if (keeper != null) {
            return new AttachedResourceNode(qn, keeper);
        } else {
            return null;
        }
    }

    @Override
    public ResourceNode resolve(ResourceID rid) {
        final ResourceNode node = rid.asResource();
        if (node.isAttached()){
            return node;
        } else {
            final QualifiedName qn = rid.getQualifiedName();
            final ResourceNode attached = findResource(qn);
            if (attached != null) {
                return attached;
            } else {
                return persist(node);
            }
        }
    }

    // ----------------------------------------------------

    protected ResourceNode persist(final ResourceNode node) {
        // 1st: create a corresponding Neo node and attach the Resource with the current context.
        AssociationKeeper keeper = context.create(node.getQualifiedName());

        // 2nd: retain copy of current associations
        final Set<Statement> copy = node.getAssociations();
        AssocKeeperAccess.getInstance().setAssociationKeeper(node, keeper);

        // 3rd: store all associations.
        for (Statement assoc : copy) {
            keeper.addAssociation(assoc);
        }

        return node;
    }


}
