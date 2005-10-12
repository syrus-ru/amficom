/*
 * $Id: ModuleMode.java,v 1.2 2005/10/12 13:29:11 peskovsky Exp $
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
