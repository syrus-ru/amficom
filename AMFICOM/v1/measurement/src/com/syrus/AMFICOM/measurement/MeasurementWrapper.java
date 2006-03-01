/*
 * $Id: MeasurementWrapper.java,v 1.15.2.2 2006/03/01 15:43:15 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.15.2.2 $, $Date: 2006/03/01 15:43:15 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementWrapper extends ActionWrapper<MeasurementResultParameter, Measurement> {
	public static final String COLUMN_TEST_ID = "test_id";

	private static MeasurementWrapper instance;

	private MeasurementWrapper() {
		super(new String[] { COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_ACTION_TEMPLATE_ID,
				COLUMN_NAME,
				COLUMN_START_TIME,
				COLUMN_DURATION,
				COLUMN_STATUS,
				COLUMN_TEST_ID });
	}

	public static MeasurementWrapper getInstance() {
		if (instance == null) {
			instance = new MeasurementWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final Measurement measurement, final String key) {
		final Object value = super.getValue(measurement, key);
		if (value == null && measurement != null) {
			if (key.equals(COLUMN_TEST_ID)) {
				return measurement.getTestId();
			}
		}
		return value;
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TEST_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
