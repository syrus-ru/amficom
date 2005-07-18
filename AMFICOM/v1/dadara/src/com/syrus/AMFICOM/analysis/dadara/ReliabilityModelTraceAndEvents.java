/*-
 * $Id: ReliabilityModelTraceAndEvents.java,v 1.2 2005/07/18 12:41:10 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Дополняет {@link ModelTraceAndEvents} информацией о надежности событий.
 * Контракт этого интерфейса заключается в том, что
 * метод {@link #getSimpleEvents()} будет возвращать
 * массив {@link ReliabilitySimpleReflectogramEvent},
 * а не просто массив {@link SimpleReflectogramEvent},
 * как это предписано суперинтерфейсом.
 * 
 * Аналогично, метод {@link #getSimpleEvent(int)} будет возвращать
 * {@link ReliabilitySimpleReflectogramEvent},
 * а не просто {@link SimpleReflectogramEvent}.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/07/18 12:41:10 $
 * @module
 */
public interface ReliabilityModelTraceAndEvents extends ModelTraceAndEvents {
    // ReliabilitySimpleReflectogramEvent[] getSimpleEvents();
    // ReliabilitySimpleReflectogramEvent getSimpleEvent(int nEvent);
}
