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

import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import javax.swing.JComponent;


/**
 * <p>Title: </p>
 * <p>Description: Модель отчётов для карты</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class MapReportModel extends ReportModel
{
	public static String rep_linkChars = "rep_linkChars";
	public static String rep_topology = "label_topology";

	public String getName() {return "mapreportmodel";}
	public String getObjectsName() {return ObjectResource.typ;}

	public String getReportsName(ObjectsReport rp)
	{
		return LangModelReport.getString(rp.field) + rp.reserveName;
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




/*		String reserve_str = (String) rp.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		if (separatPosit == -1)
		{
			Scheme scheme = (Scheme) Pool.get(Scheme.typ, reserve_str);
			return ":" + scheme.name;
		}
		else
		{
			Scheme scheme = (Scheme) Pool.get(Scheme.typ,
														 reserve_str.substring(0, separatPosit));
			String secondPart = "";
			if (rp.field.equals(MapReportModel.rep_topology))
				secondPart = ( (MapContext) Pool.get(MapContext.typ,
																 reserve_str.
																 substring(separatPosit + 1))).
					name;
			else
				secondPart = ( (SolutionCompact) Pool.get(SolutionCompact.typ,
					reserve_str.substring(separatPosit + 1))).name;
			return ":" + scheme.name + ":" + secondPart;
		}
*/
	}

	public MapReportModel()
	{
	}

	public void loadRequiredObjects(
			DataSourceInterface dsi,
			ObjectsReport rp,
			ReportTemplate rt)
	{
		String curValue = (String) rt.resourcesLoaded.get("mapProtoElementsLoaded");
		if (curValue.equals("false"))
		{
			new MapDataSourceImage(dsi).LoadProtoElements();
			rt.resourcesLoaded.put("mapProtoElementsLoaded","true");
		}

		curValue = (String) rt.resourcesLoaded.get("mapsLoaded");
		if (curValue.equals("false"))
		{
			new MapDataSourceImage(dsi).LoadMaps();
			rt.resourcesLoaded.put("mapsLoaded","true");
		}

	}

	public int getReportKind(ObjectsReport rp)
	{
		if (rp.field.equals(MapReportModel.rep_linkChars))
			return 1;

		return 0;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule) throws CreateReportException
	{
		JComponent returnValue = null;

		if (rp.field.equals(MapReportModel.rep_linkChars))
		{
			MapLinkFeatures osTable =
				new MapLinkFeatures(rp, divisionsNumber);

			returnValue = new ReportResultsTablePanel(
				osTable.columnModel,
				osTable.tableModel,
				rt.findROforReport(rp));
		}
		else
		{
			returnValue = new MapRenderPanel(rt.findROforReport(rp));
		}

		return returnValue;
	}

	public void setData(ReportTemplate rt,Object data)
	{
	};
}
