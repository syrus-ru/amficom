/*-
 * $Id: MCMBeanWrapper.java,v 1.5 2005/08/23 15:02:15 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.JGraphText;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.5 $, $Date: 2005/08/23 15:02:15 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanWrapper implements Wrapper {

	public static final String		KEY_NAME		= "name";
	public static final String		KEY_DESCRIPTION	= "description";
	public static final String		KEY_HOSTNAME	= "hostname";
	public static final String		KEY_SERVER_ID	= "serverId";
	public static final String		KEY_USER_ID		= "userd";
	
	
	public static final String		PROPERTY_USERS_REFRESHED	= "usersRefreshed";
	public static final String		PROPERTY_SERVERS_REFRESHED	= "serversRefreshed";

	private static MCMBeanWrapper	instance;

	private List<String>			keys;
	
	private Map<String, Identifier> 	serverIdMap;
	private Map<String, Identifier> 	userIdMap;
	
	private MCMBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		this.serverIdMap  = new HashMap<String, Identifier>();
		this.userIdMap  = new HashMap<String, Identifier>();		

		JGraphText.entityDispatcher.addPropertyChangeListener(
			ObjectEntities.SYSTEMUSER,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					refreshUsers();
					JGraphText.entityDispatcher.firePropertyChange(
						new PropertyChangeEvent(MCMBeanWrapper.this, PROPERTY_USERS_REFRESHED, null, null));
				}
			});
		
		JGraphText.entityDispatcher.addPropertyChangeListener(
			ObjectEntities.SERVER,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					refreshServers();
					JGraphText.entityDispatcher.firePropertyChange(
						new PropertyChangeEvent(MCMBeanWrapper.this, PROPERTY_SERVERS_REFRESHED, null, null));
				}
			});
		
		this.refreshServers();
		this.refreshUsers();
	}

	public static MCMBeanWrapper getInstance() {
		if (instance == null) {
			instance = new MCMBeanWrapper();
		}
		return instance;
	}
	
	public void refreshServers() {
		this.refreshMap(this.serverIdMap, ObjectEntities.SERVER_CODE);
	}
	
	public void refreshUsers() {
		this.refreshMap(this.userIdMap, ObjectEntities.SYSTEMUSER_CODE);
	}

	private void refreshMap(Map<String, Identifier> map,
	                              short entityCode) {
		map.clear();
		try {
			Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjectsByCondition(
				new EquivalentCondition(entityCode), 
				true);			
			for(StorableObject storableObject : storableObjects) {
				map.put(((Namable)storableObject).getName(), 
					storableObject.getId());
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getKey(int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (key.equals(KEY_NAME)) {
			return LangModelManager.getString("Entity.MCM.attributes.Name");
		} else if (key.equals(KEY_DESCRIPTION)) { 
			return LangModelManager.getString("Entity.MCM.attributes.Description"); 
		} else if (key.equals(KEY_HOSTNAME)) { 
			return LangModelManager.getString("Entity.MCM.attributes.Hostname"); 
		} else if (key.equals(KEY_SERVER_ID)) { 
			return LangModelManager.getString("Entity.MCM.attributes.Server"); 
		} else if (key.equals(KEY_USER_ID)) { 
			return LangModelManager.getString("Entity.MCM.attributes.User"); 
		} 
		
		
		return null;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_NAME) || 
				key.equals(KEY_DESCRIPTION) ||
				key.equals(KEY_HOSTNAME)) { 
			return String.class; 
		}
		if (key.equals(KEY_SERVER_ID) || 
				key.equals(KEY_USER_ID)) {
			return Identifier.class;
		}		
		return null;
	}

	public Object getPropertyValue(String key) {
		if (key.equals(KEY_SERVER_ID)) {
			return this.serverIdMap;
		}
		if (key.equals(KEY_USER_ID)) {			
			return this.userIdMap;
		}
		return null;
	}

	public Object getValue(	Object object,
							String key) {
		if (object instanceof MCMBean) {
			MCMBean bean = (MCMBean) object;
			if (key.equals(KEY_NAME)) {
				return bean.getName();
			} else if (key.equals(KEY_DESCRIPTION)) {
				return bean.getDescription();
			} else if (key.equals(KEY_HOSTNAME)) {
				return bean.getHostname();
			} else if (key.equals(KEY_SERVER_ID)) { 
				return bean.getServerId(); 
			} else if (key.equals(KEY_USER_ID)) { 
				return bean.getUserId(); 
			}  
		}
		return null;
	}

	public boolean isEditable(String key) {
		return true;
	}

	public void setPropertyValue(	String key,
									Object objectKey,
									Object objectValue) {
		// TODO Auto-generated method stub
	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof MCMBean) {
			MCMBean bean = (MCMBean) object;
			if (key.equals(KEY_NAME)) {
				bean.setName((String) value);
			} else if (key.equals(KEY_DESCRIPTION)) {
				bean.setDescription((String) value);
			} else if (key.equals(KEY_HOSTNAME)) {
				bean.setHostname((String) value);
			} else if (key.equals(KEY_SERVER_ID)) { 
				bean.setServerId((Identifier) value);
			} else if (key.equals(KEY_USER_ID)) {
				bean.setUserId((Identifier) value);
			}
		}
	}

}
