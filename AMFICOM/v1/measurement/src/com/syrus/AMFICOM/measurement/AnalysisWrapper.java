/*
 * $Id: AnalysisWrapper.java,v 1.17.2.3 2006/03/02 16:10:42 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.17.2.3 $, $Date: 2006/03/02 16:10:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisWrapper extends ActionWrapper<Analysis, AnalysisResultParameter> {
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";

	private static AnalysisWrapper instance;

	private AnalysisWrapper() {
		super(new String[] { COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_ACTION_TEMPLATE_ID,
				COLUMN_NAME,
				COLUMN_START_TIME,
				COLUMN_DURATION,
				COLUMN_STATUS,
				COLUMN_MEASUREMENT_ID });
	}

	public static AnalysisWrapper getInstance() {
		if (instance == null) {
			instance = new AnalysisWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final Analysis analysis, final String key) {
		final Object value = super.getValue(analysis, key);
		if (value == null && analysis != null) {
			if (key.equals(COLUMN_MEASUREMENT_ID)) {
				return analysis.getMeasurementId();
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
		if (key.equals(COLUMN_MEASUREMENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
