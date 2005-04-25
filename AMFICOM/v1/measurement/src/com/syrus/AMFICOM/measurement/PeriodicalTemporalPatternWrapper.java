/*-
 * $Id: PeriodicalTemporalPatternWrapper.java,v 1.1 2005/04/25 08:19:54 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/25 08:19:54 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class PeriodicalTemporalPatternWrapper extends StorableObjectWrapper {

	public static final String COLUMN_PERIOD = "PERIOD";

	private static PeriodicalTemporalPatternWrapper	instance;

	private List						keys;

	private PeriodicalTemporalPatternWrapper() {
		// empty private constructor
		String[] keysArray = new String[] {COLUMN_PERIOD};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PeriodicalTemporalPatternWrapper getInstance() {
		if (instance == null)
			instance = new PeriodicalTemporalPatternWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof AnalysisType) {
			PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) object;
			if (key.equals(COLUMN_PERIOD))
				return new Long(periodicalTemporalPattern.getPeriod());
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof AnalysisType) {
			PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) object;
			if (key.equals(COLUMN_PERIOD))
				periodicalTemporalPattern.setPeriod(((Long) value).longValue());			
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_PERIOD))
			return Long.class;
		return String.class;
	}

}
