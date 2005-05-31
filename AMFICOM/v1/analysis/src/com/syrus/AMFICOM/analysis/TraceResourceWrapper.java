/*-
 * $Id: TraceResourceWrapper.java,v 1.1 2005/05/31 10:43:02 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.awt.Color;
import java.util.*;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.util.Wrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/05/31 10:43:02 $
 * @module analysis_v1
 */

public class TraceResourceWrapper implements Wrapper {
	public static final String			KEY_TITLE	= "title";
	public static final String			KEY_COLOR	= "color";
	public static final String			KEY_IS_SHOWN	= "is_shown";

	private static TraceResourceWrapper	instance;

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private TraceResourceWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_IS_SHOWN, KEY_TITLE, KEY_COLOR };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TraceResourceWrapper getInstance() {
		if (instance == null)
			instance = new TraceResourceWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			//return LangModelAnalyse.getString("is_shown");
			return "";
		} else if (key.equals(KEY_TITLE)) { 
			//return LangModelAnalyse.getString("trace");
			return "";
		} else if (key.equals(KEY_COLOR)) { 
			//return LangModelAnalyse.getString("color");
			return "";
		}
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			return Boolean.class;
		} else if (key.equals(KEY_TITLE)) { 
			return String.class; 
		} else if (key.equals(KEY_COLOR)) { 
			return Color.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	Object object, String key) {
		
		if (object instanceof TraceResource) {
			TraceResource traceResource = (TraceResource) object;
			if (key.equals(KEY_IS_SHOWN)) {
				return new Boolean(traceResource.isShown());
			} 
			else if (key.equals(KEY_TITLE)) { 
				return traceResource.getTitle(); 
			}
			else if (key.equals(KEY_COLOR)) { 
				return traceResource.getColor(); 
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		if (key.equals(KEY_IS_SHOWN)) {
			return true;
		}
		return false;
	}

	public void setPropertyValue(	String key,
									Object objectKey,
									Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof TraceResource) {
			TraceResource traceResource = (TraceResource) object;
			if (key.equals(KEY_IS_SHOWN)) {
				traceResource.setShown(((Boolean)value).booleanValue());
			} else if (key.equals(KEY_TITLE)) {
				traceResource.setTitle((String) value);				
			} else if (key.equals(KEY_COLOR)) {
				traceResource.setColor((Color) value);				
			}
		}
	}
}
