/*
 * $Id: ModuleMode.java,v 1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;


public class ModuleMode {
	public enum MODULE_MODE {TEMPLATE_SCHEME,REPORT_PREVIEW}
	
	private static MODULE_MODE moduleMode = MODULE_MODE.TEMPLATE_SCHEME;
	
	public static MODULE_MODE getMode() {
		return moduleMode;
	}

	public static void setMode(MODULE_MODE mode) {
		moduleMode = mode;
	}
}
