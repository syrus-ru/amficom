package com.syrus.AMFICOM.Client.Map.Report;
/*
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ImagePanel;
import com.syrus.AMFICOM.Client.General.Report.ReportResultsTablePanel;
*/

public class MapReportModel {
	// @todo wait for reportbuilder classes from generalclient
}
//extends APOReportModel
//{
//	public String getName()
//	{
//		return "MapReportModel";
//	}
//
//	// ???????? ??????? ??? ?????
//  /*
//   * ???????? ?????
//   */ 
//	public static String topology = "menuMap";
//  /*
//   * ????????? ?????? ?? ???????? (?????? ???????? ? ????????)
//   */ 
//	public static String cableLayout = "mapcablepathelement";
//  /*
//   * ?????? ??????? ?? ??????? + ?? ???????????? ? ???????
//   */ 
//	public static String tunnelCableList = "tunnelCableList";
//  /*
//   * ?????????? ? ??????? (??????, ???????, ????? ? ???????,
//   * ?????????? ?? ?????, ?????????????? ??????????)
//   */ 
//	public static String markerInfo = "markerInfo";
//  /*
//   * ?????????? ? ?????????? (?????? ???????, ???????? + ?????)
//   */ 
//	public static String collectorInfo = "collectorInfo";
//  /*
//   * ?????????? ?? ???????/ ????
//   */ 
//	public static String shaftInfo = "shaftInfo";
//
//
//	public String getReportsName(ObjectsReport rp)
//	{
//		String returnValue = this.getObjectsName() + ":"
//			+ getLangForField(rp.field);
//		if (rp.reserveName != null)
//			returnValue += rp.reserveName;
//
//		return returnValue;
//	}
//
//	public String getReportsReserveName(ObjectsReport rp) throws
//		CreateReportException
//	{
//		return "";
//	}
//
//	public int getReportKind(ObjectsReport rp)
//	{
//		int returnValue = 1;
//
//		if (  rp.field.equals(topology)
//			|| rp.field.equals(cableLayout)
//			|| rp.field.equals(tunnelCableList)
//			|| rp.field.equals(markerInfo)
//			|| rp.field.equals(collectorInfo)
//			|| rp.field.equals(shaftInfo))
//			returnValue = 0;
//
//		return returnValue;
//	}
//
//	public String getObjectsName()
//	{
//		return LangModelReport.getString("label_repAnalysisResults");
//	}
//
//	public String getLangForField(String field)
//	{
//		return LangModelMap.getString(field);
//	}
//
//	public List getAvailableReports()
//	{
//		List result = new ArrayList();
//
//		result.add(topology);
//		result.add(cableLayout);
//		result.add(tunnelCableList);
//		result.add(markerInfo);
//		result.add(collectorInfo);
//    result.add(shaftInfo);
//
//		return result;
//	}
//
//	public JComponent createReport(
//		ObjectsReport rp,
//		int divisionsNumber,
//		ReportTemplate rt,
//		ApplicationContext aContext,
//		boolean fromAnotherModule)
//
//		throws CreateReportException
//	{
//		if (rp.getReserve() == null)
//			throw new CreateReportException(rp.getName(),
//				CreateReportException.cantImplement);
//
//		JComponent returnValue = null;
//		if (rp.field.equals(MapReportModel.cableLayout))
//		{
//			CableLayoutReport clReport = new CableLayoutReport(rp, divisionsNumber);
//			returnValue = new ReportResultsTablePanel(
//				clReport.columnModel,
//				clReport.tableModel,
//				rt.findROforReport(rp));
//		}
//		else if (rp.field.equals(MapReportModel.tunnelCableList))
//		{
//			TunnelCableListReport tclReport =
//        new TunnelCableListReport(rp, divisionsNumber);
//        
//			returnValue = new ReportResultsTablePanel(
//				tclReport.columnModel,
//				tclReport.tableModel,
//				rt.findROforReport(rp));
//		}
//		else if (rp.field.equals(MapReportModel.topology))
//		{
//      returnValue = new ImagePanel((Image)rp.getReserve(),rt.findROforReport(rp));
//		}
//    
//		return returnValue;
//	}
//
//	public void setData(ReportTemplate rt, AMTReport aReport)
//	{
//		if (rt.templateType.equals(ReportTemplate.rtt_Map))
//			super.setData(rt,aReport);
//	}
//}
//
