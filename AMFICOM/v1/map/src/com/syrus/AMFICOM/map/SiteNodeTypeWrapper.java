/*
* $Id: SiteNodeTypeWrapper.java,v 1.1 2005/01/26 08:59:41 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/01/26 08:59:41 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeTypeWrapper implements Wrapper {

	// codename VARCHAR2(32) NOT NULL,
	public static final String COLUMN_CODENAME      = "codename";
	// name VARCHAR2(128),
	public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// image_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_IMAGE_ID      = "image_id";
	// topological NUMBER(1),
	public static final String COLUMN_TOPOLOGICAL   = "topological";

	public static final String COLUMN_CHARACTERISTIC_ID  = "collector_id";


	protected static SiteNodeTypeWrapper	instance;

	protected List				keys;

	private SiteNodeTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_IMAGE_ID,
				COLUMN_TOPOLOGICAL, 
				COLUMN_CHARACTERISTIC_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static SiteNodeTypeWrapper getInstance() {
		if (instance == null)
			instance = new SiteNodeTypeWrapper();
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
		if (object instanceof SiteNodeType) {
			SiteNodeType siteNodeType = (SiteNodeType) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return siteNodeType.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(siteNodeType.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(siteNodeType.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return siteNodeType.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return siteNodeType.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_CODENAME))
				return siteNodeType.getCodename();			
			else if (key.equals(COLUMN_NAME))
				return siteNodeType.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return siteNodeType.getDescription();
			else if (key.equals(COLUMN_IMAGE_ID))
				return siteNodeType.getImageId().getIdentifierString();
			else if (key.equals(COLUMN_TOPOLOGICAL))
				return Boolean.toString(siteNodeType.isTopological());
			else if (key.equals(COLUMN_CHARACTERISTIC_ID))
				return siteNodeType.getCharacteristics();
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
		if (object instanceof SiteNodeType) {
			SiteNodeType siteNodeType = (SiteNodeType) object;
			if (key.equals(COLUMN_CODENAME))
				siteNodeType.setCodename((String)value);			
			else if (key.equals(COLUMN_NAME))
				siteNodeType.setName((String)value);
			else if (key.equals(COLUMN_DESCRIPTION))
				siteNodeType.setDescription((String)value);
			else if (key.equals(COLUMN_IMAGE_ID))
				siteNodeType.setImageId(new Identifier((String)value));
			else if (key.equals(COLUMN_TOPOLOGICAL))
				siteNodeType.setTopological(Boolean.valueOf((String)value).booleanValue());
			else if (key.equals(COLUMN_CHARACTERISTIC_ID)) {
					List characteristicIdStr = (List)value;
					List characteristicIds = new ArrayList(characteristicIdStr.size());
					for (Iterator it = characteristicIdStr.iterator(); it.hasNext();) 
						characteristicIds.add(new Identifier((String) it.next()));
					try {
						siteNodeType.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
					} catch (DatabaseException e) {
						Log.errorMessage("SiteNodeTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					} catch (CommunicationException e) {
						Log.errorMessage("SiteNodeTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
					}
				}
		}
	}

}
