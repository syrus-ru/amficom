#ifndef GENERAL_H
#define GENERAL_H

#define MAXLENTASK 256
#define MAXLENREPORT 34000
#define MAXLENPATH 128
#define FIFOROOTPATH "/tmp"
#define RTUMAXTIMEOUT 80	//sec
#define RTUHEADERSIZE 16
#define RTUCOMMANDMAXSIZE 100

#include <deque>
#include "MeasurementSegment.h"
#include "ResultSegment.h"

using namespace std;
typedef deque<MeasurementSegment*> MeasurementQueueT;
typedef deque<ResultSegment*> ResultQueueT;

#endif
