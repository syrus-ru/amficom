#ifndef _prf_h
#define _prf_h

#include <stdio.h>

void prf_b(char *id);
inline void prf_e() { prf_b(0); }
void prf_print(FILE *f = 0);
void prf_reset();

#endif

