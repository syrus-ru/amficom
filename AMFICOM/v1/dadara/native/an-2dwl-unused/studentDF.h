/****************************************************/
/*         –аспределение —тьюдента                  */
/****************************************************/
#ifndef __STUDENT_H__

#define ENTRY   extern
#define LOCAL   static

ENTRY double
studentDF(double n, double x);
//¬ычисл€етс€ веро€тность того, что случайна€ величина,
// подчин€юща€с€ распределению —тьюдента (T-распределению)
// c n степен€ми свободы, не превосходит (меньше или равна) x.

ENTRY double
inv_studentDF(double n, double p);
// ѕо данной веро€тности p вычисл€етс€ значение q,
// дл€ которого studentDF(n,q) вернет p.

#define	__STUDENT_H__	/* Prevents redefinition	*/
#endif              	/* Ends #ifndef __STUDENT_H__	*/


