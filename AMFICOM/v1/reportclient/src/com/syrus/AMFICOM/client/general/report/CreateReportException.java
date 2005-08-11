package com.syrus.AMFICOM.client.general.report;

import com.syrus.AMFICOM.client.general.report.LangModelReport;

/**
 * <p>Title: </p>
 * <p>Description: Exception, используемый модулем
 * построения шаблонов</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class CreateReportException extends Exception {
	private static final long serialVersionUID = 4681271057954532209L;

	public static final String reportModelIsAbsent = "reportModelIsAbsent";

	private String reportName = "";
	private String reason = "";

	public CreateReportException(String reportName, String reason) {
		this.reportName = reportName;
		this.reason = reason;
	}

	public String getMessage() {
		if (this.reason.equals(CreateReportException.reportModelIsAbsent))
			return LangModelReport.getString("report.report")
				+ " "
				+ this.reportName
				+ " "
				+ LangModelReport.getString("report.Exception.cantCreateReport")
				+ " ("
				+ this.reason
				+ " ).";

		return "";
	}
}
