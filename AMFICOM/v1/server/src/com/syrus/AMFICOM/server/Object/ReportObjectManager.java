package com.syrus.AMFICOM.server.Object;

import java.util.Hashtable;
import com.syrus.AMFICOM.CORBA.Report.ReportTemplate_Transferable;
import com.syrus.AMFICOM.CORBA.Report.FirmedTextPane_Transferable;
import com.syrus.AMFICOM.CORBA.Report.RenderingObject_Transferable;

public class ReportObjectManager
{
	private Hashtable reportTemplates;
//	private Hashtable fieldReports;

	public ReportObjectManager()
	{
		this.reportTemplates = new Hashtable();
//		this.fieldReports = new Hashtable();
	}

	public ReportTemplate saveReportTemplate(ReportTemplate_Transferable reportTemplate_t) throws Exception
	{
		ReportTemplate reportTemplate = new ReportTemplate(reportTemplate_t);
		this.reportTemplates.put(reportTemplate.getId(), reportTemplate);
		return reportTemplate;
	}

	public void updateReportTemplate(ReportTemplate_Transferable reportTemplate_t) throws Exception
	{
		ReportTemplate reportTemplate = this.retrieveReportTemplate(reportTemplate_t.id);
		reportTemplate.update(reportTemplate_t);
	}

	public void setReportTemplateFields(String reportTemplate_id, RenderingObject_Transferable[] ros) throws Exception
	{
		ReportTemplate reportTemplate = this.retrieveReportTemplate(reportTemplate_id);
		reportTemplate.setReportTemplateFields(ros);
	}

	public void setReportTemplateBoxes(String reportTemplate_id, FirmedTextPane_Transferable[] ftbs) throws Exception
	{
		ReportTemplate reportTemplate = this.retrieveReportTemplate(reportTemplate_id);
		reportTemplate.setReportTemplateBoxes(ftbs);
	}

	public ReportTemplate retrieveReportTemplate(String id) throws Exception
	{
		if (id == null || id.equals(""))
			return null;

		ReportTemplate reportTemplate;
		if (this.reportTemplates.containsKey(id))
			reportTemplate = (ReportTemplate )this.reportTemplates.get(id);
		else
		{
			reportTemplate = new ReportTemplate(id);
			this.reportTemplates.put(reportTemplate.getId(), reportTemplate);
		}
		return reportTemplate;
	}

	public ReportTemplate[] retrieveReportTemplates() throws Exception
	{
		return null;
/*
		ReportTemplate[] reportTemplates = ReportTemplate.retrieveReportTemplates();
		for (int i = 0; i < reportTemplates.length; i++)
			this.reportTemplates.put(reportTemplates[i].getId(), reportTemplates[i]);
		return reportTemplates;
*/
	}

}
