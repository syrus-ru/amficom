package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ReportModel;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JComponent;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MapReportModel extends ReportModel
{
	public String getName() {return "mapreportmodel";}
	public String getObjectsName() {return ObjectResource.typ;}

	public String getReportsName(ObjectsReport rp)
	{
		return LangModelReport.String("rep_linkChars") + rp.reserveName;
	}

	public String getReportsReserveName(ObjectsReport rp)
			throws CreateReportException
	{
		String reserve_str = (String) rp.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		String type = reserve_str.substring(0,separatPosit);
		String id = reserve_str.substring(separatPosit + 1);

		ObjectResource obj = (ObjectResource)Pool.get(type,id);
		if (obj != null)
			return ":" + obj.getName();
		else
			throw new CreateReportException("",CreateReportException.poolObjNotExists);
	}

	public MapReportModel()
	{
	}

	public void loadRequiredObjects(
			DataSourceInterface dsi,
			ObjectsReport rp,
			ReportTemplate rt)
	{
	}

	public boolean isTableReport(ObjectsReport rp)
	{
		return true;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule) throws CreateReportException
	{

		MapLinkFeatures osTable =
				new MapLinkFeatures(rp, divisionsNumber);

		JComponent returnValue = new ReportResultsTablePanel(
				osTable.columnModel,
				osTable.tableModel,
				rt.findROforReport(rp));

		return returnValue;
	}

	public void setData(ReportTemplate rt,Object data)
	{
	};
}
