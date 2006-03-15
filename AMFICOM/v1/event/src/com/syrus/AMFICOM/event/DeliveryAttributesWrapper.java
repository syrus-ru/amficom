/*-
 * $Id: DeliveryAttributesWrapper.java,v 1.9 2006/03/15 16:45:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2006/03/15 16:45:56 $
 * @module event
 */
public final class DeliveryAttributesWrapper
		extends StorableObjectWrapper<DeliveryAttributes> {
	public static final String COLUMN_SEVERITY = "severity";
	
	public static final String LINKED_COLUMN_SYSTEM_USER_IDS = "system_user_ids";
	public static final String LINKED_COLUMN_ROLE_IDS = "role_ids";

	public static final String LINKED_COLUMN_SYSTEM_USER_ID = "system_user_id";
	public static final String LINKED_COLUMN_ROLE_ID = "role_id";
	public static final String LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID = "delivery_attributes_id";

	private static DeliveryAttributesWrapper instance;

	private final List<String> keys;

	private DeliveryAttributesWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_SEVERITY,
				LINKED_COLUMN_SYSTEM_USER_IDS,
				LINKED_COLUMN_ROLE_IDS}));
	}

	/**
	 * @see com.syrus.util.Wrapper#getKeys()
	 */
	public List<String> getKeys() {
		return this.keys;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getName(String)
	 */
	public String getName(final String key) {
		return key;
	}

	/**
	 * @param key
	 * @see StorableObjectWrapper#getPropertyClass(String)
	 */
	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_SEVERITY)) {
			return Enum.class;
		}
		
		if (key.equals(LINKED_COLUMN_ROLE_IDS)
				|| key.equals(LINKED_COLUMN_SYSTEM_USER_IDS)) {
			return Set.class;
		}
		return null;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyValue(String)
	 */
	public Object getPropertyValue(final String key) {
		return null;
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see com.syrus.util.Wrapper#setPropertyValue(String, Object, Object)
	 */
	public void setPropertyValue(final String key, final Object objectKey,
			final Object objectValue) {
		// empty
	}

	/**
	 * @param deliveryAttributes
	 * @param key
	 * @see com.syrus.util.Wrapper#getValue(Object, String)
	 * @see StorableObjectWrapper#getValue(com.syrus.AMFICOM.general.StorableObject, String)
	 */
	@Override
	public Object getValue(final DeliveryAttributes deliveryAttributes,
			final String key) {
		final Object value = super.getValue(deliveryAttributes, key);
		if (value != null) {
			return value;
		}
		if (deliveryAttributes != null) {
			if (key.equals(COLUMN_SEVERITY)) {
				return deliveryAttributes.getSeverity();
			} else if (key.equals(LINKED_COLUMN_SYSTEM_USER_IDS)) {
				return deliveryAttributes.getSystemUserIds();
			} else if (key.equals(LINKED_COLUMN_ROLE_IDS)) {
				return deliveryAttributes.getRoleIds();
			}
		}
		return null;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(String)
	 */
	public boolean isEditable(final String key) {
		return false;
	}

	/**
	 * @param deliveryAttributes
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see StorableObjectWrapper#setValue(com.syrus.AMFICOM.general.StorableObject, String, Object)
	 */
	@Override
	public void setValue(final DeliveryAttributes deliveryAttributes,
			final String key,
			final Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();
	}

	public static DeliveryAttributesWrapper getInstance() {
		return instance == null
				? instance = new DeliveryAttributesWrapper()
				: instance;
	}
}
