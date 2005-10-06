/*-
* $Id: I18N.java,v 1.3 2005/10/06 14:38:32 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.resource;

import javax.swing.UIManager;

import com.syrus.util.Log;


/**
 * Internationalization
 * @version $Revision: 1.3 $, $Date: 2005/10/06 14:38:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class I18N {

	public static final String	RESOURCE_BUNDLE_KEY	= "ResourceBundle";
	
	/**
	 * Register resource bundle
	 * @param bundleName
	 */
	public static void addResourceBundle(final String bundleName) {
		if (bundleName != null) {
			assert Log.debugMessage("I18N.addResourceBundle | bundleName:" + bundleName, Log.DEBUGLEVEL10);
			UIManager.getDefaults().addResourceBundle(bundleName);
		}
	}
	
	/**
	 * @param key
	 * @return i18n string
	 */
	public static String getString(final String key) {
		final String string = UIManager.getString(key);
		assert Log.debugMessage("I18N.getString | key:'" 
				+ key 
				+ "', value:'"
				+ string 
				+ '\'', 
			Log.DEBUGLEVEL10);
		return string;
	}

}

