/*
 * $Id: RendererMode.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

public class RendererMode {
	public enum MODE {CREATE_LABEL,CREATE_IMAGE,ATTACH_LABEL,NO_SPECIAL};
	
	private static MODE commands_processing_mode = MODE.NO_SPECIAL;
	
	public static MODE getMode() {
		return commands_processing_mode;
	}

	public static void setMode(MODE mode) {
		commands_processing_mode = mode;
	}
}
