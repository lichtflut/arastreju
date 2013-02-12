package org.arastreju.sge.index;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;

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
