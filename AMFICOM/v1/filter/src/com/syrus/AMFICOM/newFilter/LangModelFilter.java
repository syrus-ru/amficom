/*-
 * $Id: LangModelFilter.java,v 1.1 2005/09/21 13:06:24 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.syrus.util.Log;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/09/21 13:06:24 $
 * @module filter
 */
public class LangModelFilter {
	
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.newFilter.filter";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelFilter() {
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
