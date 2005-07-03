/*-
 * $Id: AnalysisParametersListener.java,v 1.1 2005/05/27 16:08:52 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Subscriber that wants to know that Heap.MinuitAnalysisParameters has changed
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/05/27 16:08:52 $
 * @module analysis_v1
 */public interface AnalysisParametersListener {
    void analysisParametersUpdated();
}
