/*
 * I18N.java
 * Created on 03.06.2004 18:07:38
 * 
 */
package com.syrus.AMFICOM.Client.Survey.General;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Vladimir Dolzhenko
 * 
 * support Internationalization (I18N)
 */
public class I18N {

	private static final String			BUNDLE_NAME		= "survey";

	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle
																.getBundle(BUNDLE_NAME);

	public static String getString(String keyName) {
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			string = "!" + keyName + "!";
			try {
				throw new Exception("key '" + keyName + "' not found");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}
}