/*-
 * $Id: AbstractReflectogramMismatchEventWrapper.java,v 1.1.4.1 2006/03/21 13:46:43 arseniy Exp $
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
 * for general purpose {@link ReflectogramMismatchEventWrapper} should be used.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.1.4.1 $, $Date: 2006/03/21 13:46:43 $
 * @module event
 */
final class AbstractReflectogramMismatchEventWrapper
		extends StorableObjectWrapper<AbstractReflectogramMismatchEvent> {
	private static AbstractReflectogramMismatchEventWrapper instance;

	private static final ReflectogramMismatchEventWrapper INSTANCE = ReflectogramMismatchEventWrapper.getInstance();

	private AbstractReflectogramMismatchEventWrapper() {
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
	 * @param reflectogramMismatchEvent
	 * @param key
	 * @see com.syrus.AMFICOM.general.StorableObjectWrapper#getValue(com.syrus.AMFICOM.general.StorableObject, String)
	 */
	@Override
	public Object getValue(
			final AbstractReflectogramMismatchEvent reflectogramMismatchEvent,
			final String key) {
		final Object value = super.getValue(reflectogramMismatchEvent, key);
		return value == null
				? INSTANCE.getValue(reflectogramMismatchEvent, key)
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
	 * @param reflectogramMismatchEvent
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see com.syrus.AMFICOM.general.StorableObjectWrapper#setValue(com.syrus.AMFICOM.general.StorableObject, String, Object)
	 */
	@Override
	public void setValue(final AbstractReflectogramMismatchEvent reflectogramMismatchEvent,
			final String key, final Object value)
	throws PropertyChangeException {
		INSTANCE.setValue(reflectogramMismatchEvent, key, value);
	}

	public static AbstractReflectogramMismatchEventWrapper getInstance() {
		return instance == null
				? instance = new AbstractReflectogramMismatchEventWrapper()
				: instance;
	}
}
