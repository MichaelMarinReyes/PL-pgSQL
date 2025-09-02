package backend.tables;

import backend.analyzers.Parse;
import backend.errors.SemanticError;

import java.util.*;

public class SymbolTable {
    private static SymbolTable instance;

    private List<String> scopes;
    private final List<Symbol> symbols;
    private final List<Procedure> procedures;
    private final Map<String, Integer> scopeCounters;

    private SymbolTable() {
        this.scopes = new ArrayList<>();
        this.scopes.add("global");
        this.symbols = new ArrayList<>();
        this.procedures = new ArrayList<>();
        this.scopeCounters = new HashMap<>();
    }

    public static synchronized SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public String getCurrentScope() {
        return scopes.get(scopes.size() - 1);
    }

    public String newScope(String name) {
        String scopeName;
        if (name != null && !name.isEmpty()) {
            String base = name.replace("_block", "");
            int count = scopeCounters.getOrDefault(base, 0) + 1;
            scopeCounters.put(base, count);
            scopeName = base + "_" + count;
        } else {
            scopeName = "scope_" + scopes.size();
        }
        scopes.add(scopeName);
        return scopeName;
    }

    public String exitScope() {
        if (scopes.size() > 1) {
            return scopes.remove(scopes.size() - 1);
        }
        Parse.semanticErrors.add(new SemanticError("exit_scope", "No se puede eliminar el scope " + getCurrentScope(), 0, 0));
        return null;
    }

    public Symbol addVariable(String name, String type, Object value, Integer line, Integer column) {
        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol sym = symbols.get(i);
            if (sym.getName().equals(name) && sym.getScope().equals(getCurrentScope())) {
                Parse.semanticErrors.add(new SemanticError(name, "La variable '" + name + "' ya existe en este scope", line, column));
                return null;
            }
        }
        Symbol sym = new Symbol(name, "variable", type, value, getCurrentScope(), line, column);
        symbols.add(sym);
        return sym;
    }

    public Symbol addFunction(String name, String returnType, List<Symbol> params, int line, int column) {
        if (getCurrentScope().equals("global")) {
            for (Symbol sym : symbols) {
                if (sym.getName().equals(name) && sym.getType().equals("function")
                        && sym.getScope().equals("global")) {
                    Parse.semanticErrors.add(new SemanticError(name, "La función '" + name + "' ya fue declarada", line, column));
                    return null;
                }
            }
        }
        Symbol sym = new Symbol(name, "function", returnType, Collections.singletonMap("params", params == null ? new ArrayList<>() : params), getCurrentScope(), line, column);
        symbols.add(sym);
        return sym;
    }

    public Procedure addProcedure(String name, List<Symbol> params, List<Object> instructions,
                                  Integer line, Integer column) {
        if (getCurrentScope().equals("global")) {
            for (Procedure p : procedures) {
                if (p.getName().equals(name) && p.getScope().equals("global")) {
                    Parse.semanticErrors.add(new SemanticError(name, "El procedimiento '" + name + "' ya fue declarado", line, column));
                    return null;
                }
            }
        }
        Procedure proc = new Procedure(name, params == null ? new ArrayList<>() : params, instructions == null ? new ArrayList<>() : instructions, getCurrentScope(), line, column);
        procedures.add(proc);
        return proc;
    }

    public Procedure getProcedure(String name, String scope) {
        String useScope = (scope == null) ? getCurrentScope() : scope;
        for (int i = procedures.size() - 1; i >= 0; i--) {
            Procedure p = procedures.get(i);
            if (p.getName().equals(name)) {
                return p;
            }
        }
        Parse.semanticErrors.add(new SemanticError(name, "Procedimiento '" + name + "' no declarado en el scope actual o superiores", 0, 0));
        System.out.println("No se encontró el procedimiento: " + name);
        return null;
    }

    public Symbol getVariable(String name, String scope) {
        String useScope = (scope == null) ? getCurrentScope() : scope;

        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol sym = symbols.get(i);
            if (sym.getType().equals("variable")
                    && sym.getName().equals(name)
                    && sym.getScope().equals(useScope)) {
                return sym;
            }
        }

        int idx = scopes.indexOf(useScope);
        for (int i = idx - 1; i >= 0; i--) {
            String prev = scopes.get(i);
            for (int j = symbols.size() - 1; j >= 0; j--) {
                Symbol sym = symbols.get(j);
                if (sym.getType().equals("variable")
                        && sym.getName().equals(name)
                        && sym.getScope().equals(prev)) {
                    return sym;
                }
            }
        }

        Parse.semanticErrors.add(new SemanticError(name, "Variable '" + name + "' no declarada en el scope actual o superiores", 0, 0));
        return null;
    }

    public Symbol updateVariable(String name, Object newValue, String scope) {
        String useScope = (scope == null) ? getCurrentScope() : scope;
        int idx = scopes.indexOf(useScope);

        List<String> checkScopes = new ArrayList<>(scopes.subList(0, idx + 1));
        Collections.reverse(checkScopes);

        for (String sc : checkScopes) {
            for (int i = symbols.size() - 1; i >= 0; i--) {
                Symbol sym = symbols.get(i);
                if (sym.getType().equals("variable")
                        && sym.getName().equals(name)
                        && sym.getScope().equals(sc)) {
                    if (!checkTypeCompatibility(sym.getDataType(), newValue)) {
                        Parse.semanticErrors.add(new SemanticError(name, "Tipo incompatible al actualizar '" + name + "'. Esperado: " + sym.getDataType(), sym.getLine(), sym.getColumn()));
                        return null;
                    }
                    sym.setValue(newValue);
                    return sym;
                }
            }
        }

        Parse.semanticErrors.add(new SemanticError(name, "No se encontró la variable '" + name + "' para actualizar", 0, 0));
        return null;
    }

    private boolean checkTypeCompatibility(String expectedType, Object value) {
        switch (expectedType) {
            case "int":
                return value instanceof Integer;
            case "float":
                return value instanceof Double || value instanceof Float || value instanceof Integer;
            case "bool":
                return value instanceof Boolean;
            case "char":
                return (value instanceof Character) ||
                        (value instanceof String && ((String) value).length() == 1);
            case "varchar":
                return value instanceof String;
            default:
                return false;
        }
    }

    public void printTable() {
        System.out.println("id | name | type | data_type | value | scope | line | column");
        for (int i = 0; i < symbols.size(); i++) {
            Symbol s = symbols.get(i);
            System.out.printf("%d | %s | %s | %s | %s | %s | %s | %s\n", i, s.getName(), s.getType(), s.getDataType(), s.getValue(), s.getScope(), s.getLine(), s.getColumn());
        }
    }

    public void clear() {
        scopes = new ArrayList<>(Collections.singletonList("global"));
        symbols.clear();
        procedures.clear();
        scopeCounters.clear();
        Symbol.resetCounter();
        Parse.semanticErrors.clear();
    }
}