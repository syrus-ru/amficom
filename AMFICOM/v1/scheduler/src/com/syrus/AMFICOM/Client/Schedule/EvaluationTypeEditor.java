/*
 * $Id: EvaluationTypeEditor.java,v 1.2 2005/03/02 11:55:35 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.util.Collection;

import com.syrus.AMFICOM.measurement.EvaluationType;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/02 11:55:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface EvaluationTypeEditor {

	void setEvaluationType(EvaluationType evaluationType);
	
	void setEvaluationTypes(Collection evaluationTypes);

	EvaluationType getEvaluationType();
}
