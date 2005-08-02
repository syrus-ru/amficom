
package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Wrapper;

/*-
 * $Id: RTUBeanWrapper.java,v 1.3 2005/08/02 14:42:06 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/02 14:42:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBeanWrapper implements Wrapper {

	public static final String		KEY_NAME		= "name";
	public static final String		KEY_DESCRIPTION	= "description";
	public static final String		KEY_HOSTNAME	= "hostname";
	public static final String		KEY_PORT		= "tcpport";
	public static final String		KEY_MCM_ID		= "mcmId";

	private static RTUBeanWrapper	instance;

	private List<String>					keys;
	
	private Map<Identifier, String> 	mcmIdMapReverce; 
	private Map<String, Identifier> 	mcmIdMap;
	
	private RTUBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_PORT,
				KEY_MCM_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		
		this.mcmIdMapReverce = new HashMap<Identifier, String>();
		this.mcmIdMapReverce.put(new Identifier("MCM_1"), "MCM в Тушино");
		this.mcmIdMapReverce.put(new Identifier("MCM_2"), "MCM у чёрта на куличках");
		
		this.mcmIdMap  = new HashMap<String, Identifier>();
		
		for(Identifier identifier : this.mcmIdMapReverce.keySet()) {
			this.mcmIdMap.put(this.mcmIdMapReverce.get(identifier), identifier);
		}
		
	}

	public static RTUBeanWrapper getInstance() {
		if (instance == null) {
			instance = new RTUBeanWrapper();
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
			return LangModelManager.getString("Entity.RTU.attributes.Name");
		} else if (key.equals(KEY_DESCRIPTION)) { 
			return LangModelManager.getString("Entity.RTU.attributes.Description"); 
		} else if (key.equals(KEY_HOSTNAME)) { 
			return LangModelManager.getString("Entity.RTU.attributes.Hostname"); 
		} else if (key.equals(KEY_PORT)) { 
			return LangModelManager.getString("Entity.RTU.attributes.TCPPort"); 
		} else if (key.equals(KEY_MCM_ID)) { 
			return LangModelManager.getString("Entity.RTU.attributes.MeasurementControlModule"); 
		} 
		
		
		return null;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_NAME) || 
				key.equals(KEY_DESCRIPTION) ||
				key.equals(KEY_HOSTNAME)) { 
			return String.class; 
		}
		if (key.equals(KEY_PORT)) {
			return Integer.class;
		}
		if (key.equals(KEY_MCM_ID)) {
			return Identifier.class;
		}		
		return null;
	}

	public Object getPropertyValue(String key) {
		if (key.equals(KEY_MCM_ID)) {
			return this.mcmIdMap;
		}
		return null;
	}

	public Object getValue(	Object object,
							String key) {
		if (object instanceof RTUBean) {
			RTUBean bean = (RTUBean) object;
			if (key.equals(KEY_NAME)) {
				return bean.getName();
			} else if (key.equals(KEY_DESCRIPTION)) {
				return bean.getDescription();
			} else if (key.equals(KEY_HOSTNAME)) {
				return bean.getHostname();
			} else if (key.equals(KEY_PORT)) { 
				return bean.getPort(); 
			} else if (key.equals(KEY_MCM_ID)) { 
				return 
//				this.mcmIdMapReverce.get(
					bean.getMcmId()
//					)
					; 
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
		System.out.println("RTUBeanWrapper.setPropertyValue()");

	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof RTUBean) {
			RTUBean bean = (RTUBean) object;
			if (key.equals(KEY_NAME)) {
				bean.setName((String) value);
			} else if (key.equals(KEY_DESCRIPTION)) {
				bean.setDescription((String) value);
			} else if (key.equals(KEY_HOSTNAME)) {
				bean.setHostname((String) value);
			} else if (key.equals(KEY_PORT)) { 
				try {
					bean.setPort(Short.valueOf((String)value));
				} catch(NumberFormatException nfe) {
					// nothing can do 
				}
			} else if (key.equals(KEY_MCM_ID)) {
				bean.setMcmId((Identifier) value);
			}
		}
	}

}
