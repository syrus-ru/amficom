package com.syrus.AMFICOM.client.analysis.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalysisPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ResizableLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleGraphPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.TraceEventsPanel;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.TableModelVerticalDivider;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.io.BellcoreStructure;

public abstract class AESMPReportModel extends ReportModel
{
	//�������� ������ ��� ���� ������� (3 ������� + ������� � �������������)
	/**
	 * ����� ����������
	 */
	public static String COMMON_INFO = "statsFrame";
	/**
	 * ��������������
	 */
	public static String REFLECTOGRAMM = "analysisFrame";
	/**
	 * �������� ��������������
	 */
	public static String GENERAL_CHARACTERISTICS = "eventsFrame";

	private static final String REFLECTOGRAMM_COLOR = "reflectogrammColor";
	
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
			DataStorableElement element,
			Object data,
			ApplicationContext aContext) throws CreateReportException{
		String reportName = element.getReportName();
		String modelClassName = element.getModelClassName();		
		
		RenderingComponent result = null;
		
		if (data instanceof AbstractTableModel) {
			result = TableModelVerticalDivider.createReport(
					(AbstractTableModel)data,
					(TableDataStorableElement)element);
		}
		else if (data instanceof SimpleGraphPanel) {
			SimpleGraphPanel sgPanel = (SimpleGraphPanel)data;
			Dimension oldSize = sgPanel.getSize();			
			Dimension sizeForRendering = new Dimension(
					element.getWidth() - RenderingComponent.EDGE_SIZE,
					element.getHeight() - RenderingComponent.EDGE_SIZE);
			
			boolean oldShowMarkers = false;
			if (sgPanel instanceof AnalysisPanel) {
				oldShowMarkers = ((AnalysisPanel) sgPanel).show_markers;
				((AnalysisPanel) sgPanel).show_markers = false;
			}
			
			sgPanel.setSize(sizeForRendering);
			result = ScaledGraphPanelReport.createReport(element,sgPanel);
			sgPanel.setSize(oldSize);
			if (sgPanel instanceof AnalysisPanel)
				((AnalysisPanel)sgPanel).show_markers = oldShowMarkers;
			
		}
		else if (reportName.equals(REFLECTOGRAMM)) {
			if (!(data instanceof Identifier))
				throw new CreateReportException(
						reportName,
						modelClassName,							
						CreateReportException.WRONG_DATA_TO_INSTALL);
			
			Identifier resultId = (Identifier)data;
			try {
				Result measurementResult = 
					StorableObjectPool.getStorableObject(resultId,true);
				BellcoreStructure bStructure =
					AnalysisUtil.getBellcoreStructureFromResult(
							measurementResult);
				
				TraceEventsPanel sgPanel = new TraceEventsPanel(
						new ResizableLayeredPanel(),
						bStructure.getTraceData(),
						bStructure.getResolution());
				IntDimension elementSize = element.getSize();
				sgPanel.setColorModel(REFLECTOGRAMM_COLOR);
				sgPanel.setSize(
						elementSize.getWidth(),
						elementSize.getHeight());
				sgPanel.setPreferredSize(sgPanel.getSize());
				sgPanel.setDefaultScales();
				
				//TODO � ��� ��� JFrame?
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
		// TODO ������-��, ��� ���������� ������ ��������� �
		// ������ LangModel'�� �, ��������������, ������ ������
		//���� � ������� ������� - �����������
		if (	reportName.equals(COMMON_INFO)
			||	reportName.equals(GENERAL_CHARACTERISTICS)
			||	reportName.equals(REFLECTOGRAMM))
			return LangModelReport.getString("report.Modules.AESMPCommon." + reportName);
		return null;
	}
}
