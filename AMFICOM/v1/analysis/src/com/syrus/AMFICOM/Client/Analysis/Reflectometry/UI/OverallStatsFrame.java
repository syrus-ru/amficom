package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.io.BellcoreStructure;

public class OverallStatsFrame extends ATableFrame
implements OperationListener, bsHashChangeListener, EtalonMTMListener
{
	private FixedSizeEditableTableModel tModel;
	private JTable jTable;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	boolean analysis_performed = false;
	boolean etalon_loaded = false;

	private WholeCompareTableModel wctModel;
	private ATable jTableWholeComp;

	BorderLayout borderLayoutWholeComp = new BorderLayout();
	JPanel mainPanelWholeComp = new JPanel();
	JScrollPane scrollPaneWholeComp = new JScrollPane();
	JViewport viewportWholeComp = new JViewport();


	public OverallStatsFrame()
	{
		this(new Dispatcher());
	}

	public OverallStatsFrame(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		//dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.register(this, RefUpdateEvent.typ);
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if(rue.analysisPerformed())
			{
				updTableModel(Heap.PRIMARY_TRACE_KEY);

				if (Heap.hasEventParamsForPrimaryTrace())
					analysis_performed = true;
				if(etalon_loaded)
				{
					setWholeData();
					tabbedPane.setEnabledAt(1, true);
				}
			}
		}
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("overallTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		tModel = new FixedSizeEditableTableModel(
					new String[] {LangModelAnalyse.getString("overallKey"),
												LangModelAnalyse.getString("overallValue")},
					new String[] {"1", "2"},
					new String[] {
						LangModelAnalyse.getString("totalLength"),
						LangModelAnalyse.getString("totalLoss"),
						LangModelAnalyse.getString("totalAttenuation"),
						LangModelAnalyse.getString("totalReturnLoss"),
						LangModelAnalyse.getString("totalNoiseLevel"),
						LangModelAnalyse.getString("totalNoiseDD"),
						LangModelAnalyse.getString("totalEvents")
					},
					null);
		this.jTable = new ATable(tModel);
//		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(130);

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

//		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("overallTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		tabbedPane.add(LangModelAnalyse.getString("Title.main"), mainPanel);

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		this.jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		this.jTable.setMaximumSize(new Dimension(200, 213));
//		this.jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add(LangModelAnalyse.getString("Title.comparative"), mainPanelWholeComp);
		wctModel = new WholeCompareTableModel();
		jTableWholeComp = new ATable (wctModel);
		jTableWholeComp.getColumnModel().getColumn(0).setPreferredWidth(150);
		jTableWholeComp.getColumnModel().getColumn(1).setPreferredWidth(70);

		mainPanelWholeComp.setLayout(new BorderLayout());
		mainPanelWholeComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneWholeComp.setViewport(viewportWholeComp);
		scrollPaneWholeComp.setAutoscrolls(true);
		jTableWholeComp.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTableWholeComp.setMinimumSize(new Dimension(200, 213));

		mainPanelWholeComp.add(scrollPaneWholeComp, BorderLayout.CENTER);
		scrollPaneWholeComp.getViewport().add(jTableWholeComp);
		tabbedPane.setEnabledAt(1, false);

		updColorModel();
	}

	private void setWholeData()
	{
		ModelTraceManager etalonMTM = Heap.getMTMEtalon();
		ModelTraceAndEvents dataMTM = Heap.getMTAEPrimary();
		if(etalonMTM == null || dataMTM == null || dataMTM.getNEvents() == 0)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		double deltaX = dataMTM.getDeltaX()/1000.;

		ModelTrace etalonMTrace = etalonMTM.getModelTrace();
		ModelTrace dataMTrace = dataMTM.getModelTrace();
		SimpleReflectogramEvent []etalonSRE = etalonMTM.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent []dataSRE = dataMTM.getSimpleEvents();

		double maxDeviation = ReflectogramComparer.getMaxDeviation(etalonMTrace, dataMTrace);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(etalonMTrace, dataMTrace);
		double etalonLength = ReflectogramMath.getLastConnectorBegin(etalonSRE) * deltaX;
		double dataLength = ReflectogramMath.getLastConnectorBegin(dataSRE) * deltaX;
		double lossDifference = ReflectogramComparer.getLossDifference(etalonMTM.getMTAE(), dataMTM);

		wctModel.setValueAt(String.valueOf(MathRef.round_3(dataLength))+ " " + LangModelAnalyse.getString("km"), 0, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_3(etalonLength)) + " " + LangModelAnalyse.getString("km"), 1, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(maxDeviation)) + " " + LangModelAnalyse.getString("dB"), 2, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(meanDeviation)) + " " + LangModelAnalyse.getString("dB"), 3, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(lossDifference)) + " " + LangModelAnalyse.getString("dB"), 4, 1);
	}

	private void updColorModel()
	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
//
//		scrollPaneWholeComp.getViewport().setBackground(SystemColor.window);
//		jTableWholeComp.setBackground(SystemColor.window);
//		jTableWholeComp.setForeground(ColorManager.getColor("textColor"));
//		jTableWholeComp.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void updTableModel(String id)
	{
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);

		TraceEvent ev = Heap.getRefAnalysisByKey(id).overallStats;
		if (ev == null)
			return;

		double range_km = ev.last_point * bs.getResolution() / 1000.0;
		double loss = Math.abs(ev.data[0] -  ev.data[1]);
		double attenuation = loss / range_km;
		double orl = MathRef.calcORL(ev.data[0], ev.data[1]);
		double noise = ev.data[2];
		double DD = ev.data[2] - ev.data[3];
		int evNum = (int)ev.data[4];

		tModel.updateColumn(new Object[] {
			MathRef.round_3(range_km) + " " + LangModelAnalyse.getString("km"),
			MathRef.round_2(loss) + " " + LangModelAnalyse.getString("dB"),
			MathRef.round_4(attenuation) + " " + LangModelAnalyse.getString("dB") + '/' + LangModelAnalyse.getString("km"),
			MathRef.round_2(orl) + " " + LangModelAnalyse.getString("dB"),
			MathRef.round_2(noise) + " " + LangModelAnalyse.getString("dB"),
			MathRef.round_2(DD) + " " + LangModelAnalyse.getString("dB"),
			String.valueOf(evNum)
		}, 1);
		jTable.updateUI();
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(RefUpdateEvent.PRIMARY_TRACE))
		{
			updTableModel(key);
			setVisible(true);
			wctModel.clearTable();
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
		}
	}

	public void bsHashRemoved(String key)
	{
		if(key.equals(AnalysisUtil.ETALON))
		{
			wctModel.clearTable();
			tabbedPane.setSelectedIndex(0);
			etalon_loaded = false;
			tabbedPane.setEnabledAt(1, false);
		}
	}

	public void bsHashRemovedAll()
	{
		wctModel.clearTable();
		tabbedPane.setSelectedIndex(0);
		analysis_performed = false;
		tabbedPane.setEnabledAt(1, false);
		setVisible(false);
	}

	public void etalonMTMCUpdated()
	{
		wctModel.clearTable();
		tabbedPane.setSelectedIndex(0);
		etalon_loaded = true;
		if(analysis_performed)
		{
			setWholeData();
			tabbedPane.setEnabledAt(1, true);
		}
	}

	public void etalonMTMRemoved()
	{
		wctModel.clearTable();
		tabbedPane.setSelectedIndex(0);
		etalon_loaded = false;
		tabbedPane.setEnabledAt(1, false);
	}
}



class WholeCompareTableModel extends AbstractTableModel
{

//  String[] columnNames = {LangModelModel.String("parameter") ,
//    LangModelModel.String("value")};

	String[] columnNames = {null ,
		null};

	Object[][] data = {
		{LangModelAnalyse.getString("traceLength"), "--"},
		{LangModelAnalyse.getString("etLength"), "--"},
		{LangModelAnalyse.getString("maxDeviation"), "--"},
		{LangModelAnalyse.getString("meanDeviation"), "--"},
		{LangModelAnalyse.getString("delta"), "--"},
	};

	WholeCompareTableModel()
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

