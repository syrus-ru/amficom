package com.syrus.AMFICOM.Client.Survey.Report;

import java.util.Vector;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;

import com.syrus.AMFICOM.Client.General.Report.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ObserveReportModel extends APOReportModel
{
  public static String alarms_list = "Alarms_list";
  public static String alarms_statistics = "Alarms_statistics";
  public static String alarm_info = "Alarm_info";  
  public static String alarm_reflectogramm = "Alarm_reflectogramm";
  public static String alarm_scheme = "Alarm_scheme";
  public static String alarm_map = "Alarm_map";  

	public ObserveReportModel()
	{
	}

	public String getName()
	{
		return "surveyreportmodel";
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repSurveyResults");
	}

	public String getLangForField(String field)
	{
		return LangModelSurvey.getString(field);
	}

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(ObserveReportModel.alarms_list);
		result.add(ObserveReportModel.alarms_statistics);
		result.add(ObserveReportModel.alarm_info);
		result.add(ObserveReportModel.alarm_reflectogramm);
		result.add(ObserveReportModel.alarm_scheme);
		result.add(ObserveReportModel.alarm_map);    

		return result;
	}
  
	public void setData(ReportTemplate rt, Object data)
	{
		if (rt.templateType.equals(ReportTemplate.rtt_Survey))
		{
			AMTReport aReport = (AMTReport) data;
			for (Iterator it = rt.objectRenderers.iterator(); it.hasNext();)
			{
				RenderingObject curRenderer = (RenderingObject)it.next();
				String itsTableTitle = curRenderer.getReportToRender().field;

				for (Iterator it2 = aReport.tables.iterator(); it2.hasNext();)
				{
					AMTReportTable curTable = (AMTReportTable)it2.next();
					if (curTable.title.equals(itsTableTitle))
					{
						try
						{
							curRenderer.getReportToRender().setReserve(curTable);
						}
						catch (Exception exc)
						{}
						break;
					}
				}

				if (curRenderer.getReportToRender().getReserve() != null)
					continue;

				for (Iterator it2 = aReport.panels.iterator(); it2.hasNext();)
				{
					AMTReportPanel curPanel = (AMTReportPanel)it2.next();
					if (curPanel.title.equals(itsTableTitle))
					{
						try
						{
							curRenderer.getReportToRender().setReserve(curPanel);
						}
						catch (Exception exc)
						{}
						break;
					}
				}
			}
		}
	}
  
}