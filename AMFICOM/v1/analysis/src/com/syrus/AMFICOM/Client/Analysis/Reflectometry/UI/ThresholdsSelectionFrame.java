package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager;
import com.syrus.AMFICOM.Client.Analysis.PermissionManager.Operation;
import com.syrus.AMFICOM.Client.General.Command.Analysis.CreateEtalonCommand;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.Thresh;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager.ThreshEditor;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * Отображает:
 * <ol>
 * <li> таблицу с численными значениями порогов, которые можно редактировать;
 * <li> кнопки для управления порогами - переход к пред/след событию,
 *    увеличить/уменьшить пороги, и, вроде как, загрузить начальные пороги(?)
 * </ol>
 */
@SuppressWarnings("all")
public class ThresholdsSelectionFrame extends JInternalFrame
implements PropertyChangeListener, BsHashChangeListener, ReportTable,
	CurrentEventChangeListener, EtalonMTMListener {
	protected Dispatcher dispatcher;
	ATable jTable;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	private List<JButton> createButtons = new ArrayList<JButton>();
	private List<JButton> editButtons = new ArrayList<JButton>();
	private List<JButton> navigateButtons = new ArrayList<JButton>();

	private ThresholdTableModel tModel;

	public ThresholdsSelectionFrame(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		} catch (Exception e)
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

	private void addButton(JToolBar toolbar,
			List<JButton> group,
			String tipKey,
			String iconKey,
			String text,
			ActionListener listener) {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setMargin(UIManager.getInsets(
				ResourceKeys.INSETS_ICONED_BUTTON));
		button.setToolTipText(LangModelAnalyse.getString(tipKey));
		if (iconKey != null) {
			button.setIcon(UIManager.getIcon(iconKey));
		}
		if (text != null) {
			button.setText(text);
		}
		button.addActionListener(listener);
		group.add(button);
		toolbar.add(button);
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
					" ",
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")},
			new int[] { },
			new Object[0][0]
		);
		this.jTable = new ATable(this.tModel);
		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		this.jTable.setDefaultEditor(Object.class, ThresholdEditor.getInstance());

		this.setContentPane(mainPanel);

		//jToolBar1.setBorderPainted(true);
		JToolBar jToolBar = new JToolBar();
		jToolBar.setFloatable(false);

		addButton(jToolBar,
				this.createButtons,
				"newThresholds",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_CREATE_NEW,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new CreateEtalonCommand().execute();
					}
				});

		addButton(jToolBar,
				this.editButtons,
				"analysisInitial",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Heap.setMTMEtalon(Heap.getMTMBackupEtalon());
					}
				});

		addButton(jToolBar,
				this.editButtons,
				"decreaseThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager.ThreshEditor ted = getCurrentTED();
						if (ted != null) {
							ted.decreaseValues();
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});

		addButton(jToolBar,
				this.editButtons,
				"increaseThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager.ThreshEditor ted = getCurrentTED();
						if (ted != null) {
							ted.increaseValues();
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});

		jToolBar.add(new JToolBar.Separator());

		addButton(jToolBar,
				this.navigateButtons,
				"previuosEvent",
				null,
				"<",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Heap.gotoPreviousEtalonEvent();
					}
				});

		addButton(jToolBar,
				this.navigateButtons,
				"nextEvent",
				null,
				">",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Heap.gotoNextEtalonEvent();
					}
				});

		jToolBar.add(new JToolBar.Separator());
		addButton(jToolBar,
				this.editButtons,
				"decreaseAllSoftThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE_ALL_SOFT,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager mtm = Heap.getMTMEtalon();
						if (mtm != null) {
							mtm.changeAllDyThresholdsBy(-0.1, 0.0);
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});

		addButton(jToolBar,
				this.editButtons,
				"increaseAllSoftThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE_ALL_SOFT,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager mtm = Heap.getMTMEtalon();
						if (mtm != null) {
							mtm.changeAllDyThresholdsBy(0.1, 0.0);
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});
		jToolBar.add(new JToolBar.Separator());

		addButton(jToolBar,
				this.editButtons,
				"decreaseAllHardThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE_ALL_HARD,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager mtm = Heap.getMTMEtalon();
						if (mtm != null) {
							mtm.changeAllDyThresholdsBy(0.0, -0.5);
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});

		addButton(jToolBar,
				this.editButtons,
				"increaseAllHardThresh",
				AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE_ALL_HARD,
				null,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModelTraceManager mtm = Heap.getMTMEtalon();
						if (mtm != null) {
							mtm.changeAllDyThresholdsBy(0.0, 0.5);
							dispatcher.firePropertyChange(new RefUpdateEvent(this,
								RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
						}
					}
				});

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
			this.jTable.changeSelection(current_th, 0, false, false);
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
					key = "thresholdsAmplitude";
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
			default:
				assert false;
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

	static class ThresholdEditor extends DefaultCellEditor {
		private static ThresholdEditor INSTANCE;
		
		public static ThresholdEditor getInstance() {
			if (INSTANCE == null)
				INSTANCE = new ThresholdEditor();
			return INSTANCE;
		}
		
		private ThresholdEditor() {
			super(new JTextField());
		}

		@Override
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
			this.values = pData;
			super.fireTableDataChanged();
		}

		@Override
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
			} catch(NumberFormatException e)
			{
				// ignore invalid input
			}
		}

		public int getColumnCount()
		{
			return this.columns.length;
		}

		public int getRowCount()
		{
			return this.values.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			return this.values[rowIndex][columnIndex];
		}

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return column > 0 &&
					PermissionManager.isPermitted(Operation.EDIT_ETALON);
		}

		@Override
		public String getColumnName(int column)
		{
			return this.columns[column];
		}
	}

	private void updateMakeEtalonButton() {
		for(JButton button: this.createButtons) {
			button.setEnabled(
				PermissionManager.isPermitted(Operation.EDIT_ETALON));
		}
	}

	public void bsHashAdded(String key) {
		updateMakeEtalonButton();
	}

	public void bsHashRemoved(String key) {
		updateMakeEtalonButton();
	}

	public void bsHashRemovedAll() {
		this.jTable.setModel(this.tModel);
	}

	public void currentEventChanged()
	{
		updateThresholds();
	}

	public void etalonMTMCUpdated()
	{
		updateThresholds();
		updateMakeEtalonButton();
		final boolean editEtalonPermitted =
				PermissionManager.isPermitted(Operation.EDIT_ETALON);
		for(JButton button: this.editButtons) {
			button.setEnabled(editEtalonPermitted);
		}
		for(JButton button: this.navigateButtons) {
			button.setEnabled(true);
		}
	}

	public void etalonMTMRemoved()
	{
		updateThresholds();
		updateMakeEtalonButton();
		for(JButton button: this.editButtons) {
			button.setEnabled(false);
		}
		for(JButton button: this.navigateButtons) {
			button.setEnabled(false);
		}
	}
}
