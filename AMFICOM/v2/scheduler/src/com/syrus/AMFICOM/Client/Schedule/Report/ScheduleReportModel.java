package com.syrus.AMFICOM.Client.Schedule.Report;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.JPanelRenderer;
import com.syrus.AMFICOM.Client.General.Report.ObjectResourceDivList;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

public class ScheduleReportModel extends APOReportModel
{
	public String getName() {return "schedulereportmodel";}

	public static String plan_panel = "Plan.Title";
	public static String tests_table = "Tests_status_and_characters";

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repScheduler");
	}

	public String getReportsName(ObjectsReport rp)
	{
		String return_value = this.getObjectsName() + ":" + LangModelSchedule.getString(rp.field);
		if (rp.reserveName != null)
			return_value += rp.reserveName;

		return return_value;
	}

	public String getLangForField(String field)
	{
		return LangModelSchedule.getString(field);
	}

	public String getReportsReserveName(ObjectsReport rp)
			throws CreateReportException
	{
		String reserve_str = (String)rp.getReserve();
//		Scheme scheme = (Scheme)Pool.get(Scheme.typ,reserve_str);
//		return ":" + scheme.name;
    return reserve_str;
	}

	public List getAvailableReports()
	{
		List result = new ArrayList(2);

		result.add(ScheduleReportModel.plan_panel);
		result.add(ScheduleReportModel.tests_table);

		return result;
	}

	public void loadRequiredObjects(
				DataSourceInterface dsi,
				ObjectsReport rp,
				ReportTemplate rt)
	{

    String curValue = (String) rt.resourcesLoaded.get("ismLoaded");
    if (curValue.equals("false"))
    {
      new ConfigDataSourceImage(dsi).LoadISM();
      rt.resourcesLoaded.put("ismLoaded","true");
    }
  
		SurveyDataSourceImage sdsi = new SurveyDataSourceImage(dsi);
    
    curValue = (String) rt.resourcesLoaded.get("testTypesLoaded");
    if (curValue.equals("false"))
    {
      sdsi.LoadTestTypes();
      rt.resourcesLoaded.put("testTypesLoaded","true");
    }

    curValue = (String) rt.resourcesLoaded.get("analysisTypesLoaded");
    if (curValue.equals("false"))
    {
      sdsi.LoadAnalysisTypes();
      rt.resourcesLoaded.put("analysisTypesLoaded","true");
    }

    curValue = (String) rt.resourcesLoaded.get("evaluationTypesLoaded");
    if (curValue.equals("false"))
    {
      sdsi.LoadEvaluationTypes();
      rt.resourcesLoaded.put("evaluationTypesLoaded","true");
    }
  }

	public int getReportKind(ObjectsReport rp)
	{
		return -1;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule)

			throws CreateReportException
	{
    JComponent returnValue = null;
		if (rp.field.equals(ScheduleReportModel.plan_panel))
		{
			returnValue = new JPanelRenderer(rt.findROforReport(rp));
    }
    else if (rp.field.equals(ScheduleReportModel.tests_table))
		{
      ObjectResourceDivList ordl = new ObjectResourceDivList(rp,divisionsNumber);
			returnValue = new ReportResultsTablePanel(
        ordl.columnModel,
        ordl.tableModel,
        rt.findROforReport(rp));
    }


		return returnValue;
	}

	public void setData(ReportTemplate rt, AMTReport aReport)
	{
		if (rt.templateType.equals(ReportTemplate.rtt_Scheduler))
		{
			super.setData(rt,aReport);
		}
	}
}
