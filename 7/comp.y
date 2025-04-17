%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
int yyerror(char *str);
%}

%token IF OP CP CMP SC ASG ID NUM OPR

%%
start: sif;

sif: IF OP cmpn CP stmt {
    printf("VALID STATEMENT IF\n");
};

cmpn: ID CMP ID
    | ID CMP NUM;

stmt: ID ASG ID OPR ID SC
    | ID ASG ID OPR NUM SC
    | ID ASG NUM OPR ID SC
    | ID ASG NUM OPR NUM SC
    | ID ASG ID SC
    | ID ASG NUM SC;
%%

int yyerror(char *str)
{
    fprintf(stderr, "SYNTAX ERROR: %s\n", str);
    return 1;
}

int main()
{
    printf("Enter the statement:\n");
    yyparse();
    printf("Parsing finished.\n");
    return 0;
}
