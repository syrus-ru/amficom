/*
 * $Id: SiteNodeWrapper.java,v 1.7 2005/04/11 11:51:24 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/11 11:51:24 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeWrapper extends StorableObjectWrapper {

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// longitude NUMBER(12,6),
	public static final String			COLUMN_LONGITUDE			= "longitude";
	// latiude NUMBER(12,6),
	public static final String			COLUMN_LATIUDE				= "latiude";
	// image_id VARCHAR2(32) NOT NULL,
	public static final String			COLUMN_IMAGE_ID				= "image_id";
	// site_node_type_id VARCHAR2(32) NOT NULL,
	public static final String			COLUMN_SITE_NODE_TYPE_ID	= "site_node_type_id";
	// city VARCHAR2(128),
	public static final String			COLUMN_CITY					= "city";
	// street VARCHAR2(128),
	public static final String			COLUMN_STREET				= "street";
	// building VARCHAR2(128),
	public static final String			COLUMN_BUILDING				= "building";

	protected static SiteNodeWrapper	instance;

	protected List						keys;

	private SiteNodeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_LONGITUDE, COLUMN_LATIUDE,
				COLUMN_IMAGE_ID, COLUMN_SITE_NODE_TYPE_ID, COLUMN_CITY, COLUMN_STREET, COLUMN_BUILDING,
				COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static SiteNodeWrapper getInstance() {
		if (instance == null)
			instance = new SiteNodeWrapper();
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
			return Set.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof SiteNode) {
			SiteNode siteNode = (SiteNode) object;
			if (key.equals(COLUMN_NAME))
				return siteNode.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return siteNode.getDescription();
			else if (key.equals(COLUMN_LONGITUDE))
				return new Double(siteNode.getLocation().getX());
			else if (key.equals(COLUMN_LATIUDE))
				return new Double(siteNode.getLocation().getY());
			else if (key.equals(COLUMN_IMAGE_ID))
				return siteNode.getImageId();
			else if (key.equals(COLUMN_SITE_NODE_TYPE_ID))
				return siteNode.getType();
			else if (key.equals(COLUMN_CITY))
				return siteNode.getCity();
			else if (key.equals(COLUMN_STREET))
				return siteNode.getStreet();
			else if (key.equals(COLUMN_BUILDING))
				return siteNode.getBuilding();
			else if (key.equals(COLUMN_CHARACTERISTICS))
				return siteNode.getCharacteristics();
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
		if (object instanceof SiteNode) {
			SiteNode siteNode = (SiteNode) object;
			if (key.equals(COLUMN_NAME))
				siteNode.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				siteNode.setDescription((String) value);
			else if (key.equals(COLUMN_LONGITUDE))
				siteNode.setLongitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_LATIUDE))
				siteNode.setLatitude(((Double) value).doubleValue());
			else if (key.equals(COLUMN_IMAGE_ID))
				siteNode.setImageId((Identifier) value);
			else if (key.equals(COLUMN_SITE_NODE_TYPE_ID))
				siteNode.setType((SiteNodeType) value);
			else if (key.equals(COLUMN_CITY))
				siteNode.setCity((String) value);
			else if (key.equals(COLUMN_STREET))
				siteNode.setStreet((String) value);
			else if (key.equals(COLUMN_BUILDING))
				siteNode.setBuilding((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				siteNode.setCharacteristics((Set) value);
		}
	}
}
