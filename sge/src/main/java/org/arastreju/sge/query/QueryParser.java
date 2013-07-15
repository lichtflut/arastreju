package org.arastreju.sge.query;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.arastreju.sge.query.parser.ArasQueryBaseVisitor;
import org.arastreju.sge.query.parser.ArasQueryLexer;
import org.arastreju.sge.query.parser.ArasQueryParser;

/**
 * <p>
 *  Parser for query strings.
 * </p>
 *
 * <p>
 *  Created July 15, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryParser {

    public void adapt(Query query, String queryString) {
        CharStream input = new ANTLRInputStream(queryString);
        ArasQueryLexer lexer = new ArasQueryLexer(input);
        TokenStream tokenStream = new CommonTokenStream(lexer);

        ArasQueryParser parser = new ArasQueryParser(tokenStream);
        ParseTree tree = parser.query();
        new Visitor(query).visit(tree);
    }

    public String validate(String queryString) {
        QueryBuilder qb = new QueryBuilder();
        adapt(qb, queryString);
        return qb.toString();
    }

    // ----------------------------------------------------

    private static class Visitor extends ArasQueryBaseVisitor {

        private final Query query;

        // ----------------------------------------------------

        private Visitor(Query query) {
            this.query = query;
        }

        // ----------------------------------------------------

        @Override
        public Object visitID(@NotNull ArasQueryParser.IDContext ctx) {
            TerminalNode key = ctx.ID(0);
            TerminalNode val = ctx.ID(1);
            query.addField(text(key), text(val));
            return super.visitID(ctx);
        }

        @Override
        public Object visitVAL(@NotNull ArasQueryParser.VALContext ctx) {
            query.addValue(text(ctx.ID()));
            return super.visitVAL(ctx);
        }

        @Override
        public Object visitREL(@NotNull ArasQueryParser.RELContext ctx) {
            query.addRelation(text(ctx.ID()));
            return super.visitREL(ctx);
        }

        @Override
        public Object visitQN(@NotNull ArasQueryParser.QNContext ctx) {
            query.addURI(text(ctx.ID()));
            return super.visitQN(ctx);
        }

        @Override
        public Object visitNOT(@NotNull ArasQueryParser.NOTContext ctx) {
            query.not();
            Object result = super.visitNOT(ctx);
            query.end();
            return result;
        }

        @Override
        public Object visitAND(@NotNull ArasQueryParser.ANDContext ctx) {
            query.beginAnd();
            Object result = super.visitAND(ctx);
            query.end();
            return result;
        }

        @Override
        public Object visitOR(@NotNull ArasQueryParser.ORContext ctx) {
            query.beginOr();
            Object result = super.visitOR(ctx);
            query.end();
            return result;
        }

        // ----------------------------------------------------

        private String text(ParseTree tn) {
            String raw = tn.getText();
            if (raw != null && raw.startsWith("'") && raw.endsWith("'")) {
                return raw.substring(1, raw.length() -1);
            } else {
                return raw;
            }
        }
    }

}
