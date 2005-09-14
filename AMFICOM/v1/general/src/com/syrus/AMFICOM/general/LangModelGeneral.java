/*-
 * $Id: LangModelGeneral.java,v 1.4 2005/09/14 18:51:56 arseniy Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
