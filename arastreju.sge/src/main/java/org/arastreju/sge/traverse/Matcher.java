package org.arastreju.sge.traverse;

import org.arastreju.sge.model.nodes.SemanticNode;

import java.util.Collection;

/**
 * @author Oliver Tigges
 */
public interface Matcher {

    boolean matches(Collection<? extends SemanticNode> nodes);

}
