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
package org.arastreju.sge.io;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p>
 *  Implementation of Sesame RIO statement based on an Arastreju Statement.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RioStatement implements org.openrdf.model.Statement {

    private static final Logger LOGGER = LoggerFactory.getLogger(RioStatement.class);
	
	private static DateFormat XML_DATE = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unused")
	private static DateFormat XML_TIME = new SimpleDateFormat("HH:mm:ss");

	private final org.arastreju.sge.model.Statement arasStmt;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param stmt The Arastreju statement.
	 */
	public RioStatement(final org.arastreju.sge.model.Statement stmt) {
		this.arasStmt = stmt;
	}
	
	// -----------------------------------------------------

    @Override
    public Resource getSubject() {
        final ResourceID subject = arasStmt.getSubject();
        if (subject.asResource().isBlankNode()){
            return new BNodeImpl(subject.getQualifiedName().getSimpleName());
        }
        return sesameURI(subject);
    }

    @Override
    public URI getPredicate() {
        return sesameURI(arasStmt.getPredicate());
    }


    @Override
	public Value getObject() {
		final SemanticNode object = arasStmt.getObject();
		if (object.isResourceNode()){
			return sesameURI(object.asResource());
		} else {
			return createLiteral(object.asValue());
		}
	}

    @Override
    public Resource getContext() {
        return null;
    }

	// ----------------------------------------------------
	
	private Value createLiteral(ValueNode value) {
		switch(value.getDataType()) {
		case DATE:
			return new LiteralImpl(xmlDate(value), new URIImpl("http://www.w3.org/2001/XMLSchema#date"));
		case TIMESTAMP:
			return new LiteralImpl(value.getStringValue(), new URIImpl("http://www.w3.org/2001/XMLSchema#dateTime"));
		case TIME_OF_DAY:
			return new LiteralImpl(value.getStringValue(), new URIImpl("http://www.w3.org/2001/XMLSchema#time"));
		case INTEGER:
			return new LiteralImpl(value.getStringValue(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		case DECIMAL:
			return new LiteralImpl(value.getStringValue(), new URIImpl("http://www.w3.org/2001/XMLSchema#decimal"));
		
		case STRING:
		default:
			if (value.getLocale() != null) {
				return new LiteralImpl(value.getStringValue(), value.getLocale().getLanguage());
			} else {
				return new LiteralImpl(value.getStringValue());
			}
		}
	}
	
	private String xmlDate(ValueNode value) {
		return XML_DATE.format(value.getTimeValue());
	}

    private URI sesameURI(ResourceID rid) {
        try {
            return new URIImpl(rid.toURI());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while serializing statement {}: {}", arasStmt, e.getMessage());
            return new URIImpl("error:" +rid.toURI());
        }
    }

}
