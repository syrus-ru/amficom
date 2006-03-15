/*-
 * $Id: ActionResultParameterWrapper.java,v 1.1.2.4 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameterWrapper<R extends ActionResultParameter<A>, A extends Action<R>> extends ParameterWrapper<R> {

	ActionResultParameterWrapper(final String[] keysArray) {
		super(keysArray);
	}

	@Override
	public Object getValue(final R object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_TYPE_ID)) {
				return object.getTypeId();
			}
		}
		return value;
	}

	@Override
	public final void setValue(final R storableObject, final String key, final Object value) throws PropertyChangeException {
		//Nothing to set
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
