/*
* $Id: SiteNodeWrapper.java,v 1.2 2005/01/26 07:54:08 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/01/26 07:54:08 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeWrapper implements Wrapper {

	// name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// longitude NUMBER(12,6),
	public static final String COLUMN_LONGITUDE     = "longitude";
	// latiude NUMBER(12,6),
	public static final String COLUMN_LATIUDE       = "latiude";
	// image_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_IMAGE_ID      = "image_id";
	// site_node_type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_SITE_NODE_TYPE_ID     = "site_node_type_id";
	// city VARCHAR2(128),
	public static final String COLUMN_CITY  = "city";
	// street VARCHAR2(128),
	public static final String COLUMN_STREET        = "street";
	// building VARCHAR2(128),
	public static final String COLUMN_BUILDING      = "building";

	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";


	protected static SiteNodeWrapper	instance;

	protected List				keys;

	private SiteNodeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_LONGITUDE, COLUMN_LATIUDE, COLUMN_IMAGE_ID, COLUMN_SITE_NODE_TYPE_ID,
				COLUMN_CITY, COLUMN_STREET, COLUMN_BUILDING, 
				COLUMN_CHARACTERISTIC_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

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
		if (key.equals(COLUMN_CHARACTERISTIC_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof SiteNode) {
			SiteNode siteNode = (SiteNode) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return siteNode.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(siteNode.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(siteNode.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return siteNode.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return siteNode.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_NAME))
				return siteNode.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return siteNode.getDescription();
			else if (key.equals(COLUMN_LONGITUDE))
				return Double.toString(siteNode.getLocation().getX());
			else if (key.equals(COLUMN_LATIUDE))
				return Double.toString(siteNode.getLocation().getY());
			else if (key.equals(COLUMN_IMAGE_ID))
				return siteNode.getImageId().getIdentifierString();
			else if (key.equals(COLUMN_SITE_NODE_TYPE_ID))
				return siteNode.getType().getId().getIdentifierString();			
			else if (key.equals(COLUMN_CITY))
				return siteNode.getCity();
			else if (key.equals(COLUMN_STREET))
				return siteNode.getStreet();
			else if (key.equals(COLUMN_BUILDING))
				return siteNode.getBuilding();
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
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
				siteNode.setName0((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				siteNode.setDescription0((String) value);
			else if (key.equals(COLUMN_LONGITUDE))
				siteNode.setLongitude0(Double.parseDouble((String)value)); 
			else if (key.equals(COLUMN_LATIUDE))
				siteNode.setLatitude0(Double.parseDouble((String)value));
			else if (key.equals(COLUMN_IMAGE_ID))
				siteNode.setImageId(new Identifier((String)value));
			else if (key.equals(COLUMN_SITE_NODE_TYPE_ID))
				try {
				siteNode.setType((SiteNodeType) MapStorableObjectPool.getStorableObject(new Identifier((String)value), true));
				} catch (DatabaseException e) {
					Log.errorMessage("SiteNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				} catch (CommunicationException e) {
					Log.errorMessage("SiteNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_CITY))
				siteNode.setCity((String)value);
			else if (key.equals(COLUMN_STREET))
				siteNode.setStreet((String)value);
			else if (key.equals(COLUMN_BUILDING))
				siteNode.setBuilding((String)value);			
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
					List characteristicIdStr = (List)value;
					List characteristicIds = new ArrayList(characteristicIdStr.size());
					for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
						characteristicIds.add(new Identifier((String) it.next()));
					try {
						siteNode.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
					} catch (DatabaseException e) {
						Log.errorMessage("SiteNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("SiteNodeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
		}
	}
}
