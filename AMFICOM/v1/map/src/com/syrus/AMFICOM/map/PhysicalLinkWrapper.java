/*
* $Id: PhysicalLinkWrapper.java,v 1.2 2005/01/27 06:23:59 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/01/27 06:23:59 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkWrapper implements Wrapper {

	//	 name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// physical_link_type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_PHYSICAL_LINK_TYPE_ID = "physical_link_type_id";
	// city VARCHAR2(128),
	public static final String COLUMN_CITY  = "city";
	// street VARCHAR2(128),
	public static final String COLUMN_STREET        = "street";
	// building VARCHAR2(128),
	public static final String COLUMN_BUILDING      = "building";
	// dimension_x NUMBER(12),
	public static final String COLUMN_DIMENSION_X   = "dimension_x";
	// dimension_y NUMBER(12),
	public static final String COLUMN_DIMENSION_Y   = "dimension_y";
	// topLeft NUMBER(1),
	public static final String COLUMN_TOPLEFT       = "topLeft";
	//  start_node_id VARCHAR2(32),
	public static final String COLUMN_START_NODE_ID = "start_node_id";
	// end_node_id VARCHAR2(32),
	public static final String COLUMN_END_NODE_ID   = "end_node_id";
	
	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";


	protected static PhysicalLinkWrapper	instance;

	protected List				keys;

	private PhysicalLinkWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_PHYSICAL_LINK_TYPE_ID,
				COLUMN_CITY, COLUMN_STREET, COLUMN_BUILDING, COLUMN_DIMENSION_X, COLUMN_DIMENSION_Y,
				COLUMN_TOPLEFT,
				COLUMN_START_NODE_ID,
				COLUMN_END_NODE_ID,
				COLUMN_CHARACTERISTIC_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static PhysicalLinkWrapper getInstance() {
		if (instance == null)
			instance = new PhysicalLinkWrapper();
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
		if (object instanceof PhysicalLink) {
			PhysicalLink physicalLink = (PhysicalLink) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return physicalLink.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(physicalLink.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(physicalLink.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return physicalLink.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return physicalLink.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				return physicalLink.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return physicalLink.getDescription();
			else if (key.equals(COLUMN_PHYSICAL_LINK_TYPE_ID))
				return physicalLink.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_CITY))
				return physicalLink.getCity();
			else if (key.equals(COLUMN_STREET))
				return physicalLink.getStreet();
			else if (key.equals(COLUMN_BUILDING))
				return physicalLink.getBuilding();
			else if (key.equals(COLUMN_START_NODE_ID))
				return physicalLink.getStartNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_END_NODE_ID))
				return physicalLink.getEndNode().getId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return physicalLink.getCharacteristics();
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
		if (object instanceof PhysicalLink) {
			PhysicalLink physicalLink = (PhysicalLink) object;
			if (key.equals(COLUMN_NAME))
				physicalLink.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				physicalLink.setDescription((String) value);
			else if (key.equals(COLUMN_PHYSICAL_LINK_TYPE_ID))
				try {
					physicalLink.setPhysicalLinkType((PhysicalLinkType) MapStorableObjectPool.getStorableObject(new Identifier((String)value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
				else if (key.equals(COLUMN_CITY))
					physicalLink.setCity((String)value);
				else if (key.equals(COLUMN_STREET))
					physicalLink.setStreet((String)value);
				else if (key.equals(COLUMN_BUILDING))
					physicalLink.setBuilding((String)value);
			else if (key.equals(COLUMN_START_NODE_ID))
				try {
					physicalLink.setStartNode((AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier((String)value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_END_NODE_ID))
				try {
					physicalLink.setEndNode((AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier((String)value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
					List characteristicIdStr = (List)value;
					List characteristicIds = new ArrayList(characteristicIdStr.size());
					for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
						characteristicIds.add(new Identifier((String) it.next()));
					try {
						physicalLink.setCharacteristics(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
					} catch (DatabaseException e) {
						Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("PhysicalLinkWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
		}
	}

}
