/*-
 * $Id: I18N.java,v 1.1 2005/10/31 07:17:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/31 07:17:31 $
 * @module reflectometry
 */
final class I18N {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.reflectometry.reflectometry";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private I18N() {
		assert false;
	}

	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException mre) {
			assert Log.errorMessage("Key: '" + key + "' is not found in resource bundle '" + BUNDLE_NAME + '\'');
			return '!' + key + '!';
		}
	}
}
