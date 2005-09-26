package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.AnalysisParametersListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.AnalysisParametersWrapper;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.measurement.MeasurementSetup;

public class AnalysisSelectionFrame extends JInternalFrame implements BsHashChangeListener, PrimaryMTAEListener,
		AnalysisParametersListener, ReportTable {
	private static final long serialVersionUID = -5866433900913468687L;

	private WrapperedPropertyTableModel<AnalysisParameters> tModelMinuit;
	private WrapperedPropertyTable<AnalysisParameters> table;
	private JPanel mainPanel;
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	ApplicationContext aContext;
	private boolean analysisParametersUpdatedHere = false;

	public AnalysisSelectionFrame(final ApplicationContext aContext) {
		this.createUI();
		this.aContext = aContext;

		this.initModule();
	}

	void initModule() {
		Heap.addBsHashListener(this);
		Heap.addPrimaryMTMListener(this);
		Heap.addAnalysisParametersListener(this);
	}

	public String getReportTitle() {
		return LangModelAnalyse.getString("analysisSelectionTitle");
	}

	public TableModel getTableModel() {
		return this.tModelMinuit;
	}

	void setValues(final AnalysisParameters ap) {
		this.tModelMinuit.setObject(ap);
	}

	private void updColorModel() {
	}

	private void createUI() {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle"));

		this.tModelMinuit = new WrapperedPropertyTableModel<AnalysisParameters>(AnalysisParametersWrapper.getInstance(),
				null,
				new String[] { AnalysisParametersWrapper.KEY_SENSITIVITY,
						AnalysisParametersWrapper.KEY_MIN_CONNECTOR,
						AnalysisParametersWrapper.KEY_MIN_END,
						AnalysisParametersWrapper.KEY_NOISE_FACTOR });
		this.table = new WrapperedPropertyTable<AnalysisParameters>(this.tModelMinuit);
//		{
//			public Class getColumnClass(int columnIndex) {
//			
//				Class clazz;
//				if (columnIndex == 0) {
//					clazz = String.class;
//				} else {
//					// TODO really ?
//					clazz = Double.class;
//				}
//				return clazz;
//			}
//		};
//		JFormattedTextField editor = new JFormattedTextField(NumberFormat.getInstance());
//		editor.setHorizontalAlignment(SwingConstants.RIGHT);
//		table.setDefaultEditor(String.class, new DefaultCellEditor(editor));
//		table.setDefaultRenderer(Object.class, new ModelParamsTableRenderer(
//				tModelMinuit));
//		table.setDefaultEditor(Object.class, new ModelParamsTableEditor(
//				tModelMinuit));

		final JButton analysisStartButton = new JButton();
		final JButton analysisInitialButton = new JButton();
		final JButton analysisDefaultsButton = new JButton();

		analysisStartButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisStartButton.setToolTipText(LangModelAnalyse.getString("analysisStart"));
		analysisStartButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS));
		analysisStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AnalysisSelectionFrame.this.analysisStartButton_actionPerformed(e);
			}
		});

		analysisInitialButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisInitialButton.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		analysisInitialButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS));
		analysisInitialButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AnalysisSelectionFrame.this.analysisInitialButton_actionPerformed(e);
			}
		});

		analysisDefaultsButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisDefaultsButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		analysisDefaultsButton.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS));
		analysisDefaultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AnalysisSelectionFrame.this.analysisDefaultsButton_actionPerformed(e);
			}
		});

		this.mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(this.mainPanel);

		final JToolBar jToolBar = new JToolBar();
		jToolBar.setFloatable(false);
		jToolBar.add(analysisDefaultsButton);
		jToolBar.add(analysisInitialButton);
		jToolBar.addSeparator();
		jToolBar.add(analysisStartButton);

		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(300);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(60);

		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.getViewport().add(this.table);
		this.scrollPane.setAutoscrolls(true);

		// mainPanel.setLayout();
		this.mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.mainPanel.add(jToolBar, BorderLayout.NORTH);

		this.updColorModel();
	}

	void analysisStartButton_actionPerformed(final ActionEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new AnalysisCommand().execute();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	void analysisInitialButton_actionPerformed(final ActionEvent e) {
		Heap.setMinuitAnalysisParams(Heap.getMinuitInitialParams());
		Heap.notifyAnalysisParametersUpdated();
	}

	void analysisDefaultsButton_actionPerformed(final ActionEvent e) {
		Heap.setMinuitAnalysisParams(Heap.getMinuitDefaultParams());
		Heap.notifyAnalysisParametersUpdated();
	}

	public void bsHashAdded(final String key) {
		final String id = key;
		if (id.equals(Heap.PRIMARY_TRACE_KEY)) {
			if (Heap.getBSPrimaryTrace().getBS().measurementId == null)
				this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " ("
						+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) + ')');
			else {
				MeasurementSetup ms = Heap.getContextMeasurementSetup();
				this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " ("
						+ (ms == null
								? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
									: LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN) + ':' + ms.getDescription())
						+ ')');
			}

			final AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			this.setValues(ap);
			this.setVisible(true);
		}
	}

	public void bsHashRemoved(final String key) { // ignore: we expect bsHashRemovedAll() being called when primary trace removed
	}

	public void bsHashRemovedAll() {
		this.setVisible(false);
	}

	public void primaryMTAECUpdated() {
		final PFTrace pf = Heap.getBSPrimaryTrace();
		if (pf.getBS().measurementId == null) {
			this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " ("
					+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) + ')');
		} else {
			MeasurementSetup ms = Heap.getContextMeasurementSetup();
			this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " ("
					+ (ms == null
							? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
								: LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN) + ':' + ms.getDescription())
					+ ')');

			// вместе с первичной р/г может быть загружен и набор параметров
			final AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			this.setValues(ap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener#primaryMTMRemoved()
	 */
	public void primaryMTAERemoved() {
		// @todo Auto-generated method stub
	}

	public void analysisParametersUpdated() {
		if (this.analysisParametersUpdatedHere) {
			return;
		}
		this.setValues(Heap.getMinuitAnalysisParams());
	}
}
