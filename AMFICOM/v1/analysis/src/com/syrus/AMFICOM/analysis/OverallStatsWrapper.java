/*-
 * $Id: OverallStatsWrapper.java,v 1.7 2005/10/20 09:17:40 saa Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.util.Wrapper;

/**
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/10/20 09:17:40 $
 * @module analysis
 */

public class OverallStatsWrapper implements Wrapper<OverallStats> {
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
	public static final String KEY_MISMATCH = "mismatch";
	public static final String KEY_QUALITY_Q = "qualityQ";
	public static final String KEY_QUALITY_D = "qualityD";

	private static OverallStatsWrapper instance;

	private List<String> keys;

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	private OverallStatsWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { KEY_LENGTH,
				KEY_LOSS,
				KEY_ATTENUATION,
				KEY_RETURN_LOSS,
				KEY_NOISE_LEVEL,
				KEY_NOISE_DD,
				KEY_NOISE_DDRMS,
				KEY_EVENTS,
				KEY_ETALON_LENGTH,
				KEY_MAX_DEVIATION,
				KEY_MEAN_DEVIATION,
				KEY_D_LOSS,
				KEY_MISMATCH,
				KEY_QUALITY_Q,
				KEY_QUALITY_D};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static OverallStatsWrapper getInstance() {
		if (instance == null) {
			instance = new OverallStatsWrapper();
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

	public Class getPropertyClass(final String key) {
		if (this.keys.contains(key)) {
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(final OverallStats stats, final String key) {
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
		} else if (key.equals(KEY_EVENTS)) {
			return stats.getTotalEvents();
		} else if (key.equals(KEY_ETALON_LENGTH)) {
			return stats.getEtalonLength();
		} else if (key.equals(KEY_MAX_DEVIATION)) {
			return stats.getMaxDeviation();
		} else if (key.equals(KEY_MEAN_DEVIATION)) {
			return stats.getMeanDeviation();
		} else if (key.equals(KEY_D_LOSS)) {
			return stats.getDLoss();
		} else if (key.equals(KEY_MEAN_DEVIATION)) {
			return stats.getMeanDeviation();
		} else if (key.equals(KEY_MISMATCH)) {
			return stats.getMismatch();
		} else if (key.equals(KEY_QUALITY_Q)) {
			return stats.getQualityQ();
		} else if (key.equals(KEY_QUALITY_D)) {
			return stats.getQualityD();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// TODO Auto-generated method stub
	}

	public void setValue(final OverallStats stats, final String key, final Object value) {
		if (key.equals(KEY_LENGTH)) {
			stats.setTotalLength((String) value);
		} else if (key.equals(KEY_LOSS)) {
			stats.setTotalLoss((String) value);
		} else if (key.equals(KEY_ATTENUATION)) {
			stats.setTotalAttenuation((String) value);
		} else if (key.equals(KEY_RETURN_LOSS)) {
			stats.setTotalReturnLoss((String) value);
		} else if (key.equals(KEY_NOISE_LEVEL)) {
			stats.setTotalNoiseLevel((String) value);
		} else if (key.equals(KEY_NOISE_DD)) {
			stats.setTotalNoiseDD((String) value);
		} else if (key.equals(KEY_EVENTS)) {
			stats.setTotalEvents((String) value);
		}
	}
}
