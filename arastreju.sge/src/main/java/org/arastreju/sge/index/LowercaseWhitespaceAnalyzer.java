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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * <p>
 *  Separates tokens by whitespace and lowercases them
 * </p>
 *
 * <p>
 * 	Created Feb 12, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public final class LowercaseWhitespaceAnalyzer extends Analyzer {
	private Version matchVersion;

	public LowercaseWhitespaceAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return makeTokenStream(reader);
	}

	/*FIXME: get this function working, it really hurts performance not to have it */
	//	@Override
	//	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
	//		TokenStream tokStr = (TokenStream) getPreviousTokenStream();
	//		if (tokStr == null) {
	//			tokStr = makeTokenStream(reader);
	//			setPreviousTokenStream(tokStr);
	//		} else {
	//			tokStr.reset();
	//		}
	//
	//		return tokStr;
	//	}

	private TokenStream makeTokenStream(Reader reader) {
		return new LowerCaseFilter(matchVersion, new WhitespaceTokenizer(matchVersion, reader));
	}
}
