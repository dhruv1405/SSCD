%{
#include "y.tab.h"
#include <stdlib.h>
%}

%%
[0-9]+    { yylval.intval = atoi(yytext); return NUMBER; }
[a-z]+    { yylval.fchar = yytext; return NAME; }
[\t ]+    ;      // Ignore whitespace
\n        return 0;
.         { return yytext[0]; }
%%

int yywrap() {
    return 1;
}
