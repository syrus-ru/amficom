/*-
* $Id: I18N.java,v 1.1 2005/09/21 13:24:19 bob Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/09/21 13:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
final class I18N {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.general.csbridge";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private I18N() {
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

