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

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class OverallStatsFrame extends ATableFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener
{
	private FixedSizeEditableTableModel tModel;
	private JTable jTable;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private WholeCompareTableModel wctModel;
	private JTable jTableWholeComp;

	private JPanel mainPanelWholeComp = new JPanel();
	private JScrollPane scrollPaneWholeComp = new JScrollPane();
	private JViewport viewportWholeComp = new JViewport();

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
		Heap.addEtalonMTMListener(this);
        Heap.addPrimaryRefAnalysisListener(this);
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
		this.jTable = new JTable(tModel);
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
		jTableWholeComp = new JTable (wctModel);
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
		ModelTraceAndEvents dataMTAE = Heap.getMTAEPrimary();
		if(etalonMTM == null || dataMTAE == null || dataMTAE.getNEvents() == 0)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		double deltaX = dataMTAE.getDeltaX()/1000.;

		ModelTrace etalonMTrace = etalonMTM.getMTAE().getModelTrace();
		ModelTrace dataMTrace = dataMTAE.getModelTrace();
		SimpleReflectogramEvent []etalonSRE = etalonMTM.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent []dataSRE = dataMTAE.getSimpleEvents();

		double maxDeviation = ReflectogramComparer.getMaxDeviation(etalonMTrace, dataMTrace);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(etalonMTrace, dataMTrace);
		double etalonLength = ReflectogramMath.getEndOfTraceBegin(etalonSRE) * deltaX;
		double dataLength = ReflectogramMath.getEndOfTraceBegin(dataSRE) * deltaX;
		double lossDifference = ReflectogramComparer.getLossDifference(etalonMTM.getMTAE(), dataMTAE);

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

	void updTableModel()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();

		TraceEvent ev = Heap.getRefAnalysisPrimary().overallStats;
		if (ev == null)
			return;

		double range_km = ev.last_point * bs.getResolution() / 1000.0;
		double loss = ev.overallStatsLoss();
		double attenuation = loss / range_km;
		double orl = MathRef.calcORL(ev.overallStatsY0(), ev.overallStatsY1());
		double noise = ev.overallStatsNoiseLevel();
		double DD = ev.overallStatsDD();
		int evNum = ev.overallStatsEvNum();

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

	public void etalonMTMCUpdated()
	{
		wctModel.clearTable();
		if (Heap.getRefAnalysisPrimary() != null)
		{
			setWholeData();
			tabbedPane.setEnabledAt(1, true);
		}
	}

	public void etalonMTMRemoved()
	{
		wctModel.clearTable();
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
	}

    public void primaryRefAnalysisCUpdated() {
        updTableModel();
        setVisible(true);
        wctModel.clearTable(); // ?
        if(Heap.getMTMEtalon() != null)
        {
            setWholeData();
            tabbedPane.setEnabledAt(1, true);
        }
    }

    public void primaryRefAnalysisRemoved() {
        wctModel.clearTable();
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);
        setVisible(false);
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
		{LangModelAnalyse.getString("dLoss"), "--"},
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

