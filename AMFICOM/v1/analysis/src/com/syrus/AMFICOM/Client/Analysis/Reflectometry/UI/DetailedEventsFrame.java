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
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ComplexReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;

public class DetailedEventsFrame extends JInternalFrame
implements EtalonMTMListener,
    CurrentEventChangeListener, PrimaryRefAnalysisListener
{
	private ModelTrace alignedDataMT;

	private Map tModels = new HashMap(6);

	private ATable mainTable;
	private ATable comparativeTable;
	
	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();

	private CompareTableModel ctModel;	

	private JPanel mainPanelComp = new JPanel();
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

		mainTable = new ATable();

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

		ctModel = new CompareTableModel();
		comparativeTable = new ATable (ctModel);
//		jTableComp.getColumnModel().getColumn(0).setPreferredWidth(120);
//		jTableComp.getColumnModel().getColumn(1).setPreferredWidth(100);

		mainPanelComp.setLayout(new BorderLayout());
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

//		updColorModel();
	}

	private void setData()
	{
		int nEvent = Heap.getCurrentEvent1();
		int nEtalon = Heap.getCurrentEtalonEvent1();
		if(Heap.getMTMEtalon() == null || alignedDataMT == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		if(nEvent >= Heap.getMTAEPrimary().getNEvents() || nEvent < 0)
		{
			return;
		}
		double deltaX = Heap.getMTAEPrimary().getDeltaX();

		ComplexReflectogramEvent dataEvent =
			Heap.getMTAEPrimary().getComplexEvents()[nEvent];
		ComplexReflectogramEvent etalonEvent = nEtalon != -1
				? Heap.getMTMEtalon().getMTAE().getComplexEvents()[nEtalon]
				: null;
		int dataType = dataEvent.getEventType();
		int etalonType = etalonEvent != null
			        ? etalonEvent.getEventType()
					: SimpleReflectogramEvent.RESERVED; // 'no event'

		((CompareTableRenderer)comparativeTable.getDefaultRenderer(Object.class))
			.setSameType(dataType == etalonType);

		String dataT = AnalysisUtil.getSimpleEventNameByType(dataType);
		String etalonT = AnalysisUtil.getSimpleEventNameByType(etalonType);

		ctModel.setValueAt(dataT, 0, 1);
		ctModel.setValueAt(etalonT, 1, 1);

		// сравнение по модельной кривой
		ModelTrace etalonMT = Heap.getMTMEtalon().getMTAE().getModelTrace();
		double difference    = ReflectogramComparer.getMaxDeviation(Heap.getMTAEPrimary(), etalonMT, nEvent);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(Heap.getMTAEPrimary(), etalonMT, nEvent);

		difference           = ((int)(difference*1000.))/1000.; // точность 0.001 дБ
		meanDeviation        = ((int)(meanDeviation*1000.))/1000.;

		ctModel.setValueAt(difference + " " + LangModelAnalyse.getString("dB"), 2, 1);
		ctModel.setValueAt(meanDeviation + " " + LangModelAnalyse.getString("dB"), 3, 1);

		// сравнение с эталонным событием
		if(etalonEvent != null) // из равенства следует, что эталонное событие найдено
		{
            double lossDiff  = dataEvent.getMLoss() - etalonEvent.getMLoss();
            double widthDiff = (dataEvent.getLength() - etalonEvent.getLength()) * deltaX;
            double locationDiff = (dataEvent.getBegin() - etalonEvent.getBegin()) * deltaX;

            lossDiff        = ((int)(lossDiff*1000.))/1000.;
            widthDiff       = ((int)(widthDiff*10.))/10.;   // точность 0.1 м
            locationDiff    = ((int)(locationDiff*10.))/10.;

            ctModel.setValueAt(String.valueOf(widthDiff) + " " + LangModelAnalyse.getString("m"), 5, 1);
			ctModel.setValueAt(String.valueOf(locationDiff) + " " + LangModelAnalyse.getString("m"), 6, 1);
            if (etalonEvent.hasMLoss() && dataEvent.hasMLoss())
                ctModel.setValueAt(lossDiff + " " + LangModelAnalyse.getString("dB"), 4, 1);
                else
                    ctModel.setValueAt("--", 4, 1);

		}
		else
		{
			ctModel.setValueAt("--", 4, 1);
			ctModel.setValueAt("--", 5, 1);
			ctModel.setValueAt("--", 6, 1);
		}
		
//		updColorModel(); // XXX: так ли надо перерисовывать всю таблицу?
	}

//	private void updColorModel()
//	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));

//		scrollPaneComp.getViewport().setBackground(SystemColor.window);
//		jTableComp.setBackground(SystemColor.window);
//		jTableComp.setForeground(ColorManager.getColor("textColor"));
//		jTableComp.setGridColor(ColorManager.getColor("tableGridColor"));
//	}

	private void updateTableModel()
	{
		int num = Heap.getCurrentEvent1();
		if (num < 0)
			return;
		TraceEvent ev = Heap.getRefAnalysisPrimary().events[num];
        double res_km = Heap.getBSPrimaryTrace().getResolution() / 1000.0;

		FixedSizeEditableTableModel tModel = null;
		int eventType = ev.getType();
		String eventTypeName = AnalysisUtil.getTraceEventNameByType(eventType);
		switch (eventType)
		{
			case TraceEvent.LINEAR:
				tModel = (FixedSizeEditableTableModel) tModels.get(LINEAR);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.linearData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.linearData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.linearData3()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.linearData4()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.INITIATE:
				tModel = (FixedSizeEditableTableModel) tModels.get(INITIATE);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.initialData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.initialData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						Math.round(ev.initialData2() * res_km * 1000d) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT),
						Math.round(ev.initialData3() * res_km * 1000d) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT)}, 1);
				break;
			case TraceEvent.NON_IDENTIFIED:
				tModel = (FixedSizeEditableTableModel) tModels.get(NOID);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_3(ev.nonidData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.nonidData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.nonidData2()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.CONNECTOR:
				tModel = (FixedSizeEditableTableModel) tModels.get(CONNECTOR);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.connectorData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.connectorData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.connectorData2()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)
					}, 1);
				break;
			case TraceEvent.LOSS:
				tModel = (FixedSizeEditableTableModel) tModels.get(LOSS);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.spliceData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.spliceData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.GAIN:
				tModel = (FixedSizeEditableTableModel) tModels.get(GAIN);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.spliceData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.spliceData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.TERMINATE:
				tModel = (FixedSizeEditableTableModel) tModels.get(TERMINATE);
				tModel.setValueAt(eventTypeName, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.terminateData0()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.terminateData1()) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)
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
		ctModel.clearTable();
		if(Heap.getRefAnalysisPrimary() != null) {
			tabbedPane.setEnabledAt(1, true);
		}
		this.updateTableModel();
	}

	public void etalonMTMRemoved()
	{
		alignedDataMT = null;
		ctModel.clearTable();
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
        ctModel.clearTable();
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);
        setVisible(false);
    }
	
	private class CompareTableRenderer extends ADefaultTableCellRenderer.ObjectRenderer {

		private boolean	sameType	= true;

		public void setSameType(boolean key) {
			sameType = key;
		}

		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int row,
														int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			component.setForeground(sameType || row > 1 ? Color.BLACK : Color.RED);
//			System.out.println("(" + row + " x " + column +") " + value +", " + component.getForeground());

			return component;
		}
	}

	private class CompareTableModel extends AbstractTableModel {

		// String[] columnNames = {LangModelModel.String("parameter") ,
		// LangModelModel.String("value")};

		String[]	columnNames	= { null, null};

		Object[][]	data		= { { LangModelAnalyse.getString("eventType"), "--"},
										{ LangModelAnalyse.getString("etEventType"), "--"},
										{ LangModelAnalyse.getString("maxDeviation"), "--"},
										{ LangModelAnalyse.getString("meanDeviation"), "--"},
										{ LangModelAnalyse.getString("dLoss"), "--"},
										{ LangModelAnalyse.getString("dWidth"), "--"},
										{ LangModelAnalyse.getString("dLocation"), "--"}};

		CompareTableModel() {
			super();
		}

		public void clearTable() {
			for(int i=0;i<this.data.length;i++) {
				this.setValueAt("--", i, 1);
			}
			super.fireTableDataChanged();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(	int row,
									int column) {
			return data[row][column];
		}

		public double getvalueat(	int row,
									int column) {
			return ((Double) (data[row][column])).doubleValue();
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(	int row,
										int col) {
			return false;
		}

		public void setValueAt(	Object value,
								int row,
								int col) {
			this.data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

}
