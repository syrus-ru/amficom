/*
 * $Id: ReportProcessingMode.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

public class ReportProcessingMode {
	public static enum ProcessingMode{TEMPLATE_SCHEME,REPORT_PREVIEW}
	
	private static ProcessingMode mode;
	
	public static ProcessingMode getMode()
	{
		return ReportProcessingMode.mode;
	}
	
	public static void setMode(ProcessingMode newMode)
	{
		ReportProcessingMode.mode  = newMode;
	}
}
