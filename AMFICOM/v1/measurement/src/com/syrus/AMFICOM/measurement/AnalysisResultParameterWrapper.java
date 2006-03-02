/*-
 * $Id: AnalysisResultParameterWrapper.java,v 1.1.2.2 2006/03/02 16:11:34 arseniy Exp $
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
public final class AnalysisResultParameterWrapper extends ActionResultParameterWrapper<AnalysisResultParameter, Analysis> {
	public static final String COLUMN_ANALYSIS_ID = "analysis_id";

	private static AnalysisResultParameterWrapper instance;

	private AnalysisResultParameterWrapper() {
		super(new String[] { COLUMN_TYPE_ID, COLUMN_ANALYSIS_ID, COLUMN_VALUE });
	}

	public static AnalysisResultParameterWrapper getInstance() {
		if (instance == null) {
			instance = new AnalysisResultParameterWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final AnalysisResultParameter object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_ANALYSIS_ID)) {
				return object.getAnalysisId();
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
		if (key.equals(COLUMN_ANALYSIS_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
