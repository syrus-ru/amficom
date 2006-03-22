/*-
 * $Id: AbstractLineMismatchEventWrapper.java,v 1.1.2.1 2006/03/20 13:26:14 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * This class was intentionally made non-public: it&apos;s only necessary for
 * {@link com.syrus.AMFICOM.general.TypicalCondition} to work properly;
 * for general purpose {@link LineMismatchEventWrapper} should be used.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/20 13:26:14 $
 * @module event
 */
final class AbstractLineMismatchEventWrapper
		extends StorableObjectWrapper<AbstractLineMismatchEvent> {
	private static AbstractLineMismatchEventWrapper instance;

	private static final LineMismatchEventWrapper INSTANCE = LineMismatchEventWrapper.getInstance();

	private AbstractLineMismatchEventWrapper() {
		// empty
	}

	/**
	 * @see com.syrus.util.Wrapper#getKeys()
	 */
	public List<String> getKeys() {
		return INSTANCE.getKeys();
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getName(String)
	 */
	public String getName(final String key) {
		return INSTANCE.getName(key);
	}

	/**
	 * @param key
	 * @see com.syrus.AMFICOM.general.StorableObjectWrapper#getPropertyClass(String)
	 */
	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		return clazz == null
				? INSTANCE.getPropertyClass(key)
				: clazz;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyValue(String)
	 */
	public Object getPropertyValue(final String key) {
		return INSTANCE.getPropertyValue(key);
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see com.syrus.util.Wrapper#setPropertyValue(String, Object, Object)
	 */
	public void setPropertyValue(final String key, final Object objectKey,
			final Object objectValue) {
		INSTANCE.setPropertyValue(key, objectKey, objectValue);
	}

	/**
	 * @param lineMismatchEvent
	 * @param key
	 * @see com.syrus.AMFICOM.general.StorableObjectWrapper#getValue(com.syrus.AMFICOM.general.StorableObject, String)
	 */
	@Override
	public Object getValue(final AbstractLineMismatchEvent lineMismatchEvent,
			final String key) {
		final Object value = super.getValue(lineMismatchEvent, key);
		return value == null
				? INSTANCE.getValue(lineMismatchEvent, key)
				: value;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(String)
	 */
	public boolean isEditable(final String key) {
		return INSTANCE.isEditable(key);
	}

	/**
	 * @param lineMismatchEvent
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see com.syrus.AMFICOM.general.StorableObjectWrapper#setValue(com.syrus.AMFICOM.general.StorableObject, String, Object)
	 */
	@Override
	public void setValue(final AbstractLineMismatchEvent lineMismatchEvent,
			final String key, final Object value)
	throws PropertyChangeException {
		INSTANCE.setValue(lineMismatchEvent, key, value);
	}

	public static AbstractLineMismatchEventWrapper getInstance() {
		return instance == null
				? instance = new AbstractLineMismatchEventWrapper()
				: instance;
	}
}
