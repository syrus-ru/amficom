/*-
 * $Id: I18N.java,v 1.1 2006/06/23 13:37:05 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1 $, $Date: 2006/06/23 13:37:05 $
 * @module systemserver
 */
final class I18N {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.systemserver.systemserver";
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
