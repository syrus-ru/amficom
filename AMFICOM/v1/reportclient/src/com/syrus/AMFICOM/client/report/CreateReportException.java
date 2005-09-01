package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.client.report.LangModelReport;

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
	public static final String noDataToInstall = "noDataToInstall";	

	private String templateElementName = "";
	private String reason = "";

	public CreateReportException(String templateElementName, String reason) {
		this.templateElementName = templateElementName;
		this.reason = reason;
	}

	public String getMessage() {
		return LangModelReport.getString("report.reportTemplateElement")
			+ " "
			+ this.templateElementName
			+ " "
			+ LangModelReport.getString("report.Exception.cantImplement")
			+ " ("
			+ this.reason
			+ " ).";
	}
}
