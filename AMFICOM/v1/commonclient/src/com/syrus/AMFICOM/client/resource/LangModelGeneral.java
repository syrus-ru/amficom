/*-
 * $Id: LangModelGeneral.java,v 1.2 2005/06/06 14:52:47 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/06/06 14:52:47 $
 * @module commonclient_v1
 */

public class LangModelGeneral {
	private static final String BUNDLE_NAME			= "com.syrus.AMFICOM.client.resource.general";

	private static final ResourceBundle	RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelGeneral(){
		//nothing
	}

	public static String getString(String keyName) {
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		}
		catch (MissingResourceException e) {
			string = "!" + keyName + "!";
			try {
				String s = "key '"
						+ keyName
						+ "' "
						+ "not found";
				throw new Exception(s);
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}
}

