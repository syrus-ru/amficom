package com.syrus.AMFICOM.Client.Analysis.Report;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Report.APOReportModel;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScaledGraphPanel;

import javax.swing.JComponent;

import java.util.Vector;
import java.util.Hashtable;
import java.awt.Dimension;

abstract public class ESAPEReportModel extends APOReportModel
{
	public String getName()
	{
		return "ESAPEreportmodel";
	}

	// �������� ������ ��� �������
	public static String testParams = "parametersTitle";

	public static String commonInfo = "overallTitle";

	public static String reflectogram = "analysisTitle";

	public static String analysisParams = "analysisSelectionTitle";

	public static String commonChars = "eventTableTitle";

	// �������� ������ ��� ��������� (������������� � �������)

	public static String mask_type = "thresholdsTableTitle";

	public static String mask_view = "thresholdsTitle";

	// �������� ������ ��� ������������ (������������� � �������)

	public static String noise_level = "noiseTitle";

	public static String filtered_reflect = "filteredTitle";

	public static String histogram = "histogrammTitle";

//	public static String addChars = "eventDetailedTableTitle";

	// �������� ������ ��� �������� (������������� � �������)

	public static String pred_time_distrib = "TimedGraphTitle";

	public static String pred_stat_data = "TimedTableTitle";

	// �������� ������ ��� ������������� (������������� � �������)

	public static String model_params = "ParamsTitle";


	public String getReportsName(ObjectsReport rp)
	{
		String return_value = this.getObjectsName() + ":"
			+ getLangForField(rp.field);
		if (rp.reserveName != null)
			return_value += rp.reserveName;

		return return_value;
	}

	public String getReportsReserveName(ObjectsReport rp) throws
		CreateReportException
	{
		String result_id = (String) rp.getReserve();
		Result result = (Result) Pool.get(Result.typ, result_id);
		if (result != null)
			return ":" + result.getName();

		return "";
	}

	public ESAPEReportModel()
	{
	}

	public void loadRequiredObjects(
		DataSourceInterface dsi,
		ObjectsReport rp,
		ReportTemplate rt)
	{
	}

	public int getReportKind(ObjectsReport rp)
	{
		int returnValue = 1;

		if (  rp.field.equals(reflectogram)
			|| rp.field.equals(mask_view)
			|| rp.field.equals(noise_level)
			|| rp.field.equals(filtered_reflect)
			|| rp.field.equals(histogram)
			|| rp.field.equals(pred_time_distrib))
			returnValue = 0;

		return returnValue;
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
		if (rp.getReserve() instanceof ReportTable)
		{
			EvaluationTableReport er = new EvaluationTableReport(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
				er.columnModel,
				er.tableModel,
				rt.findROforReport(rp));
		}
		else
		{
			returnValue = new EvaluationGraphPanel(rt.findROforReport(rp));
		}
		return returnValue;
	}

	public void setData(ReportTemplate rt, Object data)
	{
		if (   rt.templateType.equals(ReportTemplate.rtt_Evaluation)
			 || rt.templateType.equals(ReportTemplate.rtt_Modeling)
			 || rt.templateType.equals(ReportTemplate.rtt_Analysis)
			 || rt.templateType.equals(ReportTemplate.rtt_Prediction)
			 || rt.templateType.equals(ReportTemplate.rtt_Survey))
		{
			AnalysisReport aReport = (AnalysisReport) data;
			for (int i = 0; i < rt.objectRenderers.size(); i++)
			{
				RenderingObject curRenderer = (RenderingObject) rt.objectRenderers.
					get(i);
				String itsTableTitle = curRenderer.getReportToRender().field;

				for (int j = 0; j < aReport.tables.size(); j++)
				{
					ReportTable curTable = (ReportTable) aReport.tables.get(j);
					if (curTable.title.equals(getLangForField(itsTableTitle)))
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

				for (int j = 0; j < aReport.panels.size(); j++)
				{
					ReportPanel curPanel = (ReportPanel) aReport.panels.get(j);
					if (curPanel.title.equals(getLangForField(itsTableTitle)))
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
