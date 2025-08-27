package backend.reports;

public class LexicalError extends ReportError {

    public LexicalError(String lexeme, String description, int line, int column, String type) {
        super(lexeme, description, line, column, type);
    }
}
