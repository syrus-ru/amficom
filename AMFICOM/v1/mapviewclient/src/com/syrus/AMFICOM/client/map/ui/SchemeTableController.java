/*-
 * $$Id: SchemeTableController.java,v 1.16 2006/02/15 11:13:06 stas Exp $$
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2006/02/15 11:13:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SchemeTableController extends StorableObjectWrapper<Scheme> {
	public static final String KEY_NAME = MapEditorResourceKeys.LABEL_NAME;
	public static final String KEY_DOMAIN = MapEditorResourceKeys.LABEL_DOMAIN;
	public static final String KEY_USER = MapEditorResourceKeys.LABEL_USER;
	public static final String KEY_CREATED = MapEditorResourceKeys.LABEL_CREATED;
	public static final String KEY_MODIFIED = MapEditorResourceKeys.LABEL_MODIFIED;

	private static SchemeTableController instance;

	private List keys;
	private String[] keysArray;

	private SchemeTableController() {
		// empty private constructor
		this.keysArray = new String[] {
			KEY_NAME, 
			KEY_DOMAIN, 
			KEY_USER, 
			KEY_CREATED, 
			KEY_MODIFIED
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
		String name = I18N.getString(key);
		return name;
	}

	@Override
	public Object getValue(final Scheme scheme, final String key) {
		Object result = null;
		if(scheme != null) {
			if (key.equals(KEY_NAME)) {
				result = scheme.getName();
			}
			else if(key.equals(KEY_DOMAIN)) {
				try {
					Domain domain = StorableObjectPool.getStorableObject(
							scheme.getDomainId(), false);
					result = domain.getName();
				} catch(final ApplicationException ae) {
					Log.errorMessage(ae);
					result = null;
				}
			}
			else if (key.equals(KEY_USER)) {
				try {
					SystemUser user = StorableObjectPool.getStorableObject(
							scheme.getCreatorId(),
							false);
					result = user.getName();
				} catch(Exception e) {
					Log.errorMessage(e);
				}
			}
			else if (key.equals(KEY_CREATED)) {
				result = MapPropertiesManager.getDateFormat().format(
						scheme.getCreated());
			}
			else if (key.equals(KEY_MODIFIED)) {
				result = MapPropertiesManager.getDateFormat().format(
						scheme.getModified());
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(Scheme object, final String key, final Object value) {
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

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}

}
