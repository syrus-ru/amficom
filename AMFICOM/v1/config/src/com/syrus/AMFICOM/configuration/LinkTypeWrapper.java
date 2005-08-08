/*
 * $Id: LinkTypeWrapper.java,v 1.16 2005/08/08 13:24:41 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.16 $, $Date: 2005/08/08 13:24:41 $
 * @author $Author: arseniy $
 * @module config
 */
public final class LinkTypeWrapper extends StorableObjectWrapper<LinkType> {

	// codename VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// sort NUMBER(2,0),
	public static final String COLUMN_NATURE = "nature";

	public static final String COLUMN_KIND = "kind";

	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER = "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturer_code";

	// image_id VARCHAR2(32),
	public static final String COLUMN_IMAGE_ID = "image_id";

	private static LinkTypeWrapper instance;

	private List<String> keys;

	private LinkTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_KIND,
				COLUMN_MANUFACTURER,
				COLUMN_MANUFACTURER_CODE,
				COLUMN_IMAGE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static LinkTypeWrapper getInstance() {
		if (instance == null)
			instance = new LinkTypeWrapper();
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
	public Object getValue(final LinkType linkType, final String key) {
		final Object value = super.getValue(linkType, key);
		if (value == null && linkType != null) {
			if (key.equals(COLUMN_CODENAME))
				return linkType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return linkType.getDescription();
			if (key.equals(COLUMN_NAME))
				return linkType.getName();
			if (key.equals(COLUMN_KIND))
				return new Integer(linkType.getSort().value());
			if (key.equals(COLUMN_MANUFACTURER))
				return linkType.getManufacturer();
			if (key.equals(COLUMN_MANUFACTURER_CODE))
				return linkType.getManufacturerCode();
			if (key.equals(COLUMN_IMAGE_ID))
				return linkType.getImageId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final LinkType linkType, final String key, final Object value) {
		if (linkType != null) {
			if (key.equals(COLUMN_NAME))
				linkType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				linkType.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				linkType.setCodename((String) value);
			else if (key.equals(COLUMN_KIND))
				linkType.setSort(LinkTypeSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_MANUFACTURER))
				linkType.setManufacturer((String) value);
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				linkType.setManufacturerCode((String) value);
			else if (key.equals(COLUMN_IMAGE_ID))
				linkType.setImageId((Identifier) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME) 
				|| key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_MANUFACTURER)
				|| key.equals(COLUMN_MANUFACTURER_CODE)) {
			return String.class;
		} else if (key.equals(COLUMN_KIND)) {
			return Integer.class;			
		} else if (key.equals(COLUMN_IMAGE_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
