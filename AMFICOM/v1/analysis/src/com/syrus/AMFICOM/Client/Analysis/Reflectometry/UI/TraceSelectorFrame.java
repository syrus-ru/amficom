package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Analysis.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class TraceSelectorFrame extends JInternalFrame
implements BsHashChangeListener, EtalonMTMListener, CurrentTraceChangeListener
{
	protected static List traces = new ArrayList();
	private FixedSizeEditableTableModel tModel; //DefaultTableModel
	private ATable jTable;

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
					new Color[] {Color.BLACK},
					null,
					null);

//		tModel = new GeneralTableModel();

		jTable = new ATable(tModel);
		jTable.setDefaultRenderer(Color.class, ColorCellRenderer.getInstance());
		
		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);

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

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		String id = key;
		if (traces.contains(id))
			return;

		traces.add(id);

		Log.debugMessage("TraceSelectorFrame.bsHashAdded | id is '" + id + '\'', Log.FINEST);
		tModel.addRow(bs.title, new Color[] {GUIUtil.getColor(id)});
		jTable.updateUI();
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

        String name = LangModelAnalyse.getString("etalon");

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
