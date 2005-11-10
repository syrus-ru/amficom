/*-
 * $Id: ReflectogramMismatchSample.java,v 1.1 2005/11/10 16:20:38 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;

/**
 * ReflectogramMismatch sample generator for Bass
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/11/10 16:20:38 $
 * @module
 */
public class ReflectogramMismatchSample {
	/**
	 * line break (parameters are guessed)
	 */
	public static ReflectogramMismatchImpl createMismatchNo1ForBass() {
		ReflectogramMismatchImpl rm = new ReflectogramMismatchImpl();
		rm.setAlarmType(ReflectogramMismatch.AlarmType.TYPE_LINEBREAK);
		rm.setSeverity(ReflectogramMismatch.Severity.SEVERITY_HARD);
		rm.setCoord(6000); // 24 km
		rm.setEndCoord(25000); // 100 km
		rm.setDeltaX(4.0);
		return rm;
	}
	/**
	 * out of mask (copy-pased from *real* mismatch)
	 */
	public static ReflectogramMismatchImpl createMismatchNo2ForBass() {
		ReflectogramMismatchImpl rm = new ReflectogramMismatchImpl();
		rm.setAlarmType(ReflectogramMismatch.AlarmType.TYPE_OUTOFMASK);
		rm.setSeverity(ReflectogramMismatch.Severity.SEVERITY_SOFT);
		rm.setCoord(12519); // 50.076 km
		rm.setEndCoord(12640);
		rm.setDeltaX(4.00003);
		rm.setMismatch(0.8, 0.9);
		return rm;
	}
}
