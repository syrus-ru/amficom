/*
 * $Id: AnalysisManager.java,v 1.8 2004/07/21 08:26:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;

/**
 * @version $Revision: 1.8 $, $Date: 2004/07/21 08:26:06 $
 * @author $Author: arseniy $
 * @module 
 */

public interface AnalysisManager {
	SetParameter[] analyse() throws AnalysisException;
}
