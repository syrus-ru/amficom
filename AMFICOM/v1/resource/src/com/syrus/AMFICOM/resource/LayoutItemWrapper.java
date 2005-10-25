/*
 * $Id: LayoutItemWrapper.java,v 1.2 2005/10/25 19:53:14 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:14 $
 * @author $Author: bass $
 * @module general
 */
public final class LayoutItemWrapper extends StorableObjectWrapper<LayoutItem> {

	public static final String COLUMN_PARENT_ID = "parent_id";
	public static final String COLUMN_LAYOUT_NAME = "layout_name";

	private static LayoutItemWrapper instance;

	protected List<String> keys;

	private LayoutItemWrapper() {
		this.keys = Collections.unmodifiableList(
			Arrays.asList(new String[] {COLUMN_PARENT_ID, COLUMN_LAYOUT_NAME, COLUMN_NAME}));

	}

	public static LayoutItemWrapper getInstance() {
		if (instance == null) {
			instance = new LayoutItemWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		if (key.equals(COLUMN_LAYOUT_NAME) ||
				key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_PARENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, 
	                             final Object objectKey, 
	                             final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Object getValue(final LayoutItem layoutItem, final String key) {
		final Object value = super.getValue(layoutItem, key);
		if (value == null && layoutItem != null) {
			if (key.equals(COLUMN_PARENT_ID))
				return layoutItem.getParentId();
			if (key.equals(COLUMN_LAYOUT_NAME))
				return layoutItem.getLayoutName();
			if (key.equals(COLUMN_NAME))
				return layoutItem.getName();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final LayoutItem layoutItem, 
	                     final String key, 
	                     final Object value) {
		if (layoutItem != null) {
			if (key.equals(COLUMN_PARENT_ID))
				layoutItem.setParentId((Identifier) value);
			else if (key.equals(COLUMN_LAYOUT_NAME))
				layoutItem.setLayoutName((String) value);
			else if (key.equals(COLUMN_NAME))
				layoutItem.setName((String) value);		
		}
	}

}
