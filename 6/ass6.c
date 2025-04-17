#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void E();
void EP();
void T();
void TP();
void F();
int i = 0;
char str[100];
char tp;
void advance() {
i++;
tp = str[i];
}
void F() {
if (tp == 'i') {
advance();
} else if (tp == 'n') {
advance();
} else if (tp == '(') {
advance();
E();
if (tp == ')') {
advance();
} else {
printf("Error: Missing closing parenthesis\n");
exit(1);
}
} else {
printf("Error: Invalid token %c\n", tp);
exit(1);
}
}
void TP() {if (tp == '*') {
advance();
F();
TP();
}
}
void T() {
F();
TP();
}
void EP() {
if (tp == '+') {
advance();
T();
EP();
}
}
void E() {
T();
EP();
}
int main() {
int op;
while (1) {
printf("Enter the string: ");
scanf("%s", str);
i = 0;
tp = str[i];
E();
if (tp == '\0')
printf("String is accepted\n");
else
printf("String is rejected\n");
printf("Enter 1 to exit, 0 to continue: ");
scanf("%d", &op);
if (op == 1)
exit(0);}
return 0;
}
