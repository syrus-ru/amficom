package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.OverallStats;
import com.syrus.AMFICOM.analysis.OverallStatsWrapper;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class OverallStatsFrame extends JInternalFrame implements EtalonMTMListener, PrimaryRefAnalysisListener, ReportTable,
		RefMismatchListener {
	private static final long serialVersionUID = 2992484290509149167L;

	private WrapperedPropertyTableModel<OverallStats> tModel;
	private WrapperedPropertyTable<OverallStats> jTable;
	private OverallStats stats;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private WrapperedPropertyTableModel<OverallStats> wctModel;
	private WrapperedPropertyTable<OverallStats> jTableWholeComp;

	private JPanel mainPanelWholeComp = new JPanel();
	private JScrollPane scrollPaneWholeComp = new JScrollPane();
	private JViewport viewportWholeComp = new JViewport();

	public OverallStatsFrame() {
		this(new Dispatcher());
	}

	public OverallStatsFrame(final Dispatcher dispatcher) {
		super();

		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.initModule(dispatcher);
	}

	void initModule(final Dispatcher dispatcher) {
		Heap.addEtalonMTMListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addRefMismatchListener(this);
	}

	public String getReportTitle() {
		return LangModelAnalyse.getString("overallTitle");
	}

	public TableModel getTableModel() {
		return this.tModel;
	}

	private void jbInit() throws Exception {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		this.stats = new OverallStats();
		this.tModel = new WrapperedPropertyTableModel<OverallStats>(OverallStatsWrapper.getInstance(),
				this.stats,
				new String[] { OverallStatsWrapper.KEY_LENGTH,
						OverallStatsWrapper.KEY_LOSS,
						OverallStatsWrapper.KEY_ATTENUATION,
						OverallStatsWrapper.KEY_RETURN_LOSS,
						OverallStatsWrapper.KEY_NOISE_LEVEL,
						OverallStatsWrapper.KEY_NOISE_DD,
						OverallStatsWrapper.KEY_NOISE_DDRMS,
						OverallStatsWrapper.KEY_EVENTS });

		this.jTable = new WrapperedPropertyTable<OverallStats>(this.tModel);
		this.jTable.setTableHeader(null);
		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(130);

		this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);

		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("overallTitle"));

		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.tabbedPane.add(LangModelAnalyse.getString("Title.main"), this.mainPanel);

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.scrollPane.getViewport().add(this.jTable);
		this.tabbedPane.setEnabledAt(0, true);

		this.tabbedPane.add(LangModelAnalyse.getString("Title.comparative"), this.mainPanelWholeComp);
		this.wctModel = new WrapperedPropertyTableModel<OverallStats>(OverallStatsWrapper.getInstance(),
				this.stats,
				new String[] { OverallStatsWrapper.KEY_LENGTH,
						OverallStatsWrapper.KEY_ETALON_LENGTH,
						OverallStatsWrapper.KEY_MAX_DEVIATION,
						OverallStatsWrapper.KEY_MEAN_DEVIATION,
						//OverallStatsWrapper.KEY_D_LOSS,
						OverallStatsWrapper.KEY_MISMATCH,
						OverallStatsWrapper.KEY_QUALITY_D,
						OverallStatsWrapper.KEY_QUALITY_Q});

		this.jTableWholeComp = new WrapperedPropertyTable<OverallStats>(this.wctModel);
		this.jTableWholeComp.setTableHeader(null);
		this.jTableWholeComp.getColumnModel().getColumn(0).setPreferredWidth(130);

		this.mainPanelWholeComp.setLayout(new BorderLayout());
		this.mainPanelWholeComp.setBorder(BorderFactory.createLoweredBevelBorder());
		this.scrollPaneWholeComp.setViewport(this.viewportWholeComp);
		this.scrollPaneWholeComp.setAutoscrolls(true);

		this.jTableWholeComp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mainPanelWholeComp.add(this.scrollPaneWholeComp, BorderLayout.CENTER);
		this.scrollPaneWholeComp.getViewport().add(this.jTableWholeComp);
		this.tabbedPane.setEnabledAt(1, false);
	}

	private void setWholeData() {
		final ReflectogramMismatchImpl mismatch = Heap.getRefMismatch();
		final ModelTraceManager etalonMTM = Heap.getMTMEtalon();
		final ModelTraceAndEvents dataMTAE = Heap.getMTAEPrimary();
		if (etalonMTM == null || dataMTAE == null) {
			this.tabbedPane.setSelectedIndex(0);
			this.tabbedPane.setEnabledAt(1, false);
			return;
		}
		final double deltaX = dataMTAE.getDeltaX() / 1000.;

		final ModelTrace etalonMTrace = etalonMTM.getMTAE().getModelTrace();
		final ModelTrace dataMTrace = dataMTAE.getModelTrace();
		final SimpleReflectogramEvent[] etalonSRE = etalonMTM.getMTAE().getSimpleEvents();
		// SimpleReflectogramEvent []dataSRE = dataMTAE.getSimpleEvents();

		final double maxDeviation = ReflectogramComparer.getMaxDeviation(etalonMTrace, dataMTrace);
		final double meanDeviation = ReflectogramComparer.getMeanDeviation(etalonMTrace, dataMTrace);
		final double etalonLength = ReflectogramMath.getEndOfTraceBegin(etalonSRE) * deltaX;
		// double dataLength = ReflectogramMath.getEndOfTraceBegin(dataSRE) *
		// deltaX;
		final double lossDifference = ReflectogramComparer.getLossDifference(etalonMTM.getMTAE(), dataMTAE);

		this.stats.initCompareStatistics(maxDeviation, meanDeviation,
				etalonLength, lossDifference, mismatch,
				Heap.getEvaluationOverallResult());
		this.tabbedPane.setEnabledAt(1, true);
		this.jTableWholeComp.getModel().fireTableDataChanged();
	}

	void updTableModel() {
		final PFTrace pf = Heap.getPFTracePrimary();

		final TraceEvent ev = Heap.getRefAnalysisPrimary().overallStats;
		if (ev == null) {
			return;
		}

		this.stats.initGeneralStatistics(ev, pf);
		this.jTable.getModel().fireTableDataChanged();
	}

	public void etalonMTMCUpdated() {
		if (Heap.getRefAnalysisPrimary() != null) {
			this.setWholeData();
		}
	}

	public void etalonMTMRemoved() {
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
	}

	public void primaryRefAnalysisCUpdated() {
		this.updTableModel();
		this.setVisible(true);
		if (Heap.getMTMEtalon() != null) {
			this.setWholeData();
		}
	}

	public void primaryRefAnalysisRemoved() {
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
		this.setVisible(false);
	}

	public void refMismatchCUpdated() {
		this.setWholeData();
	}

	public void refMismatchRemoved() {
		this.setWholeData();
	}
}

