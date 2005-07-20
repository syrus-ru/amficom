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
import javax.swing.SwingConstants;
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
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.BellcoreStructure;

public class AnalysisSelectionFrame extends JInternalFrame implements
		BsHashChangeListener, PrimaryMTAEListener, AnalysisParametersListener, ReportTable
{
	private WrapperedPropertyTableModel tModelMinuit;
	private WrapperedPropertyTable table;
	private JPanel mainPanel;
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	ApplicationContext aContext;
    private boolean analysisParametersUpdatedHere = false;

	public AnalysisSelectionFrame(ApplicationContext aContext)
	{
		super();

		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.aContext = aContext;

		init_module();
	}

	void init_module()
	{
		Heap.addBsHashListener(this);
		Heap.addPrimaryMTMListener(this);
        Heap.addAnalysisParametersListener(this);
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("analysisSelectionTitle");
	}

	public TableModel getTableModel()
	{
		return tModelMinuit;
	}

	void setValues(AnalysisParameters ap)
	{
		tModelMinuit.setObject(ap);
	}

	private void updColorModel()
	{
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon )UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle"));

		tModelMinuit = new WrapperedPropertyTableModel(AnalysisParametersWrapper
				.getInstance(), null, new String[] {
				AnalysisParametersWrapper.KEY_MIN_THRESHOLD,
				AnalysisParametersWrapper.KEY_MIN_SPLICE,
				AnalysisParametersWrapper.KEY_MIN_CONNECTOR,
				AnalysisParametersWrapper.KEY_MIN_END,
				AnalysisParametersWrapper.KEY_NOISE_FACTOR });
		table = new WrapperedPropertyTable(tModelMinuit);
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

		JButton analysisStartButton = new JButton();
		JButton analysisInitialButton = new JButton();
		JButton analysisDefaultsButton = new JButton();

		analysisStartButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisStartButton.setToolTipText(LangModelAnalyse.getString("analysisStart"));
		analysisStartButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS));
		analysisStartButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisStartButton_actionPerformed(e);
			}
		});

		analysisInitialButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisInitialButton.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		analysisInitialButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS));
		analysisInitialButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisInitialButton_actionPerformed(e);
			}
		});

		analysisDefaultsButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		analysisDefaultsButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		analysisDefaultsButton.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS));
		analysisDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisDefaultsButton_actionPerformed(e);
			}
		});

		this.mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		JToolBar jToolBar1 = new JToolBar();
		jToolBar1.setFloatable(false);
		jToolBar1.add(analysisDefaultsButton);
		jToolBar1.add(analysisInitialButton);
		JToolBar.Separator s = new JToolBar.Separator();
		s.setOrientation(jToolBar1.getOrientation() == SwingConstants.VERTICAL ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
		jToolBar1.add(s);
		jToolBar1.add(analysisStartButton);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(table);
		scrollPane.setAutoscrolls(true);

		//mainPanel.setLayout();
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(jToolBar1, BorderLayout.NORTH);

		this.updColorModel();
	}

	void analysisStartButton_actionPerformed(ActionEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new AnalysisCommand().execute();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	void analysisInitialButton_actionPerformed(ActionEvent e)
	{
		AnalysisParameters ap = Heap.getMinuitInitialParams();
		setValues(ap);
	}

	void analysisDefaultsButton_actionPerformed(ActionEvent e)
	{
		AnalysisParameters ap = Heap.getMinuitDefaultParams();
		setValues(ap);
	}

	public void bsHashAdded(String key)
	{
		String id = key;
		if (id.equals(Heap.PRIMARY_TRACE_KEY))
		{
			if (Heap.getBSPrimaryTrace().measurementId == null)
				setTitle(LangModelAnalyse.getString("analysisSelectionTitle")
						+ " ("
						+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
						+ ')');
			else
			{
				MeasurementSetup ms = Heap.getContextMeasurementSetup();
				setTitle(LangModelAnalyse.getString("analysisSelectionTitle")
						+ " ("
						+ (ms == null
							? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
							: LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN)
									+ ':' + ms.getDescription()) + ')');
			}

			AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			setValues(ap);
			setVisible(true);
		}
	}

	public void bsHashRemoved(String key)
	{ // ignore: we expect bsHashRemovedAll() being called when primary trace removed
	}

	public void bsHashRemovedAll()
	{
		setVisible(false);
	}

	public void primaryMTMCUpdated()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs.measurementId == null)
			setTitle(LangModelAnalyse.getString("analysisSelectionTitle")
					+ " ("
					+ LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
					+ ')');
		else
		{
			MeasurementSetup ms = Heap.getContextMeasurementSetup();
			setTitle(LangModelAnalyse.getString("analysisSelectionTitle")
					+ " ("
					+ (ms == null
						? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN)
						: LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN)
								+ ':' + ms.getDescription()) + ')');

			if (ms.getCriteriaSet() != null)
			{
				AnalysisParameters ap = Heap.getMinuitAnalysisParams();
				setValues(ap);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener#primaryMTMRemoved()
	 */
	public void primaryMTMRemoved()
	{
		// @todo Auto-generated method stub

	}

    public void analysisParametersUpdated() {
        if (analysisParametersUpdatedHere)
            return;
        setValues(Heap.getMinuitAnalysisParams());
    }
}
