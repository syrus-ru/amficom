/*-
 * $Id: LangModelSchedule.java,v 1.5 2005/09/14 17:39:22 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.General.lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 17:39:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class LangModelSchedule {

	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.lang.scheduler";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);
	
	private LangModelSchedule() {
		//		 private constuctor consider to skeleton
		assert false;
	}

	public static String getString(final String keyName) {		
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (final MissingResourceException e0) {
			try {
				throw new Exception(keyName);
			} catch (final Exception e1) {
				StackTraceElement[] stackTrace = e1.getStackTrace();
				System.err.println("key '" + keyName + "' not found : " + stackTrace[1]);
			}			
			string = "!" + keyName + "!";

		}
		return string;
	}

}

