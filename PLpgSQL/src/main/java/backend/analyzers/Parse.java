package backend.analyzers;

import analyzers.ParserpgSQLLexer;
import analyzers.ParserpgSQLParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Parse {

    public String analyzeText(String text) {
        ParserpgSQLLexer lexer = new ParserpgSQLLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ParserpgSQLParser parser = new ParserpgSQLParser(tokens);

        ParseTree tree = parser.init();
        return tree.toStringTree(parser);
    }
}
