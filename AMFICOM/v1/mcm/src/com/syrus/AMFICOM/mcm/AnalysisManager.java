/*
 * $Id: AnalysisManager.java,v 1.13.2.2 2006/03/06 14:15:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.AnalysisResultParameter;

/**
 * @version $Revision: 1.13.2.2 $, $Date: 2006/03/06 14:15:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

interface AnalysisManager {
	Set<AnalysisResultParameter> analyse() throws AnalysisException, ApplicationException;
}
