/*-
 * $Id: LangModelGeneral.java,v 1.3 2005/09/06 11:34:58 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/09/06 11:34:58 $
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
		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (final MissingResourceException mre) {
			Log.errorMessage("Key '" + key + "' not found in resource bundle '" + BUNDLE_NAME + "'");
			return "!" + key + "!";
		}
	}
}
