#ifndef R7GENERAL_H
#define R7GENERAL_H

#include <deque>
#include "MeasurementSegment.h"
#include "ResultSegment.h"

using namespace std;
typedef deque<MeasurementSegment*> MeasurementQueueT;
typedef deque<ResultSegment*> ResultQueueT;

#endif
