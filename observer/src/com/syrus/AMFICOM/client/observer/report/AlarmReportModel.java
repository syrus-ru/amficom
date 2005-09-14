package com.syrus.AMFICOM.client.observer.report;

import java.util.Collection;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.report.DataStorableElement;

/**
 * <p>Title: </p>
 * <p>Description: ������ ������� ��� �������� �������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class AlarmReportModel extends ReportModel {

	@Override
	public RenderingComponent createReport(DataStorableElement element, Object data, ApplicationContext aContext) throws CreateReportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getReportElementName(String reportName) {
		return null;
	}

	@Override
	public ReportType getReportKind(String reportName) {
		return null;
	}

	@Override
	public Collection<String> getReportElementNames() {
		return null;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		return null;
	}
/*
	private List objectColumnIDs;
	private List objectColumnNames;
	private List objectColumnSizes;
	private Map availableViews;
*/
	/**
	 * ���������� �������� ������
	 */
/*
	public String getName()
	{
		return "alarmreportmodel";
	}
*/
	/**
	 * ���������� �������������� �������� ��������, ����������� �������
	 */
/*
	public String getObjectsName()
	{
		return LangModelSurvey.getString("Report_by_alarm_signals");
	}
*/
	/**
	 * ���������� ��� ��������, �� ������� ��������� �����.
	 * ������������ ��� �������� DataSet, � ��������� ���
	 * ��������� ��������.
	 */
/*
	public String getObjectsType()
	{
		return Alarm.typ;
	}

	public AlarmReportModel()
	{
		super();
	}

	//�� ��� ���������� ����� ����������� �� Pool'�
	public List getAllObjectColumnIDs()
	{
		if (this.objectColumnIDs==null){			
		this.objectColumnIDs = new ArrayList();
		this.objectColumnIDs.add("source_name");
		this.objectColumnIDs.add("monitored_element_id");
		this.objectColumnIDs.add("type_id");
		this.objectColumnIDs.add("status");
		this.objectColumnIDs.add("generated");

		this.objectColumnIDs.add("assigned");
		this.objectColumnIDs.add("fixed_when");
		this.objectColumnIDs.add("assigned_to");
		this.objectColumnIDs.add("fixed_by");

		this.objectColumnIDs.add("comments");
		}

		return this.objectColumnIDs;
	}

	//�� ���� ������� ����� ���������� �� LangModelReport
	public List getAllObjectColumnNames()
	{
		if (this.objectColumnNames==null){
			this.objectColumnNames = new ArrayList();
			this.objectColumnNames.add(LangModelSurvey.getString("Event_source"));
			this.objectColumnNames.add(LangModelSurvey.getString("Monitored_element"));
			this.objectColumnNames.add(LangModelSurvey.getString("Event_type"));
			this.objectColumnNames.add(LangModelSurvey.getString("Status"));
			this.objectColumnNames.add(LangModelSurvey.getString("time"));

			this.objectColumnNames.add(LangModelSurvey.getString("Execution_start"));
			this.objectColumnNames.add(LangModelSurvey.getString("Execution_finish"));
			this.objectColumnNames.add(LangModelSurvey.getString("Chief_executor"));
			this.objectColumnNames.add(LangModelSurvey.getString("Executor"));

			this.objectColumnNames.add(LangModelSurvey.getString("Comments"));
		}  

		return this.objectColumnNames;
	}

	public List getAllObjectColumnSizes()
	{
		if (this.objectColumnSizes==null){
			this.objectColumnSizes = new ArrayList();
			this.objectColumnSizes.add(new Integer(100));
			this.objectColumnSizes.add(new Integer(100));
			this.objectColumnSizes.add(new Integer(60));
			this.objectColumnSizes.add(new Integer(60));
			this.objectColumnSizes.add(new Integer(60));
	
			this.objectColumnSizes.add(new Integer(60));
			this.objectColumnSizes.add(new Integer(60));
			this.objectColumnSizes.add(new Integer(100));
			this.objectColumnSizes.add(new Integer(100));
	
			this.objectColumnSizes.add(new Integer(100));
		}

		return this.objectColumnSizes;
	}

	public Map getAvailableViews()
	{
		if (this.availableViews==null){
			this.availableViews = new HashMap();
			
			ArrayList typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("source_name", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("monitored_element_id", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("type_id", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_timefunction);
			typesForField.add(ObjectResourceReportModel.rt_gistogram);
			this.availableViews.put("generated", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("status", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("assigned", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("assigned_to", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("fixed_when", typesForField);
	
			typesForField = new ArrayList();
			typesForField.add(ObjectResourceReportModel.rt_statistics);
			typesForField.add(ObjectResourceReportModel.rt_pieChart);
			typesForField.add(ObjectResourceReportModel.rt_pie2DChart);
			typesForField.add(ObjectResourceReportModel.rt_barChart);
			typesForField.add(ObjectResourceReportModel.rt_bar2DChart);
			this.availableViews.put("fixed_by", typesForField);
	
			typesForField = new ArrayList();
			this.availableViews.put("comments", typesForField);
		}

		return this.availableViews;
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
		List filters = rt.objectResourceFilters;

		for (Iterator it=filters.iterator();it.hasNext();)
		{
			ObjectResourceFilter curORF = (ObjectResourceFilter)it.next();
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

	public void setData(ReportTemplate rt, AMTReport aReport)
	{
    if (!rt.templateType.equals(ReportTemplate.rtt_Observe))
      return;

    ObjectResourceTableModel tableModel = (ObjectResourceTableModel)
      aReport.data.get(
        new ObserveReportModel().getLangForField(ObserveReportModel.alarms_list));
    
    if (tableModel == null)
      return;
       
    ObjectResourceReportModel.reportObjects = new ArrayList();
    for (int i = 0; i < tableModel.getRowCount(); i++)
    {
      Alarm cur_alarm = (Alarm)tableModel.getValueAt(i,0);
      ObjectResourceReportModel.reportObjects.add(cur_alarm);
    }
	}

	public String getReportsName(ObjectsReport rp)
	{
		ObjectResourceReportModel orrm = (ObjectResourceReportModel) rp.model;

		if (rp.view_type.equals(ObjectResourceReportModel.rt_objectsReport))
		{
			StringBuffer returnValue = new StringBuffer(orrm.getObjectsName());
			returnValue.append(":");
			returnValue.append(LangModelReport.getString(rp.view_type));
			returnValue.append(":");
			returnValue.append(LangModelReport.getString("label_byFields"));

      List fieldList = (List) rp.getReserve();
			int listRenderSize = (fieldList.size() > 2) ? 3 : fieldList.size();
			for (int i = 0; i < listRenderSize; i++)
			{
				returnValue.append(orrm.getColumnNamebyID((String) fieldList.get(i)));
				if (i != fieldList.size() - 1)
					returnValue.append(", ");
			}
			if (listRenderSize != fieldList.size()){
				returnValue.append(LangModelReport.getString("label_more"));
				returnValue.append(Integer.toString(fieldList.size() - listRenderSize));
				returnValue.append(")");
			}

			return returnValue.toString();
		}

		StringBuffer resultString = new StringBuffer(orrm.getObjectsName());
		if (rp.field.length()>0){
			resultString.append(":");
			resultString.append(orrm.getColumnNamebyID(rp.field));
		}
		if (rp.view_type.length()>0){			
			resultString.append(":");
			resultString.append(LangModelReport.getString(rp.view_type));
		}
		return resultString.toString();
	}
*/
}

