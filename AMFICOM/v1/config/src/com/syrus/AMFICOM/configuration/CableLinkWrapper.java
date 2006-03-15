/*-
 * $Id: CableLinkWrapper.java,v 1.4.2.1 2006/03/15 13:53:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author max
 * @author $Author: arseniy $
 * @version $Revision: 1.4.2.1 $, $Date: 2006/03/15 13:53:17 $
 * @module config
 */

public final class CableLinkWrapper extends StorableObjectWrapper<CableLink> {
	
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// inventory_no VARCHAR2(64),
	public static final String COLUMN_INVENTORY_NO = "inventory_no";

	// supplier VARCHAR2(64),
	public static final String COLUMN_SUPPLIER = "supplier";

	// supplier_code VARCHAR2(64),
	public static final String COLUMN_SUPPLIER_CODE = "supplier_code";

	// color NUMBER(38),
	public static final String COLUMN_COLOR = "color";

	// mark VARCHAR(32),
	public static final String COLUMN_MARK = "mark";

	private static CableLinkWrapper instance;

	private List<String> keys;
	
	private CableLinkWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_TYPE_ID,
				COLUMN_SUPPLIER,
				COLUMN_SUPPLIER_CODE,
				COLUMN_COLOR };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}
	
	public static CableLinkWrapper getInstance() {
		if (instance == null)
			instance = new CableLinkWrapper();
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
	public Object getValue(final CableLink cableLink, final String key) {
		final Object value = super.getValue(cableLink, key);
		if (value == null && cableLink != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				return cableLink.getDescription();
			if (key.equals(COLUMN_NAME))
				return cableLink.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return cableLink.getType();
			if (key.equals(COLUMN_SUPPLIER))
				return cableLink.getSupplier();
			if (key.equals(COLUMN_SUPPLIER_CODE))
				return cableLink.getSupplierCode();
			if (key.equals(COLUMN_COLOR))
				return new Integer(cableLink.getColor());
			if (key.equals(COLUMN_MARK))
				return cableLink.getMark();
		}
		return value;
	}
	
	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CableLink cableLink, final String key, final Object value) {
		if (cableLink != null) {
			if (key.equals(COLUMN_NAME))
				cableLink.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				cableLink.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				cableLink.setType((CableLinkType) value);
			else if (key.equals(COLUMN_SUPPLIER))
				cableLink.setSupplier((String) value);
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				cableLink.setSupplierCode((String) value);
			else if (key.equals(COLUMN_COLOR))
				cableLink.setColor(((Integer) value).intValue());
			else if (key.equals(COLUMN_MARK))
				cableLink.setMark((String) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}
	
	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
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
			return CableLinkType.class;
		}
		if (key.equals(COLUMN_COLOR)) {
			return Integer.class;
		}
		return null;
	}
}
