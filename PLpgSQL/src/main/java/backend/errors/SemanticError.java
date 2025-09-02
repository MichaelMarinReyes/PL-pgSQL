package backend.errors;

public class SemanticError {
    private final String type = "Semántico";
    private String lexema;
    private String description;
    private int line;
    private int column;

    public SemanticError(String lexema, String description, int line, int column) {
        this.lexema = lexema;
        this.description = description;
        this.line = line;
        this.column = column;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
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
}
