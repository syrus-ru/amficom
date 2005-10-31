/*-
 * $Id: LangModelFilter.java,v 1.3 2005/10/31 12:30:03 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/31 12:30:03 $
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
