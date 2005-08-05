/*
 * TestResourceController.java Created on 20.08.2004 10:38:55
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Wrapper;

public final class MapViewTableController implements Wrapper {
	public static final String KEY_NAME = "Name";

	public static final String KEY_DOMAIN = "Domain";

	public static final String KEY_USER = "User_id";

	public static final String KEY_CREATED = "Created";

	public static final String KEY_MODIFIED = "Modified";

	private static MapViewTableController instance;

	private List keys;

	private String[] keysArray;

	private MapViewTableController() {
		this.keysArray = new String[] {
			KEY_NAME,
			KEY_DOMAIN,
			KEY_USER,
			KEY_CREATED,
			KEY_MODIFIED
		};

		this.keys = Collections.unmodifiableList(Arrays.asList(this.keysArray));
	}

	public static MapViewTableController getInstance() {
		if(instance == null)
			instance = new MapViewTableController();
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

	public Object getValue(final Object object, final String key) {
		Object result = null;
		MapView view = (MapView )object;
		if(key.equals(KEY_NAME)) {
			result = view.getName();
		}
		else
		if(key.equals(KEY_DOMAIN)) {
			try {
				Domain domain = StorableObjectPool.getStorableObject(view.getDomainId(), false);
				result = domain.getName();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}
		else
		if(key.equals(KEY_USER)) {
			try {
				SystemUser user = StorableObjectPool.getStorableObject(view.getCreatorId(), false);
				result = user.getName();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}
		else
		if(key.equals(KEY_CREATED)) {
			result = MapPropertiesManager.getDateFormat().format(view.getCreated());
		}
		else
		if(key.equals(KEY_MODIFIED)) {
			result = MapPropertiesManager.getDateFormat().format(view.getModified());
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
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
