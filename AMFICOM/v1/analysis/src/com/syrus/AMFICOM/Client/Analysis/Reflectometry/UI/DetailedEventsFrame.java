package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;
import com.syrus.io.BellcoreStructure;

public class DetailedEventsFrame extends JInternalFrame
implements OperationListener, bsHashChangeListener, EtalonMTMListener
{
	private ModelTraceManager etalonMTM;
	private ModelTraceManager dataMTM;
	private ModelTrace alignedDataMT;

	private Map tModels = new HashMap(6);

	private ATable jTable;
	private int selected = 0;
	private RefAnalysis a;
	private BellcoreStructure bs;
	private double res_km;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	boolean analysis_performed = false;
	boolean etalon_loaded = false;

	private CompareTableModel ctModel;
	private ATable jTableComp;

	BorderLayout borderLayoutComp = new BorderLayout();
	JPanel mainPanelComp = new JPanel();
	JScrollPane scrollPaneComp = new JScrollPane();
	JViewport viewportComp = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private static String linear = EventsFrame.LINEAR;
	private static String connector = EventsFrame.CONNECTOR; 
	private static String gain = EventsFrame.GAIN;
	private static String loss = EventsFrame.LOSS;
	private static String initiate = EventsFrame.INITIATE;
	private static String terminate = EventsFrame.TERMINATE;
	private static String noid = EventsFrame.NO_ID;

	public DetailedEventsFrame()
	{
		this(new Dispatcher());
	}

	public DetailedEventsFrame(Dispatcher dispatcher)
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		dispatcher.register(this, RefUpdateEvent.typ);
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	private void makeAlignedDataMT()
	{
		if (etalonMTM == null || dataMTM == null)
			return;
		alignedDataMT = ReflectogramMath.createAlignedArrayModelTrace(
			dataMTM.getModelTrace(),
			etalonMTM.getModelTrace());
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if (rue.analysisPerformed())
			{
				String id = (String)(rue.getSource());
				if (id.equals(Heap.PRIMARY_TRACE_KEY))
				{
					if (Heap.getRefAnalysisByKey(id) != null)
					{
						a = Heap.getRefAnalysisByKey(id);
						bs = Heap.getAnyBSTraceByKey(id);
						res_km = bs.getResolution() / 1000.0;
						dataMTM = Heap.getMTMByKey(Heap.PRIMARY_TRACE_KEY);
						if(dataMTM != null && etalonMTM != null)
							makeAlignedDataMT();
						else alignedDataMT = null;
						if (selected >= a.events.length)
							selected = a.events.length - 1;
						updateTableModel (selected);
					}
				}
				if (Heap.hasEventParamsForPrimaryTrace())
					analysis_performed = true;
				if(etalon_loaded)
					tabbedPane.setEnabledAt(1, true);
			}
			if (rue.eventSelected())
			{
				selected = Integer.parseInt((String)rue.getSource());
				updateTableModel (selected);
				setData(selected);
			}
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		FixedSizeEditableTableModel linearModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventRMSDeviation"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(linear, linearModel);

		FixedSizeEditableTableModel initialModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventEDZ"),
						LangModelAnalyse.getString("eventADZ")
				},
				null);
		tModels.put(initiate, initialModel);

		FixedSizeEditableTableModel noidModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventMaxLevel"),
						LangModelAnalyse.getString("eventMinLevel"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(noid, noidModel);

		FixedSizeEditableTableModel connectorModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventReflectionLevel")
						//LangModelAnalyse.getString("eventFormFactor") // removed by saa 
				},
				null);
		tModels.put(connector, connectorModel);

		FixedSizeEditableTableModel spliceModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel")
				},
				null);
		tModels.put(loss, spliceModel);
		tModels.put(gain, spliceModel);

		FixedSizeEditableTableModel terminateModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventReflectionLevel")
						//LangModelAnalyse.getString("eventFormFactor") // removed by saa
				},
				null);
		tModels.put(terminate, terminateModel);

		jTable = new ATable();

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

//		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 200));
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

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		jTable.setMinimumSize(new Dimension(200, 200));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add(LangModelAnalyse.getString("Title.comparatives"), mainPanelComp);

		ctModel = new CompareTableModel();
		jTableComp = new ATable (ctModel);
		jTableComp.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTableComp.getColumnModel().getColumn(1).setPreferredWidth(100);

		mainPanelComp.setLayout(new BorderLayout());
		mainPanelComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneComp.setViewport(viewportComp);
		scrollPaneComp.setAutoscrolls(true);
		jTableComp.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTableComp.setMinimumSize(new Dimension(200, 213));
		jTableComp.setDefaultRenderer(Object.class, new CompareTableRenderer());

		mainPanelComp.add(scrollPaneComp, BorderLayout.CENTER);
		scrollPaneComp.getViewport().add(jTableComp);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);

		updColorModel();
	}


	private void setData(int nEvent)
	{
		if(etalonMTM == null || alignedDataMT == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		if(nEvent >= dataMTM.getNEvents() || nEvent < 0)
		{
			return;
		}
		double deltaX = dataMTM.getDeltaX();

		// ищем парное событие
		ComplexReflectogramEvent[] dataCRE = dataMTM.getComplexEvents();
		ComplexReflectogramEvent[] etalonCRE = etalonMTM.getComplexEvents(); 
		ReflectogramComparer rComp = new ReflectogramComparer(
			dataCRE,
			etalonCRE);
		int nEtalon = rComp.getEtalonIdByProbeId(nEvent); // может быть -1
		ComplexReflectogramEvent dataEvent = dataCRE[nEvent];
		ComplexReflectogramEvent etalonEvent = nEtalon != -1
				? etalonCRE[nEtalon]
				: null;
		int dataType = dataEvent.getEventType();
		int etalonType = etalonEvent != null
			        ? etalonEvent.getEventType()
					: SimpleReflectogramEvent.RESERVED;

		((CompareTableRenderer)jTableComp.getDefaultRenderer(Object.class))
			.setSameType(dataType == etalonType);

		String dataT;
		switch(dataType)
		{
		case SimpleReflectogramEvent.REFLECTIVE:
		    dataT = LangModelAnalyse.getString("eventType4");
			break;
		case SimpleReflectogramEvent.GAIN:
			// fall through
		case SimpleReflectogramEvent.LOSS:
		    dataT = LangModelAnalyse.getString("eventType3");
			break;
		case SimpleReflectogramEvent.LINEAR:
		    dataT = LangModelAnalyse.getString("eventType0");
			break;
		default:
		    dataT = LangModelAnalyse.getString("eventTypeUnk");
		}

		String etalonT;
		switch(etalonType)
		{
		case SimpleReflectogramEvent.REFLECTIVE:
		    etalonT = LangModelAnalyse.getString("eventType4");
			break;
		case SimpleReflectogramEvent.GAIN:
			// fall through
		case SimpleReflectogramEvent.LOSS:
		    etalonT = LangModelAnalyse.getString("eventType3");
			break;
		case SimpleReflectogramEvent.LINEAR:
		    etalonT = LangModelAnalyse.getString("eventType0");
			break;
		default:
		    etalonT = LangModelAnalyse.getString("eventTypeUnk");
		}

		ctModel.setValueAt(dataT, 0, 1);
		ctModel.setValueAt(etalonT, 1, 1);

		// сравнение по модельной кривой
		ModelTrace etalonMT = etalonMTM.getModelTrace();
		double difference    = ReflectogramComparer.getMaxDeviation(dataMTM, etalonMT, nEvent);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(dataMTM, etalonMT, nEvent);

		difference           = ((int)(difference*1000.))/1000.; // точность 0.001 дБ
		meanDeviation        = ((int)(meanDeviation*1000.))/1000.;

		ctModel.setValueAt(difference + " " + LangModelAnalyse.getString("dB"), 2, 1);
		ctModel.setValueAt(meanDeviation + " " + LangModelAnalyse.getString("dB"), 3, 1);

		// сравнение с эталонным событием
		if(etalonEvent != null) // из равенства следует, что эталонное событие найдено
		{
			double lossDiff  = dataEvent.getMLoss() - etalonEvent.getMLoss();
			double widthDiff = dataEvent.getLength() - etalonEvent.getLength();
			double locationDiff = (dataEvent.getBegin() - etalonEvent.getBegin()) * deltaX; // изменено с середины события на начало

			lossDiff        = ((int)(lossDiff*1000.))/1000.;
			widthDiff       = ((int)(widthDiff*1.))/1.;	// точность 1 м
			locationDiff    = ((int)(locationDiff*1.))/1.;

			ctModel.setValueAt(lossDiff + " " + LangModelAnalyse.getString("dB"), 4, 1);
			ctModel.setValueAt(String.valueOf(widthDiff) + " " + LangModelAnalyse.getString("m"), 5, 1);
			ctModel.setValueAt(String.valueOf(locationDiff) + " " + LangModelAnalyse.getString("m"), 6, 1);
		}
		else
		{
			ctModel.setValueAt("--", 4, 1);
			ctModel.setValueAt("--", 5, 1);
			ctModel.setValueAt("--", 6, 1);
		}
		
		updColorModel(); // XXX: так ли надо перерисовывать всю таблицу?
	}

	private void updColorModel()
	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));

//		scrollPaneComp.getViewport().setBackground(SystemColor.window);
//		jTableComp.setBackground(SystemColor.window);
//		jTableComp.setForeground(ColorManager.getColor("textColor"));
//		jTableComp.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	private void updateTableModel(int num)
	{
		if (num <0 || this.a == null || num >= a.events.length)
			return;
		TraceEvent ev = a.events[num];

		FixedSizeEditableTableModel tModel = null;
		switch (ev.getType())
		{
			case TraceEvent.LINEAR:
				tModel = (FixedSizeEditableTableModel) tModels.get(linear);
				tModel.setValueAt(linear, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.data[3]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.data[4]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.INITIATE:
				tModel = (FixedSizeEditableTableModel) tModels.get(initiate);
				tModel.setValueAt(initiate, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						Math.round(ev.data[2] * res_km * 1000d) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT),
						Math.round(ev.data[3] * res_km * 1000d) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT)}, 1);
				break;
			case TraceEvent.NON_IDENTIFIED:
				tModel = (FixedSizeEditableTableModel) tModels.get(noid);
				tModel.setValueAt(noid, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_3(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_3(ev.data[2]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.CONNECTOR:
				tModel = (FixedSizeEditableTableModel) tModels.get(connector);
				tModel.setValueAt(connector, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[2]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)
				// String.valueOf(MathRef.round_3(ev.data[3])) // removed by saa
					}, 1);
				break;
			case TraceEvent.LOSS:
				tModel = (FixedSizeEditableTableModel) tModels.get(loss);
				tModel.setValueAt(loss, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.GAIN:
				tModel = (FixedSizeEditableTableModel) tModels.get(gain);
				tModel.setValueAt(gain, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)},
					1);
				break;
			case TraceEvent.TERMINATE:
				tModel = (FixedSizeEditableTableModel) tModels.get(terminate);
				tModel.setValueAt(terminate, 0, 0);
				tModel.updateColumn(new Object[] {
						String.valueOf(num + 1),
						MathRef.round_3((ev.last_point - ev.first_point) * res_km) + " "
								+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
						MathRef.round_2(ev.data[0]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
						MathRef.round_2(ev.data[1]) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB)
				// String.valueOf(MathRef.round_3(ev.data[2])) // removed by saa
					}, 1);
				break;
			}
		jTable.setModel(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		jTable.updateUI();
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(Heap.PRIMARY_TRACE_KEY))
		{
			alignedDataMT = null;
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(0, true);
			tabbedPane.setEnabledAt(1, false);
			setVisible(true);
		}
	}

	public void bsHashRemoved(String key)
	{
		if(key.equals(AnalysisUtil.ETALON))
		{
			alignedDataMT = null;
			ctModel.clearTable();
			tabbedPane.setEnabledAt(0, true);
			tabbedPane.setSelectedIndex(0);
			etalon_loaded = false;
			tabbedPane.setEnabledAt(1, false);
		}
	}

	public void bsHashRemovedAll()
	{
		alignedDataMT = null;
		ctModel.clearTable();
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(0, true);
		analysis_performed = false;
		tabbedPane.setEnabledAt(1, false);
		setVisible(false);
	}

	public void etalonMTMCUpdated()
	{
		etalonMTM = Heap.getMTMByKey(AnalysisUtil.ETALON);
		if(dataMTM != null)
			makeAlignedDataMT();
		else
			alignedDataMT = null;
		ctModel.clearTable();
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setSelectedIndex(0);
		etalon_loaded = true;
		if(analysis_performed)
			tabbedPane.setEnabledAt(1, true);
	}

	public void etalonMTMRemoved()
	{
		alignedDataMT = null;
		ctModel.clearTable();
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setSelectedIndex(0);
		etalon_loaded = false;
		tabbedPane.setEnabledAt(1, false);
	}
}



class CompareTableModel extends AbstractTableModel
{
//  String[] columnNames = {LangModelModel.String("parameter") ,
//    LangModelModel.String("value")};

	String[] columnNames = {null ,
		null};

	Object[][] data = {
		{LangModelAnalyse.getString("eventType"), "--"},
		{LangModelAnalyse.getString("etEventType"), "--"},
		{LangModelAnalyse.getString("maxDeviation"), "--"},
		{LangModelAnalyse.getString("meanDeviation"), "--"},
		{LangModelAnalyse.getString("delta"), "--"},
		{LangModelAnalyse.getString("dWidth"), "--"},
		{LangModelAnalyse.getString("dLocation"), "--"}
	};

	CompareTableModel()
	{
		super();
	}

	public void clearTable()
	{
		setValueAt("--", 0, 1);
		setValueAt("--", 1, 1);
		setValueAt("--", 2, 1);
		setValueAt("--", 3, 1);
		setValueAt("--", 4, 1);
		setValueAt("--", 5, 1);
		setValueAt("--", 6, 1);

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

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public double getvalueat(int row, int col) {
		return ((Double)(data[row][col])).doubleValue();
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}


	public void setInicialValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}


class CompareTableRenderer extends ADefaultTableCellRenderer
{

	private boolean sameType = true;

	public void setSameType(boolean key)
	{
		sameType = key;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
												boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		/*
		if(!sameType && row == 0)
			c.setForeground(Color.red);
		else if(!sameType && row == 1) // XXX -- ?
			c.setForeground(Color.red);
		else
			c.setForeground(Color.black);*/
		
		c.setForeground(sameType || row > 1
		    		? Color.black
		            : Color.red);

		return c;
	}

	private boolean containsRow(int row, int []array)
	{
		if(array == null)
			return false;

		for(int i=0; i<array.length; i++)
		{
			if(array[i] == row)
				return true;
		}
		return false;
	}
}


