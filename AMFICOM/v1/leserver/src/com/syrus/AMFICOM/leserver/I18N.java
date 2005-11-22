/*-
 * $Id: I18N.java,v 1.1 2005/11/22 19:33:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/22 19:33:13 $
 * @module leserver
 */
final class I18N {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.leserver.leserver";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private I18N() {
		assert false;
	}

	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException mre) {
			Log.errorMessage("Key: '" + key + "' is not found in resource bundle '" + BUNDLE_NAME + '\'');
			return '!' + key + '!';
		}
	}
}
