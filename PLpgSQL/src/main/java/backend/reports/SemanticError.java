package backend.reports;

public class SemanticError extends ReportError {

    public SemanticError(String lexeme, String description, int line, int column, String type) {
        super(lexeme, description, line, column, type);
    }
}
