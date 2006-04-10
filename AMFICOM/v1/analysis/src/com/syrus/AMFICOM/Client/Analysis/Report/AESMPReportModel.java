package com.syrus.AMFICOM.Client.Analysis.Report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalysisPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ResizableLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TraceEventsPanel;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.TableModelVerticalDivider;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.io.BellcoreStructure;

public abstract class AESMPReportModel extends ReportModel
{
	//Ќазвани€ таблиц дл€ всех модулей (3 анализа + прогноз и моделирование)
	public static String COMMON_INFO = "Menu.Window.statsFrame";
	public static String REFLECTOGRAMM = "Menu.Window.analysisFrame";
	public static String GENERAL_CHARACTERISTICS = "Menu.Window.eventsFrame";

	private static final String REFLECTOGRAMM_COLOR = "ReflectogrammColor";
	
	public AESMPReportModel(){
		UIManager.put(REFLECTOGRAMM_COLOR,Color.BLUE);		
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
			AbstractDataStorableElement element,
			Object data,
			ApplicationContext aContext) throws CreateReportException{
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();		
		
		RenderingComponent result = null;
		
		if (data instanceof AbstractTableModel) {
			result = TableModelVerticalDivider.createReport(
					(AbstractTableModel)data,
					(TableDataStorableElement) element);
		}
		else if (data instanceof ResizableLayeredPanel) {
			ResizableLayeredPanel lPanel = (ResizableLayeredPanel)data;
			
			Dimension oldSize = lPanel.getMainPane().getSize();
			
			Dimension sizeForRendering = new Dimension(
					element.getWidth() - RenderingComponent.EDGE_SIZE,
					element.getHeight() - RenderingComponent.EDGE_SIZE);
			
			boolean oldShowMarkers = false;
			if (lPanel.getTopPanel() instanceof AnalysisPanel) {
				oldShowMarkers = ((AnalysisPanel)lPanel.getTopPanel()).show_markers;
				((AnalysisPanel) lPanel.getTopPanel()).show_markers = false;
			}
			
			lPanel.getMainPane().setSize(sizeForRendering);
			lPanel.resize();
			
			result = ScaledGraphPanelReport.createReport(element,lPanel.getMainPane());
			lPanel.getMainPane().setSize(oldSize);
			lPanel.resize();
			
			if (lPanel.getTopPanel() instanceof AnalysisPanel) {
				((AnalysisPanel) lPanel.getTopPanel()).show_markers = oldShowMarkers;
			}
			
		}
		else if (reportName.equals(REFLECTOGRAMM)) {
			if (!(data instanceof Identifier))
				throw new CreateReportException(
						reportName,
						modelClassName,							
						CreateReportException.WRONG_DATA_TO_INSTALL);
			
			Identifier measurementId = (Identifier)data;
			try {
				Result measurementResult = null;
				StorableObjectCondition condition2 = new LinkedIdsCondition(measurementId, ObjectEntities.RESULT_CODE);
				Set<Result> resultSet = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
				Iterator<Result> resultsIterator = resultSet.iterator();
				if (resultsIterator.hasNext())
					measurementResult = resultsIterator.next();
				
				if (measurementResult == null)
					throw new CreateReportException(
							reportName,
							modelClassName,							
							CreateReportException.ERROR_GETTING_FROM_POOL);
				
				BellcoreStructure bStructure =
					AnalysisUtil.getBellcoreStructureFromResult(
							measurementResult);
				
				TraceEventsPanel sgPanel = new TraceEventsPanel(
						new ResizableLayeredPanel(),
						bStructure.getTraceData(),
						bStructure.getResolution(),
						false);
				IntDimension elementSize = element.getSize();
				sgPanel.setColorModel(REFLECTOGRAMM_COLOR);
				sgPanel.setSize(
						elementSize.getWidth(),
						elementSize.getHeight());
				sgPanel.setPreferredSize(sgPanel.getSize());
				sgPanel.setDefaultScales();
				
				//TODO ј как без JFrame?
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				JFrame frame = new JFrame();
				frame.setLocation(screenSize.width,screenSize.height);
				frame.setSize(elementSize.getWidth(),elementSize.getHeight());
				frame.setPreferredSize(frame.getSize());
				frame.setLayout(null);
				frame.add(sgPanel);
				frame.setVisible(true);
				
				result = ScaledGraphPanelReport.createReport(
						element,
						sgPanel);
				
				frame.dispose();				
			} catch (ApplicationException e) {
				throw new CreateReportException(
						reportName,
						modelClassName,							
						CreateReportException.ERROR_GETTING_FROM_POOL);
			} catch (SimpleApplicationException e) {
				throw new CreateReportException(
						reportName,
						modelClassName,							
						CreateReportException.ERROR_GETTING_FROM_POOL);
			}
		}
		
		if (result == null)
			throw new CreateReportException(
					reportName,
					modelClassName,							
					CreateReportException.WRONG_DATA_TO_INSTALL);
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		if (	reportName.equals(COMMON_INFO)
			||	reportName.equals(GENERAL_CHARACTERISTICS)
			||	reportName.equals(REFLECTOGRAMM))
			return I18N.getString(reportName);
		throw new IllegalArgumentException(reportName);
	}
}
