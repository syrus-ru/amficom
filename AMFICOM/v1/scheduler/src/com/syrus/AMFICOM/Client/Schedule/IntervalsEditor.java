/*-
* $Id: IntervalsEditor.java,v 1.1 2005/04/27 15:27:40 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.measurement.TestTemporalStamps;


/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 15:27:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface IntervalsEditor {

	void setIntervalsTemporalPattern(TestTemporalStamps testTemporalStamps);
	
}

