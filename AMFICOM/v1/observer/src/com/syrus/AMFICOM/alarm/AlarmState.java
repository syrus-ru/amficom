/*-
 * $Id: AlarmState.java,v 1.2 2006/05/31 08:10:53 bass Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.alarm;


/**
 * @author Stanislav Kholshin
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/05/31 08:10:53 $
 * @deprecated Use either {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus} instead.
 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus
 * @module observer
 */
@Deprecated
public enum AlarmState {
	/**
	 * @deprecated Use either {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#PENDING}
	 *             or {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_ASSIGNED} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#PENDING
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_ASSIGNED
	 */
	@Deprecated
	OPENED,
	/**
	 * @deprecated Use either {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#ABANDONED}
	 *             or {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#IGNORED} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#ABANDONED
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#IGNORED
	 */
	@Deprecated
	ABORTED,
	/**
	 * @deprecated Use {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#ACKNOWLEDGED} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#ACKNOWLEDGED
	 */
	@Deprecated
	ACCEPTED,
	/**
	 * @deprecated Use {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#IN_PROGRESS} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#IN_PROGRESS
	 */
	@Deprecated
	REPARING,
	/**
	 * @deprecated Use either {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_TT_COMPLETED}
	 *             or {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#RESOLVED} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_TT_COMPLETED
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#RESOLVED
	 */
	@Deprecated
	FIXED,
	/**
	 * @deprecated Use {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_VTEST_IN_PROGRESS} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_VTEST_IN_PROGRESS
	 */
	@Deprecated
	TESTING,
	/**
	 * @deprecated Use {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_VERIFIED}
	 *             and {@link com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_CLOSED} instead.
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_VERIFIED
	 * @see com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus#XTRA_CLOSED
	 */
	@Deprecated
	CLOSED
}
