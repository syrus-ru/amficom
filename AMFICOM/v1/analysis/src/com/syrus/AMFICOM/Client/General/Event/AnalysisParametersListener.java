/*-
 * $Id: AnalysisParametersListener.java,v 1.3 2005/08/08 11:59:52 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Subscriber that wants to know that Heap.MinuitAnalysisParameters has changed
 * @author $Author: arseniy $
 * @author saa
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:59:52 $
 * @module analysis
 */
public interface AnalysisParametersListener {
	void analysisParametersUpdated();
}
