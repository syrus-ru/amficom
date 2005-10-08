/*
 * $Id: ReportException.java,v 1.2 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

public abstract class ReportException extends Exception{
	@Override
	public abstract String getMessage();
}
