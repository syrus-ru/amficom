package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelReport {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.report";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	private LangModelReport() {
		//symbols = new DateFormatSymbols(locale);
	}

	public static String getString(String keyName) {
		//System.out.println("keyName:" + keyName);
		keyName = keyName.replaceAll(" ", "_");
		String string = null;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {			

			try {
				throw new Exception("key '"
									+ keyName
									+ "' "
									+  "not found");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}
}