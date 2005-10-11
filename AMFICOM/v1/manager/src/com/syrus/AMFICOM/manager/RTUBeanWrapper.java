/*-
 * $Id: RTUBeanWrapper.java,v 1.10 2005/10/11 15:34:53 bob Exp $
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

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.10 $, $Date: 2005/10/11 15:34:53 $
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

	public static final String PROPERTY_MCMS_REFRESHED =  "mcmsRefreshed";
	
	private static RTUBeanWrapper	instance;

	private List<String>					keys;
	
	private Map<String, Identifier> 	mcmIdMap;
	
	private RTUBeanWrapper(final Dispatcher dispatcher) {
		this.keys = Collections.unmodifiableList(
			Arrays.asList(
				new String[] { KEY_NAME, 
					KEY_DESCRIPTION, 
					KEY_HOSTNAME,
					KEY_PORT,
					KEY_MCM_ID}));
		
		this.mcmIdMap  = new HashMap<String, Identifier>();

		dispatcher.addPropertyChangeListener(
			ObjectEntities.MCM,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					refreshMCMs();
					dispatcher.firePropertyChange(
						new PropertyChangeEvent(RTUBeanWrapper.this, 
							PROPERTY_MCMS_REFRESHED, 
							null, 
							null));
				}
			});
		
		this.refreshMCMs();
	}

	public static RTUBeanWrapper getInstance(final Dispatcher dispatcher) {
		if (instance == null) {
			instance = new RTUBeanWrapper(dispatcher);
		}
		return instance;
	}

	public String getKey(int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}
	
	void refreshMCMs() {
		this.mcmIdMap.clear();
		try {
			Set<MCM> mcms = StorableObjectPool.getStorableObjectsByCondition(
				new EquivalentCondition(ObjectEntities.MCM_CODE), 
				true);			
			for(MCM mcm : mcms) {
				String name = mcm.getName();
				Identifier id = mcm.getId();
				this.mcmIdMap.put(name, id);
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName(final String key) {
		if (key.equals(KEY_NAME)) {
			return I18N.getString("Manager.Entity.RTU.attributes.Name");
		} else if (key.equals(KEY_DESCRIPTION)) { 
			return I18N.getString("Manager.Entity.RTU.attributes.Description"); 
		} else if (key.equals(KEY_HOSTNAME)) { 
			return I18N.getString("Manager.Entity.RTU.attributes.Hostname"); 
		} else if (key.equals(KEY_PORT)) { 
			return I18N.getString("Manager.Entity.RTU.attributes.TCPPort"); 
		} else if (key.equals(KEY_MCM_ID)) { 
			return I18N.getString("Manager.Entity.RTU.attributes.MeasurementControlModule"); 
		} 
		
		
		return null;
	}

	public Class getPropertyClass(final String key) {
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
				return Short.valueOf(bean.getPort()); 
			} else if (key.equals(KEY_MCM_ID)) { 
				return bean.getMcmId(); 
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
					bean.setPort(Short.parseShort((String)value));
				} catch(NumberFormatException nfe) {
					// nothing can do 
				}
			} else if (key.equals(KEY_MCM_ID)) {
				bean.setMcmId((Identifier) value);
			}
		}
	}

}
