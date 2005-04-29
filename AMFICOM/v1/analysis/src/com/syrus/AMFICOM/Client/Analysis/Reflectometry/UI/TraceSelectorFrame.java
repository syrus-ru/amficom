package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class TraceSelectorFrame extends JInternalFrame
implements BsHashChangeListener, EtalonMTMListener, CurrentTraceChangeListener
{
	protected static List traces = new ArrayList();
	private FixedSizeEditableTableModel tModel; //DefaultTableModel
	private ColorChooserTable jTable;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	protected boolean here = false;

	public TraceSelectorFrame(Dispatcher dispatcher)
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

		init_module();
	}

	private void init_module()
	{
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
	}

	private void jbInit() throws Exception
	{
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		tModel = new FixedSizeEditableTableModel(
					new String[] {LangModelAnalyse.getString("selectorKey"),
												LangModelAnalyse.getString("selectorValue")},
					new Object[] {Color.BLACK},
					null,
					null);

//		tModel = new GeneralTableModel();

		jTable = new ColorChooserTable (tModel);
//		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);

		setContentPane(mainPanel);
//		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("selectorTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = jTable.getSelectionModel();
		jTable.getColumnModel().setSelectionModel(rowSM);
		rowSM.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty())
			{
						//no rows are selected
			}
			else
			{
				if (here)
					here = false;
				else
				{
					int selectedRow = lsm.getMinSelectionIndex();
					//selectedRow is selected
					Heap.setCurrentTrace((String)traces.get(selectedRow));
				}
			}
		}
		});

//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}

	class DefaultTableModel extends AbstractTableModel {
		final String[] columnNames = {"Element", "Color"};
		final Object[][] data = {
				{"DefaultElement", Color.WHITE},
				{"DefaultElement2", Color.WHITE}
				};

		public int getColumnCount() {
				return columnNames.length;
		}

		public int getRowCount() {
				return data.length;
		}

		public String getColumnName(int col) {
				return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
				return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
				//Note that the data/cell address is constant,
				//no matter where the cell appears onscreen.
				if (col < 1) {
						return false;
				} else {
						return true;
				}
		}

		public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
		}
	}
	

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		String id = key;
		if (traces.contains(id))
			return;

		traces.add(id);

		Log.debugMessage("TraceSelectorFrame.bsHashAdded | id is '" + id + '\'', Log.FINEST);
		tModel.addRow(bs.title, new Color[] {GUIUtil.getColor(id)});
		setVisible(true);
	}

	public void bsHashRemoved(String key)
	{
		int index = traces.indexOf(key);
		if (index != -1)
		{
			tModel.removeRow(index);
			traces.remove(key);
		}
	}

	public void bsHashRemovedAll()
	{
		tModel.clearTable();
		traces = new ArrayList();
		setVisible(false);
	}

	public void etalonMTMCUpdated()
	{
		// @todo: implement this idea correctly (here and in etalonMTMRemoved)
		String id = Heap.ETALON_TRACE_KEY;
		if (traces.contains(id))
			return;

		traces.add(id);

        String name = "etalon"; // @todo: externalize string

		Log.debugMessage("TraceSelectorFrame.etalonMTMCUpdated | id is '" + id + "'; name = '" + name + "'", Log.FINEST);
		tModel.addRow(name, new Color[] {GUIUtil.getColor(id)});
		setVisible(true);
	}

	public void etalonMTMRemoved()
	{
	}

	public void currentTraceChanged(String id)
	{
		here = true;
		int selected = traces.indexOf(id);
		if (selected != -1)
			jTable.setRowSelectionInterval(selected, selected);
		here = false;
	}
}
