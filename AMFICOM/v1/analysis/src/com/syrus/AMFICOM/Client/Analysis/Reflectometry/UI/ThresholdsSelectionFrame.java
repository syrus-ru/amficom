package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;
 
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.*;
import java.beans.PropertyChangeListener;

import javax.swing.*;
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
import com.syrus.AMFICOM.analysis.dadara.*;
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
public class ThresholdsSelectionFrame extends JInternalFrame
implements PropertyChangeListener, BsHashChangeListener, ReportTable,
	CurrentEventChangeListener, EtalonMTMListener
{
	protected Dispatcher dispatcher;
	ATable jTable;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	
	JButton analysisInitialButton;
	JButton analysisDefaultsButton;
	JButton increaseThreshButton;
	JButton decreaseThreshButton;
	JButton previuosEventButton;
	JButton nextEventButton;
	
	private ThresholdTableModel tModel;

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

		this.tModel = new ThresholdTableModel(
			new String[] {
					"",
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")},
			new int[] { },
			new Object[0][0]
		);
		this.jTable = new ATable(this.tModel);
		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		this.jTable.setDefaultEditor(Object.class, new ThresholdEditor());
		
		analysisInitialButton = new JButton();
		analysisInitialButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		analysisDefaultsButton = new JButton();
		analysisDefaultsButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		increaseThreshButton = new JButton();
		increaseThreshButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		decreaseThreshButton = new JButton();
		decreaseThreshButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		
		previuosEventButton = new JButton();
		previuosEventButton.setMargin(UIManager.getInsets(
			ResourceKeys.INSETS_ICONED_BUTTON));
		
		nextEventButton = new JButton();
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
				analysisInitialButton.setEnabled(true);
      	increaseThreshButton.setEnabled(true);
      	decreaseThreshButton.setEnabled(true);
      	previuosEventButton.setEnabled(true);
      	nextEventButton.setEnabled(true);
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
					dispatcher.firePropertyChange(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
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
					ted.decreaseValues();
					dispatcher.firePropertyChange(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
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

		analysisInitialButton.setEnabled(false);
  	increaseThreshButton.setEnabled(false);
  	decreaseThreshButton.setEnabled(false);
  	previuosEventButton.setEnabled(false);
  	nextEventButton.setEnabled(false);
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

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.jTable.setDefaultRenderer(Object.class,
			new ADefaultTableCellRenderer.ObjectRenderer());

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
    protected ModelTraceManager.ThreshEditorWithDefaultMark[] getTeds()
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
        ModelTraceManager.ThreshEditorWithDefaultMark[] teds = getTeds();

        if (teds == null)
            return null;

        // сначала пытаемся выбрать редактор для текущей строки
		int current_th = this.jTable.getSelectedRow();
        if (current_th >= 0 && current_th < teds.length)
                return teds[current_th];

        // если текущей строки нет, выбираем порог по умолчанию
        current_th = ModelTraceManager.getDefaultThreshEditorIndex(teds);
        if (current_th >= 0) {
            // и делаем его текущим
            this.jTable.changeSelection(current_th, 0, false, true);
            return teds[current_th];
        }

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

	void updateThresholds() {
		int selected = jTable.getSelectedRow();
		ThreshEditor[] te = getTeds();
		if (te == null)
			te = new ThreshEditor[0];

		int n = te.length;
		Object[][] pData = new Object[n][5];
		for (int i = 0; i < n; i++) {
			String key = null;
			switch (te[i].getType()) {
			case ModelTraceManager.ThreshEditor.TYPE_A:
				if (i == 0 && i != n - 1)
					key = "thresholdsAmplitudeBegin";
				else if (i == n - 1 && i != 0)
					key = "thresholdsAmplitudeEnd";
				else
					key = "thresholdsAmplitude"; // currently, never happed
				break;
			case ModelTraceManager.ThreshEditor.TYPE_L:
				key = "thresholdsHeight";
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXF:
				key = "thresholdsDXF";
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXT:
				key = "thresholdsDXT";
				break;
			// default... - @todo
			}
			pData[i][0] = LangModelAnalyse.getString(key);
			for (int j = 1; j < 5; j++)
				pData[i][j] =
					new Double(MathRef.floatRound(te[i].getValue(j - 1), 4));
		}
		tModel.updateData(pData);
		if (selected != -1 && selected <= jTable.getRowCount())
			jTable.getSelectionModel().setSelectionInterval(selected, selected);
	}

	class ThresholdEditor extends DefaultCellEditor {
		public ThresholdEditor() {
			super(new JTextField());
		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			JTextField tf = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
			Color c = UIManager.getColor(Thresh.isKeyHard(column - 1)
					? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
          : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD);
			tf.setSelectionColor(c);
			return tf;
		}
	}
	
	class ThresholdTableModel extends AbstractTableModel {
		private String[] columns;
		private Object[][] values;
		int[] editable;
		public ThresholdTableModel(String[] columns,
				int[] editable, Object[][] data) {
			this.columns = columns;
			this.values = data;
			this.editable = editable;
		}
		
		public void setColumns(String[] columns) {
			this.columns = columns;
			super.fireTableStructureChanged();
		}

		public void updateData(Object[][] pData)
		{
			values = pData;
			super.fireTableDataChanged();
		}

		public void setValueAt(Object value, int row, int col) {
            // XXX: create extra objects... it's better to save teds when it is selected and just take them now
			ModelTraceManager.ThreshEditor[] te = getTeds();
            if (te == null)
                return; // XXX: can it happen?
			try
			{
				te[row].setValue(
						col - 1,
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
			return values.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			return values[rowIndex][columnIndex];
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
		this.jTable.setModel(this.tModel);
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
		analysisInitialButton.setEnabled(false);
		increaseThreshButton.setEnabled(false);
		decreaseThreshButton.setEnabled(false);
		previuosEventButton.setEnabled(false);
		nextEventButton.setEnabled(false);
	}
}
