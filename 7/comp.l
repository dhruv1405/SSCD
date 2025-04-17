%{
#include "y.tab.h"
extern int yyerror(char *str);
extern int yyparse();
%}

%%
"if"               return IF;
"("                return OP;
")"                return CP;
"<" |
">" |
"<=" |
">=" |
"==" |
"!="              return CMP;
"+" |
"-" |
"*" |
"/"               return OPR;
"="               return ASG;
";"               return SC;
[a-zA-Z][_a-zA-Z0-9]*   return ID;
[0-9]+            return NUM;
[ \t\n]+          ;  // Ignore whitespace

.                 return yytext[0];  // Catch all for unrecognized chars
%%

int yywrap()
{
    return 1;
}
