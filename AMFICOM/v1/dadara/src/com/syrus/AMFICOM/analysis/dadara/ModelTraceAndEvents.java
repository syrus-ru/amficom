/*-
 * $Id: ModelTraceAndEvents.java,v 1.3 2005/04/30 09:39:58 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Предоставляет информацию о м.ф. и списке событий определенной р/г.
 * Эти две части связаны довольно тесно:
 * они одновременно получаются (в анализе), одновременно используются
 * при отображении аналитической кривой (т.е. м.ф.) с раскраской событий,
 * одновременно нужны в эталоне, т.к. по ним обоим формируются пороги
 * для м.ф.
 * 
 * @author $Author: saa $
 * @author: saa
 * @version $Revision: 1.3 $, $Date: 2005/04/30 09:39:58 $
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
