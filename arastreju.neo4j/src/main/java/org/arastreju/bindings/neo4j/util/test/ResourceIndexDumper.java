/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.util.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * <p>
 *  Simple IndexDumper to dump out a lucene index into a given {@PrintStream}
 * </p>
 *
 * <p>
 * 	Created May 25, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceIndexDumper {

	private final String dir;
	
	// --CONSTRUCTIR----------------------------------------
	
	/**
	 * @param dir, the specified index directory
	 */
	public ResourceIndexDumper(String dir){
		this.dir = dir;
	}
	
	// -----------------------------------------------------
	
	/**
	 * Dumps out the lucene indices into the given {@PrintStream}
	 */
	public void writeOut(final PrintStream out) throws CorruptIndexException, IOException{
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(dir)),true);
		for (int i = 0; i < reader.numDocs(); i++){
            Document document = reader.document(i);
            List<Fieldable> fields = document.getFields();
            for (Fieldable field : fields) {
            	out.println(field.name() + " -> " + field.stringValue());
			}
		}
		out.flush();
	}
}
