package com.syrus.AMFICOM.Client.Resource;

import java.util.Vector;

import com.syrus.AMFICOM.Client.General.SessionInterface;

import com.syrus.AMFICOM.CORBA.Report.ReportTemplate_Transferable;
import com.syrus.AMFICOM.CORBA.Report.ReportTemplateSeq_TransferableHolder;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;

public class RISDReportDataSource
		extends RISDDataSource
{
	protected RISDReportDataSource()
	{
		super();
	}

	public RISDReportDataSource(SessionInterface si)
	{
		super(si);
	}

	public void SaveReportTemplates(String[] reportTemplate_ids)
	{
		if (si == null)
			return;
		if (!si.isOpened())
			return;

		ReportTemplate_Transferable[] rts =
				new ReportTemplate_Transferable[reportTemplate_ids.length];

		Vector frsVector = new Vector();

		//«аписываем ReportTemplate'ы и считаем количество ObjectResourceReport'ов
		for (int i = 0; i < reportTemplate_ids.length; i++)
		{
			ReportTemplate rt = (ReportTemplate) Pool.get(
					ReportTemplate.typ, reportTemplate_ids[i]);
			rt.setTransferableFromLocal();
			rts[i] = (ReportTemplate_Transferable) rt.getTransferable();
		}

		try
		{
			si.ci.server.saveReportTemplates(si.accessIdentity, rts);
//			si.ci.server.saveReportTemplates(si.accessIdentity,new ReportTemplate_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Templates: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

	}

	public void LoadReportTemplates(String[] report_ids)
	{
		if (si == null)
			return;
		if (!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ReportTemplateSeq_TransferableHolder rth = new
				ReportTemplateSeq_TransferableHolder();
		ReportTemplate_Transferable rts[];
		ReportTemplate rt;

		try
		{
			si.ci.server.getStatedReportTemplates(si.accessIdentity, report_ids, rth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting Report Templates: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		rts = rth.value;
		count = rts.length;
		if (count != 0)
			System.out.println("...Done! " + count + " ReportTemplates(s) fetched");
		for (i = 0; i < count; i++)
		{
			rt = new ReportTemplate(rts[i]);
			Pool.put(ReportTemplate.typ, rt.getId(), rt);
		}
	}

	public void RemoveReportTemplates(String[] report_ids)
	{
		if (si == null)
			return;
		if (!si.isOpened())
			return;

		try
		{
			si.ci.server.removeReportTemplates(si.accessIdentity, report_ids);
		}
		catch (Exception ex)
		{
			System.err.print("Error RemoveReportTemplates: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}
}
