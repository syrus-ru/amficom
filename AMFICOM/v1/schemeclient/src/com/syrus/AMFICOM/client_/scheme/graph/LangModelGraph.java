/*-
 * $Id: LangModelGraph.java,v 1.2 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class LangModelGraph {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.client_.scheme.graph.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private LangModelGraph() {
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
