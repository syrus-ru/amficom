package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;
 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.*;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
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

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateEtalonCommand;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThreshEditor;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

/**
 * Отображает:
 * <ol>
 * <li> таблицу с численными значениями порогов, которые можно редактировать;
 * <li> кнопки для управления порогами - переход к пред/след событию,
 *    увеличить/уменьшить пороги, и, вроде как, загрузить начальные пороги(?)
 * </ol>
 */
public class ThresholdsSelectionFrame extends ATableFrame
implements PropertyChangeListener, BsHashChangeListener,
	CurrentEventChangeListener, EtalonMTMListener
{
	protected Dispatcher dispatcher;
	JTable jTable;

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
		
		JButton analysisInitialButton = new JButton();
		analysisInitialButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		JButton analysisDefaultsButton = new JButton();
		analysisDefaultsButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		JButton increaseThreshButton = new JButton();
		increaseThreshButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		JButton decreaseThreshButton = new JButton();
		decreaseThreshButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		
		JButton previuosEventButton = new JButton();
		previuosEventButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		JButton nextEventButton = new JButton();
		nextEventButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));

		analysisInitialButton.setToolTipText(
			LangModelAnalyse.getString("analysisInitial"));
		analysisInitialButton.setIcon(UIManager.getIcon(
			AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL));
		analysisInitialButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
                Heap.setMTMEtalon(Heap.getMTMBackupEtalon());
			}
		});

		analysisDefaultsButton.setToolTipText(
			LangModelAnalyse.getString("newThresholds"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(
			AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_CREATE_NEW));
		analysisDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new CreateEtalonCommand().execute();
			}
		});

		increaseThreshButton.setToolTipText(
			LangModelAnalyse.getString("increaseThresh"));
		increaseThreshButton.setIcon(UIManager.getIcon(
			AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE));
		increaseThreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ModelTraceManager.ThreshEditor ted = getCurrentTED();
				if (ted != null)
				{
					ted.increaseValues();
					int selectedColumn = jTable.getSelectedColumn();
					dispatcher.firePropertyChange(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
					jTable.setColumnSelectionInterval(selectedColumn,
						selectedColumn);
				}
			}
		});

		decreaseThreshButton.setToolTipText(
			LangModelAnalyse.getString("decreaseThresh"));
		decreaseThreshButton.setIcon(UIManager.getIcon(
			AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE));
		decreaseThreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ModelTraceManager.ThreshEditor ted = getCurrentTED();
				if (ted != null)
				{
					int selectedColumn = jTable.getSelectedColumn();
					ted.decreaseValues();
					dispatcher.firePropertyChange(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
					jTable.setColumnSelectionInterval(selectedColumn,
						selectedColumn);
				}
			}
		});
		
		previuosEventButton.setToolTipText(
			LangModelAnalyse.getString("previuosEvent"));
		previuosEventButton.setText("<");
		previuosEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Heap.gotoPreviousEtalonEvent();
			}
		});
		
		nextEventButton.setToolTipText(
			LangModelAnalyse.getString("nextEvent"));
		nextEventButton.setText(">");
		nextEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Heap.gotoNextEtalonEvent();
			}
		});

		this.setContentPane(mainPanel);

		//jToolBar1.setBorderPainted(true);
		JToolBar jToolBar = new JToolBar();
		jToolBar.setFloatable(false);
		jToolBar.add(analysisDefaultsButton);
		jToolBar.add(analysisInitialButton);
		jToolBar.add(decreaseThreshButton);
		jToolBar.add(increaseThreshButton);
		jToolBar.add(new JToolBar.Separator());
		jToolBar.add(previuosEventButton);
		jToolBar.add(nextEventButton);

//		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
//		this.jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		this.jTable.setMinimumSize(new Dimension(200, 213));
		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.jTable.setColumnSelectionAllowed(true);
		this.jTable.setRowSelectionAllowed(false);		
		
		this.jTable.setDefaultRenderer(Object.class,
			new ADefaultTableCellRenderer.ObjectRenderer() {

			// (1) hides 'focus' - just because its color overrides selection
			// colot
			// (2) suppress visualization of selection of 1st column
			public Component getTableCellRendererComponent(	JTable table,
															Object value,
															boolean isSelected1,
															boolean hasFocus,
															int rowIndex,
															int vColIndex) {
				int mColIndex = table.convertColumnIndexToModel(vColIndex);

				if (mColIndex == 0)
					isSelected1 = false;
				hasFocus = false;
				return super.getTableCellRendererComponent(table, value,
					isSelected1, hasFocus, rowIndex, vColIndex);
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
				Rectangle headerRect =
					table.getTableHeader().getHeaderRect(vColIndex);
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

    // gets all thresholds editors relevant to current event
    protected ThreshEditor[] getTeds()
    {
        ModelTraceManager mtm = Heap.getMTMEtalon();
        if (mtm == null)
            return null;
        int etEvent = Heap.getCurrentEtalonEvent2();
        if (etEvent < 0)
            return null;
        return mtm.getThreshEditors(etEvent);
    }
	protected ThreshEditor getCurrentTED()
	{
		// XXX - getThreshEditor will generate few unnecessary objects
		ModelTraceManager.ThreshEditor[] teds = getTeds();
		int current_th = this.jTable.getSelectedColumn() - 1;
		if (current_th >= 0 && current_th < teds.length)
			return teds[current_th];
		return null;
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		dispatcher1.addPropertyChangeListener(RefUpdateEvent.typ, this);
		Heap.addBsHashListener(this);
		Heap.addCurrentEventChangeListener(this);
		Heap.addEtalonMTMListener(this);
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(RefUpdateEvent.typ)) {
			RefUpdateEvent rue = (RefUpdateEvent) ae;
			if (rue.thresholdChanged()) {
				updateThresholds();
			}
		}
	}

	void updateThresholds()
	{
		ThreshEditor[] te = getTeds();
        if (te == null)
            te = new ThreshEditor[0];

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
            // XXX: round values to be displayed with 0.0001 precision
			for (int i = 0; i < te.length; i++)
				((Double[] )pData[k])[i] = new Double(
                        MathRef.round_4(te[i].getValue(k)));
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
            // XXX: create extra objects... it's better to save teds when it is selected and just take them now
			ModelTraceManager.ThreshEditor[] te = getTeds();
            if (te == null)
                return; // XXX: can it happen?
			try
			{
				te[col - 1].setValue(
						row,
						Double.valueOf(value.toString()).doubleValue());
				dispatcher.firePropertyChange(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
			catch(NumberFormatException e)
			{
				// ignore invalid input
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

	public void bsHashAdded(String key, BellcoreStructure bs)
	{ // ignore (?)
	}

	public void bsHashRemoved(String key)
	{ // ignore (?)
	}

	public void bsHashRemovedAll()
	{
			this.jTable.setModel(this.tModelEmpty);
	}

	public void currentEventChanged()
	{
		updateThresholds();
	}

	public void etalonMTMCUpdated()
	{
		updateThresholds();
	}

	public void etalonMTMRemoved()
	{
		updateThresholds();
	}
}
