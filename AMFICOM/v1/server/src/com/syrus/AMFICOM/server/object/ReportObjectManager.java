/*
 * $Id: ReportObjectManager.java,v 1.1.2.2 2004/08/20 17:12:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Report.*;
import java.util.Hashtable;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2004/08/20 17:12:10 $
 * @author $Author: bass $
 * @module server_v1
 */
public class ReportObjectManager
{
	private Hashtable reportTemplates;

	public ReportObjectManager()
	{
		this.reportTemplates = new Hashtable();
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
	}
}
