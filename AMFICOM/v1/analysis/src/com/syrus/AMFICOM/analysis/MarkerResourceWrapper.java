/*-
 * $Id: MarkerResourceWrapper.java,v 1.1 2005/06/02 12:53:29 stas Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/06/02 12:53:29 $
 * @module analysis_v1
 */

public class MarkerResourceWrapper implements Wrapper {
	
	public static final String KEY_A_POSITION = "markerAPos";
	public static final String KEY_A_LOSS = "markerALoss";
	public static final String KEY_A_REFLECTION = "markerAReflection";
	public static final String KEY_A_ATTENUATION = "markerAAttenuation";
	public static final String KEY_A_CUMULATIVE_LOSS = "markerACumloss";
	public static final String KEY_B_POSITION = "markerBPos";
	public static final String KEY_BA_DISTANCE = "markerABdist";
	public static final String KEY_AB_DISTANCE = "markerBAdist";
	public static final String KEY_AB_LOSS = "markerABloss";
	public static final String KEY_AB_ATTENUATION = "markerABatt";
	public static final String KEY_AB_LSA_ATTENUATION = "markerABlsaatt";
	public static final String KEY_AB_ORL = "markerABorl";
	
		private static MarkerResourceWrapper	instance;

	private List						keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private MarkerResourceWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_A_POSITION, KEY_A_LOSS,
				KEY_A_ATTENUATION, KEY_A_REFLECTION, KEY_A_CUMULATIVE_LOSS,
				KEY_B_POSITION, KEY_BA_DISTANCE, KEY_AB_DISTANCE, KEY_AB_LOSS,
				KEY_AB_ATTENUATION, KEY_AB_LSA_ATTENUATION, KEY_AB_ORL };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MarkerResourceWrapper getInstance() {
		if (instance == null)
			instance = new MarkerResourceWrapper();
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
		
		if (object instanceof MarkerResource) {
			MarkerResource res = (MarkerResource) object;
			
			if (key.equals(KEY_A_POSITION)) {
				return res.getAPosition();
			} else if (key.equals(KEY_A_LOSS)) {
				return res.getALoss();
			} else if (key.equals(KEY_A_REFLECTION)) {
				return res.getAReflectance();
			} else if (key.equals(KEY_A_ATTENUATION)) {
				return res.getAAttenuation();
			} else if (key.equals(KEY_A_CUMULATIVE_LOSS)) {
				return res.getACumulativeLoss();
			} else if (key.equals(KEY_B_POSITION)) {
				return res.getBPosition();
			} else if (key.equals(KEY_BA_DISTANCE)) {
				return res.getAbDistance();
			} else if (key.equals(KEY_AB_DISTANCE)) {
				return res.getAbDistance();
			} else if (key.equals(KEY_AB_LOSS)) {
				return res.getAbLoss();
			} else if (key.equals(KEY_AB_ATTENUATION)) {
				return res.getAbAttenuation();
			} else if (key.equals(KEY_AB_LSA_ATTENUATION)) {
				return res.getLsaAttenuation();
			} else if (key.equals(KEY_AB_ORL)) {
				return res.getAbOrl();
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
		if (object instanceof MarkerResource) {
			MarkerResource res = (MarkerResource) object;
		}
	}

}
