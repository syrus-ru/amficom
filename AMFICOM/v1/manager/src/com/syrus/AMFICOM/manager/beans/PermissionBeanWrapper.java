
/*-
 * $Id: PermissionBeanWrapper.java,v 1.3 2006/03/13 15:54:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.manager.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2006/03/13 15:54:24 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class PermissionBeanWrapper implements Wrapper<PermissionBean> {

	private static Map<Module, PermissionBeanWrapper> instanceMap;

	private List<String>					keys;
	private Map<String, PermissionCodename> permissionCodenamesMap;

	private final Module	module;

	private PermissionBeanWrapper(final Module module) {		
		this.module = module;
		final List<String> selfKeys = new ArrayList<String>();
		
		this.permissionCodenamesMap = new HashMap<String, PermissionCodename>();
		
		for(final PermissionCodename codename : PermissionAttributes.PermissionCodename.values()) {
			if (codename.getModule() == this.module && codename.isEnable()) {
				this.permissionCodenamesMap.put(codename.name(), codename);
				selfKeys.add(codename.name());					
			}
		}
		
		this.keys = Collections.unmodifiableList(selfKeys);
	}

	public static synchronized PermissionBeanWrapper getInstance(final Module module) {
		if (instanceMap == null) {
			instanceMap = new HashMap<Module, PermissionBeanWrapper>();				
		}
		
		PermissionBeanWrapper factory = instanceMap.get(module);
		if (factory == null) {
			factory = new PermissionBeanWrapper(module);
			instanceMap.put(module, factory);
		}
		return factory;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		final PermissionCodename codename = this.permissionCodenamesMap.get(key);
		if (codename != null) {
			return codename.getDescription();
		}
		return null;
	}

	public Class<?> getPropertyClass(final String key) {	
		if (this.permissionCodenamesMap.containsKey(key)) {
			return Boolean.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public Object getValue(	final PermissionBean permissionBean,
	                       	final String key) {
		if (permissionBean != null) {
			final PermissionCodename codename = this.permissionCodenamesMap.get(key);
			if (codename != null) {
				return Boolean.valueOf(permissionBean.getPermissionAttributes().isPermissionEnable(codename));
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
		// nothing
	}

	public void setValue(	final PermissionBean permissionBean,
	                     	final String key,
	                     	final Object value) {
		if (permissionBean != null) {
			final PermissionCodename codename = this.permissionCodenamesMap.get(key);
			if (codename != null) {
				Boolean bValue = (Boolean) value;
				final boolean booleanValue = bValue.booleanValue();
				final PermissionAttributes permissionAttributes = permissionBean.getPermissionAttributes();
				permissionAttributes.setPermissionEnable(codename, booleanValue);
				permissionAttributes.setDenidEnable(codename, !booleanValue);
			}
		}
	}

}
