package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class OverallStatsFrame extends ATableFrame
																		implements OperationListener
{
	private static StringBuffer km = new StringBuffer(' ').append(LangModelAnalyse.getString("km"));
	private static StringBuffer db = new StringBuffer(' ').append(LangModelAnalyse.getString("dB"));
	private static StringBuffer dbkm = new StringBuffer(' ').append(LangModelAnalyse.getString("dB")).
			append('/').append(LangModelAnalyse.getString("km"));

	private Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private ATable jTable;

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
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					updTableModel(id);
					setVisible(true);
					wctModel.clearTable();
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(1, false);
				}
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					tModel.clearTable();
					wctModel.clearTable();
					tabbedPane.setSelectedIndex(0);
					analysis_performed = false;
					tabbedPane.setEnabledAt(1, false);
					setVisible(false);
				}
				else if(id.equals(AnalysisUtil.ETALON))
				{
					wctModel.clearTable();
					tabbedPane.setSelectedIndex(0);
					etalon_loaded = false;
					tabbedPane.setEnabledAt(1, false);
				}
			}

			if(rce.OPEN_ETALON)
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
			if(rce.CLOSE_ETALON)
			{
				wctModel.clearTable();
				tabbedPane.setSelectedIndex(0);
				etalon_loaded = false;
				tabbedPane.setEnabledAt(1, false);
			}

		}

		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if(rue.ANALYSIS_PERFORMED)
			{
				String id = (String)(rue.getSource());
				if (id.equals("primarytrace"))
					updTableModel(id);

				if (Pool.get("eventparams", "primarytrace") != null)
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
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
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
		jTable = new ATable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(130);

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("overallTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		tabbedPane.add("Основная", mainPanel);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMaximumSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add("Сравнительная", mainPanelWholeComp);
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
		ReflectogramEvent []data =  (ReflectogramEvent [])Pool.get("eventparams", "primarytrace");
		ReflectogramEvent []etalon = (ReflectogramEvent[])Pool.get("eventparams", AnalysisUtil.ETALON);
		if(etalon == null || data == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		if(data.length==0)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		double delta_x = data[0].getDeltaX()/1000.;

		ReflectogramEvent []data_ = ReflectogramMath.alignClone(data, etalon);

		double maximalDeviation = ReflectogramComparer.getMaximalDeviation(etalon, data_);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(etalon, data_);
		double etalonLength = ReflectogramMath.getLastSplash(etalon)*delta_x;
		double dataLength = ReflectogramMath.getLastSplash(data_)*delta_x;
		double lossDifference = ReflectogramComparer.getLossDifference(etalon, data_);

		wctModel.setValueAt(String.valueOf(MathRef.round_3(dataLength))+ " " + LangModelAnalyse.getString("km"), 0, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_3(etalonLength)) + " " + LangModelAnalyse.getString("km"), 1, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(maximalDeviation)) + " " + LangModelAnalyse.getString("dB"), 2, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(meanDeviation)) + " " + LangModelAnalyse.getString("dB"), 3, 1);
		wctModel.setValueAt(String.valueOf(MathRef.round_4(lossDifference)) + " " + LangModelAnalyse.getString("dB"), 4, 1);
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));

		scrollPaneWholeComp.getViewport().setBackground(SystemColor.window);
		jTableWholeComp.setBackground(SystemColor.window);
		jTableWholeComp.setForeground(ColorManager.getColor("textColor"));
		jTableWholeComp.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void updTableModel(String id)
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		double alpha;

		switch ((int)bs.fxdParams.AW)
		{
			case 13100: alpha = .063; break;
			case 15500: alpha = .032; break;
			case 16250: alpha = .0265; break;
			default: alpha = .032;
		}

		TraceEvent ev = ((RefAnalysis)Pool.get("refanalysis", id)).overallStats;
		if (ev == null)
			return;

		double range = (ev.last_point) * (double)(3 * bs.fxdParams.DS[0]) / (double)(bs.fxdParams.GI * 10000);
		double loss = Math.abs(ev.data[0] -  ev.data[1]);
		double attenuation = loss / range;
		double orl = MathRef.ORL(ev.data[0], ev.data[1]);
		double noise = ev.data[2];
		double DD = ev.data[2] - ev.data[3];
		int evNum = (int)ev.data[4];

		tModel.updateColumn(new Object[] {
			new StringBuffer().append(MathRef.round_3(range)).append(km).toString(),
			new StringBuffer().append(MathRef.round_2(loss)).append(db).toString(),
			new StringBuffer().append(MathRef.round_4(attenuation)).append(dbkm).toString(),
			new StringBuffer().append(MathRef.round_2(orl)).append(db).toString(),
			new StringBuffer().append(MathRef.round_2(noise)).append(db).toString(),
			new StringBuffer().append(MathRef.round_2(DD)).append(db).toString(),
			String.valueOf(evNum)
		}, 1);
		jTable.updateUI();
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

