/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Annotation for entities based on a {@link ResourceNode}.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EntityNode {
	
	String type() default "";
	
}
