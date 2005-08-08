/*
 * $Id: LangModelScheme.java,v 1.5 2005/08/08 11:58:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:08 $
 * @module schemeclient
 */

public class LangModelScheme {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.SchemeMessages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private LangModelScheme() {
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
