package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelSurvey {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.survey";
	private static final String			OLDBUNDLE_NAME		= "com.syrus.AMFICOM.Client.General.Lang.oldsurveykey";
	private static final ResourceBundle	OLDRESOURCE_BUNDLE	= ResourceBundle.getBundle(OLDBUNDLE_NAME);

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelSurvey() {
		// private constuctor consider to skeleton
	}

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
				String s = "key '" + keyName + "' "
						+ (key == null ? "not found" : " is deprecated , use '" + key + "' key.");
				throw new Exception(s);
			} catch (Exception exc) {
				System.out.println(exc.getMessage());
				//				exc.printStackTrace();
			}
			if (key != null)
				string = LangModelSurvey.getString(key);

		}
		return string;
	}

}

