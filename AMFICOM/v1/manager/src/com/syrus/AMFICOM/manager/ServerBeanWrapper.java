/*-
 * $Id: ServerBeanWrapper.java,v 1.2 2005/10/11 15:34:53 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.2 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBeanWrapper implements Wrapper {

	public static final String		KEY_NAME		= "name";
	public static final String		KEY_DESCRIPTION	= "description";
	public static final String		KEY_HOSTNAME	= "hostname";

	private static ServerBeanWrapper	instance;

	private List<String>					keys;
	
	private ServerBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		
	}

	public static ServerBeanWrapper getInstance() {
		if (instance == null) {
			instance = new ServerBeanWrapper();
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
			return I18N.getString("Manager.Entity.Server.attributes.Name");
		} else if (key.equals(KEY_DESCRIPTION)) { 
			return I18N.getString("Manager.Entity.Server.attributes.Description"); 
		} else if (key.equals(KEY_HOSTNAME)) { 
			return I18N.getString("Manager.Entity.Server.attributes.Hostname"); 
		}
		
		
		return null;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_NAME) || 
				key.equals(KEY_DESCRIPTION) ||
				key.equals(KEY_HOSTNAME)) { 
			return String.class; 
		}		
		return null;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public Object getValue(	Object object,
							String key) {
		if (object instanceof ServerBean) {
			ServerBean bean = (ServerBean) object;
			if (key.equals(KEY_NAME)) {
				return bean.getName();
			} else if (key.equals(KEY_DESCRIPTION)) {
				return bean.getDescription();
			} else if (key.equals(KEY_HOSTNAME)) {
				return bean.getHostname();
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
		if (object instanceof ServerBean) {
			ServerBean bean = (ServerBean) object;
			if (key.equals(KEY_NAME)) {
				bean.setName((String) value);
			} else if (key.equals(KEY_DESCRIPTION)) {
				bean.setDescription((String) value);
			} else if (key.equals(KEY_HOSTNAME)) {
				bean.setHostname((String) value);
			} 
		}
	}

}
