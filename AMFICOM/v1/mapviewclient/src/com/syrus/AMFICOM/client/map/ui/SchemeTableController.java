/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.client.map.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.Scheme;

public final class SchemeTableController extends StorableObjectWrapper<Scheme> {
	public static final String KEY_DOMAIN = "Domain";

	private static SchemeTableController instance;

	private List keys;
	private String[] keysArray;

	private SchemeTableController() {
		// empty private constructor
		this.keysArray = new String[] {
			COLUMN_NAME,
			KEY_DOMAIN,
			COLUMN_CREATOR_ID,
			COLUMN_CREATED,
			COLUMN_MODIFIED
		};

		this.keys = Collections.unmodifiableList(Arrays.asList(this.keysArray));
	}

	public static SchemeTableController getInstance() {
		if(instance == null)
			instance = new SchemeTableController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String[] getKeysArray() {
		return this.keysArray;
	}

	public String getName(final String key) {
		String name = LangModelMap.getString(key);
		return name;
	}

	public Object getValue(final Scheme scheme, final String key) {
		Object result = super.getValue(scheme, key);
		if(result == null && scheme != null) {
			if(key.equals(COLUMN_NAME)) {
				result = scheme.getName();
			}
			else if(key.equals(KEY_DOMAIN)) {
				try {
					Domain domain = StorableObjectPool.getStorableObject(
							scheme.getDomainId(), false);
					result = domain.getName();
				} catch(final ApplicationException ae) {
					ae.printStackTrace();
					result = null;
				}
			}
			else if(key.equals(COLUMN_CREATOR_ID)) {
				try {
					SystemUser user = StorableObjectPool.getStorableObject(
							scheme.getCreatorId(),
							false);
					result = user.getName();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			else if(key.equals(COLUMN_CREATED)) {
				result = MapPropertiesManager.getDateFormat().format(
						scheme.getCreated());
			}
			else if(key.equals(COLUMN_MODIFIED)) {
				result = MapPropertiesManager.getDateFormat().format(
						scheme.getModified());
			}
			return result;
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Scheme object, final String key, final Object value) {
		// empty
	}

	public String getKey(final int index) {
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		return result;
	}

	public void setPropertyValue(
			String key,
			Object objectKey,
			Object objectValue) {
		// empty
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}

}
