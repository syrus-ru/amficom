/*
* $Id: PhysicalLinkTypeWrapper.java,v 1.1 2005/01/25 14:58:41 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/25 14:58:41 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkTypeWrapper implements Wrapper {

	//	 codename VARCHAR2(32) NOT NULL,
	public static final String COLUMN_CODENAME      = "codename";
	// name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// dimension_x NUMBER(12),
	public static final String COLUMN_DIMENSION_X   = "dimension_x";
	// dimension_y NUMBER(12),
	public static final String COLUMN_DIMENSION_Y   = "dimension_y";

	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";


	protected static PhysicalLinkTypeWrapper	instance;

	protected List				keys;

	private PhysicalLinkTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_DIMENSION_X,
				COLUMN_DIMENSION_Y, 
				COLUMN_CHARACTERISTIC_ID};

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
		if (key.equals(COLUMN_CHARACTERISTIC_ID))
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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return physicalLinkType.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(physicalLinkType.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(physicalLinkType.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return physicalLinkType.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return physicalLinkType.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_CODENAME))
				return physicalLinkType.getCodename();			
			else if (key.equals(COLUMN_NAME))
				return physicalLinkType.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return physicalLinkType.getDescription();
			else if (key.equals(COLUMN_DIMENSION_X))
				return Integer.toString(physicalLinkType.getBindingDimension().getWidth());
			else if (key.equals(COLUMN_DIMENSION_Y))
				return Integer.toString(physicalLinkType.getBindingDimension().getHeight());
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
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
				physicalLinkType.setCodename0((String)value);			
			else if (key.equals(COLUMN_NAME))
				physicalLinkType.setName0((String)value);
			else if (key.equals(COLUMN_DESCRIPTION))
				physicalLinkType.setDescription0((String)value);
			else if (key.equals(COLUMN_DIMENSION_X))
				physicalLinkType.setBindingDimension0(new IntDimension(Integer.parseInt((String)value), physicalLinkType.getBindingDimension().getHeight()));
			else if (key.equals(COLUMN_DIMENSION_Y))
				physicalLinkType.setBindingDimension0(new IntDimension(physicalLinkType.getBindingDimension().getWidth(), Integer.parseInt((String)value)));
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
					List characteristicIdStr = (List)value;
					List characteristicIds = new ArrayList(characteristicIdStr.size());
					for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
						characteristicIds.add(new Identifier((String) it.next()));
					try {
						physicalLinkType.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
					} catch (DatabaseException e) {
						Log.errorMessage("PhysicalLinkTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("PhysicalLinkTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
		}
	}


}
