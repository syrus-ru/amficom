/*-
 * $Id: MCMBeanWrapper.java,v 1.3 2006/03/13 15:54:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.3 $, $Date: 2006/03/13 15:54:24 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanWrapper implements Wrapper<MCMBean> {

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
	
	private MCMBeanWrapper(final Dispatcher dispatcher) {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID}));
		this.serverIdMap  = new HashMap<String, Identifier>();
		this.userIdMap  = new HashMap<String, Identifier>();		
		
		dispatcher.addPropertyChangeListener(
			ObjectEntities.SYSTEMUSER,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					refreshUsers();
					dispatcher.firePropertyChange(
						new PropertyChangeEvent(MCMBeanWrapper.this, PROPERTY_USERS_REFRESHED, null, null));
				}
			});
		
		dispatcher.addPropertyChangeListener(
			ObjectEntities.SERVER,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					refreshServers();
					dispatcher.firePropertyChange(
						new PropertyChangeEvent(MCMBeanWrapper.this, PROPERTY_SERVERS_REFRESHED, null, null));
				}
			});
		
//		this.refreshServers();
//		this.refreshUsers();
	}

	public static MCMBeanWrapper getInstance(final Dispatcher dispatcher) {
		if (instance == null) {
			instance = new MCMBeanWrapper(dispatcher);
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
			return I18N.getString("Manager.Entity.MCM.attributes.Name");
		} else if (key.equals(KEY_DESCRIPTION)) { 
			return I18N.getString("Manager.Entity.MCM.attributes.Description"); 
		} else if (key.equals(KEY_HOSTNAME)) { 
			return I18N.getString("Manager.Entity.MCM.attributes.Hostname"); 
		} else if (key.equals(KEY_SERVER_ID)) { 
			return I18N.getString("Manager.Entity.MCM.attributes.Server"); 
		} else if (key.equals(KEY_USER_ID)) { 
			return I18N.getString("Manager.Entity.MCM.attributes.User"); 
		} 
		
		
		return null;
	}

	public Class<?> getPropertyClass(String key) {
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

	public Object getValue(	MCMBean mcmBean,
							String key) {
		if (mcmBean != null) {
			if (key.equals(KEY_NAME)) {
				return mcmBean.getName();
			} else if (key.equals(KEY_DESCRIPTION)) {
				return mcmBean.getDescription();
			} else if (key.equals(KEY_HOSTNAME)) {
				return mcmBean.getHostname();
			} else if (key.equals(KEY_SERVER_ID)) { 
				return mcmBean.getServerId(); 
			} else if (key.equals(KEY_USER_ID)) { 
				return mcmBean.getUserId(); 
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

	public void setValue(	MCMBean mcmBean,
							String key,
							Object value) {
		if (mcmBean != null) {
			if (key.equals(KEY_NAME)) {
				mcmBean.setName((String) value);
			} else if (key.equals(KEY_DESCRIPTION)) {
				mcmBean.setDescription((String) value);
			} else if (key.equals(KEY_HOSTNAME)) {
				mcmBean.setHostname((String) value);
			} else if (key.equals(KEY_SERVER_ID)) { 
				mcmBean.setServerId((Identifier) value);
			} else if (key.equals(KEY_USER_ID)) {
				mcmBean.setUserId((Identifier) value);
			}
		}
	}

}
