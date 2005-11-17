/*-
 * $Id: DomainBeanWrapper.java,v 1.1 2005/11/17 09:00:32 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.manager.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanWrapper implements Wrapper<DomainBean> {

	public static final String		KEY_NAME		= "name";
	public static final String		KEY_DESCRIPTION	= "description";

	private static DomainBeanWrapper	instance;

	private List<String>					keys;
	
	private DomainBeanWrapper() {
		this.keys = Collections.unmodifiableList(
			Arrays.asList(new String[] { KEY_NAME, 
				KEY_DESCRIPTION}));

	}

	public static DomainBeanWrapper getInstance() {
		if (instance == null) {
			instance = new DomainBeanWrapper();
		}
		return instance;
	}

	public String getKey(int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		key = key.intern();
		if (key == KEY_NAME) {
			return I18N.getString("Manager.Entity.Domain.attributes.Name");
		} else if (key == KEY_DESCRIPTION) { 
			return I18N.getString("Manager.Entity.Domain.attributes.Description"); 
		}		
		return null;
	}

	public Class getPropertyClass(String key) {
		key = key.intern();
		if (key == KEY_NAME || 
				key == KEY_DESCRIPTION) { 
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public Object getValue(	DomainBean domainBean,
							String key) {
		if (domainBean != null) {
			key = key.intern();
			if (key == KEY_NAME) {
				return domainBean.getName();
			} else if (key == KEY_DESCRIPTION) {				
				return domainBean.getDescription();
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
		// nothing
	}

	public void setValue(	DomainBean domainBean,
							String key,
							Object value) {
		if (domainBean != null) {
			key = key.intern();
			if (key == KEY_NAME) {
				domainBean.setName((String) value);
			} else if (key == KEY_DESCRIPTION) {
				domainBean.setDescription((String) value);
			}
		}
	}

}
