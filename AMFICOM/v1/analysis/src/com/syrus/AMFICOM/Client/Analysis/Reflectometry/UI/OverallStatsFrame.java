package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class OverallStatsFrame extends JInternalFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener, ReportTable
{
	private WrapperedPropertyTableModel tModel;
	private WrapperedPropertyTable jTable;
	private OverallStats stats;
	
	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private WrapperedPropertyTableModel wctModel;
	private WrapperedPropertyTable jTableWholeComp;

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
		
		stats = new OverallStats();
		tModel = new WrapperedPropertyTableModel(OverallStatsWrapper.getInstance(),
				stats,
				new String[] { OverallStatsWrapper.KEY_LENGTH,
						OverallStatsWrapper.KEY_LOSS,
						OverallStatsWrapper.KEY_ATTENUATION,
						OverallStatsWrapper.KEY_RETURN_LOSS,
						OverallStatsWrapper.KEY_NOISE_LEVEL,
						OverallStatsWrapper.KEY_NOISE_DD,
						OverallStatsWrapper.KEY_NOISE_DDRMS,
						OverallStatsWrapper.KEY_EVENTS });

		this.jTable = new WrapperedPropertyTable(tModel);
		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(130);

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

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
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add(LangModelAnalyse.getString("Title.comparative"), mainPanelWholeComp);
		wctModel = new WrapperedPropertyTableModel(OverallStatsWrapper
				.getInstance(), stats, new String[] {
				OverallStatsWrapper.KEY_LENGTH,
				OverallStatsWrapper.KEY_ETALON_LENGTH,
				OverallStatsWrapper.KEY_MAX_DEVIATION,
				OverallStatsWrapper.KEY_MEAN_DEVIATION,
				OverallStatsWrapper.KEY_D_LOSS });
		jTableWholeComp = new WrapperedPropertyTable(wctModel);
		jTableWholeComp.getColumnModel().getColumn(0).setPreferredWidth(130);

		mainPanelWholeComp.setLayout(new BorderLayout());
		mainPanelWholeComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneWholeComp.setViewport(viewportWholeComp);
		scrollPaneWholeComp.setAutoscrolls(true);

		this.jTableWholeComp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainPanelWholeComp.add(scrollPaneWholeComp, BorderLayout.CENTER);
		scrollPaneWholeComp.getViewport().add(jTableWholeComp);
		tabbedPane.setEnabledAt(1, false);
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
//		SimpleReflectogramEvent []dataSRE = dataMTAE.getSimpleEvents();

		double maxDeviation = ReflectogramComparer.getMaxDeviation(etalonMTrace, dataMTrace);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(etalonMTrace, dataMTrace);
		double etalonLength = ReflectogramMath.getEndOfTraceBegin(etalonSRE) * deltaX;
//		double dataLength = ReflectogramMath.getEndOfTraceBegin(dataSRE) * deltaX;
		double lossDifference = ReflectogramComparer.getLossDifference(etalonMTM.getMTAE(), dataMTAE);

		stats.initCompareStatistics(maxDeviation, meanDeviation, etalonLength, lossDifference);
		jTableWholeComp.updateUI();
	}

	void updTableModel()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();

		TraceEvent ev = Heap.getRefAnalysisPrimary().overallStats;
		if (ev == null)
			return;

		stats.initGeneralStatistics(ev, bs);
		jTable.updateUI();
	}

	public void etalonMTMCUpdated()
	{
		if (Heap.getRefAnalysisPrimary() != null)
		{
			setWholeData();
			tabbedPane.setEnabledAt(1, true);
		}
	}

	public void etalonMTMRemoved()
	{
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
	}

    public void primaryRefAnalysisCUpdated() {
        updTableModel();
        setVisible(true);
        if(Heap.getMTMEtalon() != null)
        {
            setWholeData();
            tabbedPane.setEnabledAt(1, true);
        }
    }

    public void primaryRefAnalysisRemoved() {
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);
        setVisible(false);
    }

}

