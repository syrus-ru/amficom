package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
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
	JTable jTable;

	private BellcoreStructure bs; // для доступа к самой р/г во время пересчета порогов

	protected ModelTraceManager mtm;

	protected int current_ev = -1;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	
	private ThresholdTableModel tModelEmpty;

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
		return this.jTable.getModel();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("thresholdsTableTitle"));

		this.tModelEmpty = new ThresholdTableModel( // FIXME - переделать
			new String[] { LangModelAnalyse.getString("thresholdsKey") },
			new String[] {
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")},
			new int[] { },
			null
		);
		this.jTable = new ATable(this.tModelEmpty);
		
		JButton alysisInitialButton = new JButton();
		JButton analysisDefaultsButton = new JButton();
		JButton increaseThreshButton = new JButton();
		JButton decreaseThreshButton = new JButton();
		
		JButton previuosEventButton = new JButton();
		JButton nextEventButton = new JButton();

		{	// set up button size
			JButton[] buttons = new JButton[] { alysisInitialButton, analysisDefaultsButton, increaseThreshButton, decreaseThreshButton, previuosEventButton, 
					nextEventButton};
			for (int i = 0; i < buttons.length; i++)
			{
				buttons[i].setMaximumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
				buttons[i].setMinimumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
				buttons[i].setPreferredSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
			}
		}

		alysisInitialButton.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		alysisInitialButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL));
		alysisInitialButton.addActionListener(new ActionListener()
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
					int selectedColumn = jTable.getSelectedColumn();
					dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
					jTable.setColumnSelectionInterval(selectedColumn, selectedColumn);
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
					int selectedColumn = jTable.getSelectedColumn();
					ted.decreaseValues();
					dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
					jTable.setColumnSelectionInterval(selectedColumn, selectedColumn);
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
		JToolBar jToolBar = new JToolBar();
		jToolBar.setFloatable(false);
		jToolBar.add(alysisInitialButton);
		jToolBar.add(analysisDefaultsButton);
		jToolBar.add(decreaseThreshButton);
		jToolBar.add(increaseThreshButton);
		jToolBar.add(Box.createRigidArea(UIManager.getDimension(ResourceKeys.SIZE_BUTTON)));
		jToolBar.add(previuosEventButton);
		jToolBar.add(nextEventButton);

		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		this.jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		this.jTable.setMinimumSize(new Dimension(200, 213));
		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.jTable.setColumnSelectionAllowed(true);
		this.jTable.setRowSelectionAllowed(false);		
		
		this.jTable.setDefaultRenderer(Object.class, new ADefaultTableCellRenderer() {

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
		
		this.jTable.getTableHeader().addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				/* recipe get at http://www.javaalmanac.com/egs/javax.swing.table/ColHeadEvent.html */
				JTable table = ((JTableHeader) evt.getSource()).getTable();
				TableColumnModel colModel = table.getColumnModel();

				// The index of the column whose header was clicked
				int vColIndex = colModel.getColumnIndexAtX(evt.getX());
				int mColIndex = table.convertColumnIndexToModel(vColIndex);

				// Return if not clicked on any column header
				if (vColIndex == -1) { return; }

				// Determine if mouse was clicked between column heads
				Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
				if (vColIndex == 0) {
					headerRect.width -= 3; // Hard-coded constant
				} else {
					headerRect.grow(-3, 0); // Hard-coded constant
				}
				if (headerRect.contains(evt.getX(), evt.getY())) {
					table.setColumnSelectionInterval(mColIndex, mColIndex);
				}
			}
		});

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(this.jTable);
		scrollPane.setAutoscrolls(true);

		JPanel northPanel = new JPanel(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(jToolBar,  BorderLayout.NORTH);
		//northPanel.add(jComboBox1,  BorderLayout.CENTER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}

	protected ThreshEditor getCurrentTED()
	{
		if (mtm != null && current_ev != -1)
		{
			// XXX - getThreshEditor will generate few unnecessary objects
			ModelTraceManager.ThreshEditor[] teds = mtm.getThreshEditor(current_ev);
			int current_th = this.jTable.getSelectedColumn() - 1;
			if (current_th >= 0 && current_th < teds.length)
				return teds[current_th];
		}
		return null;
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
				String id = (String) (rce.getSource());
				if (id.equals("all")) {
					this.mtm = null;
					this.jTable.setModel(this.tModelEmpty);
				}
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

	void updateThresholds()
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
		this.jTable.setModel(tModel);
	}

	class ThresholdTableModel extends AbstractTableModel {
		private String[] columns;
		private Object[][] values;
		private String[] rows;
		int[] editable;
		public ThresholdTableModel(String[] columns,
				String[] rows, int[] editable, Object[][] data) {
			this.columns = columns;
			this.values = data;
			this.rows = rows;
			this.editable = editable;
		}

		public void updateData(Object[][] pData)
		{
			values = pData;
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
			return columns.length;
		}

		public int getRowCount()
		{
			return rows.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			if (columnIndex == 0)
				return rows[rowIndex];
			else
			{
				if (values == null)
					return ""; // XXX
				else
					return values[rowIndex][columnIndex - 1];
			}
		}

		public boolean isCellEditable(int row, int column)
		{
			return column > 0;
		}

		public String getColumnName(int column)
		{
			return columns[column];
		}
	}
}