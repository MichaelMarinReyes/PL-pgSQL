package backend.tables;

import java.util.LinkedHashMap;
import java.util.Map;

public class Symbol {
    private final int id;
    private final String name;
    private final String type;
    private final String dataType;
    private final String scope;
    private final Integer line;
    private final Integer column;
    private static int counter = 0;
    private Object value;

    public Symbol(String name, String type, String dataType, Object value, String scope, Integer line, Integer column) {
        this.id = ++counter;
        this.name = name;
        this.type = type;
        this.dataType = dataType;
        this.value = value;
        this.scope = scope;
        this.line = line;
        this.column = column;
    }

    public static void resetCounter() {
        counter = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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