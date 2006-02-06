/*-
 * $Id: PrimaryRefAnalysisListener.java,v 1.2 2005/07/22 06:56:50 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/07/22 06:56:50 $
 * @module
 */
public interface PrimaryRefAnalysisListener {
	void primaryRefAnalysisCUpdated(); // Created or Updated
	void primaryRefAnalysisRemoved(); // Removed
}
