/*
 * $Id: AnalysisManager.java,v 1.10 2005/06/16 10:54:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Parameter;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/16 10:54:57 $
 * @author $Author: bass $
 * @module mcm_v1
 */

public interface AnalysisManager {
	Parameter[] analyse() throws AnalysisException;
}
