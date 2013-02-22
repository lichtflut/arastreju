package org.arastreju.sge.index;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Index search result collector, used to collect all results of a search
 * </p>
 *
 * <p>
 * 	Created Feb 21, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class AllHitsCollector extends Collector {
	private static final Logger LOGGER = LoggerFactory.getLogger(AllHitsCollector.class);

	/* the result list */
	private List<QualifiedName> docList = new LinkedList<QualifiedName>();

	private IndexReader curReader;

	public int count() {
		return docList.size();
	}

	public List<QualifiedName> getList() {
		return docList;
	}

	@Override
	public void setScorer(Scorer scorer) {
		/* don't need this */
	}

	@Override
	public void collect(int doc) {
		try {
			Document d = curReader.document(doc);
	        docList.add(new QualifiedName(d.get(IndexFields.QUALIFIED_NAME)));
        } catch (IOException e) {
        	String msg = "caught IOException while collecting document " + doc + " on reader " + curReader;
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
        }
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase) {
		curReader = reader;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
}