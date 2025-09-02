package backend.analyzers;

import analyzers.ParserpgSQLLexer;
import analyzers.ParserpgSQLParser;
import backend.errors.SemanticError;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class Parse {
    public static List<SemanticError> semanticErrors = new ArrayList<>();

    public String analyzeText(String text) {
        ParserpgSQLLexer lexer = new ParserpgSQLLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ParserpgSQLParser parser = new ParserpgSQLParser(tokens);

        ParseTree tree = parser.init();
        return tree.toStringTree(parser);
    }
}
