package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.client.resource.I18N;

/**
 * <p>Title: </p>
 * <p>Description: Exception, используемый модулем
 * построения шаблонов</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class CreateReportException extends ReportException {
	private static final long serialVersionUID = 4681271057954532209L;

	public static final String REPORT_MODEL_IS_ABSENT = "reportModelIsAbsent";
	public static final String NO_DATA_TO_INSTALL = "noDataToInstall";
	public static final String WRONG_DATA_TO_INSTALL = "wrongDataToInstall";	
	public static final String ERROR_GETTING_FROM_POOL = "errorGettingFromPool";	

	private String elementName = "";
	private String modelName = "";	
	private String reason = "";

	public CreateReportException(
			String elementName,
			String modelName,
			String reason) {
		this.elementName = elementName;
		this.modelName = modelName;
		this.reason = reason;
	}

	@Override
	public String getMessage() {
		String fullReportName = null;
		try {
			fullReportName = ReportModelPool.getModel(this.modelName)
				.getReportElementFullName(this.elementName);
		} catch (CreateModelException e) {
			return e.getMessage();
		}
		return I18N.getString("report.reportTemplateElement")
			+ " "
			+ fullReportName
			+ " "
			+ I18N.getString("report.Exception.cantImplement")
			+ " ("
			+ I18N.getString("report.Exception." + this.reason)
			+ " ).";
	}
}
