/*
 * $Id: PhysicalLinkTypeWrapper.java,v 1.4 2005/02/03 08:38:02 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/03 08:38:02 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkTypeWrapper implements StorableObjectWrapper {

	// codename VARCHAR2(32) NOT NULL,
	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// dimension_x NUMBER(12),
	public static final String					COLUMN_DIMENSION_X			= "dimension_x";
	// dimension_y NUMBER(12),
	public static final String					COLUMN_DIMENSION_Y			= "dimension_y";

	protected static PhysicalLinkTypeWrapper	instance;

	protected List								keys;

	private PhysicalLinkTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DIMENSION_X,
				COLUMN_DIMENSION_Y, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static PhysicalLinkTypeWrapper getInstance() {
		if (instance == null)
			instance = new PhysicalLinkTypeWrapper();
		return instance;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof PhysicalLinkType) {
			PhysicalLinkType physicalLinkType = (PhysicalLinkType) object;
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
			else if (key.equals(COLUMN_CHARACTERISTICS))
				return physicalLinkType.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof PhysicalLinkType) {
			PhysicalLinkType physicalLinkType = (PhysicalLinkType) object;
			if (key.equals(COLUMN_CODENAME))
				physicalLinkType.setCodename((String) value);
			else if (key.equals(COLUMN_NAME))
				physicalLinkType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				physicalLinkType.setDescription((String) value);
			else if (key.equals(COLUMN_DIMENSION_X))
				physicalLinkType.setBindingDimension(new IntDimension(((Integer) value).intValue(), physicalLinkType
						.getBindingDimension().getHeight()));
			else if (key.equals(COLUMN_DIMENSION_Y))
				physicalLinkType.setBindingDimension(new IntDimension(
																		physicalLinkType.getBindingDimension()
																				.getWidth(), ((Integer) value)
																				.intValue()));
			else if (key.equals(COLUMN_CHARACTERISTICS))
				physicalLinkType.setCharacteristics((List) value);
		}
	}

}
