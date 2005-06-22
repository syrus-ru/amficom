/*
 * $Id: LinkWrapper.java,v 1.10 2005/06/22 15:05:18 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/22 15:05:18 $
 * @author $Author: bass $
 * @module configuration_v1
 */
public final class LinkWrapper extends StorableObjectWrapper {
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// inventory_no VARCHAR2(64),
	public static final String	COLUMN_INVENTORY_NO		= "inventory_no";

	// supplier VARCHAR2(64),
	public static final String	COLUMN_SUPPLIER			= "supplier";

	// supplier_code VARCHAR2(64),
	public static final String	COLUMN_SUPPLIER_CODE	= "supplier_code";

	// color NUMBER(38),
	public static final String	COLUMN_COLOR			= "color";

	// mark VARCHAR(32),
	public static final String	COLUMN_MARK				= "mark";

	private static LinkWrapper	instance;

	private List				keys;

	private LinkWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID,
				COLUMN_SUPPLIER, COLUMN_SUPPLIER_CODE, COLUMN_COLOR, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static LinkWrapper getInstance() {
		if (instance == null)
			instance = new LinkWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof Link) {
			Link link = (Link) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return link.getDescription();
			if (key.equals(COLUMN_NAME))
				return link.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return link.getType();
			if (key.equals(COLUMN_SUPPLIER))
				return link.getSupplier();
			if (key.equals(COLUMN_SUPPLIER_CODE))
				return link.getSupplierCode();
			if (key.equals(COLUMN_COLOR))
				return new Integer(link.getColor());
			if (key.equals(COLUMN_MARK))
				return link.getMark();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return link.getCharacteristics();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Link) {
			Link link = (Link) object;
			if (key.equals(COLUMN_NAME))
				link.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				link.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				link.setType((LinkType) value);
			else if (key.equals(COLUMN_SUPPLIER))
				link.setSupplier((String) value);
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				link.setSupplierCode((String) value);
			else if (key.equals(COLUMN_COLOR))
				link.setColor(((Integer) value).intValue());
			else if (key.equals(COLUMN_MARK))
				link.setMark((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				link.setCharacteristics((Set) value);
		}
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
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_SUPPLIER)
				|| key.equals(COLUMN_SUPPLIER_CODE)
				|| key.equals(COLUMN_MARK)) {
			return String.class;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return LinkType.class;
		}
		if (key.equals(COLUMN_COLOR)) {
			return Integer.class;
		}
		if (key.equals(COLUMN_CHARACTERISTICS)) {
			return Set.class;
		}
		return null;
	}
}
