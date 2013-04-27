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

import de.lichtflut.infra.exceptions.NotYetSupportedException;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.BlankNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * <p>
 * 	Implementation of {@link RDFHandler} for reading of RDF input.
 * </p>
 * 
 * <p>
 * 	Created: 14.07.2009
 * </p>
 *
 * @author Oliver Tigges
 */
public class RdfReadHandler implements RDFHandler {
	
	private static final int MAX_TEXT_LENGTH = 100 * 1000;

	private final Logger logger = LoggerFactory.getLogger(RdfReadHandler.class);
	
	private final ReadStatementListener listener;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public RdfReadHandler(final ReadStatementListener listener) {
		this.listener = listener;
		
	}
	
	// -- RDFHandler --------------------------------------
	
	public void startRDF() throws RDFHandlerException {
		logger.debug("start RDF");
	}

	public void endRDF() throws RDFHandlerException {
		logger.debug("end RDF");
	}

	public void handleComment(final String comment) throws RDFHandlerException {
		logger.debug("comment: " + comment);
	}

	public void handleNamespace(final String prefix, final String uri)
			throws RDFHandlerException {
		logger.debug("namespace: " + prefix + " -> " + uri);
	}

	public void handleStatement(final Statement stmt)
			throws RDFHandlerException {
		logger.debug("statement: " + stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
			
		Resource subject = stmt.getSubject();
		URI predicate = stmt.getPredicate();
		Value object = stmt.getObject();
		
		listener.onNewStatement(
				new DetachedStatement(toResourceNode(subject), toResourceRef(predicate), toNode(object)));	
	}
	
	// -----------------------------------------------------
	
	protected ResourceID toResourceRef(final Resource resource){
		if (isUriNode(resource)){
			final URI uri = (URI) resource;
			return new SimpleResourceID(toQualifiedName(uri));
		} else if (isBlankNode(resource)){
			return new BlankNode();
		} else {
			throw new NotYetSupportedException("Don't know how to handle non URI node: " + resource);
		}
	}
	
	protected ResourceID toResourceRef(final URI uri){
		return new SimpleResourceID(uri.getNamespace(), uri.getLocalName());
	}
	
	protected ResourceID toResourceNode(Resource resource){
		if (isUriNode(resource)){
			return toResourceNode((URI) resource);
		} else if (isBlankNode(resource)){
			return new BlankNode();
		} else {
			throw new NotYetSupportedException("Don't know how to handle non URI node: " + resource);
		}
	}
	
	protected ResourceID toResourceNode(final URI uri){
		return new SimpleResourceID(toQualifiedName(uri));
	}
	
	protected Context toContext(final Resource ctx) {
		if (ctx == null) {
			return null;
		} else {
			return new SimpleContextID(QualifiedName.create(ctx.stringValue()));
		}
	}
	
	protected SemanticNode toNode(final Value value) {
		if (isUriNode(value)){
			return toResourceRef((URI) value);
		} else if (value instanceof LiteralImpl){
			final LiteralImpl literal = (LiteralImpl) value;
			String content = value.stringValue();
			if (content.length() > MAX_TEXT_LENGTH){
				logger.warn("Text too long; chopping content: " + content);
				content = content.substring(0, MAX_TEXT_LENGTH);
			}
			final Locale locale = getLocale(literal);
			return new SNText(content, locale);
		} else {
			return null;
		}
	}
	
	protected Locale getLocale(LiteralImpl literal) {
		if (literal.getLanguage() != null && literal.getLanguage().length() >=2 ) {
			String expr = literal.getLanguage();
			String language = expr.substring(0, 2);
			if (expr.length() >= 5 ) {
				String country = expr.substring(3, 5);
				return new Locale(language, country);
			} else {
				return new Locale(language);
			}
		}
		return null;
	}

	protected boolean isUriNode(final Object resource){
		return resource instanceof URI;
	}
	
	protected boolean isBlankNode(final Object resource){
		return resource instanceof BNode;
	}
	
	protected QualifiedName toQualifiedName(final URI uri){
		return QualifiedName.create(uri.getNamespace(), uri.getLocalName());
	}
	
}