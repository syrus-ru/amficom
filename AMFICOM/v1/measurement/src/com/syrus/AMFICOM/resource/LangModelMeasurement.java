/*
 * LangModelMeasurement.java
 * Created on 25.06.2004 10:15:19
 * 
 */
package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * @author Vladimir Dolzhenko
 */
public class LangModelMeasurement {
	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.resource.measurement";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);
	
	private LangModelMeasurement(){
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
