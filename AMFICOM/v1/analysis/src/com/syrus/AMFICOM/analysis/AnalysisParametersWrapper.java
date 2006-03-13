/*-
 * $Id: AnalysisParametersWrapper.java,v 1.10 2006/03/13 15:54:24 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.InvalidAnalysisParametersException;
import com.syrus.util.Wrapper;

/**
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/03/13 15:54:24 $
 * @module analysis
 */

public class AnalysisParametersWrapper implements Wrapper<AnalysisParameters> {

	// public static final String KEY_MIN_THRESHOLD = "analysisMinEvent";
	// public static final String KEY_MIN_SPLICE = "analysisMinWeld";
	public static final String KEY_SENSITIVITY = "analysisSensitivity";
	public static final String KEY_MIN_CONNECTOR = "analysisMinConnector";
	public static final String KEY_MIN_END = "analysisMinEnd";
	public static final String KEY_NOISE_FACTOR = "analysisNoiseFactor";
	public static final String KEY_EOT_LEVEL = "analysisEotLevel";

	private static AnalysisParametersWrapper instance;
	private static Map<Double, Double> noiseFactors = new HashMap<Double, Double>();
	static {
		final double[] nf = AnalysisParameters.getRecommendedNoiseFactors();
		for (int i = 0; i < nf.length; i++) {
			final Double d = new Double(nf[i]);
			noiseFactors.put(d, d);
		}
	}

	private List<String> keys;

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	private AnalysisParametersWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] {
				KEY_MIN_CONNECTOR,
				KEY_MIN_END,
				KEY_SENSITIVITY,
				KEY_NOISE_FACTOR,
				KEY_EOT_LEVEL };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static AnalysisParametersWrapper getInstance() {
		if (instance == null) {
			instance = new AnalysisParametersWrapper();
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
		if (this.keys.contains(key)) {
			return Double.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		if (key.equals(KEY_NOISE_FACTOR)) {
			return noiseFactors;
		}
		return null;
	}

	public Object getValue(AnalysisParameters params, String key) {
		if (key.equals(KEY_MIN_CONNECTOR)) {
			return new Double(params.getConnectorTh());
		} else if (key.equals(KEY_MIN_END)) {
			return new Double(params.getEndTh());
		} else if (key.equals(KEY_SENSITIVITY)) {
			return new Double(params.getSentitivity());
		} else if (key.equals(KEY_NOISE_FACTOR)) {
			return new Double(params.getNoiseFactor());
		} else if (key.equals(KEY_EOT_LEVEL)) {
			return new Double(params.getLevelEot());
		}
		return null;
	}
	
	public boolean isEditable(final String key) {
		return true;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// TODO Auto-generated method stub
	}

	public void setValue(final AnalysisParameters params, final String key, final Object value) {
		try {
			if (key.equals(KEY_MIN_CONNECTOR)) {
				params.setConnectorTh(Double.parseDouble((String) value));
			} else if (key.equals(KEY_MIN_END)) {
				params.setEndTh(Double.parseDouble((String) value));
			} else if (key.equals(KEY_SENSITIVITY)) {
				params.setSensitivity(Double.parseDouble((String) value));
			} else if (key.equals(KEY_NOISE_FACTOR)) {
				params.setNoiseFactor(((Double) value).doubleValue());
			} else if (key.equals(KEY_EOT_LEVEL)) {
				params.setLevelEot(Double.parseDouble((String) value));
			}
			Heap.notifyAnalysisParametersUpdated();
		} catch (NumberFormatException e) {
			//TODO make double editor
			// ignore
		} catch (InvalidAnalysisParametersException e) {
			GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_INVALID_AP);
		}
	}
}
