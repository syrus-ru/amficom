/*
 * $Id: LinkWrapper.java,v 1.4 2005/02/01 06:15:29 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/01 06:15:29 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class LinkWrapper implements Wrapper {

	// table :: Link
	public static final String	COLUMN_TYPE_ID			= "type_id";

	// sort NUMBER(2,0),
	public static final String	COLUMN_SORT				= "sort";

	// name VARCHAR2(64) NOT NULL,
	public static final String	COLUMN_NAME				= "name";

	// description VARCHAR2(256),
	public static final String	COLUMN_DESCRIPTION		= "description";

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

	public static final String	COLUMN_CHARACTERISTICS	= "characteristics";

	private static LinkWrapper	instance;

	private List				keys;

	private LinkWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID, COLUMN_SORT,
				COLUMN_SUPPLIER, COLUMN_SUPPLIER_CODE, COLUMN_COLOR, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
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

	public Object getValue(final Object object, final String key) {
		if (object instanceof Link) {
			Link link = (Link) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return link.getDescription();
			if (key.equals(COLUMN_NAME))
				return link.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return link.getType();
			if (key.equals(COLUMN_SORT))
				return new Integer(link.getSort().value());
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
		return null;
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
			else if (key.equals(COLUMN_SORT))
				link.setSort(LinkSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_SUPPLIER))
				link.setSupplier((String) value);
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				link.setSupplierCode((String) value);
			else if (key.equals(COLUMN_COLOR))
				link.setColor(((Integer) value).intValue());
			else if (key.equals(COLUMN_MARK))
				link.setMark((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				link.setCharacteristics((List) value);
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}
}
