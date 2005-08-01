/*
 * $Id: LangModelScheme.java,v 1.3 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class LangModelScheme {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.scheme_messages";//$NON-NLS-1$

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
