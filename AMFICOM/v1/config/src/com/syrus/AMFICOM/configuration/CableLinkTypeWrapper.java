/*
 * $Id: CableLinkTypeWrapper.java,v 1.24 2006/03/15 15:35:12 arseniy Exp $
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
 * @version $Revision: 1.24 $, $Date: 2006/03/15 15:35:12 $
 * @author $Author: arseniy $
 * @module config
 */
public final class CableLinkTypeWrapper extends StorableObjectWrapper<CableLinkType> {

	// codename VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// sort NUMBER(2,0),
	public static final String COLUMN_KIND = "kind";
	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER = "manufacturer";
	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturer_code";
	// image_id VARCHAR2(32),
	public static final String COLUMN_IMAGE_ID = "image_id";

	public static final String LINK_FIELD_CABLE_THREAD_TYPES = "cable_thread_types";

	private static CableLinkTypeWrapper instance;

	private List<String> keys;

	private CableLinkTypeWrapper() {
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

	public static CableLinkTypeWrapper getInstance() {
		if (instance == null)
			instance = new CableLinkTypeWrapper();
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
	public Object getValue(final CableLinkType cableLinkType, final String key) {
		final Object value = super.getValue(cableLinkType, key);
		if (value == null && cableLinkType != null) {
			if (key.equals(COLUMN_CODENAME))
				return cableLinkType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return cableLinkType.getDescription();
			if (key.equals(COLUMN_NAME))
				return cableLinkType.getName();
			if (key.equals(COLUMN_KIND))
				return new Integer(cableLinkType.getSort().value());
			if (key.equals(COLUMN_MANUFACTURER))
				return cableLinkType.getManufacturer();
			if (key.equals(COLUMN_MANUFACTURER_CODE))
				return cableLinkType.getManufacturerCode();
			if (key.equals(COLUMN_IMAGE_ID))
				return cableLinkType.getImageId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CableLinkType cableLinkType, final String key, final Object value) {
		if (cableLinkType != null) {
			if (key.equals(COLUMN_NAME))
				cableLinkType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				cableLinkType.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				cableLinkType.setCodename((String) value);
			else if (key.equals(COLUMN_KIND))
				cableLinkType.setSort(LinkTypeSort.from_int(((Integer)value).intValue()));
			else if (key.equals(COLUMN_MANUFACTURER))
				cableLinkType.setManufacturer((String) value);
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				cableLinkType.setManufacturerCode((String) value);
			else if (key.equals(COLUMN_IMAGE_ID))
				cableLinkType.setImageId((Identifier) value);
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
				|| key.equals(COLUMN_MANUFACTURER)
				|| key.equals(COLUMN_MANUFACTURER_CODE)) {
			return String.class;
		}
		if (key.equals(COLUMN_KIND)) {
			return Integer.class;
		}
		if (key.equals(COLUMN_IMAGE_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
