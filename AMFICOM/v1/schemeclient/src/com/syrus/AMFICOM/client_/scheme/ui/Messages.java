/*
 * $Id: Messages.java,v 1.1 2005/03/17 14:45:36 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:45:36 $
 * @module schemeclient_v1
 */

public class Messages {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.client_.scheme.ui.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
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
