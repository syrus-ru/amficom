package com.syrus.AMFICOM.client.analysis.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalysisPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScaledGraphPanel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.report.DataStorableElement;

public abstract class AESMPReportModel extends ReportModel
{
	//Названия таблиц для всех модулей (3 анализа + прогноз и моделирование)
	/**
	 * Общая информация
	 */
	public static String COMMON_INFO = "statsFrame";
	/**
	 * Рефлектограмма
	 */
	public static String REFLECTOGRAMM = "analysisFrame";
	/**
	 * Основные характеристики
	 */
	public static String GENERAL_CHARACTERISTICS = "eventsFrame";

	public AESMPReportModel(){
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = ReportType.TABLE;
		if (reportName.equals(AESMPReportModel.REFLECTOGRAMM))
			result = ReportType.GRAPH;
		return result;
	}
	
	@Override
	public RenderingComponent createReport(
			DataStorableElement element,
			Object data,
			ApplicationContext aContext) throws CreateReportException{
		RenderingComponent result = null;
		
		if (data instanceof AbstractTableModel)
		{
//			TableModelDividerReport er = new TableModelDividerReport(rp, divisionsNumber);
//			returnValue = new ReportResultsTablePanel(
//				er.columnModel,
//				er.tableModel,
//				rt.findROforReport(rp));
		}
		else if (data instanceof ScaledGraphPanel)
		{
			BufferedImage image = new BufferedImage(
					element.getWidth(),
					element.getHeight(),
					BufferedImage.TYPE_USHORT_565_RGB);

			ScaledGraphPanel sgPanel = (ScaledGraphPanel)data;
			Dimension oldSize = sgPanel.getSize();
			boolean oldShowMarkers = false;

			if (sgPanel instanceof AnalysisPanel)
			{
				oldShowMarkers = ((AnalysisPanel) sgPanel).show_markers;
				((AnalysisPanel) sgPanel).show_markers = false;
			}

			Graphics imageGraphics = image.getGraphics();
			imageGraphics.setColor(Color.white);
			imageGraphics.fillRect(
					0,
					0,
					element.getWidth(),
					element.getHeight());

			Dimension sizeForRendering = new Dimension(
					element.getWidth() - RenderingComponent.EDGE_SIZE,
					element.getHeight() - RenderingComponent.EDGE_SIZE);
			
			sgPanel.setGraphSize(sizeForRendering);
			sgPanel.paint(image.getGraphics());
			sgPanel.setGraphSize(oldSize);

			if (sgPanel instanceof AnalysisPanel)
				((AnalysisPanel)sgPanel).show_markers = oldShowMarkers;
			
			result = new ImageRenderingComponent(element,image);
		}
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		if (	reportName.equals(COMMON_INFO)
			||	reportName.equals(GENERAL_CHARACTERISTICS)
			||	reportName.equals(REFLECTOGRAMM))
			return LangModelReport.getString("report.Modules.AESMPCommon." + reportName);
		return null;
	}
}
