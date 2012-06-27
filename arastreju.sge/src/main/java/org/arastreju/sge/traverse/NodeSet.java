package org.arastreju.sge.traverse;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.SemanticNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 *  A set of nodes used during navigation through a graph.
 * </p>
 *
 * <p>
 * 	Created Jun 26, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeSet<T extends SemanticNode> implements Iterable<T> {

    private final Set<T> nodes = new HashSet<T>();

    // ----------------------------------------------------

    public NodeSet() {
    }

    public NodeSet(T... nodes) {
        for (T current : nodes) {
            this.nodes.add(current);
        }
    }

    public NodeSet(Collection<T> nodes) {
        this.nodes.addAll(nodes);
    }

    // ----------------------------------------------------

    @Override
    public Iterator<T> iterator() {
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

    public T getSingle() {
        return nodes.iterator().next();
    }

    // ----------------------------------------------------

    public ResourceNodeSet asResource() {
        return new ResourceNodeSet();
    }

    public ValueNodeSet asValue() {
        return new ValueNodeSet();
    }

    // ----------------------------------------------------

    public NodeSet<T> walk(ResourceID predicate) {
        return new NodeSet<T>();
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
        return "NodeSet{" + nodes + '}';
    }
}
