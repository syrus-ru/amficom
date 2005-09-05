package com.syrus.AMFICOM.Client.General.lang;

import java.util.*;

public class LangModelSchedule {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.lang.scheduler";

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
			
			try {
				throw new Exception(_keyName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			string = "!" + _keyName + "!";

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
//			if (key != null) string = LangModelSurvey.getString(key);

		}
		return string;
	}

}

