package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import com.syrus.AMFICOM.Client.General.Report.*;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import javax.swing.JComponent;

import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: Модель отчётов для карты</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class MapReportModel extends APOReportModel
{
	public static String rep_linkChars = "rep_linkChars";
	public static String rep_topology = "rep_topology";

	public String getName() {return "mapreportmodel";}
	public String getObjectsName()
  {
    return LangModelReport.getString("label_repTopologicalScheme");
  }

	public String getReportsName(ObjectsReport rp)
	{
    String returnValue = this.getObjectsName() + ":" + LangModelMap.getString(rp.field);
    if (rp.reserveName != null)
      returnValue += rp.reserveName;
      
		return returnValue;
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

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(MapReportModel.rep_topology);

		return result;
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

	public String getLangForField(String field)
	{
		return LangModelMap.getString(field);
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
    
		else if (rp.field.equals(MapReportModel.rep_topology))
		{
      if (rp.getReserve() instanceof MapRenderPanel)
      {
        returnValue = (MapRenderPanel)rp.getReserve();
        ((MapRenderPanel)returnValue).fitToRenderingObject(rt.findROforReport(rp));
      }
      else
  			returnValue = new MapRenderPanel(rt.findROforReport(rp));
		}

		return returnValue;
	}

	public void setData(ReportTemplate rt, Object data)
	{
//      if (rt.templateType.equals(ReportTemplate.rtt_Map))
		if (rt.templateType.equals(ReportTemplate.rtt_Map))
		{
			AMTReport aReport = (AMTReport) data;
			for (int i = 0; i < rt.objectRenderers.size(); i++)
			{
				RenderingObject curRenderer = (RenderingObject) rt.objectRenderers.
					get(i);
				String itsTableTitle = curRenderer.getReportToRender().field;

/*            for (int j = 0; j < aReport.tables.size(); j++)
				{
					AMTReportTable curTable = (AMTReportTable) aReport.tables.get(j);
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
					continue;*/

				for (int j = 0; j < aReport.panels.size(); j++)
				{
					AMTReportPanel curPanel = (AMTReportPanel) aReport.panels.get(j);
					if (curPanel.title.equals(itsTableTitle))
					{
						try
						{
							curRenderer.getReportToRender().setReserve(curPanel.panel);
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
