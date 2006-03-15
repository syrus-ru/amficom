/*-
 * $Id: ModelingResultParameterWrapper.java,v 1.1.2.3 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingResultParameterWrapper extends ActionResultParameterWrapper<ModelingResultParameter, Modeling> {
	public static final String COLUMN_MODELING_ID = "modeling_id";

	private static ModelingResultParameterWrapper instance;

	private ModelingResultParameterWrapper() {
		super(new String[] { COLUMN_TYPE_ID, COLUMN_MODELING_ID, COLUMN_VALUE });
	}

	public static ModelingResultParameterWrapper getInstance() {
		if (instance == null) {
			instance = new ModelingResultParameterWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final ModelingResultParameter object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_MODELING_ID)) {
				return object.getModelingId();
			}
		}
		return value;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_MODELING_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
