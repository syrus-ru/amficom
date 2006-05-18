/*
 * $Id: SiteNodeWrapper.java,v 1.16 2006/03/13 15:54:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.16 $, $Date: 2006/03/13 15:54:26 $
 * @author $Author: bass $
 * @module map
 */
public final class SiteNodeWrapper extends StorableObjectWrapper<SiteNode> {

	// name VARCHAR2(128),
	// description VARCHAR2(256),
	// longitude NUMBER(12,6),
	public static final String COLUMN_LONGITUDE = "longitude";
	// latiude NUMBER(12,6),
	public static final String COLUMN_LATIUDE = "latiude";
	// image_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_IMAGE_ID = "image_id";
	// site_node_type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_SITE_NODE_TYPE_ID = "site_node_type_id";
	// city VARCHAR2(128),
	public static final String COLUMN_CITY = "city";
	// street VARCHAR2(128),
	public static final String COLUMN_STREET = "street";
	// building VARCHAR2(128),
	public static final String COLUMN_BUILDING = "building";
	
	public static final String COLUMN_ATTACHMENT_SITE_NODE_ID	= "attachment_site_node_id";

	private static SiteNodeWrapper instance;

	private List<String> keys;

	private SiteNodeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_LONGITUDE,
				COLUMN_LATIUDE,
				COLUMN_IMAGE_ID,
				COLUMN_SITE_NODE_TYPE_ID,
				COLUMN_CITY,
				COLUMN_STREET,
				COLUMN_BUILDING,
				COLUMN_ATTACHMENT_SITE_NODE_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static SiteNodeWrapper getInstance() {
		if (instance == null)
			instance = new SiteNodeWrapper();
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
	public Class<?> getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final SiteNode siteNode, final String key) {
		if (siteNode != null) {
			if (key.equals(COLUMN_NAME)) {
				return siteNode.getName();
			}
			else if (key.equals(COLUMN_DESCRIPTION)) {
				return siteNode.getDescription();
			}
			else if (key.equals(COLUMN_LONGITUDE)) {
				return new Double(siteNode.getLocation().getX());
			}
			else if (key.equals(COLUMN_LATIUDE)) {
				return new Double(siteNode.getLocation().getY());
			}
			else if (key.equals(COLUMN_IMAGE_ID)) {
				return siteNode.getImageId();
			}
			else if (key.equals(COLUMN_SITE_NODE_TYPE_ID)) {
				return siteNode.getType();
			}
			else if (key.equals(COLUMN_CITY)) {
				return siteNode.getCity();
			}
			else if (key.equals(COLUMN_STREET)) {
				return siteNode.getStreet();
			}
			else if (key.equals(COLUMN_BUILDING)) {
				return siteNode.getBuilding();
			}
			else if (key.equals(COLUMN_ATTACHMENT_SITE_NODE_ID)) {
				return siteNode.getAttachmentSiteNodeId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public void setValue(final SiteNode siteNode, final String key, final Object value) {
		if (siteNode != null) {
			if (key.equals(COLUMN_NAME)) {
				siteNode.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				siteNode.setDescription((String) value);
			} else if (key.equals(COLUMN_LONGITUDE)) {
				siteNode.setLongitude(((Double) value).doubleValue());
			} else if (key.equals(COLUMN_LATIUDE)) {
				siteNode.setLatitude(((Double) value).doubleValue());
			} else if (key.equals(COLUMN_IMAGE_ID)) {
				siteNode.setImageId((Identifier) value);
			} else if (key.equals(COLUMN_SITE_NODE_TYPE_ID)) {
				siteNode.setType((SiteNodeType) value);
			} else if (key.equals(COLUMN_CITY)) {
				siteNode.setCity((String) value);
			} else if (key.equals(COLUMN_STREET)) {
				siteNode.setStreet((String) value);
			} else if (key.equals(COLUMN_BUILDING)) {
				siteNode.setBuilding((String) value);
			} else if (key.equals(COLUMN_ATTACHMENT_SITE_NODE_ID)) {
				siteNode.setAttachmentSiteNodeId((Identifier) value);
			}
		}
	}
}
