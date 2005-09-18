/*
 * $Id: RendererMode.java,v 1.2 2005/09/18 13:13:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

public class RendererMode {
	public enum RENDERER_MODE {CREATE_LABEL,CREATE_IMAGE,ATTACH_LABEL,NO_SPECIAL};
	
	private static RENDERER_MODE commands_processing_mode = RENDERER_MODE.NO_SPECIAL;
	
	public static RENDERER_MODE getMode() {
		return commands_processing_mode;
	}

	public static void setMode(RENDERER_MODE mode) {
		commands_processing_mode = mode;
	}
}
