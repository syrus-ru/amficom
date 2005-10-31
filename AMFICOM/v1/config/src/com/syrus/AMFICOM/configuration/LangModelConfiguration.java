/*-
 * $Id: LangModelConfiguration.java,v 1.3 2005/10/31 12:29:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/10/31 12:29:56 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
final class LangModelConfiguration {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.configuration.configuration";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelConfiguration() {
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
