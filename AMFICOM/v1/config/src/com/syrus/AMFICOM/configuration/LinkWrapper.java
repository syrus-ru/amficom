/*
 * $Id: LinkWrapper.java,v 1.1 2005/01/26 13:18:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/26 13:18:49 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class LinkWrapper implements Wrapper {

	public static final String	COLUMN_NAME				= "name";
	public static final String	COLUMN_TYPE_ID			= "type_id";
	public static final String	COLUMN_SORT				= "sort";
	public static final String	COLUMN_SUPPLIER			= "supplier";
	public static final String	COLUMN_SUPPLIER_CODE	= "supplier_code";
	public static final String	COLUMN_COLOR			= "color";
	public static final String	COLUMN_MARK				= "mark";
	public static final String	COLUMN_CHARACTERISTICS	= "characteristics";

	private static LinkWrapper	instance;

	private List				keys;

	private LinkWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, StorableObjectType.COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_TYPE_ID, COLUMN_SORT, COLUMN_SUPPLIER, COLUMN_SUPPLIER_CODE, COLUMN_COLOR,
				COLUMN_CHARACTERISTICS};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return link.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return link.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return link.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return link.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return link.getModifierId().getIdentifierString();
			if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				return link.getDescription();
			if (key.equals(COLUMN_NAME))
				return link.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return link.getType().getId().getIdentifierString();
			if (key.equals(COLUMN_SORT))
				return Integer.toString(link.getSort().value());
			if (key.equals(COLUMN_SUPPLIER))
				return link.getSupplier();
			if (key.equals(COLUMN_SUPPLIER_CODE))
				return link.getSupplierCode();
			if (key.equals(COLUMN_COLOR))
				return Integer.toString(link.getColor());
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
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				link.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) {
				try {
					link.setType((LinkType) ConfigurationStorableObjectPool.getStorableObject(new Identifier((String) value),
						true));
				} catch (ApplicationException e) {
					Log.errorMessage("LinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_SORT))
				link.setSort(LinkSort.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_SUPPLIER))
				link.setSupplier((String) value);
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				link.setSupplierCode((String) value);
			else if (key.equals(COLUMN_COLOR))
				link.setColor(Integer.parseInt((String) value));
			else if (key.equals(COLUMN_MARK))
				link.setMark((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List charIdStr = (List) value;
				List characteristicIds = new ArrayList(charIdStr.size());
				for (Iterator it = charIdStr.iterator(); it.hasNext();)
					characteristicIds.add(new Identifier((String) it.next()));
				try {
					link.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("LinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			}
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
