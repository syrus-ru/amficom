/*-
 * $Id: ActionParameterWrapper.java,v 1.1.2.2 2006/02/16 12:50:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/16 12:50:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterWrapper extends ParameterWrapper<ActionParameter> {
	public static final String COLUMN_BINDING_ID = "binding_id";

	private static ActionParameterWrapper instance;

	private ActionParameterWrapper() {
		super(new String[] { COLUMN_BINDING_ID, COLUMN_VALUE });
	}

	public static ActionParameterWrapper getInstance() {
		if (instance == null) {
			instance = new ActionParameterWrapper();
		}
		return instance;
	}

	@Override
	public Object getValue(final ActionParameter object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_BINDING_ID)) {
				return object.getBindingId();
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
		if (key.equals(COLUMN_BINDING_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
