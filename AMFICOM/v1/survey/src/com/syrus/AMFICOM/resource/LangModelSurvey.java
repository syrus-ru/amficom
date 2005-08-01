/*-
 * $Id: LangModelSurvey.java,v 1.1 2005/08/01 08:34:15 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/08/01 08:34:15 $
 * @module surveyclient_v1
 */

public class LangModelSurvey {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.survey_messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private LangModelSurvey() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
