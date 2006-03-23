/*-
 * $Id: LineMismatchEventAlarmWrapper.java,v 1.1 2006/03/23 11:09:40 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.eventv2.AbstractLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.AbstractReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.util.Log;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

public class LineMismatchEventAlarmWrapper implements Wrapper<AbstractLineMismatchEvent> {
	public static final String COLUMN_DATE_CREATED = "date_created";
	public static final String COLUMN_PATHELEMENT_NAME = "path_element_name";
	public static final String COLUMN_PATH_NAME = "path_name";
	
	private final List<String> keys;
	
	private static final LineMismatchEventWrapper WRAPPER = LineMismatchEventWrapper.getInstance();
	private static LineMismatchEventAlarmWrapper instance;

	// caching
	private static Map<Identifiable, String> linkedPathElementNames = new HashMap<Identifiable, String>();
	private static Map<Identifiable, String> linkedPathNames = new HashMap<Identifiable, String>();
	private static Map<Identifiable, String> linkedDate = new HashMap<Identifiable, String>();
	
	public static LineMismatchEventAlarmWrapper getInstance() {
		return instance == null
				? instance = new LineMismatchEventAlarmWrapper()
				: instance;
	}
	
	private LineMismatchEventAlarmWrapper() {
		List<String> keys1 = new ArrayList<String>(WRAPPER.getKeys());
		keys1.addAll(Arrays.asList(
				COLUMN_DATE_CREATED,
				COLUMN_PATHELEMENT_NAME, 
				COLUMN_PATH_NAME));
		this.keys = Collections.unmodifiableList(keys1);
	}
	
	public List<String> getKeys() {
		return this.keys;		
	}

	public String getName(final String key) {
		return key;
	}

	public Class< ? > getPropertyClass(final String key) {
		final String internedKey = key.intern();
		if (internedKey == COLUMN_DATE_CREATED
				|| internedKey == COLUMN_PATHELEMENT_NAME
				|| internedKey == COLUMN_PATH_NAME) {
			return String.class;
		}
		return WRAPPER.getPropertyClass(key);
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	public Object getValue(final AbstractLineMismatchEvent lineMismatchEvent, final String key) {
		if (lineMismatchEvent == null) {
			return null;
		}
		final String internedKey = key.intern();
		if (internedKey == COLUMN_DATE_CREATED) {
			String date = linkedDate.get(lineMismatchEvent.getId());
			if (date == null) {
				try {
					final AbstractReflectogramMismatchEvent reflectogramMismatchEvent = StorableObjectPool.getStorableObject(lineMismatchEvent.getReflectogramMismatchEventId(), true);
					Date created = reflectogramMismatchEvent.getCreated();
					date = ((SimpleDateFormat)UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT)).format(created);
					linkedDate.put(lineMismatchEvent.getId(), date);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					date = "error";
				}
			}
			return date;
		} else if (internedKey == COLUMN_PATHELEMENT_NAME) {
			String peName = linkedPathElementNames.get(lineMismatchEvent.getId());
			if (peName == null) {
				try {
					final PathElement pathElement = (PathElement)StorableObjectPool.getStorableObject(lineMismatchEvent.getAffectedPathElementId(), true);
					peName = pathElement.getName();
					linkedPathElementNames.put(lineMismatchEvent.getId(), peName);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					peName = "error";
				}
			}
			return peName;
		} else if (internedKey == COLUMN_PATH_NAME) {
			String pathName = linkedPathNames.get(lineMismatchEvent.getId());
			if (pathName == null) {
				try {
					final PathElement pathElement = (PathElement)StorableObjectPool.getStorableObject(lineMismatchEvent.getAffectedPathElementId(), true);
					pathName = pathElement.getParentPathOwner().getName();
					linkedPathNames.put(lineMismatchEvent.getId(), pathName);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					pathName = "error";
				}
			}
			return pathName;
		}
		return WRAPPER.getValue(lineMismatchEvent, key);
	}

	public boolean isEditable(String key) {
		final String internedKey = key.intern();
		if (internedKey == COLUMN_PATHELEMENT_NAME
				|| internedKey == COLUMN_PATH_NAME) {
			return false;
		}
		return WRAPPER.isEditable(key);
	}

	public void setValue(AbstractLineMismatchEvent object, String key, Object value) throws PropertyChangeException {
		// empty
	}
}
