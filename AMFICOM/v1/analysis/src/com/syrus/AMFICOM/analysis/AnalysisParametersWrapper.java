/*-
 * $Id: AnalysisParametersWrapper.java,v 1.2 2005/06/03 10:43:53 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.util.Wrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/06/03 10:43:53 $
 * @module analysis_v1
 */

public class AnalysisParametersWrapper implements Wrapper {
	
	public static final String KEY_MIN_THRESHOLD = "analysisMinEvent";
	public static final String KEY_MIN_SPLICE = "analysisMinWeld";
	public static final String KEY_MIN_CONNECTOR = "analysisMinConnector";
	public static final String KEY_MIN_END = "analysisMinEnd";
	public static final String KEY_NOISE_FACTOR = "analysisNoiseFactor";
	
	private static AnalysisParametersWrapper	instance;
	private static Map noiseFactors = new HashMap();
	static {
		double[] nf = AnalysisParameters.getRecommendedNoiseFactors();
		for (int i = 0; i < nf.length; i++) {
			Double d = new Double(nf[i]);
			noiseFactors.put(d, d);
		}
	}

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private AnalysisParametersWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_MIN_CONNECTOR, KEY_MIN_END,
				KEY_MIN_SPLICE, KEY_MIN_THRESHOLD, KEY_NOISE_FACTOR };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static AnalysisParametersWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisParametersWrapper();
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
			return Double.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		if (key.equals(KEY_NOISE_FACTOR)) {
			return noiseFactors;
		}
		return null;
	}

	public Object getValue(	Object object, String key) {
		
		if (object instanceof AnalysisParameters) {
			AnalysisParameters params = (AnalysisParameters) object;
			
			if (key.equals(KEY_MIN_CONNECTOR)) {
				return new Double(params.getMinConnector());
			} else if (key.equals(KEY_MIN_END)) {
				return new Double(params.getMinEnd());
			} else if (key.equals(KEY_MIN_SPLICE)) {
				return new Double(params.getMinSplice());
			} else if (key.equals(KEY_MIN_THRESHOLD)) {
				return new Double(params.getMinThreshold());
			} else if (key.equals(KEY_NOISE_FACTOR)) {
				return new Double(params.getNoiseFactor());
			} 
		}
		return null;
	}
	
	public boolean isEditable(String key) {
		return true;
	}

	public void setPropertyValue(	String key,
									Object objectKey,
									Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof AnalysisParameters) {
			AnalysisParameters params = (AnalysisParameters) object;
			
			try {
				if (key.equals(KEY_MIN_CONNECTOR)) {
					params.setMinConnector(Double.parseDouble((String)value));
				} else if (key.equals(KEY_MIN_END)) {
					params.setMinEnd(Double.parseDouble((String)value));
				} else if (key.equals(KEY_MIN_SPLICE)) {
					params.setMinSplice(Double.parseDouble((String)value));
				} else if (key.equals(KEY_MIN_THRESHOLD)) {
					params.setMinThreshold(Double.parseDouble((String)value));
				} else if (key.equals(KEY_NOISE_FACTOR)) {
					params.setNoiseFactor(((Double)value).doubleValue());
				}
			} catch (NumberFormatException e) {
				//TODO make double editor
				// ignore
			} 
		}

	}
}
