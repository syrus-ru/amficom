/*
* $Id: TopologicalNodeWrapper.java,v 1.2 2005/01/27 06:23:59 bob Exp $
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
public class TopologicalNodeWrapper implements Wrapper {

	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	
	//	 name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// longitude NUMBER(12,6),
	public static final String COLUMN_LONGITUDE     = "longitude";
	// latiude NUMBER(12,6),
	public static final String COLUMN_LATIUDE       = "latiude";
	// active NUMBER(1),
	public static final String COLUMN_ACTIVE        = "active";


	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";


	protected static TopologicalNodeWrapper	instance;

	protected List				keys;

	private TopologicalNodeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_LONGITUDE, COLUMN_LATIUDE, COLUMN_ACTIVE,
				NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID,
				COLUMN_CHARACTERISTIC_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static TopologicalNodeWrapper getInstance() {
		if (instance == null)
			instance = new TopologicalNodeWrapper();
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
		if (object instanceof TopologicalNode) {
			TopologicalNode topologicalNode = (TopologicalNode) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return topologicalNode.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(topologicalNode.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(topologicalNode.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return topologicalNode.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return topologicalNode.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				return topologicalNode.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return topologicalNode.getDescription();
			else if (key.equals(COLUMN_LONGITUDE))
				return Double.toString(topologicalNode.getLocation().getX());
			else if (key.equals(COLUMN_LATIUDE))
				return Double.toString(topologicalNode.getLocation().getY());
			else if (key.equals(COLUMN_ACTIVE))
				return Boolean.toString(topologicalNode.isActive());
			else if (key.equals(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID))
				return topologicalNode.getPhysicalLink().getId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return topologicalNode.getCharacteristics();
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
		if (object instanceof TopologicalNode) {
			TopologicalNode topologicalNode = (TopologicalNode) object;
			if (key.equals(COLUMN_NAME))
				topologicalNode.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				topologicalNode.setDescription((String) value);
			else if (key.equals(COLUMN_LONGITUDE))
				topologicalNode.setLongitude(Double.parseDouble((String)value)); 
			else if (key.equals(COLUMN_LATIUDE))
				topologicalNode.setLatitude(Double.parseDouble((String)value));
			else if (key.equals(COLUMN_ACTIVE))
				topologicalNode.setActive(Boolean.valueOf((String)value).booleanValue());
			else if (key.equals(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID))				
					try {
						topologicalNode.setPhysicalLink((PhysicalLink) MapStorableObjectPool.getStorableObject(new Identifier((String)value), true));
					} catch (DatabaseException e) {
						Log.errorMessage("TopologicalNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("TopologicalNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
					List characteristicIdStr = (List)value;
					List characteristicIds = new ArrayList(characteristicIdStr.size());
					for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
						characteristicIds.add(new Identifier((String) it.next()));
					try {
						topologicalNode.setCharacteristics(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
					} catch (DatabaseException e) {
						Log.errorMessage("TopologicalNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("TopologicalNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
		}
	}

}
