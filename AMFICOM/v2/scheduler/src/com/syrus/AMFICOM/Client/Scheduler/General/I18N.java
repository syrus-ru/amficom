package com.syrus.AMFICOM.Client.Scheduler.General;

import java.util.*;

public class I18N {

	private static final String			BUNDLE_NAME		= "scheduler";

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