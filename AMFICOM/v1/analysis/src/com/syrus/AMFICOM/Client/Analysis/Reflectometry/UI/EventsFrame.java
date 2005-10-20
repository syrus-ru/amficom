package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.CompositeEventList;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.DetailedEventResource;
import com.syrus.AMFICOM.analysis.DetailedEventWrapper;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class EventsFrame extends JInternalFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener, ReportTable,
		CurrentEventChangeListener, PropertyChangeListener, RefMismatchListener {
	private static final long serialVersionUID = 6768761574582221386L;

	private ApplicationContext aContext;
	private WrapperedTable<DetailedEventResource> table;
	
	private boolean primaryShown = false;
	private boolean etalonShown = false;
	private boolean primaryOpened = false;
	private boolean etalonOpened = false;

	private final boolean showComparison;

	protected static class TableView {
		public static final int COMP = 10;
		public static final int PRIM = 11;
		public static final int ETAL = 12;
		private int viewMode;

		public TableView(final int viewMode) {
			this.viewMode = viewMode;
		}

		public void setViewMode(final int viewMode) {
			this.viewMode = viewMode;
		}

		public int nRows(final CompositeEventList eList) {
			switch (this.viewMode) {
				case COMP:
					return eList.getNCompositeEvents();
				case PRIM:
					return eList.getNEvents();
				case ETAL:
					return eList.getNEtalonEvents();
				default:
					throw new InternalError();
			}
		}

		public int nPri1(final CompositeEventList eList, final int row) {
			switch (this.viewMode) {
				case COMP:
					return eList.getC2P(row);
				case PRIM:
					return row;
				case ETAL:
					return eList.getC2P(eList.getE2C(row));
				default:
					throw new InternalError();
			}
		}

		public int nEt1(final CompositeEventList eList, final int row) {
			switch (this.viewMode) {
				case COMP:
					return eList.getC2E(row);
				case PRIM:
					return eList.getC2E(eList.getP2C(row));
				case ETAL:
					return row;
				default:
					throw new InternalError();
			}
		}

		public int nComp(final CompositeEventList eList, final int row) {
			switch (this.viewMode) {
				case COMP:
					return row;
				case PRIM:
					return eList.getP2C(row);
				case ETAL:
					return eList.getE2C(row);
				default:
					throw new InternalError();
			}
		}

		public void toNextRow(final CompositeEventList.Walker walker) {
			switch (this.viewMode) {
				case COMP:
					walker.toNextCompositeEvent();
					break;
				case PRIM:
					walker.toNextEvent();
					break;
				case ETAL:
					walker.toNextEtalonEvent();
					break;
				default:
					throw new InternalError();
			}
		}

		public int currentRow2() {
			switch (this.viewMode) {
				case COMP:
					return Heap.getCurrentCompositeEvent();
				case PRIM:
					return Heap.getCurrentEvent2();
				case ETAL:
					return Heap.getCurrentEtalonEvent2();
				default:
					throw new InternalError();
			}
		}

		public void moveTo(final int row) {
			switch (this.viewMode) {
				case COMP:
					Heap.setCurrentCompositeEvent(row);
					return;
				case PRIM:
					Heap.setCurrentEvent(row);
					return;
				case ETAL:
					Heap.setCurrentEtalonEvent(row);
					return;
				default:
					throw new InternalError();
			}
		}
	}

	protected TableView view; // State

	public EventsFrame(final ApplicationContext aContext, final boolean showComparison) {
		this.showComparison = showComparison;
		this.init();
		this.initModule(aContext);
	}

	private void initModule(final ApplicationContext aContext1) {
		this.aContext = aContext1;
		this.aContext.getDispatcher().addPropertyChangeListener(RefUpdateEvent.typ, this);

		this.view = new TableView(TableView.PRIM);
		this.updateEventsModel();

		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addRefMismatchListener(this);
	}

	public String getReportTitle() {
		return LangModelAnalyse.getString("eventTableTitle");
	}

	public TableModel getTableModel() {
		return this.table.getModel();
	}

	private void init() {
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		final String[] keys = this.showComparison
			? new String[] {
					DetailedEventWrapper.KEY_N,
					DetailedEventWrapper.KEY_IMAGE,
					DetailedEventWrapper.KEY_TYPE,
					DetailedEventWrapper.KEY_DISTANCE,
					DetailedEventWrapper.KEY_LENGTH,
					DetailedEventWrapper.KEY_REFLECTANCE,
					DetailedEventWrapper.KEY_LOSS,
					DetailedEventWrapper.KEY_ATTENUATION,
					DetailedEventWrapper.KEY_QUALITY_QI,
					DetailedEventWrapper.KEY_QUALITY_KI
					}
			: new String[] {
					DetailedEventWrapper.KEY_N,
					DetailedEventWrapper.KEY_IMAGE,
					DetailedEventWrapper.KEY_TYPE,
					DetailedEventWrapper.KEY_DISTANCE,
					DetailedEventWrapper.KEY_LENGTH,
					DetailedEventWrapper.KEY_REFLECTANCE,
					DetailedEventWrapper.KEY_LOSS,
					DetailedEventWrapper.KEY_ATTENUATION
					};
		this.table = new WrapperedTable<DetailedEventResource>(
				new WrapperedTableModel<DetailedEventResource>(
					DetailedEventWrapper.getInstance(),
					keys));
		this.table.setAllowSorting(false);
		this.table.setAllowAutoResize(true);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setAutoscrolls(true);
		
		super.setContentPane(scrollPane);
		this.setResizable(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("eventTableTitle"));

		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final ListSelectionModel rowSM = this.table.getSelectionModel();		
		rowSM.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(final ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting()) {
					return;
				}

				final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!lsm.isSelectionEmpty()) {
					final int selected = lsm.getMinSelectionIndex();
					if (EventsFrame.this.view.currentRow2() != selected) {
						EventsFrame.this.view.moveTo(selected);
					}
				}
			}
		});

		final JPopupMenu popupMenu = this.createPopupMenu();

		// FIXME: event edition is currently for internal use only. Should be either improved or disabled in final version. 
		this.table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		this.table.setDefaultRenderer(Object.class, new EventTableRenderer());
		
		scrollPane.getViewport().add(this.table);
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(RefUpdateEvent.typ)) {
			final RefUpdateEvent ev = (RefUpdateEvent) evt;
			if (ev.traceChanged()) {
				final TraceResource tr = (TraceResource) evt.getNewValue();
				if (tr.getId().equals(Heap.PRIMARY_TRACE_KEY)) {
					this.primaryShown = tr.isShown();
					this.updateEventsModel();
				} else if (tr.getId().equals(Heap.ETALON_TRACE_KEY)) {
					this.etalonShown = tr.isShown();
					this.updateEventsModel();
				}
			}
		}
	}

	private JPopupMenu createPopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();
		final JMenuItem joinPreviousMenuItem = new JMenuItem("Join previous");
		final JMenuItem splitTo2MenuItem = new JMenuItem("Split to 2");
		final JMenuItem splitTo3MenuItem = new JMenuItem("Split to 3");
		final JMenuItem changeToConnectorMenuItem = new JMenuItem("Change to CONNECTOR");
		final JMenuItem changeToGainMenuItem = new JMenuItem("Change to GAIN");
		final JMenuItem changeToLinearMenuItem = new JMenuItem("Change to LINEAR");
		final JMenuItem changeToLossMenuItem = new JMenuItem("Change to LOSS");
		final JMenuItem changeToNonidMenuItem = new JMenuItem("Change to NOT IDENTIFIED");
		final JMenuItem moveBeginToMarkerA = new JMenuItem("Move begin to Marker A");
		final JMenuItem moveEndToMarkerA = new JMenuItem("Move end to Marker A");

		joinPreviousMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.joinCurrentEventWithPrevious();
			}
		});

		splitTo2MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.splitCurrentEventToN(2);
			}
		});

		splitTo3MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.splitCurrentEventToN(3);
			}
		});

		changeToConnectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.changeCurrentEventType(SimpleReflectogramEvent.CONNECTOR);
			}
		});

		changeToGainMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.changeCurrentEventType(SimpleReflectogramEvent.GAIN);
			}
		});

		changeToLinearMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.changeCurrentEventType(SimpleReflectogramEvent.LINEAR);
			}
		});

		changeToLossMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.changeCurrentEventType(SimpleReflectogramEvent.LOSS);
			}
		});

		changeToNonidMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Heap.changeCurrentEventType(SimpleReflectogramEvent.NOTIDENTIFIED);
			}
		});

		moveBeginToMarkerA.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (Heap.hasMarkerPosition()) {
					Heap.changeCurrentEventBegin(Heap.getMarkerPosition());
				}
			}
		});

		moveEndToMarkerA.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (Heap.hasMarkerPosition()) {
					Heap.changeCurrentEventEnd(Heap.getMarkerPosition());
				}
			}
		});

		popupMenu.add(joinPreviousMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(splitTo2MenuItem);
		popupMenu.add(splitTo3MenuItem);
		popupMenu.addSeparator();
		popupMenu.add(changeToLinearMenuItem);
		popupMenu.add(changeToLossMenuItem);
		popupMenu.add(changeToGainMenuItem);
		popupMenu.add(changeToConnectorMenuItem);
		popupMenu.add(changeToNonidMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(moveBeginToMarkerA);
		popupMenu.add(moveEndToMarkerA);
		return popupMenu;
	}

	private void updateTableModel() {
		// подгоняем текущее выделение к тому месту в таблице,
		// которое должно соответствовать текущей паре событий
		// XXX: если текущего выделения не должно быть, надо бы его убрать.
		final int nRow = this.view.currentRow2();
		if (nRow != -1 && nRow < this.table.getRowCount()) {
			this.table.setRowSelectionInterval(nRow, nRow);
			this.table.scrollRectToVisible(this.table.getCellRect(this.table.getSelectedRow(), this.table.getSelectedColumn(), true));
		}
	}

	private void updateColors() {
		// this.jTable.revalidate();
		this.table.repaint();
	}

	private void setTableModel() {
		if (Heap.getMTAEPrimary() == null) {
			return; // XXX
		}
		final DetailedEvent[] pevents = Heap.getMTAEPrimary().getDetailedEvents();
		final DetailedEvent[] eevents = Heap.getMTMEtalon() != null
				? Heap.getMTMEtalon().getMTAE().getDetailedEvents() : null;
		final PFTrace pf = Heap.getPFTracePrimary();
		final double resMt = pf.getResolution();
		final double resKm = resMt / 1000.0;
		final double sigma = MathRef.calcSigma(pf.getWavelength(), pf.getPulsewidth());

		final int nRows = this.view.nRows(Heap.getEventList());

		final CompositeEventList eList = Heap.getEventList();
		final CompositeEventList.Walker w = eList.new Walker();

		final WrapperedTableModel<DetailedEventResource> model =
				this.table.getModel();
		model.clear();

		for (int row = 0; row < nRows; row++, this.view.toNextRow(w)) {
			// nPri или nEt может быть -1, но не оба одновременно
			final int nPri = this.view.nPri1(eList, row);
			final int nEt = this.view.nEt1(eList, row);

			// выбираем, параметры какого события будем выводить - primary или etalon
			// предпочтение отдается primary
			final DetailedEvent ev = nPri >= 0 ? pevents[nPri] : eevents[nEt];
			final DetailedEventResource res = new DetailedEventResource();
			res.initGeneral(ev, nPri + 1, resKm, sigma);
			if (this.showComparison) {
				if (nPri >= 0 && nEt >= 0 && Heap.getMTMEtalon() != null) {
					res.initComparative(pevents[nPri],
							eevents[nEt],
							Heap.getMTMEtalon().getMTAE().getModelTrace(),
							resMt,
							Heap.getEvaluationPerEventResult(),
							nPri);
				}
			}
			model.addObject(res);
		}
		this.updateTableModel();
		this.table.getModel().fireTableDataChanged();
	}

	private class EventTableRenderer
	extends ADefaultTableCellRenderer.ObjectRenderer {
		@Override
		public Component getTableCellRendererComponent(final JTable table1,
				final Object value,
				final boolean isSelected1,
				final boolean hasFocus,
				final int row,
				final int column) {
			final Component c = super.getTableCellRendererComponent(table1,
					value, isSelected1, hasFocus, row, column);

			final int nPrimary = EventsFrame.this.view.nPri1(Heap.getEventList(), row);
			final int nEtalon = EventsFrame.this.view.nEt1(Heap.getEventList(), row);

			// SimpleReflectogramEventComparer comp = Heap.getEventComparer();
			String colorCode = null;

			chooseColor: {
				// XXX: performance: все эти вычисления производятся для каждой отрисовки каждой ячейки таблицы
				if (Heap.getMTMEtalon() == null) {
					break chooseColor; // no etalon - use default colors
				}
				if (nPrimary < 0) {
					colorCode = isSelected1 ? AnalysisResourceKeys.COLOR_EVENTS_LOST_SELECTED : AnalysisResourceKeys.COLOR_EVENTS_LOST;
					break chooseColor; // etalon-only event
				}
				// nPrimary >= 0
				if (nEtalon < 0) {
					colorCode = isSelected1 ? AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED : AnalysisResourceKeys.COLOR_EVENTS_NEW;
					break chooseColor; // probe-only event
				}
				final DetailedEvent pri = Heap.getMTAEPrimary().getDetailedEvent(nPrimary);
				final DetailedEvent et = Heap.getMTMEtalon().getMTAE().getDetailedEvent(nEtalon);
				if (Heap.getRefMismatch() != null) {
					final int dist = Heap.getRefMismatch().getCoord();
					// Дистация аларма привязана к событиям эталона,
					// поэтому подсветку аларма делаем согласно событиями эталона,
					// даже если это немного за пределами текущего события
					if (et.getBegin() <= dist && et.getEnd() > dist) {
						colorCode = isSelected1 ? AnalysisResourceKeys.COLOR_EVENTS_ALARM_SELECTED : AnalysisResourceKeys.COLOR_EVENTS_ALARM;
						break chooseColor; // mismatched event
					}
				}
				if (SimpleReflectogramEventComparer.eventsAreDifferent(pri, et, SimpleReflectogramEventComparer.CHANGETYPE_LOSS, 0.5)) {
					colorCode = isSelected1
							? AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED
								: AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED;
				}
				else if (SimpleReflectogramEventComparer.eventsAreDifferent(pri, et, SimpleReflectogramEventComparer.CHANGETYPE_AMPL, 0.5)) {
					colorCode = isSelected1
							? AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED
								: AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED;
					// no change - use default colors
				}
			}

			if (colorCode != null) {
				c.setForeground(UIManager.getColor(colorCode));
			}
			else {
				c.setForeground(Color.BLACK); // XXX: default color
			}

			return c;
		}
	}

	public void etalonMTMCUpdated() {
		this.etalonOpened = true;
		this.updateEventsModel();
		this.updateTableModel();
		this.updateColors();
	}

	public void etalonMTMRemoved() {
		this.etalonOpened = false;
		this.updateEventsModel();
		this.updateTableModel();
		this.updateColors();
	}

	public void currentEventChanged() {
		this.updateTableModel();
	}

	public void primaryRefAnalysisCUpdated() {
		this.updateColors();
		if (Heap.getRefAnalysisPrimary() != null) {
			this.primaryOpened = true;
			this.updateEventsModel();
			this.updateTableModel();
		}
		super.setVisible(true);
	}

	public void primaryRefAnalysisRemoved() {
		this.primaryOpened = false;
		this.table.getModel().clear();

		this.table.revalidate();
		this.table.repaint();
		super.setVisible(false);
	}
	
	private void updateEventsModel() {
		final boolean showPrimary = this.primaryOpened && this.primaryShown;
		final boolean showEtalon = this.etalonOpened && this.etalonShown;
		
		if (showPrimary && showEtalon) {
			this.view.setViewMode(TableView.COMP);
		} else if (showEtalon) {
			this.view.setViewMode(TableView.ETAL);
		} else {
			this.view.setViewMode(TableView.PRIM);
		}
		this.setTableModel();
	}

	public void refMismatchCUpdated() {
		this.updateEventsModel();
	}

	public void refMismatchRemoved() {
		this.updateEventsModel();
	}
}
