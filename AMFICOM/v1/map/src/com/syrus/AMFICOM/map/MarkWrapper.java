/*
 * $Id: MarkWrapper.java,v 1.4 2005/02/01 07:25:22 bob Exp $
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

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/01 07:25:22 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MarkWrapper implements Wrapper {

	// name VARCHAR2(128),
	public static final String		COLUMN_NAME					= "name";
	// description VARCHAR2(256),
	public static final String		COLUMN_DESCRIPTION			= "description";
	// longitude NUMBER(12,6),
	public static final String		COLUMN_LONGITUDE			= "longitude";
	// latiude NUMBER(12,6),
	public static final String		COLUMN_LATIUDE				= "latiude";
	// physical_link_id VARCHAR2(32) NOT NULL,
	public static final String		COLUMN_PHYSICAL_LINK_ID		= "physical_link_id";
	// distance NUMBER(12,6),
	public static final String		COLUMN_DISTANCE				= "distance";
	// city VARCHAR2(128),
	public static final String		COLUMN_CITY					= "city";
	// street VARCHAR2(128),
	public static final String		COLUMN_STREET				= "street";
	// building VARCHAR2(128),
	public static final String		COLUMN_BUILDING				= "building";

	public static final String		COLUMN_CHARACTERISTIC_ID	= "collector_id";

	protected static MarkWrapper	instance;

	protected List					keys;

	private MarkWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_LONGITUDE, COLUMN_LATIUDE,
				COLUMN_PHYSICAL_LINK_ID, COLUMN_DISTANCE, COLUMN_CITY, COLUMN_STREET, COLUMN_BUILDING};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static MarkWrapper getInstance() {
		if (instance == null)
			instance = new MarkWrapper();
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
		if (key.equals(COLUMN_CHARACTERISTIC_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof Mark) {
			Mark mark = (Mark) object;
			if (key.equals(COLUMN_NAME))
				return mark.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return mark.getDescription();
			else if (key.equals(COLUMN_LONGITUDE))
				return new Double(mark.getLocation().getX());
			else if (key.equals(COLUMN_LATIUDE))
				return new Double(mark.getLocation().getY());
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				return mark.getPhysicalLink().getId();
			else if (key.equals(COLUMN_DISTANCE))
				return new Double(mark.getDistance());
			else if (key.equals(COLUMN_CITY))
				return mark.getCity();
			else if (key.equals(COLUMN_STREET))
				return mark.getStreet();
			else if (key.equals(COLUMN_BUILDING))
				return mark.getBuilding();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return mark.getCharacteristics();

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
		if (object instanceof Mark) {
			Mark mark = (Mark) object;
			if (key.equals(COLUMN_NAME))
				mark.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				mark.setDescription((String) value);
			else if (key.equals(COLUMN_LONGITUDE))
				mark.setLongitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_LATIUDE))
				mark.setLatitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_PHYSICAL_LINK_ID))
				mark.setPhysicalLink((PhysicalLink) value);
			else if (key.equals(COLUMN_DISTANCE))
				mark.setDistance(((Double) value).doubleValue());
			else if (key.equals(COLUMN_CITY))
				mark.setCity((String) value);
			else if (key.equals(COLUMN_STREET))
				mark.setStreet((String) value);
			else if (key.equals(COLUMN_BUILDING))
				mark.setBuilding((String) value);
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				mark.setCharacteristics((List) value);
		}
	}

}
