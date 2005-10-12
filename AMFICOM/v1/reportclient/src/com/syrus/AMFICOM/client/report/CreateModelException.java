/*
 * $Id: CreateModelException.java,v 1.3 2005/10/12 13:27:04 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.client.resource.I18N;

public final class CreateModelException extends ReportException {
	private static final long serialVersionUID = 7773133357012231431L;

	private String modelName = "";	

	public CreateModelException(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public String getMessage() {
		return I18N.getString("report.reportModel")
			+ " "
			+ this.modelName
			+ " "
			+ I18N.getString("report.Exception.cantCreateModel")
			+ ".";
	}
}
