/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.io.File;
import java.io.IOException;

import org.arastreju.bindings.neo4j.index.NeoIndexingService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Container for services, indexes and registries. 
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class GraphDataStore {
	
	private final GraphDatabaseService gdbService;
	
	private final NeoIndexingService indexService;
	
	private final ResourceRegistry registry = new ResourceRegistry();
	
	private final Logger logger = LoggerFactory.getLogger(GraphDataStore.class);

	// -----------------------------------------------------

	/**
	 * Default constructor. Will use a <b>temporary</b> datastore!.
	 */
	public GraphDataStore() throws IOException {
		this(prepareTempStore());
	}
	
	/**
	 * Constructor. Creates a store using given directory.
	 * @param dir The directory for the store.
	 */
	public GraphDataStore(final String dir) {
		logger.info("Neo4jDataStore created in " + dir);
		gdbService = new EmbeddedGraphDatabase(dir); 
		indexService = new NeoIndexingService(gdbService);
	}
	
	// -----------------------------------------------------

	/**
	 * @return the gdbService
	 */
	public GraphDatabaseService getGdbService() {
		return gdbService;
	}

	/**
	 * @return the indexService
	 */
	public NeoIndexingService getIndexService() {
		return indexService;
	}

	/**
	 * @return the registry
	 */
	public ResourceRegistry getRegistry() {
		return registry;
	}
	
	// -----------------------------------------------------
	
	private static String prepareTempStore() throws IOException {
		final File temp = File.createTempFile("aras", Long.toString(System.nanoTime()));
		if (!temp.delete()) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}
		if (!temp.mkdir()) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}
		
		return temp.getAbsolutePath();
	}

}
