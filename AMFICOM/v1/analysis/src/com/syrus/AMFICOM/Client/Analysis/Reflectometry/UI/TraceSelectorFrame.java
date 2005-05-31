package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.beans.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.Analysis.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class TraceSelectorFrame extends JInternalFrame
implements BsHashChangeListener, EtalonMTMListener, CurrentTraceChangeListener, PropertyChangeListener
{
	Dispatcher dispatcher;
	protected static List traces = new LinkedList();
	private WrapperedTable jTable;
	private WrapperedTableModel tModel;

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

		init_module(dispatcher);
	}

	private void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
	}

	private void jbInit() throws Exception
	{
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
//		tModel = new GeneralTableModel();

		tModel = new WrapperedTableModel(TraceResourceWrapper.getInstance(), new String[] {
			TraceResourceWrapper.KEY_IS_SHOWN,
			TraceResourceWrapper.KEY_TITLE,
			TraceResourceWrapper.KEY_COLOR
		});
		jTable = new WrapperedTable(tModel);
		jTable.setDefaultRenderer(Color.class, ColorCellRenderer.getInstance());
		
		jTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(250);

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
		
		TraceResource tr = new TraceResource(id);
		tr.addPropertyChangeListener(this);
		tr.setTitle(bs.title);
		tr.setColor(GUIUtil.getColor(id));
		tr.setShown(true);
		
		tModel.addObject(tr);
		setVisible(true);
	}

	public void bsHashRemoved(String key)
	{
		int index = traces.indexOf(key);
		if (index != -1)
		{
			TraceResource tr = (TraceResource)tModel.getObject(index);
			tr.removePropertyChangeListener(this);
			tModel.removeObject(tr);
			traces.remove(key);
		}
	}

	public void bsHashRemovedAll()
	{
		List values = tModel.getValues();
		for (Iterator it = values.iterator(); it.hasNext();) {
			TraceResource tr = (TraceResource)it.next();	
			tr.removePropertyChangeListener(this);
			it.remove();
		}
		traces.clear();
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
		
		TraceResource tr = new TraceResource(id);
		tr.addPropertyChangeListener(this);
		tr.setTitle(name);
		tr.setColor(GUIUtil.getColor(id));
		tr.setShown(true);
		
		tModel.addObject(tr);
		
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
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(TraceResourceWrapper.KEY_IS_SHOWN)) {
			TraceResource tr = (TraceResource)evt.getSource();
			this.dispatcher.firePropertyChange(new RefUpdateEvent(this, tr, RefUpdateEvent.TRACE_CHANGED_EVENT));
		}
	}
}
