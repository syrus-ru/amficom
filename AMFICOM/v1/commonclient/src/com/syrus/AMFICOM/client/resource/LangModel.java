
package com.syrus.AMFICOM.client.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class LangModel {

	private static final String			BUNDLE_NAME		= "com.syrus.AMFICOM.client.resource.generalclient";

	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModel() {
		// singleton
		assert false;
	}

	public static String getString(String keyName) {
		String keyName1 = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName1);
		} catch (MissingResourceException e) {
			try {
				string = RESOURCE_BUNDLE.getString(keyName1 + "Text");
			} catch (MissingResourceException ex) {
				try {
					throw new Exception("key '" + keyName1 + "' not found");
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
				string = keyName;
			}
		}
		return string;
	}

}
