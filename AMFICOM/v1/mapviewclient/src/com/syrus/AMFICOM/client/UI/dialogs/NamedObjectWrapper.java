/*-
 * $$Id: NamedObjectWrapper.java,v 1.8 2006/03/13 15:54:27 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2006/03/13 15:54:27 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class NamedObjectWrapper implements Wrapper {
	public static final String KEY_NAME = MapEditorResourceKeys.LABEL_NAME; //$NON-NLS-1$

	private static NamedObjectWrapper instance;

	private List keys;
	private String[] keysArray;

	private NamedObjectWrapper() {
		// empty private constructor
		this.keysArray = new String[] { KEY_NAME };

		this.keys = Collections.unmodifiableList(new ArrayList(
				Arrays.asList(this.keysArray)));
	}

	public static NamedObjectWrapper getInstance() {
		if(instance == null)
			instance = new NamedObjectWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String[] getKeysArray() {
		return this.keysArray;
	}

	public String getName(final String key) {
		String name = null;
		if(key.equals(KEY_NAME))
			name = I18N.getString(MapEditorResourceKeys.LABEL_NAME); //$NON-NLS-1$
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;

		if(object != null && key.equals(KEY_NAME)) {
			Class clazz = object.getClass();
			String methodName = "getName"; //$NON-NLS-1$
			String name = ""; //$NON-NLS-1$
			try {
				Method method = clazz.getMethod(methodName, new Class[0]);
				name = (String )(method.invoke(object, new Object[0]));
				result = name;
			} catch(InvocationTargetException iae) {
				// ignore
			} catch(IllegalAccessException iae) {
				// ignore
			} catch(NoSuchMethodException nsme) {
				// ignore
			}

			if(result == null) {
				methodName = "name"; //$NON-NLS-1$
				try {
					Method method = clazz.getMethod(methodName, new Class[0]);
					name = (String )(method.invoke(object, new Object[0]));
					result = name;
				} catch(InvocationTargetException iae) {
					// ignore
				} catch(IllegalAccessException iae) {
					// ignore
				} catch(NoSuchMethodException nsme) {
					// ignore
				}
			}
			if(result == null) {
				String fieldName = "name"; //$NON-NLS-1$
				try {
					Field field = clazz.getField(fieldName);
					name = (String )(field.get(object));
					result = name;
				} catch(IllegalAccessException iae) {
					// ignore
				} catch(SecurityException e) {
					// ignore
				} catch(NoSuchFieldException e) {
					// ignore
				}
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		//empty
	}

	public String getKey(final int index) {
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		return ""; //$NON-NLS-1$
	}

	public void setPropertyValue(
			String key,
			Object objectKey,
			Object objectValue) {
		// empty
	}

	public Class<?> getPropertyClass(String key) {
		return String.class;
	}
}
