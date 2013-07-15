grammar ArasQuery;

query  : query_expr*;

query_expr :
    field_expr                  # FXP
 |  AND '(' list_expr ')'       # AND
 |  OR '(' list_expr ')'        # OR
 |  NOT '(' query_expr ')'      # NOT
;

list_expr : query_expr ( COMMA query_expr)*;

field_expr :
    QN '=' ID         # QN
 |  VAL '=' ID        # VAL
 |  REL '=' ID        # REL
 |  ID '=' ID         # ID
;

COMMENT : '/*' .*? '*/' -> skip ; // .*? matches anything until the first */;

AND : 'AND';
OR : 'OR';
NOT : 'NOT';

QN : 'QN';
VAL : 'VAL';
REL : 'REL';

COMMA : ',';

// URI: /^([a-z0-9+.-]+):(?://(?:((?:[a-z0-9-._~!$&'()*+,;=:]|%[0-9A-F]{2})*)@)?((?:[a-z0-9-._~!$&'()*+,;=]|%[0-9A-F]{2})*)(?::(\d*))?(/(?:[a-z0-9-._~!$&'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?(?:[a-z0-9-._~!$&'()*+,;=:@]|%[0-9A-F]{2})+(?:[a-z0-9-._~!$&'()*+,;=:@/]|%[0-9A-F]{2})*)?)(?:\?((?:[a-z0-9-._~!$&'()*+,;=:/?@]|%[0-9A-F]{2})*))?(?:#((?:[a-z0-9-._~!$&'()*+,;=:/?@]|%[0-9A-F]{2})*))?$/i

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

ID : '\'' ~['\'' \t\r\n]+ '\'';

