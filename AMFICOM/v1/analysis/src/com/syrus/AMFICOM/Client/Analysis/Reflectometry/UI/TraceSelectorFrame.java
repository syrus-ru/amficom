package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

public class TraceSelectorFrame extends JInternalFrame implements BsHashChangeListener, EtalonMTMListener,
		CurrentTraceChangeListener, PropertyChangeListener, RefMismatchListener {
	private static final long serialVersionUID = -2281313783873630551L;

	Dispatcher dispatcher;
	protected List<String> traces = new LinkedList<String>();
	private WrapperedTable jTable;
	private WrapperedTableModel<TraceResource> tModel;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();
	protected boolean here = false;

	public TraceSelectorFrame(final Dispatcher dispatcher) {
		super();

		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	private void init_module(final Dispatcher dispatcher1) {
		this.dispatcher = dispatcher1;
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentTraceChangeListener(this);
		Heap.addRefMismatchListener(this);
	}

	private void jbInit() throws Exception {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		// tModel = new GeneralTableModel();

		this.tModel = new WrapperedTableModel<TraceResource>(TraceResourceWrapper.getInstance(),
				new String[] { TraceResourceWrapper.KEY_IS_SHOWN, TraceResourceWrapper.KEY_TITLE, TraceResourceWrapper.KEY_COLOR });
		this.tModel.setColumnEditable(0, true);
		this.jTable = new WrapperedTable<TraceResource>(this.tModel);
		this.jTable.setAllowSorting(false);
		this.jTable.setTableHeader(null);

		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		this.jTable.getColumnModel().getColumn(1).setPreferredWidth(250);

		super.setContentPane(this.mainPanel);
		// this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		// this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("selectorTitle"));

		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final ListSelectionModel rowSM = this.jTable.getSelectionModel();
		this.jTable.getColumnModel().setSelectionModel(rowSM);
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting()) {
					return;
				}

				final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					// no rows are selected
				} else {
					if (TraceSelectorFrame.this.here) {
						TraceSelectorFrame.this.here = false;
					} else {
						final int selectedRow = lsm.getMinSelectionIndex();
						// XXX: по непон€тным мне причинам, при нажатии
						// TAB в последней строке таблицы, selectedRow
						// становитс€ равным кол-ву строк, что породит
						// ArrayIndexOutOfBoundsException
						// при вызове traces.get(selectedRow).
						// ѕоэтому принимаем только значени€ < traces.size()
						if (selectedRow < traces.size()) { 
							Heap.setCurrentTrace(traces.get(selectedRow));
						}
					}
				}
			}
		});

		// jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		// jTable.setMinimumSize(new Dimension(200, 213));
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.scrollPane.getViewport().add(this.jTable);
	}

	private void traceAdded(final String key) {
		final String id = key;
		if (traces.contains(id)) {
			return;
		}
		Log.debugMessage("id is '" + id + '\'', Level.FINEST);

		final TraceResource tr = new TraceResource(id);
		tr.addPropertyChangeListener(this);
		tr.setColor(GUIUtil.getColor(id));
		tr.setShown(true);

		if (Heap.ETALON_TRACE_KEY.equals(key)) {
			tr.setTitle(Heap.getEtalonName());
			//tr.setTitle(LangModelAnalyse.getString("etalon"));
		}
		else {
			tr.setTitle(Heap.getAnyPFTraceByKey(key).getBS().title);
		}

		if (Heap.PRIMARY_TRACE_KEY.equals(key)) {
			this.tModel.addObject(0, tr);
			traces.add(0, id);
		} else if (Heap.ETALON_TRACE_KEY.equals(key)) {
			final int index = traces.isEmpty() ? 0 : 1;
			this.tModel.addObject(index, tr);
			traces.add(index, id);
		} else {
			this.tModel.addObject(tr);
			traces.add(id);
		}
		super.setVisible(true);
	}

	private void traceRemoved(final String key) {
		int index = traces.indexOf(key);
		if (index != -1) {
			final TraceResource tr = this.tModel.getObject(index);
			tr.removePropertyChangeListener(this);
			this.tModel.removeObject(tr);
			traces.remove(key);
		}
	}

	public void bsHashAdded(final String key) {
		this.traceAdded(key);
	}

	public void bsHashRemoved(final String key) {
		this.traceRemoved(key);
	}

	public void bsHashRemovedAll() {
		final List<TraceResource> values = this.tModel.getValues();
		// NB: необходимо преобразовать к массиву, т.к. список, возвращенный
		// getValues(), может измен€тьс€ во врем€ удалени€ объектов,
		// что приведет к ConcurrentModificationException
		final TraceResource[] va = values.toArray(new TraceResource[values.size()]);
		for (final TraceResource tr: va) {
			tr.removePropertyChangeListener(this);
			this.tModel.removeObject(tr);
		}
		traces.clear();
		super.setVisible(false);
	}

	public void etalonMTMCUpdated() {
		this.traceAdded(Heap.ETALON_TRACE_KEY);
	}

	public void etalonMTMRemoved() {
		this.traceRemoved(Heap.ETALON_TRACE_KEY);
	}

	public void currentTraceChanged(final String id) {
		this.here = true;
		int selected = traces.indexOf(id);
		if (selected != -1) {
			this.jTable.setRowSelectionInterval(selected, selected);
		}
		this.here = false;
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(TraceResourceWrapper.KEY_IS_SHOWN)) {
			final TraceResource tr = (TraceResource) evt.getSource();
			this.dispatcher.firePropertyChange(new RefUpdateEvent(this, tr, RefUpdateEvent.TRACE_CHANGED_EVENT));
		}
	}

	private void updMismatchmark() {
		int index = traces.indexOf(Heap.PRIMARY_TRACE_KEY);
		if (index >= 0) {
			this.tModel.getObject(index).setAlarm(Heap.getRefMismatch() != null);
		}
		this.jTable.repaint(); // XXX: is this correct way of refreshing?
	}

	public void refMismatchCUpdated() {
		this.updMismatchmark();
	}

	public void refMismatchRemoved() {
		this.updMismatchmark();
	}
}
