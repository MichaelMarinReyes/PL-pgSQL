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
    | createFunction
    | replaceFunction
    | alterFunction
    ;

createSchema : CREATE SCHEMA ID;

useSchema    : USE SCHEMA ID;

createTable  : CREATE TABLE ID '(' columnListDef? ')' ';';

columnListDef: columnDef (',' columnDef)*;

columnDef    : ID typeSpec columnConstraints*
             | foreignKeyDef;

columnConstraints : PRIMARY KEY
                  | NOT NULL_
                  | NULL_;

foreignKeyDef : FOREIGN KEY '(' ID ')' REFERENCES ID '(' ID ')';

alterTable   : ALTER TABLE ID alterAction;

alterAction  : ADD COLUMN ID typeSpec
             | DROP COLUMN ID
             | ADD CONSTRAINT ID FOREIGN KEY '(' ID ')' REFERENCES ID '(' ID ')'
             | DROP CONSTRAINT ID;

dropTable    : DROP TABLE ID;

insertInto   : INSERT INTO (ID '.' ID | ID);

typeSpec : baseType
         | ARRAY '<' baseType '>';

baseType : INTEGER
         | DECIMAL
         | VARCHAR '(' INT_LITERAL ')'
         | CHAR
         | BOOLEAN
         | DATE;

//DML
dml : insertStmt
    | updateStmt
    | deleteStmt
    | selectStmt;

insertStmt  : INSERT INTO ID '(' columnList ')' VALUES '(' valueList ')';

updateStmt  : UPDATE ID SET assignmentList whereClause?;

deleteStmt  : DELETE FROM ID whereClause?;

selectStmt  : SELECT selectList (FROM tableSource (joinClause)*)? whereClause? ';'?;

selectNoSemi : SELECT selectList (FROM tableSource (joinClause)*)? whereClause?;

joinClause  : (JOIN | LEFT JOIN) tableSource ON condition;

tableSource : ID;

columnList  : ID (',' ID)*;

valueList   : value (',' value)*;

assignmentList : assignment (',' assignment)*;

assignment : ID '=' value;

selectList : '*'
           | columnRef (',' columnRef)*
           | functionCall (',' functionCall)*;

functionCall : ID '(' argList? ')';

argList : expr (',' expr)*;

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

value :
      | STRING_LITERAL
      | INT_LITERAL
      | DECIMAL_LITERAL
      | BOOLEAN_LITERAL
      | DATE_LITERAL;

//DCL
dcl : createUser
    | grantStmt
    | revokeStmt;

createUser : CREATE USER ID ';';

grantStmt : GRANT permiso ON objeto TO ID ';';

permiso : SELECT
        | INSERT
        | UPDATE
        | DELETE;

objeto : ID '.' (ID | '*');

revokeStmt : REVOKE permiso ON ID FROM ID ';';

// Lenguaje pg_sql
pgSql : block;

block : declareSection? stmtList;

declareSection : DECLARE declItem+;

declItem : ID typeSpec ('=' expr)? ';';

stmtList : stmt*;

stmt : assignStmt
     | flowControl
     | cicles
     | controlStmt
     | message
     | transactional
     | builtIn
     | functions
     | procedures
     ;

assignStmt : ID ':=' expr ';';

expr : boolExpr ;

//Operaciones aritméticas y relacionales y booleanas
boolExpr : boolOrExpr;

boolOrExpr : boolAndExpr (OR boolAndExpr)*;

boolAndExpr : boolNotExpr (AND boolNotExpr)*;

boolNotExpr : NOT boolNotExpr
            | relationalExpr;

relationalExpr : mathOp (comparator mathOp?);

mathOp : addExpr;

addExpr : mulExpr (('+' | '-') mulExpr)*;

mulExpr : powExpr (('*' | '/') powExpr)*;

powExpr : atom ('^' powExpr)?;

atom : INT_LITERAL
     | DECIMAL_LITERAL
     | STRING_LITERAL
     | CHAR_LITERAL
     | BOOLEAN_LITERAL
     | DATE_LITERAL
     | arrayLiteral
     | ID
     | '(' boolExpr ')';

// Arreglos
arrayLiteral : '[' (expr (',' expr)*)? ']';


//Control de Flujo
flowControl : ifStmt
            | caseStmt;

ifStmt : IF boolExpr THEN stmtList (ELSE stmtList)? END IF ';';

caseStmt : CASE caseWhen+ (ELSE stmtList)? END CASE ';';

caseWhen : WHEN boolExpr THEN stmtList;

//Ciclos
cicles : forNumericStmt
       | foreachArrayStmt
       | foreachQueryStmt;

forNumericStmt : FOR ID IN expr DOTDOT expr LOOP stmtList END LOOP ';';

foreachArrayStmt : FOREACH ID IN ARRAY ID LOOP stmtList END LOOP ';';

foreachQueryStmt : FOREACH ID IN selectStmt LOOP stmtList END LOOP ';';

//Sentencias de Control
controlStmt : EXIT
            | CONTINUE;

//Impresión de mensajes y excepciones
message : raiseStmt
        | raiseLevel;

raiseStmt : RAISE ';'
          | RAISE raiseLevel (STRING_LITERAL | expr)(',' exprList)? ';';

raiseLevel : NOTICE
           | WARNING
           | EXCEPTION;

exprList : expr (',' expr)*;

//Transaccional
transactional : BEGIN_ stmtList (COMMIT | ROLLBACK) ';';

//Funciones built-in
builtIn : NOW '(' ')'
        | CONCAT '(' exprList ')'
        | MODIFY_DATE '(' expr (',' expr)* ')'
        | CAST '(' expr ',' baseType ')'
        | RANDOM '(' ')'
        | SUBSTRING '(' expr (',' expr) ')'
        | ARRAY_PUSH '(' expr (',' expr) ')'
        | LEN '(' expr ')';

//Funciones
functions : CREATE FUNCTION ID '(' params ')' RETURNS baseType AS '$''$' expr '$''$' LANGUAGE PLPGSQL;

createFunction
    : CREATE FUNCTION ID '(' params? ')' RETURNS baseType AS DOLLAR_DOLLAR beginFunctionBlock DOLLAR_DOLLAR LANGUAGE PLPGSQL ';'?
    ;

replaceFunction
    : REPLACE FUNCTION ID '(' params? ')' RETURNS baseType AS DOLLAR_DOLLAR beginFunctionBlock DOLLAR_DOLLAR LANGUAGE PLPGSQL ';'?
    ;

alterFunction
    : ALTER FUNCTION ID '(' params? ')' RENAME TO ID ';'?
    ;

params : param (',' param)* ;

param  : ID typeSpec;

beginFunctionBlock : BEGIN_ stmtListFunction END ';'? ;

stmtListFunction : stmt;

//Procedimientos
procedures : createProcedure
           | callProcedure
           | alterProcedure
           | replaceProcedure;

createProcedure : CREATE PROCEDURE ID '(' paramList? ')' LANGUAGE PLPGSQL AS DOLLAR_DOLLAR procedureBody DOLLAR_DOLLAR;

callProcedure : CALL ID '(' argList? ')';

alterProcedure : ALTER PROCEDURE ID '(' paramTypeList? ')' RENAME TO ID;

replaceProcedure : REPLACE PROCEDURE ID '(' paramList? ')' RETURNS baseType AS DOLLAR_DOLLAR procedureBody DOLLAR_DOLLAR LANGUAGE PLPGSQL;

paramList : param (',' param)*;

paramTypeList : baseType (',' baseType)*;

procedureBody : (procedureStmt)*;

procedureStmt : INSERT INTO ID '(' columnList '(' VALUES ')' argList ')' ';'
                    | RAISE NOTICE STRING_LITERAL (',' expr)* ';'
                    | BEGIN_ procedureBody END ';'
                    | expr ';';

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
NULL_: 'NULL';
BOOLEAN: 'BOOLEAN';
INTEGER: 'INTEGER';
DECIMAL: 'DECIMAL';
VARCHAR: 'VARCHAR';
CHAR: 'CHAR';
DATE: 'DATE';
ARRAY: 'ARRAY';
DECLARE: 'DECLARE';
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
//IF THEN ELSE
IF: 'IF';
THEN: 'THEN';
ELSE: 'ELSE';
END: 'END';
CASE: 'CASE';
WHEN: 'WHEN';
//Ciclos
FOR: 'FOR';
IN: 'IN';
DOTDOT: '..';
LOOP: 'LOOP';
FOREACH: 'FOREACH';
//Sentenciasl de control
EXIT: 'EXIT';
CONTINUE: 'CONTINUE';
//Mensajes
RAISE: 'RAISE';
NOTICE: 'NOTICE';
WARNING: 'WARNING';
EXCEPTION: 'EXCEPTION';
//Transaccional
BEGIN_: 'BEGIN';
COMMIT: 'COMMIT';
ROLLBACK: 'ROLLBACK';
//Built-in
NOW: 'NOW';
CONCAT: 'CONCAT';
MODIFY_DATE: 'MODIFY_DATE';
CAST: 'CAST';
RANDOM: 'RANDOM';
SUBSTRING: 'SUBSTRING';
ARRAY_PUSH: 'ARRAY_PUSH';
LEN: 'LEN';
//Funciones
FUNCTION: 'FUNCTION';
RETURNS: 'RETURNS';
AS: 'AS';
LANGUAGE: 'LANGUAGE';
PLPGSQL: 'PLPGSQL';
DOLLAR_DOLLAR: '$$';
REPLACE: 'REPLACE';
RENAME: 'RENAME';
PROCEDURE: 'PROCEDURE';
CALL: 'CALL';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT_LITERAL: [0-9]+;
DECIMAL_LITERAL : [0-9]+'.'[0-9]+;
STRING_LITERAL  : '\'' ( ~["\\] | '\\' . )* '\'' ;
CHAR_LITERAL:  '\'' ( ~['\\\r\n] | '\\' . ) '\'' ;
BOOLEAN_LITERAL: TRUE | FALSE;
DATE_LITERAL:  [0-9][0-9][0-9][0-9] '-' [0-9][0-9] '-' [0-9][0-9];

WS: [ \t\r\n]+ -> skip;
LINE_COMMENT: '--' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;