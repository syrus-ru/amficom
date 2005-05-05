/*-
 * $Id: DetailedEventUtil.java,v 1.1 2005/05/05 11:45:28 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

/**
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/05/05 11:45:28 $
 * @module
 */
public class DetailedEventUtil {
    public static double getLoss(DetailedEvent ev)
    throws NoSuchFieldException {
        if (ev instanceof LinearDetailedEvent)
            return ((LinearDetailedEvent)ev).getLoss();
        if (ev instanceof SpliceDetailedEvent)
            return ((SpliceDetailedEvent)ev).getLoss();
        if (ev instanceof NotIdentifiedDetailedEvent)
            return ((NotIdentifiedDetailedEvent)ev).getLoss();
        if (ev instanceof ConnectorDetailedEvent)
            return ((ConnectorDetailedEvent)ev).getLoss();
        throw new NoSuchFieldException();        
    }
    public static double getLossDiff(DetailedEvent ev1, DetailedEvent ev2)
    throws NoSuchFieldException {
        return getLoss(ev1) - getLoss(ev2);
    }

    public static int getWidth(DetailedEvent ev) {
        return ev.getEnd() - ev.getBegin();
    }
    public static int getWidthDiff(DetailedEvent ev1, DetailedEvent ev2) {
        return getWidth(ev1) - getWidth(ev2);
    }

    public static double getAmpl(DetailedEvent ev)
    throws NoSuchFieldException {
        if (ev instanceof LinearDetailedEvent)
            return 0;
        if (ev instanceof SpliceDetailedEvent)
            return 0;
        if (ev instanceof ConnectorDetailedEvent)
            return ((ConnectorDetailedEvent)ev).getY2() -
                ((ConnectorDetailedEvent)ev).getY0();
        if (ev instanceof EndOfTraceDetailedEvent)
            return ((EndOfTraceDetailedEvent)ev).getY2() -
                ((EndOfTraceDetailedEvent)ev).getY0();
        throw new NoSuchFieldException();
    }
    public static double getAmplDiff(DetailedEvent ev1, DetailedEvent ev2)
    throws NoSuchFieldException {
        return getAmpl(ev1) - getAmpl(ev2);
    }
}
