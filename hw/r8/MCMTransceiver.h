//////////////////////////////////////////////////////////////////////
// $Id: MCMTransceiver.h,v 1.2 2005/10/09 13:49:23 arseniy Exp $
// 
// Syrus Systems.
// Научно-технический центр
// 2004-2005 гг.
// Проект: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.2 $, $Date: 2005/10/09 13:49:23 $
// $Author: arseniy $
//
// MCMTransceiver.h: interface for the MCMTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_)
#define AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"
#include "pthread.h"
#include "SegmentProcessor.h"


class MCMTransceiver {
	private:
		SegmentProcessor* segmentProcessor;
		pthread_t thread;
		unsigned int timewait;
		unsigned int maxMCMTimeout;
		int running;

		SOCKET sockfd;
		unsigned short port;

	public:
		MCMTransceiver(const unsigned int timewait,
			const unsigned int maxMCMTimeout,
			SegmentProcessor* segmentProcessor,
			const unsigned short port);
		virtual ~MCMTransceiver();

		unsigned int getMaxMCMTimeout() const;

		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		static void* run(void* args);

		void cleanupSocket();
		static int startupWSA();
		static void cleanupWSA();
//		static void printMeasurementSegment(const MeasurementSegment* measurementSegment);

};

#endif // !defined(AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_)
