/*
 * $Id: CreateModelException.java,v 1.2 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.client.report.LangModelReport;

public final class CreateModelException extends ReportException {
	private static final long serialVersionUID = 7773133357012231431L;

	private String modelName = "";	

	public CreateModelException(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public String getMessage() {
		return LangModelReport.getString("report.reportModel")
			+ " "
			+ this.modelName
			+ " "
			+ LangModelReport.getString("report.Exception.cantCreateModel")
			+ ".";
	}
}
