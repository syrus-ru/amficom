/*
 * $Id: PortWrapper.java,v 1.2 2005/01/26 15:09:22 bob Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/26 15:09:22 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class PortWrapper implements Wrapper {
	// type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TYPE_ID       = "type_id";

	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";

	// equipment_id VARCHAR2(32),
	public static final String COLUMN_EQUIPMENT_ID  = "equipment_id";

	// sort NUMBER(2) NOT NULL,
	public static final String COLUMN_SORT  = "sort";	

	public static final String	COLUMN_CHARACTERISTICS	= "characteristics";

	private static PortWrapper	instance;

	private List				keys;

	private PortWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_DESCRIPTION, COLUMN_TYPE_ID,
				COLUMN_SORT, COLUMN_EQUIPMENT_ID, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static PortWrapper getInstance() {
		if (instance == null)
			instance = new PortWrapper();
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
		if (object instanceof Port) {
			Port port = (Port) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return port.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return port.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return port.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return port.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return port.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_DESCRIPTION))
				return port.getDescription();
			if (key.equals(COLUMN_TYPE_ID))
				return port.getType().getId().getIdentifierString();
			if (key.equals(COLUMN_SORT))
				return Integer.toString(port.getSort());
			if (key.equals(COLUMN_EQUIPMENT_ID))
				return port.getEquipmentId().getIdentifierString();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return port.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Port) {
			Port port = (Port) object;
			if (key.equals(COLUMN_DESCRIPTION))
				port.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) {
				try {
					port.setType((PortType) ConfigurationStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("PortWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_EQUIPMENT_ID))
				port.setEquipmentId(new Identifier((String) value));
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List charIdStr = (List) value;
				List characteristicIds = new ArrayList(charIdStr.size());
				for (Iterator it = charIdStr.iterator(); it.hasNext();)
					characteristicIds.add(new Identifier((String) it.next()));
				try {
					port.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("PortWrapper.setValue | key '" + key + "' caught " + e.getMessage());
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
