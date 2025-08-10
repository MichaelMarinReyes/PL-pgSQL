grammar ParserpgSQL;


// Gramáticas
init : sqlLanguaje
     | pgSql;


// Lenguaje SQL

sqlLanguaje : ddl
             | 'dml'
             ;

ddl : createSchema
    ;


createSchema : 'CREATE' 'SCHEMA' ID;

// Lenguaje pg_sql
pgSql : 'a';


//Lexer
ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT: [0-9];
DOUBLE: [0-9].[0-9]*;
WS: [ \t\r\n]+ -> skip;
COMMENT_LINE: '--'~[\r\n] -> skip;
COMMENT_BLOCK: '/*' .*?'*/' -> skip;