/*
 * $Id: PermissionAttributesWrapper.java,v 1.2 2005/08/05 09:46:31 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/05 09:46:31 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module admin
 */
public class PermissionAttributesWrapper extends StorableObjectWrapper<PermissionAttributes> {

	public static final String	COLUMN_USER_ID			= "user_id";
	public static final String	COLUMN_PERMISSION_MASK	= "permission_mask";
	
	private static PermissionAttributesWrapper instance;

	private List<String> keys;

	private PermissionAttributesWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DOMAIN_ID, 
				COLUMN_USER_ID, 
				COLUMN_PERMISSION_MASK };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PermissionAttributesWrapper getInstance() {
		if (instance == null)
			instance = new PermissionAttributesWrapper();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final PermissionAttributes permissionAttributes, 
	                       final String key) {
		final Object value = super.getValue(permissionAttributes, key);
		if (value == null && permissionAttributes != null) {
			if (key.equals(COLUMN_DOMAIN_ID)) {
				return permissionAttributes.getDomainId();
			}
			if (key.equals(COLUMN_USER_ID)) {
				return permissionAttributes.getUserId();
			}
			if (key.equals(COLUMN_PERMISSION_MASK)) {
				return permissionAttributes.getPermissionMask();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(PermissionAttributes permissionAttributes, 
	                     final String key, 
	                     final Object value) {
		if (permissionAttributes != null) {
			if (key.equals(COLUMN_DOMAIN_ID)) {
				permissionAttributes.setDomainId((Identifier) value);
			} else if (key.equals(COLUMN_USER_ID)) {
				permissionAttributes.setUserId((Identifier) value);
			} else if (key.equals(COLUMN_PERMISSION_MASK)) {
				permissionAttributes.setPermissionMask((Long) value);
			}
			
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, 
	                             final Object objectKey, 
	                             final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DOMAIN_ID)
				|| key.equals(COLUMN_USER_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_PERMISSION_MASK)) {
			return Long.class;
		}
		return null;
	}

}
