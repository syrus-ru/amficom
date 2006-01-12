package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

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
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer.NumberRenderer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.measurement.MeasurementSetup;

public class AnalysisSelectionFrame extends JInternalFrame
implements BsHashChangeListener, PrimaryMTAEListener,
		AnalysisParametersListener, ReportTable {
	private static final long serialVersionUID = -5866433900913468687L;
	private static final String TITLE = LangModelAnalyse.getString("analysisSelectionTitle");

	private WrapperedPropertyTable<AnalysisParameters> table;
	private JPanel mainPanel;
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	ApplicationContext aContext;
	private boolean analysisParametersUpdatedHere = false;
	private NumberRenderer numberRenderer =
			new ADefaultTableCellRenderer.NumberRenderer () {
		private NumberFormat formatter;
		@Override
		public void setValue(Object value) {
			if (this.formatter == null) {
				this.formatter = NumberFormat.getInstance();
				// Значения min/max fraction digits
				// выбраны из следующих соображений:
				// 1. обеспечивать достаточную точность отображения
				//   (для этого надо maximumFractionDigits >= 4)
				// 2. отображать так же, как это будет делать редактор
				//   (min=1, max - достаточно велико)
				this.formatter.setMaximumFractionDigits(5);
				this.formatter.setMinimumFractionDigits(1);
			}
//			super.label.setText((value == null) ? "" : this.formatter.format(value));
			if (value instanceof Double) {
				super.label.setText(this.formatter.format(value));
			} else {
				super.setValue(value);
			}
		}
	};

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
		return TITLE;
	}

	public TableModel getTableModel() {
		return this.table.getModel();
	}

	void setValues(final AnalysisParameters ap) {
		this.table.getModel().setObject(ap);
	}

	private void updColorModel() {
	}

	private void updateTitle() {
	}

	private void createUI() {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(TITLE);

		this.table = new WrapperedPropertyTable<AnalysisParameters>(
				new WrapperedPropertyTableModel<AnalysisParameters>(AnalysisParametersWrapper.getInstance(),
					null,
					new String[] { AnalysisParametersWrapper.KEY_SENSITIVITY,
							AnalysisParametersWrapper.KEY_MIN_CONNECTOR,
							AnalysisParametersWrapper.KEY_MIN_END,
							AnalysisParametersWrapper.KEY_NOISE_FACTOR,
							AnalysisParametersWrapper.KEY_EOT_LEVEL}));
		
		this.table.setRenderer(numberRenderer, AnalysisParametersWrapper.KEY_SENSITIVITY);
		this.table.setRenderer(numberRenderer, AnalysisParametersWrapper.KEY_MIN_CONNECTOR);
		this.table.setRenderer(numberRenderer, AnalysisParametersWrapper.KEY_MIN_END);

		
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

	private void update() {
		this.setValues(Heap.getMinuitAnalysisParams());
		final PFTrace primaryTrace = Heap.getPFTracePrimary();
		if (primaryTrace == null)
			return; // not loaded
		if (primaryTrace.getBS().measurementId == null)
			this.setTitle(TITLE + " ("
					+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) + ')');
		else {
			MeasurementSetup ms = Heap.getContextMeasurementSetup();
			this.setTitle(TITLE + " ("
					+ (ms == null
							? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
								: LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN) + ": " + ms.getDescription())
					+ ')');
		}

	}

	public void bsHashAdded(final String key) {
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			update();
			this.setVisible(true);
		}
	}

	public void bsHashRemoved(final String key) { // ignore: we expect bsHashRemovedAll() being called when primary trace removed
	}

	public void bsHashRemovedAll() {
		this.setVisible(false);
	}

	public void primaryMTAECUpdated() {
		update();
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
		update();
	}
}
