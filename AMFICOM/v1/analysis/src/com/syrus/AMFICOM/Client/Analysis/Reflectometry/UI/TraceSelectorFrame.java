package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

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

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.analysis.TraceResourceWrapper;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

public class TraceSelectorFrame extends JInternalFrame
implements BsHashChangeListener, EtalonMTMListener, CurrentTraceChangeListener,
		PropertyChangeListener, RefMismatchListener
{
	Dispatcher dispatcher;
	protected static List<String> traces = new LinkedList<String>();
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
		} catch (Exception e)
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
		Heap.addRefMismatchListener(this);
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
		tModel.setColumnEditable(0, true);
		jTable = new WrapperedTable(tModel);
		jTable.setAllowSorting(false);
				
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
			} else
			{
				if (here)
					here = false;
				else
				{
					int selectedRow = lsm.getMinSelectionIndex();
					//selectedRow is selected
					Heap.setCurrentTrace(traces.get(selectedRow));
				}
			}
		}
		});

//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}

	private void traceAdded(String key) {
		String id = key;
		if (traces.contains(id))
			return;

		Log.debugMessage("TraceSelectorFrame.traceAdded | id is '" + id + '\'', Level.FINEST);

		TraceResource tr = new TraceResource(id);
		tr.addPropertyChangeListener(this);
		tr.setColor(GUIUtil.getColor(id));
		tr.setShown(true);

		if (Heap.ETALON_TRACE_KEY.equals(key))
			tr.setTitle(LangModelAnalyse.getString("etalon"));
		else
			tr.setTitle(Heap.getAnyBSTraceByKey(key).title);

		if (Heap.PRIMARY_TRACE_KEY.equals(key)) {
			tModel.addObject(0, tr);
			traces.add(0, id);
		} else if (Heap.ETALON_TRACE_KEY.equals(key)) {
			int index = traces.isEmpty() ? 0 : 1;
			tModel.addObject(index, tr);
			traces.add(index, id);
		} else {
			tModel.addObject(tr);
			traces.add(id);
		}
		setVisible(true);
	}

	private void traceRemoved(String key) {
		int index = traces.indexOf(key);
		if (index != -1)
		{
			TraceResource tr = (TraceResource)tModel.getObject(index);
			tr.removePropertyChangeListener(this);
			tModel.removeObject(tr);
			traces.remove(key);
		}
	}

	public void bsHashAdded(String key) {
		traceAdded(key);
	}

	public void bsHashRemoved(String key) {
		traceRemoved(key);
	}

	public void bsHashRemovedAll() {
		List<TraceResource> values = tModel.getValues();
		for (Iterator<TraceResource> it = values.iterator(); it.hasNext();) {
			TraceResource tr = it.next();	
			tr.removePropertyChangeListener(this);
			it.remove();
		}
		traces.clear();
		setVisible(false);
	}

	public void etalonMTMCUpdated() {
		traceAdded(Heap.ETALON_TRACE_KEY);
	}

	public void etalonMTMRemoved() {
		traceRemoved(Heap.ETALON_TRACE_KEY);
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

	private void updMismatchmark() {
		int index = traces.indexOf(Heap.PRIMARY_TRACE_KEY);
		if (index >= 0) {
			((TraceResource)tModel.getObject(index)).setAlarm(
					Heap.getRefMismatch() != null);
		}
		jTable.repaint(); // XXX: is this correct way of refreshing?
	}

	public void refMismatchCUpdated() {
		updMismatchmark();
	}

	public void refMismatchRemoved() {
		updMismatchmark();
	}
}
