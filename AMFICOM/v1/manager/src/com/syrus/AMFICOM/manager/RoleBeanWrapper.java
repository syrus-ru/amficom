/*-
 * $Id: RoleBeanWrapper.java,v 1.1 2005/11/07 15:21:45 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/11/07 15:21:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RoleBeanWrapper implements Wrapper<RoleBean> {

	public static final String		NAME				= "name";

	private static RoleBeanWrapper	instance;

	private List<String>					keys;

	public String getKey(int index) {
		return this.keys.get(index);
	}

	private RoleBeanWrapper() {
		String[] keysArray = new String[] { NAME};
		
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static synchronized RoleBeanWrapper getInstance() {
		if (instance == null) {
			instance = new RoleBeanWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		key = key.intern();
		if (key == NAME) {
			return I18N.getString("Manager.Entity.User.attributes.Name");
		}		
		return null;
	}

	public Class getPropertyClass(String key) {
		key = key.intern();
		if (key == NAME) { 
			return String.class; 
		}
		
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	final RoleBean roleBean,
	                       	String key) {
		if (roleBean != null) {
			key = key.intern();
			if (key == NAME) {
				return roleBean.getName();
			} 
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return true;
	} 

	public void setPropertyValue(	final String key,
	                             	final Object objectKey,
	                             	final Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	final RoleBean roleBean,
	                     	String key,
	                     	final Object value) {
		if (roleBean != null) {
			key = key.intern();
			if (key == NAME) {
				roleBean.setName((String) value);
			}
		}
	}

}
