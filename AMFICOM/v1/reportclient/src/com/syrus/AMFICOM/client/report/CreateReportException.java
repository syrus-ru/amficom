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

	public static final String REPORT_MODEL_IS_ABSENT = "reportModelIsAbsent";
	public static final String NO_DATA_TO_INSTALL = "noDataToInstall";
	public static final String WRONG_DATA_TO_INSTALL = "wrongDataToInstall";	
	public static final String ERROR_GETTING_FROM_POOL = "errorGettingFromPool";	

	private String templateElementName = "";
	private String reason = "";

	public CreateReportException(String templateElementName, String reason) {
		this.templateElementName = templateElementName;
		this.reason = reason;
	}

	public String getMessage() {
		return LangModelReport.getString("report.reportTemplateElement")
			+ " "
			+ LangModelReport.getString(this.templateElementName)
			+ " "
			+ LangModelReport.getString("report.Exception.cantImplement")
			+ " ("
			+ LangModelReport.getString("report.Exception." + this.reason)
			+ " ).";
	}
}
