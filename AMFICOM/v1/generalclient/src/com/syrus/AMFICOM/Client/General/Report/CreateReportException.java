package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

/**
 * <p>Title: </p>
 * <p>Description: Exception, используемый модулем
 * построения шаблонов</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class CreateReportException extends Exception
{
  public static String cantImplement = "label_cantImpl";
  public static String templatePiece = "label_templatePiece";
  public static String poolObjNotExists = "label_poolObjNotExists";
  public static String generalError = "label_generalCRError";
  public static String noImageSelected = "label_generalCRError";

  public String reportName = "";
  public String reason = "";

  public CreateReportException(String reportName, String reason)
  {
	 this.reportName = reportName;
	 this.reason = reason;
  }

  public String getMessage()
  {
	 if (reason.equals(cantImplement))
		return LangModelReport.getString("label_report") + reportName + LangModelReport.getString(reason);
	 if (reason.equals(templatePiece))
		return LangModelReport.getString(reason) + " (" + reportName + ")";
	 if (reason.equals(poolObjNotExists))
		return LangModelReport.getString(reason) + reportName;
	 if (reason.equals(generalError))
		return LangModelReport.getString(reason);

	 return "";
  }
}
