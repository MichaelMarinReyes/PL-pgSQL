grammar ParserpgSQL;

// Gramáticas
init : instr;

instr : sqlLanguaje
     | pgSql;

// Lenguaje SQL
sqlLanguaje : ddl
            | dml
            | dcl
            ;

//DDL
ddl : createSchema
    | useSchema
    | createTable
    | alterTable
    | dropTable
    | insertInto
    ;

createSchema : CREATE SCHEMA ID;
useSchema    : USE SCHEMA ID;

createTable  : CREATE TABLE ID '(' columnListDef? ')';
columnListDef: columnDef (',' columnDef)*;

columnDef    : ID type columnConstraints*
             | foreignKeyDef;

columnConstraints : PRIMARY KEY
                  | NOT NULL
                  | NULL;

foreignKeyDef : FOREIGN KEY '(' ID ')' REFERENCES ID '(' ID ')';

alterTable   : ALTER TABLE ID alterAction;
alterAction  : ADD COLUMN ID type
             | DROP COLUMN ID
             | ADD CONSTRAINT ID FOREIGN KEY '(' ID ')' REFERENCES ID '(' ID ')'
             | DROP CONSTRAINT ID;

dropTable    : DROP TABLE ID;

insertInto   : INSERT INTO (ID '.' ID | ID);

type : INTEGER
     | BOOLEAN
     | VARCHAR '(' INT_LITERAL ')';

//DML
dml : insertStmt
    | updateStmt
    | deleteStmt
    | selectStmt;

insertStmt  : INSERT INTO ID '(' columnList ')' VALUES '(' valueList ')';

updateStmt  : UPDATE ID SET assignmentList whereClause?;

deleteStmt  : DELETE FROM ID whereClause?;

selectStmt  : SELECT selectList FROM tableSource (joinClause)* whereClause?;

joinClause  : (JOIN | LEFT JOIN) tableSource ON condition;

tableSource : ID;

columnList  : ID (',' ID)*;

valueList   : value (',' value)*;

assignmentList : assignment (',' assignment)*;

assignment     : ID '=' value;

selectList : '*'
           | columnRef (',' columnRef)*;

columnRef : ID ('.' ID)?;

// WHERE y condiciones
whereClause : WHERE condition;

condition   : ID comparator value;

comparator  : '='
            | '<>'
            | '<'
            | '>'
            | '<='
            | '>=';

value : VARCHAR_LITERAL
      | INT_LITERAL
      | BOOLEAN_LITERAL;

//DCL
dcl : createUser
    | grantStmt
    | revokeStmt;

createUser : CREATE USER ID;

grantStmt  : GRANT permiso ON objeto TO ID;

permiso : SELECT
        | INSERT
        | UPDATE
        | DELETE;

objeto : ID '.' (ID | '*');

revokeStmt : REVOKE permiso ON ID FROM ID;

// Lenguaje pg_sql
pgSql : boolExpr
      | flowControl
      | cicles
      | controlStmt
      | print
      | transactional
      | builtIn
      | functions
      | procedures;

//Operaciones aritméticas y relacionales y booleanas
boolExpr : boolOrExpr;

boolOrExpr : boolAndExpr (OR boolAndExpr)*;

boolAndExpr : boolNotExpr (AND boolNotExpr)*;

boolNotExpr : NOT boolNotExpr
            | relationalExpr;

relationalExpr : mathOp (comparator mathOp)?;

mathOp : addExpr;

addExpr : mulExpr (('+' | '-') mulExpr)*;

mulExpr : powExpr (('*' | '/') powExpr)*;

powExpr : atom ('^' powExpr)?;

atom : INT_LITERAL
     | BOOLEAN_LITERAL
     | '(' boolExpr ')';


//Control de Flujo
flowControl : ;

//Ciclos
cicles : ;

//Sentencias de Control
controlStmt : ;

//Impresión de mensajes y excepciones
print : ;

//Transaccional
transactional : ;

//Funciones built-in
builtIn : ;

//Funciones
functions: ;

//Procedimientos
procedures : ;

// Lexer
CREATE: 'CREATE';
USE: 'USE';
SCHEMA: 'SCHEMA';
TABLE: 'TABLE';
ALTER: 'ALTER';
DROP: 'DROP';
ADD: 'ADD';
COLUMN: 'COLUMN';
CONSTRAINT: 'CONSTRAINT';
PRIMARY: 'PRIMARY';
KEY: 'KEY';
FOREIGN: 'FOREIGN';
REFERENCES: 'REFERENCES';
NOT: 'NOT';
NULL: 'NULL';
BOOLEAN: 'BOOLEAN';
INTEGER: 'INTEGER';
VARCHAR: 'VARCHAR';
INSERT: 'INSERT';
INTO: 'INTO';
VALUES: 'VALUES';
UPDATE: 'UPDATE';
SET: 'SET';
DELETE: 'DELETE';
FROM: 'FROM';
WHERE: 'WHERE';
SELECT: 'SELECT';
JOIN: 'JOIN';
LEFT: 'LEFT';
ON: 'ON';
TRUE: 'TRUE';
FALSE: 'FALSE';
USER: 'USER';
GRANT: 'GRANT';
TO: 'TO';
REVOKE: 'REVOKE';
OR: 'OR';
AND: 'AND';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT_LITERAL: [0-9]+;
DECIMAL_LITERAL : [0-9]+'.'[0-9]+;
VARCHAR_LITERAL: '\'' ( ~['\\] | '\\' . )* '\'';
CHAR_LITERAL: [a-z];
BOOLEAN_LITERAL: TRUE | FALSE;
//ARRAY
//DATE

WS: [ \t\r\n]+ -> skip;
LINE_COMMENT: '--' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;