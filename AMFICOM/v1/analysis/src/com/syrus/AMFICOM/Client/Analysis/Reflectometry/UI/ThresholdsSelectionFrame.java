package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThreshEditor;
import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;
import com.syrus.io.BellcoreStructure;

public class ThresholdsSelectionFrame extends ATableFrame
	implements OperationListener
{
	protected Dispatcher dispatcher;
	private JTable jTable;

	private BellcoreStructure bs; // для доступа к самой р/г во время пересчета порогов

	protected ModelTraceManager mtm;

	protected int current_ev = -1;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JToolBar jToolBar1 = new JToolBar();

	public ThresholdsSelectionFrame(Dispatcher dispatcher)
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

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("thresholdsTableTitle");
	}

	public TableModel getTableModel()
	{
		return jTable.getModel();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("thresholdsTableTitle"));

		ThresholdTableModel tModelEmpty = new ThresholdTableModel( // FIXME - переделать
			new String[] { LangModelAnalyse.getString("thresholdsKey") },
			new String[] {
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")},
			new int[] { },
			null
		);
		jTable = new ATable(tModelEmpty);
		
		JButton nalysisInitialButton = new JButton();
		JButton analysisDefaultsButton = new JButton();
		JButton increaseThreshButton = new JButton();
		JButton decreaseThreshButton = new JButton();
		
		JButton previuosEventButton = new JButton();
		JButton nextEventButton = new JButton();

		{	// set up button size
			JButton[] buttons = new JButton[] { nalysisInitialButton, analysisDefaultsButton, increaseThreshButton, decreaseThreshButton, previuosEventButton, 
					nextEventButton};
			for (int i = 0; i < buttons.length; i++)
			{
				buttons[i].setMaximumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
				buttons[i].setMinimumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
				buttons[i].setPreferredSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
			}
		}

		nalysisInitialButton.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		nalysisInitialButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL));
		nalysisInitialButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (mtm != null && current_ev != -1)
				{
					// FIXME: outdated / useless mtm.setThreshold(...,init_Threshs...) removed
					// the button should be removed or replaced with something more useful
				}
			}
		});

		analysisDefaultsButton.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DEFAULT));
		analysisDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (mtm != null && current_ev != -1)
				{
					mtm.setDefaultThreshold(current_ev);
					updateThresholds();
					dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				}
			}
		});

		increaseThreshButton.setToolTipText(LangModelAnalyse.getString("increaseThresh"));
		increaseThreshButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE));
		increaseThreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ModelTraceManager.ThreshEditor ted = getCurrentTED();
				if (ted != null)
				{
					ted.increaseValues();
					dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				}
			}
		});

		decreaseThreshButton.setToolTipText(LangModelAnalyse.getString("decreaseThresh"));
		decreaseThreshButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE));
		decreaseThreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ModelTraceManager.ThreshEditor ted = getCurrentTED();
				if (ted != null)
				{
					ted.decreaseValues();
					dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				}
			}
		});
		
		previuosEventButton.setToolTipText(LangModelAnalyse.getString("previuosEvent"));
		previuosEventButton.setText("<");
		previuosEventButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				dispatcher.notify(new OperationEvent(this, 0, AnalyseApplicationModel.SELECT_PREVIOUS_EVENT));

			}
		});
		
		nextEventButton.setToolTipText(LangModelAnalyse.getString("nextEvent"));
		nextEventButton.setText(">");
		nextEventButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				dispatcher.notify(new OperationEvent(this, 0, AnalyseApplicationModel.SELECT_NEXT_EVENT));

			}
		});

		this.setContentPane(mainPanel);

		//jToolBar1.setBorderPainted(true);
		jToolBar1.setFloatable(false);
		jToolBar1.add(nalysisInitialButton);
		jToolBar1.add(analysisDefaultsButton);
		jToolBar1.add(decreaseThreshButton);
		jToolBar1.add(increaseThreshButton);
		jToolBar1.add(Box.createRigidArea(UIManager.getDimension(ResourceKeys.SIZE_BUTTON)));
		jToolBar1.add(previuosEventButton);
		jToolBar1.add(nextEventButton);

		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable.setColumnSelectionAllowed(true);
		jTable.setRowSelectionAllowed(false);		
		
		jTable.setDefaultRenderer(Object.class, new ADefaultTableCellRenderer() {

			// (1) hides 'focus' - just because its color overrides selection
			// colot
			// (2) suppress visualization of selection of 1st column
			public Component getTableCellRendererComponent(	JTable table,
															Object value,
															boolean isSelected,
															boolean hasFocus,
															int rowIndex,
															int vColIndex) {
				int mColIndex = table.convertColumnIndexToModel(vColIndex);

				if (mColIndex == 0)
					isSelected = false;
				hasFocus = false;
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
			}
		});

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(jTable);
		scrollPane.setAutoscrolls(true);

		JPanel northPanel = new JPanel(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(jToolBar1,  BorderLayout.NORTH);
		//northPanel.add(jComboBox1,  BorderLayout.CENTER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		updColorModel();
	}

	protected ThreshEditor getCurrentTED()
	{
		if (mtm != null && current_ev != -1)
		{
			// XXX - getThreshEditor will generate few unnecessary objects
			ModelTraceManager.ThreshEditor[] teds = mtm.getThreshEditor(current_ev);
			int current_th = jTable.getSelectedColumn() - 1;
			if (current_th >= 0 && current_th < teds.length)
				return teds[current_th];
		}
		return null;
	}

	private void updColorModel()
	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefUpdateEvent.typ);
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
					this.mtm = null;
			}
		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if(rue.thresholdsUpdated())
			{
				String id = (String)(rue.getSource());
				//if (id.equals("primarytrace"))
				{
					mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id);
					updateThresholds();
				}
			}
			if(rue.analysisPerformed())
			{
				String id = (String)(rue.getSource());
				//if (id.equals("primarytrace"))
				{
				    //System.out.println("thresholdsSelectionFrame: ANALYSIS_PERFORMED: source = '"+id+"'"); // FIXIT

					ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id);
				    //ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);
					bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
					
					if (mtm != null)
					{
						// FIXME:
						// --- тут была попытка сохранить пороги при
						// замене списка событий, и, в принципе, ее можно бы
						// доделать, если только она будет делать что-то осмысленное
						updateThresholds();
					}
				}
			}
			if(rue.eventSelected())
			{
				if (mtm != null)
				{
					current_ev = Integer.parseInt((String)rue.getSource());
					if (current_ev < 0 || current_ev >= mtm.getNEvents())
					{
					    System.out.println("Warning: current_ev out of range");
					    current_ev = 0;
					}
					updateThresholds();
				}
			}
			if(rue.thresholdChanged())
			{
				updateThresholds();
			}
		}
	}

	private void updateThresholds()
	{
		if (mtm == null)
			return;
		if (current_ev == -1)
			return;
		if (current_ev >= mtm.getNEvents())
			current_ev = mtm.getNEvents() - 1;

		ModelTraceManager.ThreshEditor[] te = mtm.getThreshEditor(current_ev);
		String[] pColumns = new String[te.length + 1];
		pColumns[0] = LangModelAnalyse.getString("thresholdsKey");
		for (int i = 1; i < pColumns.length; i++)
		{
			switch(te[i - 1].getType())
			{
			case ModelTraceManager.ThreshEditor.TYPE_A:
				pColumns[i] = LangModelAnalyse.getString("thresholdsAmplitude");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_L:
				pColumns[i] = LangModelAnalyse.getString("thresholdsHeight");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXF:
				pColumns[i] = LangModelAnalyse.getString("thresholdsDXF");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXT:
				pColumns[i] = LangModelAnalyse.getString("thresholdsDXT");
				break;
			// default... - @todo
			}
		}

		Object[][] pData = new Object[4][];
		for (int k = 0; k < 4; k++)
		{
			pData[k] = new Double[te.length];
			for (int i = 0; i < te.length; i++)
				((Double[] )pData[k])[i] = new Double(te[i].getValue(k));
		}

		ThresholdTableModel tModel = new ThresholdTableModel(
			pColumns,
			new String[] {
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")
			},
			new int[]    { 1, 2, 3, 4, 5 },
			pData);

		tModel.updateData(pData);
		jTable.setModel(tModel);
	}

	class ThresholdTableModel extends AbstractTableModel {
		private String[] p_columns;
		private Object[][] p_values;
		private String[] p_rows;
		int[] editable;
		public ThresholdTableModel(String[] p_columns,
				String[] p_rows, int[] editable, Object[][] p_data) {
			//super(p_columns, p_defaultv, p_rows, editable);
			this.p_columns = p_columns;
			this.p_values = p_data;
			this.p_rows = p_rows;
			this.editable = editable;
		}

		public void updateData(Object[][] pData)
		{
			p_values = pData;
		}

		public void setValueAt(Object value, int row, int col) {
			if (mtm != null && current_ev != -1) {
				ModelTraceManager.ThreshEditor[] te =
					mtm.getThreshEditor(current_ev);
				try
				{
					te[col - 1].setValue(
							row,
							Double.valueOf(value.toString()).doubleValue());
					dispatcher.notify(new RefUpdateEvent(this,
							RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				}
				catch(NumberFormatException e)
				{
					// ignore invalid input
				}
			}
		}

		public int getColumnCount()
		{
			return p_columns.length;
		}

		public int getRowCount()
		{
			return p_rows.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			if (columnIndex == 0)
				return p_rows[rowIndex];
			else
			{
				if (p_values == null)
					return ""; // XXX
				else
					return p_values[rowIndex][columnIndex - 1];
			}
		}

		public boolean isCellEditable(int row, int column)
		{
			return column > 0;
		}

		public String getColumnName(int column)
		{
			return p_columns[column];
		}
	}
}