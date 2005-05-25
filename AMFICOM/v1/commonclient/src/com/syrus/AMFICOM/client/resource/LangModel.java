
package com.syrus.AMFICOM.client.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @deprecated use {@link com.syrus.AMFICOM.client.resource.LangModelGeneral}
 * @version $Revision: 1.2 $, $Date: 2005/05/25 07:53:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
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
