package backend.reports;

public class ReportError {
    private String lexeme;
    private String description;
    private int line;
    private int column;
    private String type;

    public ReportError(String lexeme, String description, int line, int column, String type) {
        this.lexeme = lexeme;
        this.description = description;
        this.line = line;
        this.column = column;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
