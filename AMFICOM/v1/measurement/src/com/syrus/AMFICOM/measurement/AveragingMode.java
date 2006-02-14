/*-
 * $Id: AveragingMode.java,v 1.1.2.1 2006/02/14 01:26:43 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Calendar;
import java.util.Date;

/**
 * Описывает режимы усреднения рефлектограмм на агенте и взаимную зависимость
 * ({@link #getBaseAveragingMode()} этих режимов.
 * @author saa
 * @author $Author: arseniy $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/14 01:26:43 $
 * @module measurement
 */
public enum AveragingMode {
	HOURLY	("hourly",	null,	Calendar.MINUTE, 0),
	DAYLY	("daily",	HOURLY,	Calendar.HOUR_OF_DAY, 0),
	WEEKLY	("weekly",	DAYLY,	Calendar.DAY_OF_WEEK, Calendar.MONDAY),
	MONTHLY	("monthly",	DAYLY,	Calendar.DAY_OF_MONTH, 1),
	YEARLY	("yearly",	MONTHLY,Calendar.MONTH, 0);

	// FIXME: should I implement values() as well? Is the default impl. safe enough?

	private static final Calendar calendar = Calendar.getInstance();

	public AveragingMode getBaseAveragingMode() {
		return this.base;
	}
	public String getCodename() {
		return this.codename;
	}

	protected void shiftToRangeBegin() {
		if (this.base == null) {
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else {
			this.base.shiftToRangeBegin();
		}
		// FIXME: it seems setting of day of week will not give desired result
		calendar.set(this.resetField, this.resetFieldValue);
	}

	public Date getRangeStart(final Date date) {
		synchronized (calendar) {
			calendar.setTime(date);
			shiftToRangeBegin();
			return calendar.getTime();
		}
	}

	private final String codename;
	private final int resetField;
	private final int resetFieldValue;
	private final AveragingMode base;

	private AveragingMode(final String codename, final AveragingMode base,
			final int resetField, final int resetFieldValue) {
		this.codename = codename;
		this.base = base;
		this.resetField = resetField;
		this.resetFieldValue = resetFieldValue;
	}
}
