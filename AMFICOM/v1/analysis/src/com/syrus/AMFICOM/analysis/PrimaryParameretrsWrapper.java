/*-
 * $Id: PrimaryParameretrsWrapper.java,v 1.2 2005/06/02 12:53:29 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/06/02 12:53:29 $
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
			PrimaryParameters parameters = (PrimaryParameters) object;
			
			if (key.equals(KEY_MODULE_ID)) {
				return parameters.getModuleId();
			} else if (key.equals(KEY_WAVELENGTH)) {
				return parameters.getWavelength();
			} else if (key.equals(KEY_PULSEWIDTH)) {
				return parameters.getPulsewidth();
			} else if (key.equals(KEY_GROUPINDEX)) {
				return parameters.getGroupindex();
			} else if (key.equals(KEY_AVERAGES)) {
				return parameters.getAverages();
			} else if (key.equals(KEY_RESOLUTION)) {
				return parameters.getResolution();
			} else if (key.equals(KEY_RANGE)) {
				return parameters.getRange();
			} else if (key.equals(KEY_DATE)) {
				return parameters.getDate();
			} else if (key.equals(KEY_TIME)) {
				return parameters.getTime();
			} else if (key.equals(KEY_BACKSCATTER)) {
				return parameters.getBackscatter();
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
		if (object instanceof PrimaryParameters) {
			PrimaryParameters parameters = (PrimaryParameters) object;
			
			if (key.equals(KEY_MODULE_ID)) {
				parameters.setModuleId((String) value);
			} else if (key.equals(KEY_WAVELENGTH)) {
				parameters.setWavelength((String) value);
			} else if (key.equals(KEY_PULSEWIDTH)) {
				parameters.setPulsewidth((String) value);
			} else if (key.equals(KEY_GROUPINDEX)) {
				parameters.setGroupindex((String)value);
			} else if (key.equals(KEY_AVERAGES)) {
				parameters.setAverages((String) value);
			} else if (key.equals(KEY_RESOLUTION)) {
				parameters.setResolution((String) value);
			} else if (key.equals(KEY_RANGE)) {
				parameters.setRange((String) value);
			} else if (key.equals(KEY_DATE)) {
				parameters.setDate((String) value);
			} else if (key.equals(KEY_TIME)) {
				parameters.setTime((String) value);
			} else if (key.equals(KEY_BACKSCATTER)) {
				parameters.setBackscatter((String) value);
			}
		}
	}
}
