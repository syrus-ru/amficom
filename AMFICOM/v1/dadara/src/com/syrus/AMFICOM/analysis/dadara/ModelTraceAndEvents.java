/*-
 * $Id: ModelTraceAndEvents.java,v 1.7 2005/07/15 09:53:39 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;

/**
 * Предоставляет информацию о м.ф. и списке событий определенной р/г.
 * Эти две части связаны довольно тесно:
 * они одновременно получаются (в анализе), одновременно используются
 * при отображении аналитической кривой (т.е. м.ф.) с раскраской событий,
 * одновременно нужны в эталоне, т.к. по ним обоим формируются пороги
 * для м.ф.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.7 $, $Date: 2005/07/15 09:53:39 $
 * @module
 */
public interface ModelTraceAndEvents
{
	double getDeltaX();
	ModelTrace getModelTrace();
	SimpleReflectogramEvent[] getSimpleEvents();
	SimpleReflectogramEvent getSimpleEvent(int nEvent);
	DetailedEvent getDetailedEvent(int i);
	DetailedEvent[] getDetailedEvents();
	int getEventByCoord(int x);
	int getNEvents();
}
