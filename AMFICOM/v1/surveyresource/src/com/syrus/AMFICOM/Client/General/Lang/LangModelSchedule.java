package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelSchedule {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.scheduler";

	private static final String			OLDBUNDLE_NAME		= "com.syrus.AMFICOM.Client.General.Lang.oldschedulerkey";
	private static final ResourceBundle	OLDRESOURCE_BUNDLE	= ResourceBundle
																	.getBundle(OLDBUNDLE_NAME);

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);
	
	private LangModelSchedule() {
		//		 private constuctor consider to skeleton
	}

	public static String getString(final String keyName) {		
		String _keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(_keyName);
		} catch (MissingResourceException e) {
			String key = null;
			string = "!" + _keyName + "!";
			try {
				key = OLDRESOURCE_BUNDLE.getString(_keyName);
				key = key.replaceAll("\\s+", "");
			} catch (MissingResourceException ex) {
				// nothing
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(_keyName + "Text");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}
			if (key == null) {
				try {
					key = OLDRESOURCE_BUNDLE.getString(_keyName + "ToolTip");
					key = key.replaceAll("\\s+", "");
				} catch (MissingResourceException ex) {
					// nothing
				}
			}

			try {
				String s = "key '"
						+ _keyName
						+ "' "
						+ (key == null ? "not found" : " is deprecated , use '"
								+ key + "' key.");
				throw new Exception(s);
			} catch (Exception exc) {
				System.out.println(exc.getMessage());
//				exc.printStackTrace();
			}
			if (key != null) string = LangModelSurvey.getString(key);

		}
		return string;
	}

}

