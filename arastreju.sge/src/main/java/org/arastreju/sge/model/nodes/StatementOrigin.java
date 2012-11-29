package org.arastreju.sge.model.nodes;

/**
 * <p>
 *  Enumeration of possible origins of a statement.
 *  <ul>
 *      <li>Asserted</li>
 *      <li>Inferred</li>
 *      <li>Inherited (Specialisation of inferred)</li>
 *  </ul>
 * </p>
 * <p/>
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public enum StatementOrigin {

    ASSERTED,
    INFERRED,
    INHERITED;

}
