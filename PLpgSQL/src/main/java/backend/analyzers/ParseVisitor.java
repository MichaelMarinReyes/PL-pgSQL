package backend.analyzers;

import analyzers.ParserpgSQLBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class ParseVisitor extends ParserpgSQLBaseVisitor {
    Map<String, String> memory = new HashMap<>();
    
}
