/*-
 * $Id: ModelTraceAndEvents.java,v 1.2 2005/04/11 10:35:31 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/04/11 10:35:31 $
 * @module
 */
public interface ModelTraceAndEvents
{
	double getDeltaX();
	ModelTrace getModelTrace();
	SimpleReflectogramEvent[] getSimpleEvents();
	SimpleReflectogramEvent getSimpleEvent(int nEvent);
	ComplexReflectogramEvent[] getComplexEvents();
	int getEventByCoord(int x);
	int getNEvents();
}
