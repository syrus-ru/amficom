/*
 * $Id: EvaluationManager.java,v 1.7 2005/02/03 16:03:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;

/**
 * @version $Revision: 1.7 $, $Date: 2005/02/03 16:03:08 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public interface EvaluationManager {

	SetParameter[] evaluate() throws EvaluationException;
//
//	AlarmLevel getAlarmLevel();
}
