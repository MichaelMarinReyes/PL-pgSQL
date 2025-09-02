package backend.tables;

import java.util.List;

public class Procedure {
    private int counter = 0;
    private final String name;
    private final List<Symbol> params;
    private final List<Object> instructions;
    private final String scope;
    private final Integer line;
    private final Integer column;

    public Procedure(String name, List<Symbol> params, List<Object> instructions, String scope, Integer line, Integer column) {
        this.counter += 1;
        this.name = name;
        this.params = params;
        this.instructions = instructions;
        this.scope = scope;
        this.line = line;
        this.column = column;
    }

    public int getCounter() {
        return counter;
    }

    public String getName() {
        return name;
    }

    public List<Symbol> getParams() {
        return params;
    }

    public List<Object> getInstructions() {
        return instructions;
    }

    public String getScope() {
        return scope;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getColumn() {
        return column;
    }
}

