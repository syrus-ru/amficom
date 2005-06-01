/*-
 * $Id: PrimaryParameretrsWrapper.java,v 1.1 2005/06/01 08:14:42 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.util.Wrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/06/01 08:14:42 $
 * @module analysis_v1
 */

public class PrimaryParameretrsWrapper implements Wrapper {
	
	public static final String KEY_MODULE_ID = "module_id";
	public static final String KEY_WAVELENGTH = "wavelength";
	public static final String KEY_PULSEWIDTH = "pulsewidth";
	public static final String KEY_GROUPINDEX = "groupindex";
	public static final String KEY_AVERAGES = "averages";
	public static final String KEY_RESOLUTION = "resolution";
	public static final String KEY_RANGE = "range";
	public static final String KEY_DATE = "date";
	public static final String KEY_TIME = "time";
	public static final String KEY_BACKSCATTER = "backscatter";

	private static PrimaryParameretrsWrapper	instance;

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private PrimaryParameretrsWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_MODULE_ID, KEY_WAVELENGTH,
				KEY_PULSEWIDTH, KEY_GROUPINDEX, KEY_AVERAGES, KEY_RESOLUTION,
				KEY_RANGE, KEY_DATE, KEY_TIME, KEY_BACKSCATTER };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PrimaryParameretrsWrapper getInstance() {
		if (instance == null)
			instance = new PrimaryParameretrsWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (this.keys.contains(key)) {
			return LangModelAnalyse.getString(key);
		}
		return key;
	}

	public Class getPropertyClass(String key) {
		if (this.keys.contains(key)) {
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	Object object, String key) {
		
		if (object instanceof PrimaryParameters) {
			PrimaryParameters paramaters = (PrimaryParameters) object;
			
			if (key.equals(KEY_MODULE_ID)) {
				return paramaters.getModuleId();
			} else if (key.equals(KEY_WAVELENGTH)) {
				return paramaters.getWavelength();
			} else if (key.equals(KEY_PULSEWIDTH)) {
				return paramaters.getPulsewidth();
			} else if (key.equals(KEY_GROUPINDEX)) {
				return paramaters.getGroupindex();
			} else if (key.equals(KEY_AVERAGES)) {
				return paramaters.getAverages();
			} else if (key.equals(KEY_RESOLUTION)) {
				return paramaters.getResolution();
			} else if (key.equals(KEY_RANGE)) {
				return paramaters.getRange();
			} else if (key.equals(KEY_DATE)) {
				return paramaters.getDate();
			} else if (key.equals(KEY_TIME)) {
				return paramaters.getTime();
			} else if (key.equals(KEY_BACKSCATTER)) {
				return paramaters.getBackscatter();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
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
			PrimaryParameters paramaters = (PrimaryParameters) object;
			
			if (key.equals(KEY_MODULE_ID)) {
				paramaters.setModuleId((String) value);
			} else if (key.equals(KEY_WAVELENGTH)) {
				paramaters.setWavelength((String) value);
			} else if (key.equals(KEY_PULSEWIDTH)) {
				paramaters.setPulsewidth((String) value);
			} else if (key.equals(KEY_GROUPINDEX)) {
				paramaters.setGroupindex((String)value);
			} else if (key.equals(KEY_AVERAGES)) {
				paramaters.setAverages((String) value);
			} else if (key.equals(KEY_RESOLUTION)) {
				paramaters.setResolution((String) value);
			} else if (key.equals(KEY_RANGE)) {
				paramaters.setRange((String) value);
			} else if (key.equals(KEY_DATE)) {
				paramaters.setDate((String) value);
			} else if (key.equals(KEY_TIME)) {
				paramaters.setTime((String) value);
			} else if (key.equals(KEY_BACKSCATTER)) {
				paramaters.setBackscatter((String) value);
			}
		}
	}
}
