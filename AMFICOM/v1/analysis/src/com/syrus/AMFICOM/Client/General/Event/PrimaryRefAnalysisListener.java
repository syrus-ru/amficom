/*-
 * $Id: PrimaryRefAnalysisListener.java,v 1.1 2005/04/29 12:37:00 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/04/29 12:37:00 $
 * @module
 */
public interface PrimaryRefAnalysisListener {
    void primaryRefAnalysisCUpdated(); // Created or Updated
    void primaryRefAnalysisRemoved(); // Removed
}
