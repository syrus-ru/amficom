/*
 * $Id: PhysicalLinkTypeWrapper.java,v 1.8 2005/07/26 11:39:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/26 11:39:26 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class PhysicalLinkTypeWrapper extends StorableObjectWrapper {

	// codename VARCHAR2(32) NOT NULL,
	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// dimension_x NUMBER(12),
	public static final String COLUMN_DIMENSION_X = "dimension_x";
	// dimension_y NUMBER(12),
	public static final String COLUMN_DIMENSION_Y = "dimension_y";

	protected static PhysicalLinkTypeWrapper instance;

	protected List<String> keys;

	private PhysicalLinkTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME,
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DIMENSION_X,
				COLUMN_DIMENSION_Y };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static PhysicalLinkTypeWrapper getInstance() {
		if (instance == null)
			instance = new PhysicalLinkTypeWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		if (object instanceof PhysicalLinkType) {
			final PhysicalLinkType physicalLinkType = (PhysicalLinkType) object;
			if (key.equals(COLUMN_CODENAME))
				return physicalLinkType.getCodename();
			else if (key.equals(COLUMN_NAME))
				return physicalLinkType.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return physicalLinkType.getDescription();
			else if (key.equals(COLUMN_DIMENSION_X))
				return new Integer(physicalLinkType.getBindingDimension().getWidth());
			else if (key.equals(COLUMN_DIMENSION_Y))
				return new Integer(physicalLinkType.getBindingDimension().getHeight());
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof PhysicalLinkType) {
			final PhysicalLinkType physicalLinkType = (PhysicalLinkType) object;
			if (key.equals(COLUMN_CODENAME))
				physicalLinkType.setCodename((String) value);
			else if (key.equals(COLUMN_NAME))
				physicalLinkType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				physicalLinkType.setDescription((String) value);
			else if (key.equals(COLUMN_DIMENSION_X))
				physicalLinkType.setBindingDimension(new IntDimension(((Integer) value).intValue(),
						physicalLinkType.getBindingDimension().getHeight()));
			else if (key.equals(COLUMN_DIMENSION_Y))
				physicalLinkType.setBindingDimension(new IntDimension(physicalLinkType.getBindingDimension().getWidth(),
						((Integer) value).intValue()));
		}
	}

}
