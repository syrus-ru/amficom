/*
 * $Id: AnalysisManager.java,v 1.13.2.1 2006/03/02 16:12:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import com.syrus.AMFICOM.measurement.AnalysisResultParameter;

/**
 * @version $Revision: 1.13.2.1 $, $Date: 2006/03/02 16:12:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

interface AnalysisManager {
	Set<AnalysisResultParameter> analyse() throws AnalysisException;
}
