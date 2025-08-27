package backend.reports;

public class SyntaxError extends ReportError {
    public SyntaxError(String lexeme, String description, int line, int column, String type) {
        super(lexeme, description, line, column, type);
    }
}
