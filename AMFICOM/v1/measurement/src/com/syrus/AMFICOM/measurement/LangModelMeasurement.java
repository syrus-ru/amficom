/*
 * $Id: LangModelMeasurement.java,v 1.3 2005/10/31 12:30:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @version $ $, $Date: 2005/10/31 12:30:15 $
 * @author $Author: bass $
 * @module measurement
 */

final class LangModelMeasurement {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.measurement.measurement";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMeasurement() {
		// nothing
	}

	public static String getString(final String key) {
		final String validKey = key.replaceAll(" ", "_");
		try {
			return RESOURCE_BUNDLE.getString(validKey);
		}
		catch (MissingResourceException mre) {
			Log.errorMessage("Key '" + validKey + "' not found in resource bundle '" + RESOURCE_BUNDLE + "'");
			return "!" + validKey + "!";
		}
	}
}
