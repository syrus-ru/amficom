/*
 * $Id: AnalysisManager.java,v 1.9 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;

/**
 * @version $Revision: 1.9 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public interface AnalysisManager {
	SetParameter[] analyse() throws AnalysisException;
}
