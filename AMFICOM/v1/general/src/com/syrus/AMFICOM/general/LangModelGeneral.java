/*-
 * $Id: LangModelGeneral.java,v 1.2 2005/09/06 07:03:43 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/06 07:03:43 $
 * @author $Author: bob $
 * @module general
 */
final class LangModelGeneral {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.general.general";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelGeneral() {
		// singleton
		assert false;
	}

	public static String getString(final String key) {
		final String validKey = key.replaceAll(" ", "_");
		try {
			return RESOURCE_BUNDLE.getString(validKey);
		}
		catch (final MissingResourceException mre) {
			Log.errorMessage("Key '" + validKey + "' not found in resource bundle '" + BUNDLE_NAME + "'");
			return "!" + validKey + "!";
		}
	}
}
