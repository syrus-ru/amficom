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

	private static final String			BUNDLE_NAME			= "survey";
	private static final String			OLDBUNDLE_NAME		= "oldurveykey";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);
	private static final ResourceBundle	OLDRESOURCE_BUNDLE	= ResourceBundle
																	.getBundle(OLDBUNDLE_NAME);

	public static String getString(String keyName) {
		System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			String key = null;
			string = "!" + keyName + "!";
			try {
				key = OLDRESOURCE_BUNDLE.getString(keyName);
			} catch (MissingResourceException ex) {
				//
			}
			try {
				String s = "key '"
						+ keyName
						+ "' not found"
						+ (key == null ? "" : " , but old key '" + key
								+ "' found.");
				throw new Exception(s);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			if (key != null) string = I18N.getString(key);

		}
		return string;
	}
}