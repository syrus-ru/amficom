/*
 * $Id: LangModelManager.java,v 1.1 2005/07/14 13:16:18 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.manager;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @version $ $, $Date: 2005/07/14 13:16:18 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class LangModelManager {
	private static final String BUNDLE_NAME			= "com.syrus.AMFICOM.manager.resources.manager";

	private static final ResourceBundle	RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelManager(){
		//nothing
	}

	public static String getString(String keyName) {
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			string = "!" + keyName + "!";
			try {
				String s = "key '"
						+ keyName
						+ "' "
						+ "not found";
				throw new Exception(s);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}

}
