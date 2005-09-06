/*-
 * $Id: LangModelSchedule.java,v 1.4 2005/09/06 07:44:12 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.General.lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/06 07:44:12 $
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
		} catch (MissingResourceException e) {
			try {
				throw new Exception(keyName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			string = "!" + keyName + "!";

		}
		return string;
	}

}

