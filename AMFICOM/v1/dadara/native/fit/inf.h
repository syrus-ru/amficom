#ifndef _INF_H
#define _INF_H

// finite() check
#ifndef finite
	#ifdef _MSC_VER
		#include <float.h>
		#define finite(x) _finite(x)
	#else
		#include <math.h>
	#endif
#endif

#define HUGE 1e99

#endif

