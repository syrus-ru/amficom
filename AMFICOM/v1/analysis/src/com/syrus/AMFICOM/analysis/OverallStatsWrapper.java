/*-
 * $Id: OverallStatsWrapper.java,v 1.2 2005/06/02 12:53:29 stas Exp $
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

public class OverallStatsWrapper implements Wrapper {
	
	public static final String KEY_LENGTH = "totalLength";
	public static final String KEY_LOSS = "totalLoss";
	public static final String KEY_ATTENUATION = "totalAttenuation";
	public static final String KEY_RETURN_LOSS = "totalReturnLoss";
	public static final String KEY_NOISE_LEVEL = "totalNoiseLevel";
	public static final String KEY_NOISE_DD = "totalNoiseDD";
	public static final String KEY_NOISE_DDRMS = "totalNoiseDDRMS";
	public static final String KEY_EVENTS = "totalEvents";

	public static final String KEY_ETALON_LENGTH = "etLength";
	public static final String KEY_MAX_DEVIATION = "maxDeviation";
	public static final String KEY_MEAN_DEVIATION = "meanDeviation";
	public static final String KEY_D_LOSS = "dLoss";
	
	private static OverallStatsWrapper	instance;

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private OverallStatsWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_LENGTH, KEY_LOSS, KEY_ATTENUATION,
				KEY_RETURN_LOSS, KEY_NOISE_LEVEL, KEY_NOISE_DD, KEY_NOISE_DDRMS,
				KEY_EVENTS, KEY_ETALON_LENGTH, KEY_MAX_DEVIATION, KEY_MEAN_DEVIATION,
				KEY_D_LOSS };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static OverallStatsWrapper getInstance() {
		if (instance == null)
			instance = new OverallStatsWrapper();
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
		
		if (object instanceof OverallStats) {
			OverallStats stats = (OverallStats) object;
			
			if (key.equals(KEY_LENGTH)) {
				return stats.getTotalLength();
			} else if (key.equals(KEY_LOSS)) {
				return stats.getTotalLoss();
			} else if (key.equals(KEY_ATTENUATION)) {
				return stats.getTotalAttenuation();
			} else if (key.equals(KEY_RETURN_LOSS)) {
				return stats.getTotalReturnLoss();
			} else if (key.equals(KEY_NOISE_LEVEL)) {
				return stats.getTotalNoiseLevel();
			} else if (key.equals(KEY_NOISE_DD)) {
				return stats.getTotalNoiseDD();
			} else if (key.equals(KEY_NOISE_DDRMS)) {
				return stats.getTotalNoiseDDRMS();
			}	else if (key.equals(KEY_EVENTS)) {
				return stats.getTotalEvents();
			} else if (key.equals(KEY_ETALON_LENGTH)) {
				return stats.getEtalonLength();
			} else if (key.equals(KEY_MAX_DEVIATION)) {
				return stats.getMaxDeviation();
			} else if (key.equals(KEY_MEAN_DEVIATION)) {
				return stats.getMeanDeviation();
			} else if (key.equals(KEY_D_LOSS)) {
				return stats.getDLoss();
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
		if (object instanceof OverallStats) {
			OverallStats stats = (OverallStats) object;
			
			if (key.equals(KEY_LENGTH)) {
				stats.setTotalLength((String)value);
			} else if (key.equals(KEY_LOSS)) {
				stats.setTotalLoss((String)value);
			} else if (key.equals(KEY_ATTENUATION)) {
				stats.setTotalAttenuation((String)value);
			} else if (key.equals(KEY_RETURN_LOSS)) {
				stats.setTotalReturnLoss((String)value);
			} else if (key.equals(KEY_NOISE_LEVEL)) {
				stats.setTotalNoiseLevel((String)value);
			} else if (key.equals(KEY_NOISE_DD)) {
				stats.setTotalNoiseDD((String)value);
			} else if (key.equals(KEY_EVENTS)) {
				stats.setTotalEvents((String)value);
			}  
		}
	}
}
