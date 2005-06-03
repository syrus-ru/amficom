/*-
 * $Id: DetailedEventWrapper.java,v 1.2 2005/06/03 09:48:34 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.*;

import javax.swing.Icon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.util.Wrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/06/03 09:48:34 $
 * @module analysis_v1
 */

public class DetailedEventWrapper implements Wrapper {
	
	public static final String KEY_N = "eventNum";
	public static final String KEY_IMAGE = "eventImage";
	public static final String KEY_TYPE = "eventType";
	public static final String KEY_DISTANCE = "eventStartLocationKM";
	public static final String KEY_LENGTH = "eventLengthKM";
	public static final String KEY_REFLECTANCE = "eventReflectanceDB";
	public static final String KEY_LOSS = "eventLossDB";
	public static final String KEY_ATTENUATION = "eventLeadAttenuationDBKM";
	
	public static final String KEY_EXTENSION = "eventLength";
	public static final String KEY_START_LEVEL = "eventStartLevel";
	public static final String KEY_END_LEVEL = "eventEndLevel";
	public static final String KEY_MEAN_DEVIATION = "eventRMSDeviation";
	public static final String KEY_MAXDEVIATION = "eventMaxDeviation";
	public static final String KEY_REFLECTION_LEVEL = "eventReflectionLevel";
	public static final String KEY_ADZ = "eventADZ";
	public static final String KEY_EDZ = "eventEDZ";
	public static final String KEY_MAX_LEVEL = "eventMaxLevel";
	public static final String KEY_MIN_LEVEL = "eventMinLevel";
	
	public static final String KEY_ETALON_TYPE = "etEventType";
	public static final String KEY_ETALON_MAX_DEVIATION = "maxDeviation";
	public static final String KEY_ETALON_MEAN_DEVIATION = "meanDeviation";
	public static final String KEY_LOSS_DIFFERENCE = "dLoss";
	public static final String KEY_LOCATION_DIFFERENCE = "dLocation";
	public static final String KEY_LENGTH_DIFFERENCE = "dWidth";
	
	private static DetailedEventWrapper	instance;

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private DetailedEventWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_N, KEY_TYPE, KEY_DISTANCE,
				KEY_LENGTH, KEY_REFLECTANCE, KEY_LOSS, KEY_ATTENUATION,
				KEY_START_LEVEL, KEY_END_LEVEL, KEY_MEAN_DEVIATION, KEY_MAXDEVIATION,
				KEY_REFLECTION_LEVEL, KEY_ADZ, KEY_EDZ, KEY_MAX_LEVEL, KEY_MIN_LEVEL,
				KEY_EXTENSION, KEY_ETALON_TYPE, KEY_ETALON_MAX_DEVIATION,
				KEY_ETALON_MEAN_DEVIATION, KEY_LOSS_DIFFERENCE,
				KEY_LOCATION_DIFFERENCE, KEY_LENGTH_DIFFERENCE, KEY_IMAGE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static DetailedEventWrapper getInstance() {
		if (instance == null)
			instance = new DetailedEventWrapper();
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
		if (key.equals(KEY_IMAGE)) {
			return Icon.class;
		} else if (this.keys.contains(key)) {
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	Object object, String key) {
		
		if (object instanceof DetailedEventResource) {
			DetailedEventResource ev = (DetailedEventResource) object;
			
			if (key.equals(KEY_N)) {
				return ev.getNumber();
			} if (key.equals(KEY_IMAGE)) {
				return ev.getImage();
			} else if (key.equals(KEY_TYPE)) {
				return ev.getType();
			} else if (key.equals(KEY_DISTANCE)) {
				return ev.getLocation();
			} else if (key.equals(KEY_LENGTH)) {
				return ev.getLength();
			} else if (key.equals(KEY_REFLECTANCE)) {
				return ev.getReflectance();
			} else if (key.equals(KEY_LOSS)) {
				return ev.getLoss();
			} else if (key.equals(KEY_ATTENUATION)) {
				return ev.getAttenuation();
			} else if (key.equals(KEY_START_LEVEL)) {
				return ev.getStartLevel();
			} else if (key.equals(KEY_END_LEVEL)) {
				return ev.getEndLevel();
			} else if (key.equals(KEY_MEAN_DEVIATION)) {
				return ev.getMeanDeviation();
			} else if (key.equals(KEY_MAXDEVIATION)) {
				return ev.getMaxDeviation();
			} else if (key.equals(KEY_REFLECTION_LEVEL)) {
				return ev.getReflectionLevel();
			} else if (key.equals(KEY_EDZ)) {
				return ev.getEdz();
			} else if (key.equals(KEY_ADZ)) {
				return ev.getAdz();
			} else if (key.equals(KEY_MAX_LEVEL)) {
				return ev.getMaxLevel();
			} else if (key.equals(KEY_MIN_LEVEL)) {
				return ev.getMinLevel();
			} else if (key.equals(KEY_EXTENSION)) {
				return ev.getExtension();
			} else if (key.equals(KEY_ETALON_TYPE)) {
				return ev.getEtalonType();
			} else if (key.equals(KEY_ETALON_MAX_DEVIATION)) {
				return ev.getEtalonMaxDeviation();
			} else if (key.equals(KEY_ETALON_MEAN_DEVIATION)) {
				return ev.getEtalonMeanDeviation();
			} else if (key.equals(KEY_LOSS_DIFFERENCE)) {
				return ev.getLossDifference();
			} else if (key.equals(KEY_LOCATION_DIFFERENCE)) {
				return ev.getLocationDifference();
			} else if (key.equals(KEY_LENGTH_DIFFERENCE)) {
				return ev.getLengthDifference();
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
		if (object instanceof DetailedEventResource) {
			DetailedEventResource ev = (DetailedEventResource) object;
			
			if (key.equals(KEY_N)) {
				ev.setNumber((String)value);
			} else if (key.equals(KEY_TYPE)) {
				ev.setType((String)value);
			} else if (key.equals(KEY_DISTANCE)) {
				ev.setLocation((String)value);
			} else if (key.equals(KEY_LENGTH)) {
				ev.setLength((String)value);
			} else if (key.equals(KEY_REFLECTANCE)) {
				ev.setReflectance((String)value);
			} else if (key.equals(KEY_LOSS)) {
				ev.setLoss((String)value);
			} else if (key.equals(KEY_ATTENUATION)) {
				ev.setAttenuation((String)value);
			} else if (key.equals(KEY_START_LEVEL)) {
				ev.setStartLevel((String)value);
			} else if (key.equals(KEY_END_LEVEL)) {
				ev.setEndLevel((String)value);
			} else if (key.equals(KEY_MEAN_DEVIATION)) {
				ev.setMeanDeviation((String)value);
			} else if (key.equals(KEY_MAXDEVIATION)) {
				ev.setMaxDeviation((String)value);
			} else if (key.equals(KEY_REFLECTION_LEVEL)) {
				ev.setReflectionLevel((String)value);
			} else if (key.equals(KEY_EDZ)) {
				ev.setEdz((String)value);
			} else if (key.equals(KEY_ADZ)) {
				ev.setAdz((String)value);
			}	else if (key.equals(KEY_MAX_LEVEL)) {
				ev.setMaxLevel((String)value);
			} else if (key.equals(KEY_MIN_LEVEL)) {
				ev.setMinLevel((String)value);
			}	else if (key.equals(KEY_EXTENSION)) {
				ev.setExtension((String)value);
			}	else if (key.equals(KEY_ETALON_TYPE)) {
				ev.setEtalonType((String)value);
			} else if (key.equals(KEY_ETALON_MAX_DEVIATION)) {
				ev.setEtalonMaxDeviation((String)value);
			} else if (key.equals(KEY_ETALON_MEAN_DEVIATION)) {
				ev.setEtalonMeanDeviation((String)value);
			} else if (key.equals(KEY_LOSS_DIFFERENCE)) {
				ev.setLossDifference((String)value);
			} else if (key.equals(KEY_LOCATION_DIFFERENCE)) {
				ev.setLocationDifference((String)value);
			} else if (key.equals(KEY_LENGTH_DIFFERENCE)) {
				ev.setLengthDifference((String)value);
			}
		}
	}
}
