/*
 * $Id: KISTracer.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.server.LogWriter;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @module serverprocess
 */
public final class KISTracer extends Thread
{
    int timeInterval = 3000;

	LogWriter log;

	private volatile boolean running = true;

	public KISTracer(LogWriter log)
	{
		this.log = log;
	}

	private KISTracer()
	{
	}

	public void stopRunning()
	{
		running = false;
	}

	public void run()
	{
		log.log("KIS Tracer:");
		log.log("Starting operation");
		try
		{
			while (running)
			{
				try
				{
					sleep(timeInterval);
//					log.log("Woke up!");
//					checkAlarmState (logicalNetLayer);
				}
				catch (InterruptedException e)
				{
					log.log("Error in execution: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			log.log("Could not finish operation");
		}
		finally
		{
			log.log("KIS Tracer thread finished");
		}
	}
}
