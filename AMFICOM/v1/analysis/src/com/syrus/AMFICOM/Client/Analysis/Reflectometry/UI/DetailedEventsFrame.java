package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonComparisonListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.DetailedEventResource;
import com.syrus.AMFICOM.analysis.DetailedEventWrapper;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class DetailedEventsFrame extends JInternalFrame
implements EtalonMTMListener, CurrentEventChangeListener,
		PrimaryRefAnalysisListener, EtalonComparisonListener {
	private static final long serialVersionUID = 2729642346362069321L;

	private ModelTrace alignedDataMT;

	private DetailedEventResource res;

	private WrapperedPropertyTable<DetailedEventResource> mainTable;
	private WrapperedPropertyTable<DetailedEventResource> comparativeTable;

	private JTabbedPane tabbedPane;

	private static final String[] EMPTY_KEYS = new String[0];

	private static final String[] LINEAR_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_MAXDEVIATION,
			DetailedEventWrapper.KEY_MEAN_DEVIATION };

	private static final String[] DZ_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_EDZ,
			DetailedEventWrapper.KEY_ADZ };

	private static final String[] SPLICE_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_END_LEVEL };

	private static final String[] NOT_ID_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_MAX_LEVEL,
			DetailedEventWrapper.KEY_MIN_LEVEL,
			DetailedEventWrapper.KEY_MAXDEVIATION };

	private static final String[] REFLECTION_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_REFLECTION_LEVEL };

	private static final String[] END_KEYS = new String[] { DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_REFLECTION_LEVEL };

	private static final String[] COMPARE_KEYS = new String[] { DetailedEventWrapper.KEY_TYPE_GENERAL,
			DetailedEventWrapper.KEY_ETALON_TYPE_GENERAL,
			DetailedEventWrapper.KEY_ETALON_MAX_DEVIATION,
			DetailedEventWrapper.KEY_ETALON_MEAN_DEVIATION,
			DetailedEventWrapper.KEY_LOSS_DIFFERENCE,
			DetailedEventWrapper.KEY_LOCATION_DIFFERENCE,
			DetailedEventWrapper.KEY_LENGTH_DIFFERENCE };

	public DetailedEventsFrame() {
		this.init();
		this.initModule();
	}

	private void initModule() {
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addEtalonComparisonListener(this);
	}

	private void makeAlignedDataMT() {
		// XXX: is alignment really required? Should it be performed here?
		if (Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null) {
			this.alignedDataMT = null;
		} else {
			this.alignedDataMT = ReflectogramMath.createAlignedArrayModelTrace(Heap.getMTAEPrimary().getModelTrace(),
					Heap.getMTMEtalon().getMTAE().getModelTrace());
		}
	}

	private void init() {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		this.res = new DetailedEventResource();

		this.mainTable = new WrapperedPropertyTable<DetailedEventResource>(
				new WrapperedPropertyTableModel<DetailedEventResource>(
						DetailedEventWrapper.getInstance(), this.res, EMPTY_KEYS));
		this.mainTable.setTableHeader(null);

		this.tabbedPane = new JTabbedPane();
		this.getContentPane().add(this.tabbedPane);

		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		// this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("eventDetailedTableTitle"));

		final JScrollPane scrollPane = new JScrollPane(this.mainTable);
		scrollPane.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		
		this.tabbedPane.add(LangModelAnalyse.getString("Title.mains"), scrollPane);

		this.mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mainTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		this.mainTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		
		this.tabbedPane.setEnabledAt(0, true);

		this.comparativeTable = new WrapperedPropertyTable<DetailedEventResource>(
				new WrapperedPropertyTableModel<DetailedEventResource>(
						DetailedEventWrapper.getInstance(), this.res, COMPARE_KEYS));
		this.comparativeTable.setTableHeader(null);
		this.comparativeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.comparativeTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		this.comparativeTable.getColumnModel().getColumn(1).setPreferredWidth(100);

		final JScrollPane scrollPaneComp = new JScrollPane(this.comparativeTable);
		scrollPaneComp.setAutoscrolls(true);
		scrollPaneComp.setBorder(BorderFactory.createLoweredBevelBorder());
		this.comparativeTable.setDefaultRenderer(Object.class, new CompareTableRenderer());		
		
		this.tabbedPane.add(LangModelAnalyse.getString("Title.comparatives"), 
			scrollPaneComp);
		
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
	}

	private void setData() {
		final int nEvent = Heap.getCurrentEvent1();
		final int nEtalon = Heap.getCurrentEtalonEvent1();
		if (Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null || this.alignedDataMT == null) {
			this.tabbedPane.setSelectedIndex(0);
			this.tabbedPane.setEnabledAt(1, false);
			return;
		}
		final double deltaX = Heap.getMTAEPrimary().getDeltaX();

		// ComplexReflectogramEvent dataEvent = Heap.getMTAEPrimary().getComplexEvents()[nEvent];
		// ComplexReflectogramEvent etalonEvent = nEtalon != -1
		// ? Heap.getMTMEtalon().getMTAE().getComplexEvents()[nEtalon]
		// : null;
		final DetailedEvent dataEvent = (nEvent != -1) ? Heap.getMTAEPrimary().getDetailedEvent(nEvent) : null;
		final DetailedEvent etalonEvent = (nEtalon != -1) ? Heap.getMTMEtalon().getMTAE().getDetailedEvent(nEtalon) : null;

		final int dataType = (dataEvent != null) ? dataEvent.getEventType() : SimpleReflectogramEvent.RESERVED; // 'no event'
		final int etalonType = (etalonEvent != null) ? etalonEvent.getEventType() : SimpleReflectogramEvent.RESERVED; // 'no event'

		((CompareTableRenderer) this.comparativeTable.getDefaultRenderer(Object.class)).setSameType(dataType == etalonType
				&& dataType != SimpleReflectogramEvent.RESERVED);

		final EvaluationPerEventResult perEvent = Heap.getEvaluationPerEventResult();

		// сравнение по модельной кривой
		final ModelTrace etalonMT = Heap.getMTMEtalon().getMTAE().getModelTrace();
		this.res.initComparative(dataEvent, etalonEvent, etalonMT, deltaX,
				perEvent, nEvent, nEtalon, Heap.getAnchorer());
		this.comparativeTable.getModel().fireTableDataChanged();
	}

	private void updateTableModel() {
		int num = Heap.getCurrentEvent1();
		final WrapperedPropertyTableModel<DetailedEventResource> model = 
			this.mainTable.getModel();
		if (num >= 0) {
			final DetailedEvent ev = Heap.getMTAEPrimary().getDetailedEvent(num);
			final int eventType = ev.getEventType();
			final double resMt = Heap.getPFTracePrimary().getResolution();
			final double resKm = resMt / 1000.0;
			this.res.initAdditional(ev, resKm);
			switch (eventType) {
				case SimpleReflectogramEvent.LINEAR:
					model.setKeys(LINEAR_KEYS);
					break;
				case SimpleReflectogramEvent.DEADZONE:
					model.setKeys(DZ_KEYS);
					break;
				case SimpleReflectogramEvent.NOTIDENTIFIED:
					model.setKeys(NOT_ID_KEYS);
					break;
				case SimpleReflectogramEvent.CONNECTOR:
					model.setKeys(REFLECTION_KEYS);
					break;
				case SimpleReflectogramEvent.LOSS:
				case SimpleReflectogramEvent.GAIN:
					model.setKeys(SPLICE_KEYS);
					break;
				case SimpleReflectogramEvent.ENDOFTRACE:
					model.setKeys(END_KEYS);
					break;
			}
		} else {
			model.setKeys(EMPTY_KEYS);
		}
		model.fireTableDataChanged();
		this.setData();
	}

	public void etalonComparisonCUpdated() {
		this.updateTableModel();
	}

	public void etalonComparisonRemoved() {
		// XXX: is this a suitable behaviour?
		this.updateTableModel();
	}

	public void etalonMTMCUpdated() {
		this.makeAlignedDataMT();
		this.clearCTModelValues();
		if (Heap.getRefAnalysisPrimary() != null) {
			this.tabbedPane.setEnabledAt(1, true);
		}
		this.updateTableModel();
	}

	public void etalonMTMRemoved() {
		this.alignedDataMT = null;
		this.clearCTModelValues();
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
		this.mainTable.repaint();
		this.mainTable.revalidate();
	}

	public void currentEventChanged() {
		this.updateTableModel();
	}

	public void primaryRefAnalysisCUpdated() {
		this.makeAlignedDataMT();
		if (Heap.getMTMEtalon() != null) {
			this.tabbedPane.setEnabledAt(1, true);
		}
		this.updateTableModel();
		this.setVisible(true);
	}

	public void primaryRefAnalysisRemoved() {
		this.alignedDataMT = null;
		this.clearCTModelValues();
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
		this.setVisible(false);
	}

	private void clearCTModelValues() {
		final WrapperedPropertyTableModel<DetailedEventResource> model = 
			this.comparativeTable.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.setValueAt("--", i, 1);
		}
	}	

	private class CompareTableRenderer extends ADefaultTableCellRenderer.ObjectRenderer {

		private boolean sameType = true;

		public void setSameType(boolean key) {
			this.sameType = key;
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value,
				final boolean isSelected1,
				final boolean hasFocus,
				final int row,
				final int column) {
			final Component component = super.getTableCellRendererComponent(table, value, isSelected1, hasFocus, row, column);

			component.setForeground(this.sameType || row > 1 ? Color.BLACK : Color.RED);

			return component;
		}
	}

}
