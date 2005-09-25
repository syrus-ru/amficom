/**
 * $Id: MapLibraryTableController.java,v 1.4 2005/09/25 16:08:03 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.util.Wrapper;

public class MapLibraryTableController implements Wrapper {

	static MapLibraryTableController instance = null;

	public static MapLibraryTableController getInstance() {
		if(instance == null)
			instance = new MapLibraryTableController();
		return instance;
	}

	public static final String KEY_NAME = MapEditorResourceKeys.LABEL_NAME;

	public static final String KEY_USER = MapEditorResourceKeys.LABEL_USER;

	public static final String KEY_CREATED = MapEditorResourceKeys.LABEL_CREATED;

	public static final String KEY_MODIFIED = MapEditorResourceKeys.LABEL_MODIFIED;

	private List keys;

	private String[] keysArray;

	private MapLibraryTableController() {
		// empty private constructor
		this.keysArray = new String[] {
			KEY_NAME,
			KEY_USER,
			KEY_CREATED,
			KEY_MODIFIED
		};

		this.keys = Collections.unmodifiableList(new ArrayList(
				Arrays.asList(this.keysArray)));
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
		MapLibrary mapLibrary = (MapLibrary )object;
		if(key.equals(KEY_NAME)) {
			result = mapLibrary.getName();
		}
		else
		if(key.equals(KEY_USER)) {
			try {
				SystemUser user = StorableObjectPool.getStorableObject(mapLibrary.getCreatorId(), false);
				result = user.getName();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}
		else
		if(key.equals(KEY_CREATED)) {
			result = MapPropertiesManager.getDateFormat().format(mapLibrary.getCreated());
		}
		else
		if(key.equals(KEY_MODIFIED)) {
			result = MapPropertiesManager.getDateFormat().format(mapLibrary.getModified());
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
		Object result = ""; //$NON-NLS-1$
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
