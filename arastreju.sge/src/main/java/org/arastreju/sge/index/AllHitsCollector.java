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
