
package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Wrapper;

/*-
 * $Id: MCMBeanWrapper.java,v 1.1 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/01 11:32:03 $
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

	private static MCMBeanWrapper	instance;

	private List<String>			keys;
	
	private MCMBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static MCMBeanWrapper getInstance() {
		if (instance == null) {
			instance = new MCMBeanWrapper();
		}
		return instance;
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
//			return this.mcmIdMap;
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
		System.out.println("MCMBeanWrapper.setPropertyValue()");

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
