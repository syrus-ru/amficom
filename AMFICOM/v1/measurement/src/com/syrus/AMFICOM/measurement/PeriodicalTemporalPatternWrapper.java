/*-
 * $Id: PeriodicalTemporalPatternWrapper.java,v 1.4 2005/07/25 20:50:00 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/07/25 20:50:00 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class PeriodicalTemporalPatternWrapper extends StorableObjectWrapper {

	public static final String COLUMN_PERIOD = "PERIOD";

	private static PeriodicalTemporalPatternWrapper instance;

	private List<String> keys;

	private PeriodicalTemporalPatternWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] {COLUMN_PERIOD};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PeriodicalTemporalPatternWrapper getInstance() {
		if (instance == null)
			instance = new PeriodicalTemporalPatternWrapper();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof PeriodicalTemporalPattern) {
			final PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) object;
			if (key.equals(COLUMN_PERIOD))
				return new Long(periodicalTemporalPattern.getPeriod());
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof PeriodicalTemporalPattern) {
			final PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) object;
			if (key.equals(COLUMN_PERIOD))
				periodicalTemporalPattern.setPeriod(((Long) value).longValue());			
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_PERIOD))
			return Long.class;
		return null;
	}

}
