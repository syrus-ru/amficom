/*-
 * $Id: TraceResourceWrapper.java,v 1.7 2006/03/13 15:54:24 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.util.Wrapper;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/03/13 15:54:24 $
 * @module analysis
 */

public class TraceResourceWrapper implements Wrapper<TraceResource> {
	public static final String KEY_TITLE = "title";
	public static final String KEY_COLOR = "color";
	public static final String KEY_IS_SHOWN = "is_shown";

	private static TraceResourceWrapper instance;

	private List<String> keys;

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	private TraceResourceWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { KEY_IS_SHOWN, KEY_TITLE, KEY_COLOR };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TraceResourceWrapper getInstance() {
		if (instance == null) {
			instance = new TraceResourceWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			// return LangModelAnalyse.getString("is_shown");
			return "";
		} else if (key.equals(KEY_TITLE)) {
			// return LangModelAnalyse.getString("trace");
			return "";
		} else if (key.equals(KEY_COLOR)) {
			// return LangModelAnalyse.getString("color");
			return "";
		}
		return key;
	}

	public Class<?> getPropertyClass(final String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			return Boolean.class;
		} else if (key.equals(KEY_TITLE)) {
			return String.class;
		} else if (key.equals(KEY_COLOR)) {
			return Color.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(final TraceResource traceResource, final String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			return new Boolean(traceResource.isShown());
		} else if (key.equals(KEY_TITLE)) {
			// @todo: помечать несоответствие не словом mismatch, а цветом
			return traceResource.isAlarm() ? traceResource.getTitle() + " <mismatch>" : traceResource.getTitle();
		} else if (key.equals(KEY_COLOR)) {
			return traceResource.getColor();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			return true;
		}
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// TODO Auto-generated method stub
	}

	public void setValue(final TraceResource traceResource, final String key, final Object value) {
		if (key.equals(KEY_IS_SHOWN)) {
			traceResource.setShown(((Boolean) value).booleanValue());
		} else if (key.equals(KEY_TITLE)) {
			traceResource.setTitle((String) value);
		} else if (key.equals(KEY_COLOR)) {
			traceResource.setColor((Color) value);
		}
	}
}
