/*
 * $Id: AnalysisManager.java,v 1.12 2005/08/17 11:48:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Parameter;

/**
 * @version $Revision: 1.12 $, $Date: 2005/08/17 11:48:45 $
 * @author $Author: arseniy $
 * @module mcm
 */

interface AnalysisManager {
	Parameter[] analyse() throws AnalysisException;
}
