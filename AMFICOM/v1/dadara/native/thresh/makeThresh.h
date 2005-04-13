#ifndef _MAKETHRESH_H
#define _MAKETHRESH_H

#include "../JNI/ThreshArray.h"

// если thYc == 0, то ничего не делает
void extendThreshToCover(THX *thX, THY *thY, int thXc, int thYc, int isUpper,
		double *yBase, int xMin, int xMax, double *yTgt);

#endif
