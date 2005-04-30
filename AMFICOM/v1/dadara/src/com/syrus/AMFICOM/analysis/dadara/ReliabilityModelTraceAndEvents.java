/*-
 * $Id: ReliabilityModelTraceAndEvents.java,v 1.1 2005/04/30 09:09:30 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Method {@link #getSimpleEvents()} will return
 * array of {@link ReliabilitySimpleReflectogramEvent},
 * not just
 * array of {@link SimpleReflectogramEvent}.
 * 
 * Method {@link #getSimpleEvent(int)} will return
 * {@link ReliabilitySimpleReflectogramEvent},
 * not just {@link SimpleReflectogramEvent}.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/04/30 09:09:30 $
 * @module
 */
public interface ReliabilityModelTraceAndEvents extends ModelTraceAndEvents {
    // ReliabilitySimpleReflectogramEvent[] getSimpleEvents();
    // ReliabilitySimpleReflectogramEvent getSimpleEvent(int nEvent);
}
