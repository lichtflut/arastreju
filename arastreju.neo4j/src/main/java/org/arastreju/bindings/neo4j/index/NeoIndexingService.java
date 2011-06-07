package org.arastreju.bindings.neo4j.index;

import org.apache.lucene.search.Query;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Nils Bleisch
 */
public final class NeoIndexingService extends LuceneFulltextQueryIndexService {
	
	private static final String[] SPECIAL_CHARACTERS = new String[] {
			"+","-","&&","||","!","(",")","{","}","[","]","^","~","?",":"	
	};
	
	// -----------------------------------------------------
	
	/**
	 * @param graphDb
	 */
	public NeoIndexingService(GraphDatabaseService graphDb) {
		super(graphDb);
	}
	
	// -----------------------------------------------------

	protected Query formQuery(String key, Object value, Object matching){
		String val = value.toString();
		for (int i = 0; i < SPECIAL_CHARACTERS.length; i++) {
		  val = val.replace(SPECIAL_CHARACTERS[i], "\\" + SPECIAL_CHARACTERS[i]);
		}
		
		return super.formQuery(key, val, matching);
	}
}