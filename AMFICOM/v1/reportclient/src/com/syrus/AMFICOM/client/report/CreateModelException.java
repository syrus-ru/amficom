/*
 * $Id: CreateModelException.java,v 1.1 2005/09/16 13:26:29 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.client.report.LangModelReport;

public final class CreateModelException extends ReportException {
	private String modelName = "";	

	public CreateModelException(String modelName) {
		this.modelName = modelName;
	}

	public String getMessage() {
		return LangModelReport.getString("report.reportModel")
			+ " "
			+ this.modelName
			+ " "
			+ LangModelReport.getString("report.Exception.cantCreateModel")
			+ ".";
	}
}
