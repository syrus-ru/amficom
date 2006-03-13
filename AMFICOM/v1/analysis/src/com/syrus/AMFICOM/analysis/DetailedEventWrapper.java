/*-
 * $Id: DetailedEventWrapper.java,v 1.9 2006/03/13 15:54:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.util.Wrapper;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2006/03/13 15:54:24 $
 * @module analysis
 */

public class DetailedEventWrapper implements Wrapper<DetailedEventResource> {
	
	public static final String KEY_N = "eventNum";
	public static final String KEY_IMAGE = "eventImage";
	public static final String KEY_TYPE_GENERAL = "eventTypeGeneral";
	public static final String KEY_TYPE_DETAILED = "eventTypeDetailed";
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
	
	public static final String KEY_ETALON_TYPE_GENERAL = "etEventTypeGeneral";
	public static final String KEY_ETALON_MAX_DEVIATION = "maxDeviation";
	public static final String KEY_ETALON_MEAN_DEVIATION = "meanDeviation";
	public static final String KEY_LOSS_DIFFERENCE = "dLoss";
	public static final String KEY_LOCATION_DIFFERENCE = "dLocation";
	public static final String KEY_LENGTH_DIFFERENCE = "dWidth";

	public static final String KEY_QUALITY_QI = "qi";
	public static final String KEY_QUALITY_KI = "ki";

	public static final String KEY_ANCHOR_IMAGE = "anchorImage";
	public static final String KEY_ANCHORED = "anchor";

	private static DetailedEventWrapper instance;

	private List<String> keys;

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	private DetailedEventWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { KEY_N,
				KEY_TYPE_GENERAL, KEY_TYPE_DETAILED, KEY_DISTANCE,
				KEY_LENGTH, KEY_REFLECTANCE, KEY_LOSS, KEY_ATTENUATION,
				KEY_START_LEVEL, KEY_END_LEVEL, KEY_MEAN_DEVIATION, KEY_MAXDEVIATION,
				KEY_REFLECTION_LEVEL, KEY_ADZ, KEY_EDZ, KEY_MAX_LEVEL, KEY_MIN_LEVEL,
				KEY_EXTENSION, KEY_ETALON_TYPE_GENERAL, KEY_ETALON_MAX_DEVIATION,
				KEY_ETALON_MEAN_DEVIATION, KEY_LOSS_DIFFERENCE,
				KEY_LOCATION_DIFFERENCE, KEY_LENGTH_DIFFERENCE, KEY_IMAGE,
				KEY_QUALITY_QI, KEY_QUALITY_KI, KEY_ANCHORED, KEY_ANCHOR_IMAGE
		};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static synchronized DetailedEventWrapper getInstance() {
		if (instance == null) {
			instance = new DetailedEventWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (this.keys.contains(key)) {
			return LangModelAnalyse.getString(key);
		}
		return key;
	}

	public Class<?> getPropertyClass(final String key) {
		if (key.equals(KEY_IMAGE) || key.equals(KEY_ANCHOR_IMAGE)) {
			return Icon.class;
		} else if (this.keys.contains(key)) {
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(final DetailedEventResource ev, final String key) {
		if (key.equals(KEY_N)) {
			return ev.getNumber();
		}
		if (key.equals(KEY_IMAGE)) {
			return ev.getImage();
		} else if (key.equals(KEY_TYPE_GENERAL)) {
			return ev.getTypeGeneral();
		} else if (key.equals(KEY_TYPE_DETAILED)) {
			return ev.getTypeDetailed();
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
		} else if (key.equals(KEY_ETALON_TYPE_GENERAL)) {
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
		} else if (key.equals(KEY_QUALITY_QI)) {
			return ev.getQi();
		} else if (key.equals(KEY_QUALITY_KI)) {
			return ev.getKi();
		} else if (key.equals(KEY_ANCHORED)) {
			return Boolean.valueOf(ev.isAnchored());
		} else if (key.equals(KEY_ANCHOR_IMAGE)) {
			return ev.getAnchorImage();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(final DetailedEventResource ev, final String key, final Object value) {
		if (key.equals(KEY_N)) {
			ev.setNumber((String) value);
		} else if (key.equals(KEY_TYPE_GENERAL)) {
			ev.setTypeGeneral((String) value);
		} else if (key.equals(KEY_TYPE_DETAILED)) {
			ev.setTypeDetailed((String) value);
		} else if (key.equals(KEY_DISTANCE)) {
			ev.setLocation((String) value);
		} else if (key.equals(KEY_LENGTH)) {
			ev.setLength((String) value);
		} else if (key.equals(KEY_REFLECTANCE)) {
			ev.setReflectance((String) value);
		} else if (key.equals(KEY_LOSS)) {
			ev.setLoss((String) value);
		} else if (key.equals(KEY_ATTENUATION)) {
			ev.setAttenuation((String) value);
		} else if (key.equals(KEY_START_LEVEL)) {
			ev.setStartLevel((String) value);
		} else if (key.equals(KEY_END_LEVEL)) {
			ev.setEndLevel((String) value);
		} else if (key.equals(KEY_MEAN_DEVIATION)) {
			ev.setMeanDeviation((String) value);
		} else if (key.equals(KEY_MAXDEVIATION)) {
			ev.setMaxDeviation((String) value);
		} else if (key.equals(KEY_REFLECTION_LEVEL)) {
			ev.setReflectionLevel((String) value);
		} else if (key.equals(KEY_EDZ)) {
			ev.setEdz((String) value);
		} else if (key.equals(KEY_ADZ)) {
			ev.setAdz((String) value);
		} else if (key.equals(KEY_MAX_LEVEL)) {
			ev.setMaxLevel((String) value);
		} else if (key.equals(KEY_MIN_LEVEL)) {
			ev.setMinLevel((String) value);
		} else if (key.equals(KEY_EXTENSION)) {
			ev.setExtension((String) value);
		} else if (key.equals(KEY_ETALON_TYPE_GENERAL)) {
			ev.setEtalonType((String) value);
		} else if (key.equals(KEY_ETALON_MAX_DEVIATION)) {
			ev.setEtalonMaxDeviation((String) value);
		} else if (key.equals(KEY_ETALON_MEAN_DEVIATION)) {
			ev.setEtalonMeanDeviation((String) value);
		} else if (key.equals(KEY_LOSS_DIFFERENCE)) {
			ev.setLossDifference((String) value);
		} else if (key.equals(KEY_LOCATION_DIFFERENCE)) {
			ev.setLocationDifference((String) value);
		} else if (key.equals(KEY_LENGTH_DIFFERENCE)) {
			ev.setLengthDifference((String) value);
		} else if (key.equals(KEY_QUALITY_QI)) {
			ev.setQi((String) value);
		} else if (key.equals(KEY_QUALITY_KI)) {
			ev.setKi((String) value);
		} else if (key.equals(KEY_ANCHORED)) {
			ev.setAnchored(((Boolean)value).booleanValue());
		}
	}
}
