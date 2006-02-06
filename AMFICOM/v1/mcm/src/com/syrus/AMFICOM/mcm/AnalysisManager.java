/*
 * $Id: AnalysisManager.java,v 1.13 2005/09/14 18:13:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Parameter;

/**
 * @version $Revision: 1.13 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

interface AnalysisManager {
	Parameter[] analyse() throws AnalysisException;
}
