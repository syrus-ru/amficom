package com.syrus.AMFICOM.Client.Survey.Report;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.*;

import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Filter.LogicScheme;
import com.syrus.AMFICOM.Client.Survey.Alarm.Filter.AlarmFilter;

import javax.swing.JPanel;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * <p>Title: </p>
 * <p>Description: Модель отчётов для сигналов тревоги</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class AlarmReportModel extends ObjectResourceReportModel
{
	/**
	 * Возвращает название модели
*/
	public String getName()
	{
		return "alarmreportmodel";
	}

	/**
	 * Возвращает локализованное название объектов, описываемых моделью
*/
	public String getObjectsName()
	{
		return LangModelSurvey.getString("Report_by_alarm_signals");
	}

	/**
	 * Возвращает тип объектов, по которым генерится отчёт.
	 * Используется при создании DataSet, в частности при
	 * фильтарии объектов.
*/
	public String getObjectsType()
	{
		return Alarm.typ;
	}

	public AlarmReportModel()
	{
		super();
	}

	//По ним информация будет доставаться из Pool'а
	public Vector getAllObjectColumnIDs()
	{
		Vector result = new Vector();
		result.add("source_name");
		result.add("monitored_element_id");
		result.add("type_id");
		result.add("status");
		result.add("generated");

		result.add("assigned");
		result.add("fixed_when");
		result.add("assigned_to");
		result.add("fixed_by");

		result.add("comments");

		return result;
	}

	//По этим строкам имена достанутся из LangModelReport
	public Vector getAllObjectColumnNames()
	{
		Vector result = new Vector();

		result.add(LangModelSurvey.getString("Event_source"));
		result.add(LangModelSurvey.getString("Monitored_element"));
		result.add(LangModelSurvey.getString("Event_type"));
		result.add(LangModelSurvey.getString("Status"));
		result.add(LangModelSurvey.getString("time"));

		result.add(LangModelSurvey.getString("Execution_start"));
		result.add(LangModelSurvey.getString("Execution_finish"));
		result.add(LangModelSurvey.getString("Chief_executor"));
		result.add(LangModelSurvey.getString("Executor"));

		result.add(LangModelSurvey.getString("Comments"));

		return result;
	}

	public Vector getAllObjectColumnSizes()
	{
		Vector result = new Vector();
		result.add(new Integer(100));
		result.add(new Integer(100));
		result.add(new Integer(60));
		result.add(new Integer(60));
		result.add(new Integer(60));

		result.add(new Integer(60));
		result.add(new Integer(60));
		result.add(new Integer(100));
		result.add(new Integer(100));

		result.add(new Integer(100));

		return result;
	}

	public Hashtable getAvailableViews()
	{
		Hashtable result = new Hashtable();

		Vector typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("source_name", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("monitored_element_id", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("type_id", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_timefunction);
		typesForField.add(ObjectResourceReportModel.rt_gistogram);
		result.put("generated", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("status", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("assigned", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("assigned_to", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("fixed_when", typesForField);

		typesForField = new Vector();
		typesForField.add(ObjectResourceReportModel.rt_statistics);
		typesForField.add(ObjectResourceReportModel.rt_pieChart);
		typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
		typesForField.add(ObjectResourceReportModel.rt_barChart);
		typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
		result.put("fixed_by", typesForField);

		typesForField = new Vector();
		result.put("comments", typesForField);

		return result;
	}

	public void loadRequiredObjects(
		DataSourceInterface dsi,
		ObjectsReport rp,
		ReportTemplate rt)
	{
		String curValue = (String) rt.resourcesLoaded.get("alarmsLoaded");
		if (curValue.equals("false"))
		{
			new SurveyDataSourceImage(dsi).GetAlarms();
			rt.resourcesLoaded.put("alarmsLoaded", "true");
		}
	}

	public ObjectResourceFilter findORFilterforModel(
		  ReportTemplate rt,DataSourceInterface dsi)
	{
		Vector filters = rt.objectResourceFilters;

		for (int i = 0; i < filters.size(); i++)
		{
			ObjectResourceFilter curORF = (ObjectResourceFilter) filters.get(i);
			if (curORF instanceof AlarmFilter)
			{
				loadRequiredObjects(dsi,null,rt);
				return curORF;
			}
		}

		AlarmFilter af = new AlarmFilter();
		loadRequiredObjects(dsi,null,rt);

		rt.objectResourceFilters.add(af);
		return af;
	}

	public void setData(ReportTemplate rt, Object data)
	{
	}

	public String getReportsName(ObjectsReport rp)
	{
		ObjectResourceReportModel orrm = (ObjectResourceReportModel) rp.model;

		if (rp.view_type.equals(ObjectResourceReportModel.rt_objectsReport))
		{
			String returnValue =
				orrm.getObjectsName() + ":" +
				LangModelReport.getString(rp.view_type) + ":" +
				LangModelReport.getString("label_byFields");

			Vector fieldList = (Vector) rp.getReserve();
			int listRenderSize = (fieldList.size() > 2) ? 3 : fieldList.size();
			for (int i = 0; i < listRenderSize; i++)
			{
				returnValue +=
					(String) orrm.getColumnNamebyID((String) fieldList.get(i));
				if (i != fieldList.size() - 1)
					returnValue += ", ";
			}
			if (listRenderSize != fieldList.size())
				returnValue +=
					(LangModelReport.getString("label_more") +
					Integer.toString(fieldList.size() - listRenderSize) + ")");

			return returnValue;
		}

		String resultString = orrm.getObjectsName();
		if (!rp.field.equals(""))
			resultString = resultString + ":" + orrm.getColumnNamebyID(rp.field);
		if (!rp.view_type.equals(""))
			resultString = resultString + ":"
				+ LangModelReport.getString(rp.view_type);
		return resultString;
	}
}

