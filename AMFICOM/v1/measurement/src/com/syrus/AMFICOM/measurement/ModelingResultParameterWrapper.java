/*-
 * $Id: ModelingResultParameterWrapper.java,v 1.1.2.1 2006/02/11 18:40:46 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingResultParameterWrapper extends ActionResultParameterWrapper<ModelingResultParameter> {
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_MODELING_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
