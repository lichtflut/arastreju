/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.HalfStatement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Decorator implementing aras:inheritsFrom.
 * </p>
 *
 * <p>
 *  Created 19.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class InheritedDecorator extends ResourceView {

    public static InheritedDecorator from(SemanticNode node) {
        if (node instanceof InheritedDecorator) {
            return (InheritedDecorator) node;
        } else if (node instanceof ResourceNode) {
            return new InheritedDecorator((ResourceNode) node);
        } else {
            return null;
        }
    }

    public static void revoke(ResourceNode node, HalfStatement revokedStmt) {
        SemanticNode revokeDef = SNOPS.fetchObject(node, Aras.REVOKES);
        if (revokeDef == null) {
            revokeDef = new SNResource();
            node.addAssociation(Aras.REVOKES, revokeDef);
        }
        revokeDef.asResource().addAssociation(revokedStmt.getPredicate(), revokedStmt.getObject());
    }

    // ----------------------------------------------------

    public InheritedDecorator() {
    }

    public InheritedDecorator(ResourceNode resource) {
        super(resource);
    }

    public InheritedDecorator(QualifiedName qn) {
        super(qn);
    }

    // ----------------------------------------------------

    @Override
    public Set<Statement> getAssociations() {
        final Set<Statement> directAssocs = super.getAssociations();
        final Set<ResourceNode> inheritors = getInheritors(directAssocs);
        if (inheritors.isEmpty()) {
            return directAssocs;
        }

        final Set<Statement> result = new HashSet<Statement>();
        // 1st: Add all inherited - excluding revoked
        final RevocationDef revocationDef = getRevocationDef();
        Set<ResourceID> revokedPredicates = revocationDef.getRevokedPredicates();
        Set<HalfStatement> revokedStatements = revocationDef.getRevokedStatements();

        for (ResourceNode inheritor : inheritors) {
            for (Statement inherited : inheritor.getAssociations()) {
                HalfStatement halfStatement = HalfStatement.from(inherited);
                if (revokedPredicates.contains(inherited.getPredicate())) {
                    continue;
                }
                if (revokedStatements.contains(halfStatement) ) {
                    continue;
                }
                result.add(halfStatement.toFullStatement(this, inherited.getMetaInfo().inherit()));
            }
        }

        // 3rd: Add all own statements
        result.addAll(directAssocs);

        return result;
    }

    // ----------------------------------------------------

    protected Set<ResourceNode> getInheritors() {
        return getInheritors(super.getAssociations());
    }

    protected RevocationDef getRevocationDef() {
        return getRevocationDef(super.getAssociations());
    }

    protected Set<Statement> directAssociations() {
        return super.getAssociations();
    }

    // ----------------------------------------------------

    private Set<ResourceNode> getInheritors(Collection<Statement> statements){
        final Set<ResourceNode> result = new HashSet<ResourceNode>();
        for (Statement assoc : statements) {
            if (assoc.getPredicate().equals(Aras.INHERITS_FROM) && assoc.getObject().isResourceNode()) {
                result.add(assoc.getObject().asResource());
            }
        }
        // Remove this to avoid cycles
        result.remove(this);
        return result;
    }

    private RevocationDef getRevocationDef(Collection<Statement> statements) {
        for (Statement statement : statements) {
            if (Aras.REVOKES.equals(statement.getPredicate()) && statement.getObject().isResourceNode()) {
                return new RevocationDef(statement.getObject().asResource());
            }
        }
        return RevocationDef.EMPTY;
}

    // ----------------------------------------------------

    private static class RevocationDef extends ResourceView {

        private static RevocationDef EMPTY = new RevocationDef();

        private RevocationDef(ResourceNode resource) {
            super(resource);
        }

        private RevocationDef() {
        }

        // ----------------------------------------------------

        public Set<ResourceID> getRevokedPredicates() {
            Set<ResourceID> predicates = new HashSet<ResourceID>();
            for (Statement assoc : getAssociations()) {
                if (Aras.ANY.equals(assoc.getObject())) {
                    predicates.add(assoc.getPredicate());
                }
            }
            return predicates;
        }

        public Set<HalfStatement> getRevokedStatements() {
            // return all statements - also those with aras:Any as object.
            Set<Statement> associations = getAssociations();
            Set<HalfStatement> result = new HashSet<HalfStatement>(associations.size());
            for (Statement current : associations) {
                result.add(HalfStatement.from(current));
            }
            return result;
        }

    }

}
