package com.syrus.AMFICOM.Client.Configure.Report;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ReportModel;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
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

public class EquipFeaturesReportModel extends ReportModel
{
	public String getName() {return "equipfeaturesreportmodel";}
	public String getObjectsName() {return ObjectResource.typ;}

	public String getReportsName(ObjectsReport rp)
	{
		return LangModelReport.getString("rep_equipChars") + rp.reserveName;
	}

	public String getReportsReserveName(ObjectsReport rp)
			throws CreateReportException
	{
		String reserve_str = (String) rp.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		String type = reserve_str.substring(0,separatPosit);
		String id = reserve_str.substring(separatPosit + 1);

		if (type.equals(Equipment.typ))
			type = "kisequipment";

		ObjectResource obj = (ObjectResource)Pool.get(type,id);
		if (obj != null)
			return ":" + obj.getName();
		else
			throw new CreateReportException("",CreateReportException.poolObjNotExists);
	}

	public EquipFeaturesReportModel()
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
		return 1;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule) throws CreateReportException
	{

		EquipFeatures osTable =
				new EquipFeatures(rp, divisionsNumber);

		JComponent returnValue = new ReportResultsTablePanel(
				osTable.columnModel,
				osTable.tableModel,
				rt.findROforReport(rp));

		return returnValue;
	}

	public void setData(ReportTemplate rt,AMTReport data)
	{
	};
}
