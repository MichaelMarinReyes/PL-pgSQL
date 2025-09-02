package backend.analyzers;

import analyzers.ParserpgSQLBaseVisitor;
import backend.tables.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseVisitor extends ParserpgSQLBaseVisitor<Object> {
    private final SymbolTable symbolTable = SymbolTable.getInstance();


}