package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;
 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThreshEditor;
import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;
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
implements OperationListener, BsHashChangeListener,
	CurrentEventChangeListener, EtalonMTMListener
{
	protected Dispatcher dispatcher;
	JTable jTable;

	protected ModelTraceManager et_mtm; // etalon MTM

	protected int currentEtEv = -1;

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
				if (et_mtm != null && currentEtEv != -1)
				{
					// FIXME: outdated / useless mtm.setThreshold(...,init_Threshs...) removed
					// the button should be removed or replaced with something more useful
				}
			}
		});

		analysisDefaultsButton.setToolTipText(
			LangModelAnalyse.getString("analysisDefaults"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(
			AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_CREATE_NEW));
		analysisDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (et_mtm != null && currentEtEv != -1)
				{
					et_mtm.setDefaultThreshold(currentEtEv);
					updateThresholds();
					dispatcher.notify(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				}
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
					dispatcher.notify(new RefUpdateEvent(this,
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
					dispatcher.notify(new RefUpdateEvent(this,
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

				dispatcher.notify(new OperationEvent(this,
					0,
					AnalyseApplicationModel.SELECT_PREVIOUS_EVENT));

			}
		});
		
		nextEventButton.setToolTipText(
			LangModelAnalyse.getString("nextEvent"));
		nextEventButton.setText(">");
		nextEventButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				dispatcher.notify(new OperationEvent(this,
					0,
					AnalyseApplicationModel.SELECT_NEXT_EVENT));

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
			new ADefaultTableCellRenderer() {

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

	protected ThreshEditor getCurrentTED()
	{
		if (et_mtm != null && currentEtEv != -1)
		{
			// XXX - getThreshEditor will generate few unnecessary objects
			ModelTraceManager.ThreshEditor[] teds =
				et_mtm.getThreshEditors(currentEtEv);
			int current_th = this.jTable.getSelectedColumn() - 1;
			if (current_th >= 0 && current_th < teds.length)
				return teds[current_th];
		}
		return null;
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		dispatcher1.register(this, RefUpdateEvent.typ);
		Heap.addBsHashListener(this);
		Heap.addCurrentEventChangeListener(this);
		Heap.addEtalonMTMListener(this);
	}

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(RefUpdateEvent.typ)) {
			RefUpdateEvent rue = (RefUpdateEvent) ae;
//			if (rue.thresholdsUpdated()) {
//				{
//					et_mtm = Heap.getMTMEtalon();
//					updateThresholds();
//				}
//			}
			if (rue.analysisPerformed()) {
				//mtm = Heap.getMTMByKey(id); // FIXME: what is MTM here?  
				//bs = Heap.getAnyBSTraceByKey(id); // bs was never read locally
				updateThresholds();
			}
			if (rue.thresholdChanged()) {
				updateThresholds();
			}
		}
	}

	void updateThresholds()
	{
		// default values: no thresholds
		ModelTraceManager.ThreshEditor[] te = null;

		// if current event is present, select it
		if (et_mtm != null && currentEtEv >= 0)
		{
			if (currentEtEv >= et_mtm.getMTAE().getNEvents()) // XXX: should never happen?
				currentEtEv = et_mtm.getMTAE().getNEvents() - 1;
			te = et_mtm.getThreshEditors(currentEtEv);
		}
		else
			te = new ModelTraceManager.ThreshEditor[0];

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
			if (et_mtm != null && currentEtEv != -1) {
				ModelTraceManager.ThreshEditor[] te =
					et_mtm.getThreshEditors(currentEtEv);
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

	public void bsHashAdded(String key, BellcoreStructure bs)
	{ // ignore (?)
	}

	public void bsHashRemoved(String key)
	{ // ignore (?)
	}

	public void bsHashRemovedAll()
	{
			this.et_mtm = null;
			this.jTable.setModel(this.tModelEmpty);
	}

	public void currentEventChanged()
	{
		if (et_mtm != null) {
			currentEtEv = Heap.getCurrentEtalonEvent();
			if (currentEtEv < -1 ||
					currentEtEv >= et_mtm.getMTAE().getNEvents()) {
				// XXX: debug sysout
				System.out.println("ThresholdsSelectionFrame: Warning: current_ev out of range:"
					+ " currentEtEv=" + currentEtEv
					+ " et_mtm.MTAE.NEvents=" + et_mtm.getMTAE().getNEvents()
					+ " Heap.MTMEtalon.MTAE.NEvents=" + Heap.getMTMEtalon().getMTAE().getNEvents());
				currentEtEv = -1;
			}
			updateThresholds();
		}
	}

	public void etalonMTMCUpdated()
	{
		et_mtm = Heap.getMTMEtalon();
		updateThresholds();
	}

	public void etalonMTMRemoved()
	{
		et_mtm = Heap.getMTMEtalon();
		updateThresholds();
	}
}
