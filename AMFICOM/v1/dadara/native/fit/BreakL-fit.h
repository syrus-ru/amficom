#ifndef _BREAKL_FIT_H
#define _BREAKL_FIT_H

#include "../Common/ModelF.h"


/*
 * linkFlags = BREAKL_LINK_FIXLEFT: левая точка должна быть установлена в заданное положение
 *  (это учитывается только во время y-фитировки)
 */
const int BREAKL_LINK_FIXLEFT = 0x1;
int BreakL_getLinkDataLength(int linkFlags);
int BreakL_getLinkDataOffset(int linkFlags, int desiredData);

// создает mf вида MF_ID_BREAKL (начальное состояние mf забывается)
void BreakL_FitL (ModelF &mf, double *data, int i0, int x0, int length, int linkFlags, double *linkData);
void BreakL_FitI (ModelF &mf, double *data, int i0, int x0, int length, int linkFlags, double *linkData, int quick);
void BreakL_Fit  (ModelF &mf, double *data, int i0, int x0, int length, int linkFlags, double *linkData, int quick, double error1 = 0, double error2 = -999, int max_points = 0);
void BreakL_Fit2 (ModelF &mf, double *data, int i0, int x0, int length, int linkFlags, double *linkData, int quick, double *error);

#endif

