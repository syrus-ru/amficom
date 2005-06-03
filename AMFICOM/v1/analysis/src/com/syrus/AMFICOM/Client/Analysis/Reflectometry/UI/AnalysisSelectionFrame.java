package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.AnalysisParametersWrapper;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.BellcoreStructure;

public class AnalysisSelectionFrame extends JInternalFrame implements
		BsHashChangeListener, PrimaryMTAEListener, AnalysisParametersListener, ReportTable
{
	static final Double[] nf = { new Double(0.7), new Double(1.0),
			new Double(1.5), new Double(2.0), new Double(2.5), new Double(3) };

	private Dispatcher dispatcher;

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

		init_module(aContext.getDispatcher());
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
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

    // @todo: this update should be performed every time the user changes analysis parameter(s), not only when a button clicked.
    // (both if a value is entered and if a combobox selection is changed)
    void updateHeapAP()
    {
        Heap.getMinuitAnalysisParams().setMinThreshold(
                ((Double )table.getValueAt(0, 1)).doubleValue());
        Heap.getMinuitAnalysisParams().setMinSplice(
                ((Double )table.getValueAt(1, 1)).doubleValue());
        Heap.getMinuitAnalysisParams().setMinConnector(
                ((Double )table.getValueAt(2, 1)).doubleValue());
        Heap.getMinuitAnalysisParams().setMinEnd(
                ((Double )table.getValueAt(3, 1)).doubleValue());
        Heap.getMinuitAnalysisParams().setNoiseFactor(
                ((Double )table.getValueAt(4, 1)).doubleValue());
        analysisParametersUpdatedHere = true;
        Heap.notifyAnalysisParametersUpdated();
        analysisParametersUpdatedHere  = false;
    }

	void analysisStartButton_actionPerformed(ActionEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		updateHeapAP();
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

	private class ParamTableModel extends AbstractTableModel
	{
		AComboBox nfComboBox = new AComboBox(AComboBox.SMALL_FONT);

		String[] columnNames = { "", "" };

		Object[][] data = {
				{ LangModelAnalyse.getString("analysisMinEvent"), new Double(0) },
				{ LangModelAnalyse.getString("analysisMinWeld"), new Double(0) },
				{ LangModelAnalyse.getString("analysisMinConnector"),
						new Double(0) },
				{ LangModelAnalyse.getString("analysisMinEnd"), new Double(0) },
				{ LangModelAnalyse.getString("analysisNoiseFactor"), nfComboBox } };

		ParamTableModel() {
			for (int i = 0; i < nf.length; i++) {
				nfComboBox.addItem(nf[i]);
			}

			this.nfComboBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getID() == ItemEvent.ITEM_STATE_CHANGED) {
						AnalysisSelectionFrame.this.updateHeapAP();
					}

				}
			});
		}
        void updateData(Object[] d)
        {
            for (int i = 0; i < d.length; i++) {
                if (data[i][1] instanceof Double)
                    data[i][1] = d[i];
                else if (data[i][1] instanceof JComboBox)
                    ((JComboBox )data[i][1]).setSelectedItem(d[i]);
            }
            super.fireTableDataChanged();
        }

		public void clearTable()
		{
			data = new Object[][] {};
			super.fireTableDataChanged();
		}

		public int getColumnCount()
		{
			return columnNames.length;
		}

		public int getRowCount()
		{
			return data.length;
		}

		public String getColumnName(int col)
		{
			return columnNames[col];
		}

		public Object getValueAt(int row, int col)
		{
			if (data[row][col] instanceof JComboBox)
				return ((JComboBox )data[row][col]).getSelectedItem();
			else
				return data[row][col];
		}

		public Class getColumnClass(int p_col)
		{
			return Object.class;
		}

		public boolean isCellEditable(int row, int col)
		{
			if (col < 1)
				return false;
			else
				return true;
		}

		public void setValueAt(Object value, int row, int col)
		{
			if (!(data[row][col] instanceof JComboBox))	{
				data[row][col] = value;
				super.fireTableCellUpdated(row, col);
				AnalysisSelectionFrame.this.updateHeapAP();
			}
		}
	}

	/*private class ModelParamsTableEditor extends DefaultCellEditor
	{
		Object editor;

		ParamTableModel model;

		public ModelParamsTableEditor(ParamTableModel model)
		{
			super(new JTextField());
			this.model = model;
			setClickCountToStart(1);
			super.editorComponent.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					ModelParamsTableEditor.this.stopCellEditing();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected1, int row, int column)
		{
			editor = value;
			if (model.data[row][column] instanceof JComboBox)
			{
				JComboBox box = (JComboBox )model.data[row][column];
				box.setBackground(SystemColor.window);
				return box;
			} else
				return super.getTableCellEditorComponent(table, value,
					isSelected1, row, column);
		}

		public Object getCellEditorValue()
		{
			if (editor instanceof JComboBox)
				return editor;
			Object obj = super.getCellEditorValue();
			if (obj instanceof String)
			{
				String str = (String )obj;
				while (str.length() > 0)
				{
					try
					{
						return Double.valueOf(str);
					} catch (NumberFormatException ex)
					{
						str = str.substring(0, str.length() - 1);
					}
				}
				return new Double(0);
			} else
				return obj;
		}
	}

	private class ModelParamsTableRenderer extends ADefaultTableCellRenderer.ObjectRenderer
	{
		ParamTableModel model;

		public ModelParamsTableRenderer(ParamTableModel model)
		{
			this.model = model;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected1, boolean hasFocus, int row,
				int column)
		{
			if (model.data[row][column] instanceof JComboBox)
				return (JComboBox )model.data[row][column];
			else
				return super.getTableCellRendererComponent(table, value,
					isSelected1, hasFocus, row, column);
		}
	}*/

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		String id = key;
		if (id.equals(Heap.PRIMARY_TRACE_KEY))
		{
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
			}

			AnalysisParameters ap = Heap.getMinuitAnalysisParams();
			setValues(ap);
			setVisible(true);
		}
	}

	public void bsHashRemoved(String key)
	{
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
