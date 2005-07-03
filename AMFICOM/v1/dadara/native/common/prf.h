#ifndef _prf_h
#define _prf_h

void prf_b(char *id);
inline void prf_e() { prf_b(0); }
void prf_print();
void prf_reset();

//#include <stdio.h>
//void prf_print(FILE *f);

#endif

