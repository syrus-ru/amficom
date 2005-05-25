package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class DetailedEventsFrame extends JInternalFrame
implements EtalonMTMListener,
    CurrentEventChangeListener, PrimaryRefAnalysisListener
{
	private ModelTrace alignedDataMT;

	private Map tModels = new HashMap(6);

	private JTable mainTable;
	private JTable comparativeTable;
	
	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();

	private DefaultTableModel ctModel;	

	private JPanel mainPanelComp = new JPanel(new BorderLayout());
	private JScrollPane scrollPaneComp = new JScrollPane();
	private JViewport viewportComp = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	// these are just internally-used keys
	private static final String LINEAR = "linear";
	private static final String INITIATE = "initiate";
	private static final String NOID = "noid";
	private static final String CONNECTOR = "connector";
	private static final String LOSS = "loss";
	private static final String GAIN = "gain";
	private static final String TERMINATE = "terminate";

	public DetailedEventsFrame()
	{
		this.init();
		this.initModule();
	}

	private void initModule()
	{
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
        Heap.addPrimaryRefAnalysisListener(this);
	}

	private void makeAlignedDataMT()
	{
		// XXX: is alignment really required? Should it be performed here?
		if (Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null) {
			alignedDataMT = null;
		} else {
			alignedDataMT = ReflectogramMath.createAlignedArrayModelTrace(
			Heap.getMTAEPrimary().getModelTrace(),
			Heap.getMTMEtalon().getMTAE().getModelTrace());
		}
	}

	private void init() {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		FixedSizeEditableTableModel linearModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventRMSDeviation"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(LINEAR, linearModel);

		FixedSizeEditableTableModel initialModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventEDZ"),
						LangModelAnalyse.getString("eventADZ")
				},
				null);
		tModels.put(INITIATE, initialModel);

		FixedSizeEditableTableModel noidModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventMaxLevel"),
						LangModelAnalyse.getString("eventMinLevel"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(NOID, noidModel);

		FixedSizeEditableTableModel connectorModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventReflectionLevel")
				},
				null);
		tModels.put(CONNECTOR, connectorModel);

		FixedSizeEditableTableModel spliceModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel")
				},
				null);
		tModels.put(LOSS, spliceModel);
		tModels.put(GAIN, spliceModel);

		FixedSizeEditableTableModel terminateModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"),
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventReflectionLevel")
				},
				null);
		tModels.put(TERMINATE, terminateModel);

		this.mainTable = new JTable();

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

//		setContentPane(mainPanel);
//		this.setSize(new Dimension(200, 200));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("eventDetailedTableTitle"));

		tabbedPane.add(LangModelAnalyse.getString("Title.mains"), mainPanel);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
//		jTable.setMinimumSize(new Dimension(200, 200));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(mainTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add(LangModelAnalyse.getString("Title.comparatives"), mainPanelComp);

		this.ctModel = new DefaultTableModel(new Object[][]{ { LangModelAnalyse.getString("eventType"), "--"},
			{ LangModelAnalyse.getString("etEventType"), "--"},
			{ LangModelAnalyse.getString("maxDeviation"), "--"},
			{ LangModelAnalyse.getString("meanDeviation"), "--"},
			{ LangModelAnalyse.getString("dLoss"), "--"},
			{ LangModelAnalyse.getString("dWidth"), "--"},
			{ LangModelAnalyse.getString("dLocation"), "--"}}, 
			new Object[] {"", ""}
		) {
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
		
		this.comparativeTable = new JTable(this.ctModel);
		this.comparativeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		jTableComp.getColumnModel().getColumn(0).setPreferredWidth(120);
//		jTableComp.getColumnModel().getColumn(1).setPreferredWidth(100);

		mainPanelComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneComp.setViewport(viewportComp);
		scrollPaneComp.setAutoscrolls(true);
//		jTableComp.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTableComp.setMinimumSize(new Dimension(200, 213));
		comparativeTable.setDefaultRenderer(Object.class, new CompareTableRenderer());

		mainPanelComp.add(scrollPaneComp, BorderLayout.CENTER);
		scrollPaneComp.getViewport().add(comparativeTable);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);

	}

	private void setData()
	{
		int nEvent = Heap.getCurrentEvent1();
		int nEtalon = Heap.getCurrentEtalonEvent1();
		if(Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null || alignedDataMT == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		double deltaX = Heap.getMTAEPrimary().getDeltaX();

//		ComplexReflectogramEvent dataEvent =
//			Heap.getMTAEPrimary().getComplexEvents()[nEvent];
//		ComplexReflectogramEvent etalonEvent = nEtalon != -1
//				? Heap.getMTMEtalon().getMTAE().getComplexEvents()[nEtalon]
//				: null;
        DetailedEvent dataEvent = nEvent != -1
            ? Heap.getMTAEPrimary().getDetailedEvents()[nEvent]
            : null;
        DetailedEvent etalonEvent = nEtalon != -1
                ? Heap.getMTMEtalon().getMTAE().getDetailedEvents()[nEtalon]
                : null;
		int dataType = dataEvent != null
                    ? dataEvent.getEventType()
                    : SimpleReflectogramEvent.RESERVED; // 'no event'
		int etalonType = etalonEvent != null
			        ? etalonEvent.getEventType()
					: SimpleReflectogramEvent.RESERVED; // 'no event'

        // выбираем, какое событие считать основным
        DetailedEvent ev = dataEvent != null ? dataEvent : etalonEvent;
		((CompareTableRenderer)comparativeTable.getDefaultRenderer(Object.class))
			.setSameType(dataType == etalonType && dataType != SimpleReflectogramEvent.RESERVED);

		String dataT = AnalysisUtil.getSimpleEventNameByType(dataType);
		String etalonT = AnalysisUtil.getSimpleEventNameByType(etalonType);

		ctModel.setValueAt(dataT, 0, 1);
		ctModel.setValueAt(etalonT, 1, 1);

		// сравнение по модельной кривой
		ModelTrace etalonMT = Heap.getMTMEtalon().getMTAE().getModelTrace();
        if (ev.getBegin() < Heap.getMTAEPrimary().getModelTrace().getLength()
            && ev.getBegin() < etalonMT.getLength())
        {
    		double difference    = ReflectogramComparer.getMaxDeviation(Heap.getMTAEPrimary(), etalonMT, ev);
    		double meanDeviation = ReflectogramComparer.getMeanDeviation(Heap.getMTAEPrimary(), etalonMT, ev);
    
    		difference           = ((int)(difference*1000.))/1000.; // точность 0.001 дБ
    		meanDeviation        = ((int)(meanDeviation*1000.))/1000.;
    
    		ctModel.setValueAt(difference + " " + LangModelAnalyse.getString("dB"), 2, 1);
    		ctModel.setValueAt(meanDeviation + " " + LangModelAnalyse.getString("dB"), 3, 1);
        }
        else
        {
            ctModel.setValueAt("--", 2, 1);
            ctModel.setValueAt("--", 3, 1);
        }

		// сравнение с эталонным событием
		if(dataEvent != null && etalonEvent != null)
		{
            String value;

            try {
                double lossDiff = DetailedEventUtil.
                    getLossDiff(dataEvent,etalonEvent);
                lossDiff = ((int)(lossDiff*1000.))/1000.;
                value = lossDiff + " " + LangModelAnalyse.getString("dB");
            } catch (NoSuchFieldException e) {
                value = "--";
            }
            ctModel.setValueAt(value, 4, 1);

            double widthDiff =
                DetailedEventUtil.getWidthDiff(dataEvent, etalonEvent) * deltaX;
            widthDiff = ((int)(widthDiff*10.))/10.;   // точность 0.1 м
            value = String.valueOf(widthDiff) + " " + LangModelAnalyse.getString("m");
            ctModel.setValueAt(value, 5, 1);

            double locationDiff = (dataEvent.getBegin() - etalonEvent.getBegin()) * deltaX;
            locationDiff    = ((int)(locationDiff*10.))/10.;
            value = String.valueOf(locationDiff) + " " + LangModelAnalyse.getString("m");
			ctModel.setValueAt(value, 6, 1);
		}
		else
		{
			ctModel.setValueAt("--", 4, 1);
			ctModel.setValueAt("--", 5, 1);
			ctModel.setValueAt("--", 6, 1);
		}
	}
	
	private void updateTableModel()
	{
		int num = Heap.getCurrentEvent1();
		if (num < 0) {
            // FIXME: set empty table 
            return;
        }
		DetailedEvent ev = Heap.getMTAEPrimary().getDetailedEvents()[num];
        double resMt =  Heap.getBSPrimaryTrace().getResolution();
        double resKm = resMt / 1000.0;

		FixedSizeEditableTableModel tModel = null;
		int eventType = ev.getEventType();
		String eventTypeName = AnalysisUtil.getSimpleEventNameByType(eventType);
        String vCol1 = String.valueOf(num + 1);
        String vCol2 = MathRef.round_3(DetailedEventUtil.getWidth(ev) * resKm)
            + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM);
        String sDB = " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB);
        String sMT = " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT);
		switch (eventType)
		{
			case SimpleReflectogramEvent.LINEAR:
				tModel = (FixedSizeEditableTableModel) tModels.get(LINEAR);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
					vCol1,
                    vCol2,
					-MathRef.round_2(((LinearDetailedEvent)ev).getY0()) + sDB,
					-MathRef.round_2(((LinearDetailedEvent)ev).getY1()) + sDB,
					MathRef.round_3(((LinearDetailedEvent)ev).getRmsDev()) + sDB,
					MathRef.round_3(((LinearDetailedEvent)ev).getMaxDev()) + sDB
				}, 1);
				break;
			case SimpleReflectogramEvent.DEADZONE:
				tModel = (FixedSizeEditableTableModel) tModels.get(INITIATE);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                    vCol1,
                    vCol2,
					-MathRef.round_2(((DeadZoneDetailedEvent)ev).getPo()) + sDB,
					-MathRef.round_2(((DeadZoneDetailedEvent)ev).getY1()) + sDB,
					Math.round(((DeadZoneDetailedEvent)ev).getEdz() * resMt) + sMT,
					Math.round(((DeadZoneDetailedEvent)ev).getAdz() * resMt) + sMT
			    }, 1);
				break;
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				tModel = (FixedSizeEditableTableModel) tModels.get(NOID);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                    vCol1,
                    vCol2,
					-MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getYMin()) + sDB,
					-MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getYMax()) + sDB,
					MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getMaxDev()) + sDB
                }, 1);
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				tModel = (FixedSizeEditableTableModel) tModels.get(CONNECTOR);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                        vCol1,
                        vCol2,
						-MathRef.round_2(((ConnectorDetailedEvent)ev).getY0()) + sDB,
						-MathRef.round_2(((ConnectorDetailedEvent)ev).getY1()) + sDB,
						-MathRef.round_2(((ConnectorDetailedEvent)ev).getY2()) + sDB
					}, 1);
				break;
			case SimpleReflectogramEvent.LOSS:
				tModel = (FixedSizeEditableTableModel) tModels.get(LOSS);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                        vCol1,
                        vCol2,
                        -MathRef.round_2(((SpliceDetailedEvent)ev).getY0()) + sDB,
                        -MathRef.round_2(((SpliceDetailedEvent)ev).getY1()) + sDB
				    }, 1);
				break;
			case SimpleReflectogramEvent.GAIN:
				tModel = (FixedSizeEditableTableModel) tModels.get(GAIN);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                        vCol1,
                        vCol2,
                        -MathRef.round_2(((SpliceDetailedEvent)ev).getY0()) + sDB,
                        -MathRef.round_2(((SpliceDetailedEvent)ev).getY1()) + sDB
				    }, 1);
				break;
			case SimpleReflectogramEvent.ENDOFTRACE:
				tModel = (FixedSizeEditableTableModel) tModels.get(TERMINATE);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
                        vCol1,
                        vCol2,
                        -MathRef.round_2(((EndOfTraceDetailedEvent)ev).getY0()) + sDB,
                        -MathRef.round_2(((EndOfTraceDetailedEvent)ev).getY2()) + sDB
					}, 1);
				break;
			}
		mainTable.setModel(tModel);
		mainTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		this.comparativeTable.repaint();
		this.comparativeTable.revalidate();
	}

	public void etalonMTMCUpdated()
	{
        makeAlignedDataMT();
		this.clearCTModelValues();
		if(Heap.getRefAnalysisPrimary() != null) {
			tabbedPane.setEnabledAt(1, true);
		}
		this.updateTableModel();
	}

	public void etalonMTMRemoved()
	{
		alignedDataMT = null;
		this.clearCTModelValues();
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
		this.mainTable.repaint();
		this.mainTable.revalidate();
	}

	public void currentEventChanged()
	{
		this.updateTableModel();
		this.setData();
	}

    public void primaryRefAnalysisCUpdated() {
        makeAlignedDataMT();
        if (Heap.getMTMEtalon() != null)
            tabbedPane.setEnabledAt(1, true);
        this.updateTableModel();
        setVisible(true);
    }

    public void primaryRefAnalysisRemoved() {
        alignedDataMT = null;
		this.clearCTModelValues();
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);
        setVisible(false);
    }
	
	private void clearCTModelValues() {
		for(int i=0;i<this.ctModel.getRowCount();i++) {
			this.ctModel.setValueAt("--", i, 1);
		}
	}
	
	private class CompareTableRenderer extends ADefaultTableCellRenderer.ObjectRenderer {

		private boolean	sameType	= true;

		public void setSameType(boolean key) {
			sameType = key;
		}

		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected1,
														boolean hasFocus,
														int row,
														int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected1, hasFocus, row, column);

			component.setForeground(sameType || row > 1 ? Color.BLACK : Color.RED);

			return component;
		}
	}
}
