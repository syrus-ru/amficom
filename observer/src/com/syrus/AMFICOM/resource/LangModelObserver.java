/*-
 * $Id: LangModelObserver.java,v 1.1 2005/09/11 17:39:24 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/09/11 17:39:24 $
 * @module surveyclient_v1
 */

public class LangModelObserver {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.observer";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private LangModelObserver() {
		//empty
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
