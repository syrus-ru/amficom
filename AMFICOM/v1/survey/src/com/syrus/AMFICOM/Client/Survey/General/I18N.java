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

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.Survey.General.survey";
	private static final String			OLDBUNDLE_NAME		= "com.syrus.AMFICOM.Client.Survey.General.oldsurveykey";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);
	private static final ResourceBundle	OLDRESOURCE_BUNDLE	= ResourceBundle
																	.getBundle(OLDBUNDLE_NAME);

	public static String getString(String keyName) {
		//System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			String key = null;
			string = "!" + keyName + "!";
			try {
				key = OLDRESOURCE_BUNDLE.getString(keyName);
				key = key.replaceAll("\\s+", "");
			} catch (MissingResourceException ex) {
				// nothing
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(keyName + "Text");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(keyName + "ToolTip");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}

			try {
				String s = "key '"
						+ keyName
						+ "' "
						+ (key == null ? "not found"
								: " is deprecated , use '" + key
										+ "' key.");
				throw new Exception(s);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			if (key != null) string = I18N.getString(key);

		}
		return string;
	}
}