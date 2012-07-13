/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  A Walker is a utility for walking through a graph.
 * </p>
 *
 * <p>
 * 	Created Feb 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class Walker {

    private Set<SemanticNode> nodes = new HashSet<SemanticNode>();

    // ----------------------------------------------------

    protected Walker(SemanticNode... nodes) {
        for (SemanticNode current : nodes) {
            this.nodes.add(current);
        }
    }

    protected Walker(Collection<? extends SemanticNode> nodes) {
        this.nodes.addAll(nodes);
    }

    // ----------------------------------------------------

    public static Walker start(SemanticNode... startNodes) {
        return new Walker(startNodes);
    }

    public static Walker start(Collection<? extends SemanticNode> startNodes) {
        return new Walker(startNodes);
    }

    // ----------------------------------------------------

    public Walker walk(ResourceID predicate) {
        Set<SemanticNode> result = new HashSet<SemanticNode>();
        for (SemanticNode current : nodes) {
            result.addAll(getObjects(current, predicate));
        }
        this.nodes = result;
        return this;
    }

    // ----------------------------------------------------

    public Iterator<SemanticNode> iterator() {
        return nodes.iterator();
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public boolean isSingle() {
        return nodes.size() == 1;
    }

    public SemanticNode getSingle() {
        if (nodes.isEmpty()) {
            return null;
        } else {
            return nodes.iterator().next();
        }
    }

    // ----------------------------------------------------

    public boolean matches(String value) {
        return matches(ValueMatcher.equals(value));
    }

    public boolean matches(ResourceID rid) {
        return matches(ResourceMatcher.equals(rid));
    }

    public boolean matches(Matcher matcher) {
        return matcher.matches(nodes);
    }

    // ----------------------------------------------------

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return nodes.equals(o);
    }

    @Override
    public String toString() {
        return "Walker{" + nodes + '}';
    }

    // ----------------------------------------------------

    private Collection<SemanticNode> getObjects(SemanticNode src, ResourceID predicate) {
        if (src == null || !src.isResourceNode()) {
            return Collections.emptySet();
        } else {
            return SNOPS.objects(src.asResource(), predicate);
        }
    }

}
