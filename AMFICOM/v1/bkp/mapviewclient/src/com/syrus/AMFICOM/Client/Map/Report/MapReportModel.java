package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ImagePanel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.Image;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;


public class MapReportModel extends APOReportModel
{
	public String getName()
	{
		return "MapReportModel";
	}

	// Названия отчётов для карты
  /*
   * Картинка карты
   */ 
	public static String topology = "menuMap";
  /*
   * Прокладка кабеля по тоннелям (список колодцев и тоннелей)
   */ 
	public static String cableLayout = "mapcablepathelement";
  /*
   * Список кабелей по тоннелю + их расположение в тоннеле
   */ 
	public static String tunnelCableList = "tunnelCableList";
  /*
   * Информация о маркере (кабель, тоннель, место в тоннеле,
   * расстояние до узлов, географические координаты)
   */ 
	public static String markerInfo = "markerInfo";
  /*
   * Информация о коллекторе (список пикетов, колодцев + длина)
   */ 
	public static String collectorInfo = "collectorInfo";
  /*
   * Информация по колодцу/ узлу
   */ 
	public static String shaftInfo = "shaftInfo";


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
		return "";
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

	public int getReportKind(ObjectsReport rp)
	{
		int returnValue = 1;

		if (  rp.field.equals(topology)
			|| rp.field.equals(cableLayout)
			|| rp.field.equals(tunnelCableList)
			|| rp.field.equals(markerInfo)
			|| rp.field.equals(collectorInfo)
			|| rp.field.equals(shaftInfo))
			returnValue = 0;

		return returnValue;
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repAnalysisResults");
	}

	public String getLangForField(String field)
	{
		return LangModelMap.getString(field);
	}

	public List getAvailableReports()
	{
		List result = new ArrayList();

		result.add(topology);
		result.add(cableLayout);
		result.add(tunnelCableList);
		result.add(markerInfo);
		result.add(collectorInfo);
    result.add(shaftInfo);

		return result;
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
		if (rp.field.equals(MapReportModel.cableLayout))
		{
			CableLayoutReport clReport = new CableLayoutReport(rp, divisionsNumber);
			returnValue = new ReportResultsTablePanel(
				clReport.columnModel,
				clReport.tableModel,
				rt.findROforReport(rp));
		}
		else if (rp.field.equals(MapReportModel.tunnelCableList))
		{
			TunnelCableListReport tclReport =
        new TunnelCableListReport(rp, divisionsNumber);
        
			returnValue = new ReportResultsTablePanel(
				tclReport.columnModel,
				tclReport.tableModel,
				rt.findROforReport(rp));
		}
		else if (rp.field.equals(MapReportModel.topology))
		{
      returnValue = new ImagePanel((Image)rp.getReserve(),rt.findROforReport(rp));
		}
    
		return returnValue;
	}

	public void setData(ReportTemplate rt, AMTReport aReport)
	{
		if (rt.templateType.equals(ReportTemplate.rtt_Map))
			super.setData(rt,aReport);
	}
}

