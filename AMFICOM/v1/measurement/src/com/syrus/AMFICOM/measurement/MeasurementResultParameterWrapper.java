/*-
 * $Id: MeasurementResultParameterWrapper.java,v 1.1.2.2 2006/03/02 16:11:34 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/02 16:11:34 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementResultParameterWrapper extends ActionResultParameterWrapper<MeasurementResultParameter, Measurement> {
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";

	private static MeasurementResultParameterWrapper instance;

	private MeasurementResultParameterWrapper() {
		super(new String[] { COLUMN_TYPE_ID, COLUMN_MEASUREMENT_ID, COLUMN_VALUE });
	}

	public static MeasurementResultParameterWrapper getInstance() {
		if (instance == null) {
			instance = new MeasurementResultParameterWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final MeasurementResultParameter object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_MEASUREMENT_ID)) {
				return object.getMeasurementId();
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
