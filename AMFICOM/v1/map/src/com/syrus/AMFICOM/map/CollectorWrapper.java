/*
* $Id: CollectorWrapper.java,v 1.3 2005/01/27 06:23:59 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/01/27 06:23:59 $
 * @author $Author: bob $
 * @module map_v1
 */
public class CollectorWrapper implements Wrapper {
	// name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";

	
	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";
	
	// collector_id VARCHAR2(32),
	public static final String LINK_COLUMN_COLLECTOR_ID  = "collector_id";
	// physical_link_id VARCHAR2(32),
	public static final String LINK_COLUMN_PHYSICAL_LINK_ID      = "physical_link_id";

	protected static CollectorWrapper	instance;

	protected List			keys;

	private CollectorWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIER_ID,
				COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_CHARACTERISTIC_ID, LINK_COLUMN_PHYSICAL_LINK_ID};	
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static CollectorWrapper getInstance() {
		if (instance == null)
			instance = new CollectorWrapper();
		return instance;
	}
	
	public String getKey(int index) {
		return (String)this.keys.get(index);
	}
	
	public List getKeys() {
		return this.keys;
	}	

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}
	
	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTIC_ID) || key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
			return List.class;		
		return String.class;
	}
	
	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof Collector) {
			Collector collector = (Collector) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return collector.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(collector.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(collector.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return collector.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return collector.getModifierId().getIdentifierString();			
			else if (key.equals(COLUMN_NAME))
				return collector.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return collector.getDescription();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return collector.getCharacteristics();
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				return collector.getPhysicalLinks();

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
		if (object instanceof Collector) {
			Collector collector = (Collector) object;
			if (key.equals(COLUMN_NAME))
				collector.setName((String)value);
			else if (key.equals(COLUMN_DESCRIPTION))
				collector.setDescription((String)value);
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
				List characteristicIdStr = (List)value;
				List characteristicIds = new ArrayList(characteristicIdStr.size());
				for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
					characteristicIds.add(new Identifier((String) it.next()));
				try {
					collector.setCharacteristics(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
				} catch (DatabaseException e) {
					Log.errorMessage("CollectorWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("CollectorWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			}
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID)) {
				List physicalLinkIdStr = (List)value;
				List physicalLinkIds = new ArrayList(physicalLinkIdStr.size());
				for (Iterator it = physicalLinkIdStr.iterator(); it.hasNext();) 
					physicalLinkIds.add(new Identifier((String) it.next()));
				try {
					collector.setCharacteristics(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
				} catch (DatabaseException e) {
					Log.errorMessage("CollectorWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("CollectorWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			}
		}
	}

}
