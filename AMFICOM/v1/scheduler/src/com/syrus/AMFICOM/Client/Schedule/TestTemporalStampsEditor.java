/*
 * $Id: TestTemporalStampsEditor.java,v 1.2 2005/03/02 11:55:36 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.util.Collection;

import com.syrus.AMFICOM.measurement.TestTemporalStamps;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/02 11:55:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface TestTemporalStampsEditor {

	void setTemporalPatterns(Collection temporalPatterns);

	void setTestTemporalStamps(TestTemporalStamps testTemporalStamps);

	TestTemporalStamps getTestTemporalStamps();
}
