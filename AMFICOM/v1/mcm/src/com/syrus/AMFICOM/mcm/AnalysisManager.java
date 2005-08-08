/*
 * $Id: AnalysisManager.java,v 1.11 2005/08/08 11:46:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Parameter;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/08 11:46:55 $
 * @author $Author: arseniy $
 * @module mcm
 */

public interface AnalysisManager {
	Parameter[] analyse() throws AnalysisException;
}
