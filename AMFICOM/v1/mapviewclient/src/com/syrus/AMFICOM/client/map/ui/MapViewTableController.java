/*-
 * $$Id: MapViewTableController.java,v 1.17 2005/12/08 09:20:58 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.17 $, $Date: 2005/12/08 09:20:58 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapViewTableController implements Wrapper {
	public static final String KEY_NAME = MapEditorResourceKeys.LABEL_NAME;

	public static final String KEY_DOMAIN = MapEditorResourceKeys.LABEL_DOMAIN;

	public static final String KEY_USER = MapEditorResourceKeys.LABEL_USER;

	public static final String KEY_CREATED = MapEditorResourceKeys.LABEL_CREATED;

	public static final String KEY_MODIFIED = MapEditorResourceKeys.LABEL_MODIFIED;

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
		String name = I18N.getString(key);
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
				Domain domain = StorableObjectPool.getStorableObject(view.getDomainId(), true);
				result = domain.getName();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}
		else
		if(key.equals(KEY_USER)) {
			try {
				SystemUser user = StorableObjectPool.getStorableObject(view.getCreatorId(), true);
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
