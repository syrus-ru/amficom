//////////////////////////////////////////////////////////////////////
// $Id: r7general.h,v 1.2 2005/08/29 18:09:57 arseniy Exp $
// 
// Syrus Systems.
// Научно-технический центр
// 2004-2005 гг.
// Проект: r7
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.2 $, $Date: 2005/08/29 18:09:57 $
// $Author: arseniy $
//
// r7general.h: common definitions.
//
//////////////////////////////////////////////////////////////////////

#ifndef R7GENERAL_H
#define R7GENERAL_H

#include <deque>
#include "MeasurementSegment.h"
#include "ResultSegment.h"

using namespace std;
typedef deque<MeasurementSegment*> MeasurementQueueT;
typedef deque<ResultSegment*> ResultQueueT;

#endif
