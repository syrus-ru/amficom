package com.syrus.AMFICOM.Client.Survey.Report;

import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;
import javax.swing.JComponent;
import java.util.Vector;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.*;

import com.syrus.AMFICOM.Client.Analysis.Report.EvaluationTableReport;

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
  public static String alarm_info = "Alarm_info";  
  public static String alarm_reflectogramm = "Alarm_reflectogramm";
  public static String alarm_scheme = "Alarm_scheme";
  public static String alarm_map = "Alarm_map";  

	public ObserveReportModel()
	{
	}

	public String getName()
	{
		return "observereportmodel";
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repObservation");
	}

	public int getReportKind(ObjectsReport rp)
	{
		int returnValue = 1;

		if (rp.field.equals(alarm_scheme))
      return -1;
    else if (rp.field.equals(alarm_reflectogramm) || rp.field.equals(alarm_map))
      return 0;
    
    return 1;
	}

	public String getLangForField(String field)
	{
		return LangModelSurvey.getString(field);
	}

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(ObserveReportModel.alarm_info);
		result.add(ObserveReportModel.alarm_reflectogramm);
		result.add(ObserveReportModel.alarm_scheme);
		result.add(ObserveReportModel.alarm_map);    

		return result;
	}

	public String getReportsName(ObjectsReport rp)
	{
		String return_value = this.getObjectsName() + ":"
			+ getLangForField(rp.field);

		return return_value;
	}


	public String getReportsReserveName(ObjectsReport rp) throws
		CreateReportException
	{
		return "";
	}

	public JComponent createReport(
		ObjectsReport rp,
		int divisionsNumber,
		ReportTemplate rt,
		ApplicationContext aContext,
		boolean fromAnotherModule)

		throws CreateReportException
	{
		if (rp.getReserve() == null)
			throw new CreateReportException(rp.getName(),
				CreateReportException.cantImplement);

		JComponent returnValue = null;
    
		if (rp.field.equals(ObserveReportModel.alarms_list))
		{
			AlarmsList er = new AlarmsList(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
				er.columnModel,
				er.tableModel,
				rt.findROforReport(rp));
		}
		else if (rp.field.equals(ObserveReportModel.alarm_info))
		{
			returnValue = new TextPanel(rp);
		}
		else if (rp.field.equals(ObserveReportModel.alarm_reflectogramm))
		{
      rp.field = AnalysisReportModel.reflectogram;
      
			returnValue = new AnalysisReportModel().createReport(
        rp,divisionsNumber,
        rt,
        aContext,
        fromAnotherModule);
        
      rp.field = ObserveReportModel.alarm_reflectogramm;
		}
		else if (rp.field.equals(ObserveReportModel.alarm_scheme))
		{
      rp.field = SchemeReportModel.scheme;
      
			returnValue = new SchemeReportModel().createReport(
        rp,divisionsNumber,
        rt,
        aContext,
        fromAnotherModule);
        
      rp.field = ObserveReportModel.alarm_scheme;
		}
    
		return returnValue;
	}

	public void loadRequiredObjects(
		DataSourceInterface dsi,
		ObjectsReport rp,
		ReportTemplate rt)
	{
    new SurveyDataSourceImage(dsi).GetAlarms();
	}

  
	public void setData(ReportTemplate rt, AMTReport aReport)
	{
		if (rt.templateType.equals(ReportTemplate.rtt_Observe))
		{
      super.setData(rt,aReport);
		}
	}
  
}