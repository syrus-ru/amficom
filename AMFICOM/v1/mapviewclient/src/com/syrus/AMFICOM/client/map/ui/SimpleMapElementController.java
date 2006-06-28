/*-
 * $$Id: SimpleMapElementController.java,v 1.20 2006/06/23 14:29:00 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.20 $, $Date: 2006/06/23 14:29:00 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SimpleMapElementController implements Wrapper {

	public static final String KEY_NAME = MapEditorResourceKeys.LABEL_NAME;
	public static final String KEY_TYPE = MapEditorResourceKeys.LABEL_TYPE;

	private static SimpleMapElementController instance;

	private List<String> keys;

	private SimpleMapElementController() {
		this.keys = Collections.unmodifiableList(Arrays.asList(
				new String[] { KEY_NAME, KEY_TYPE 
		}));
	}

	public static SimpleMapElementController getInstance() {
		if(instance == null)
			instance = new SimpleMapElementController();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if(key.equals(KEY_NAME))
			name = I18N.getString(MapEditorResourceKeys.LABEL_NAME);
		else if(key.equals(KEY_TYPE))
			name = I18N.getString(MapEditorResourceKeys.LABEL_TYPE);
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;

		if(object == null) {
			result = " "; //$NON-NLS-1$
		} else if(key.equals(KEY_NAME)) {
			if (object instanceof Namable) {
				result = ((Namable)object).getName();
			} else {
				Class clazz = object.getClass();
				String methodName = "getName"; //$NON-NLS-1$
				String name = ""; //$NON-NLS-1$
				try {
					Method method = clazz.getMethod(methodName, new Class[0]);
					name = (String) (method.invoke(object, new Object[0]));
					result = name;
				} catch(InvocationTargetException iae) {
					result = " "; //$NON-NLS-1$
				} catch(IllegalAccessException iae) {
					result = " "; //$NON-NLS-1$
				} catch(NoSuchMethodException nsme) {
					result = " "; //$NON-NLS-1$
				}

				if(result == null) {
					methodName = "name"; //$NON-NLS-1$
					try {
						Method method = clazz.getMethod(methodName, new Class[0]);
						name = (String) (method.invoke(object, new Object[0]));
						result = name;
					} catch(InvocationTargetException iae) {
						result = " "; //$NON-NLS-1$
					} catch(IllegalAccessException iae) {
						result = " "; //$NON-NLS-1$
					} catch(NoSuchMethodException nsme) {
						result = " "; //$NON-NLS-1$
					}
				}
			}
		} else if(object instanceof MapElement) {
			MapElement me = (MapElement) object;
			if(key.equals(KEY_TYPE)) {
				if(me instanceof SiteNode) {
					SiteNodeType siteNodeType = (((SiteNode) me).getType());
					result = siteNodeType.getName();
				} else if(me instanceof PhysicalLink) {
					PhysicalLinkType physicalLinkType = (((PhysicalLink) me)
							.getType());
					result = physicalLinkType.getName();
				}
			}
		} else {
			result = " "; //$NON-NLS-1$
		}
		return result + " "; //$NON-NLS-1$
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		// empty
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
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

	public Class<?> getPropertyClass(String key) {
		Class<?> clazz = String.class;
		return clazz;
	}
}
