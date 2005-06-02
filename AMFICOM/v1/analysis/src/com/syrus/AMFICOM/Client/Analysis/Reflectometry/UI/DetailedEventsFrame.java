package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class DetailedEventsFrame extends JInternalFrame
implements EtalonMTMListener,
    CurrentEventChangeListener, PrimaryRefAnalysisListener
{
	private ModelTrace alignedDataMT;

	private WrapperedPropertyTableModel tModel;
	private WrapperedPropertyTableModel ctModel;
	private DetailedEventResource res;
	
	private WrapperedPropertyTable mainTable;
	private WrapperedPropertyTable comparativeTable;
	
	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();

	private JPanel mainPanelComp = new JPanel(new BorderLayout());
	private JScrollPane scrollPaneComp = new JScrollPane();
	private JViewport viewportComp = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private static final String[] linearKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL, DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_MAXDEVIATION,
			DetailedEventWrapper.KEY_MEAN_DEVIATION };

	private static final String[] dzKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL, DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_EDZ, DetailedEventWrapper.KEY_ADZ };

	private static final String[] spliceKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL, DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_MAXDEVIATION,
			DetailedEventWrapper.KEY_MEAN_DEVIATION };

	private static final String[] notidKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_MAX_LEVEL, DetailedEventWrapper.KEY_MIN_LEVEL,
			DetailedEventWrapper.KEY_MAXDEVIATION };
	
	private static final String[] reflectionKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL, DetailedEventWrapper.KEY_END_LEVEL,
			DetailedEventWrapper.KEY_REFLECTION_LEVEL };
	
	private static final String[] endKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_EXTENSION,
			DetailedEventWrapper.KEY_START_LEVEL,
			DetailedEventWrapper.KEY_REFLECTION_LEVEL };
	
	private static final String[] compareKeys = new String[] {
			DetailedEventWrapper.KEY_TYPE, DetailedEventWrapper.KEY_ETALON_TYPE,
			DetailedEventWrapper.KEY_ETALON_MAX_DEVIATION,
			DetailedEventWrapper.KEY_ETALON_MEAN_DEVIATION,
			DetailedEventWrapper.KEY_LOSS_DIFFERENCE,
			DetailedEventWrapper.KEY_LOCATION_DIFFERENCE,
			DetailedEventWrapper.KEY_LENGTH_DIFFERENCE };
	
	public DetailedEventsFrame()
	{
		this.init();
		this.initModule();
	}

	private void initModule()
	{
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
    Heap.addPrimaryRefAnalysisListener(this);
	}

	private void makeAlignedDataMT()
	{
		// XXX: is alignment really required? Should it be performed here?
		if (Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null) {
			alignedDataMT = null;
		} else {
			alignedDataMT = ReflectogramMath.createAlignedArrayModelTrace(
			Heap.getMTAEPrimary().getModelTrace(),
			Heap.getMTMEtalon().getMTAE().getModelTrace());
		}
	}

	private void init() {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		res = new DetailedEventResource();
		tModel = new WrapperedPropertyTableModel(DetailedEventWrapper.getInstance(), res, linearKeys);
		
		this.mainTable = new WrapperedPropertyTable(tModel);

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("eventDetailedTableTitle"));

		tabbedPane.add(LangModelAnalyse.getString("Title.mains"), mainPanel);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(mainTable);
		tabbedPane.setEnabledAt(0, true);

		tabbedPane.add(LangModelAnalyse.getString("Title.comparatives"), mainPanelComp);

		this.ctModel = new WrapperedPropertyTableModel(DetailedEventWrapper.getInstance(), res, compareKeys);
		this.comparativeTable = new WrapperedPropertyTable(this.ctModel);
		this.comparativeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.comparativeTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		this.comparativeTable.getColumnModel().getColumn(1).setPreferredWidth(100);

		mainPanelComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneComp.setViewport(viewportComp);
		scrollPaneComp.setAutoscrolls(true);
		comparativeTable.setDefaultRenderer(Object.class, new CompareTableRenderer());

		mainPanelComp.add(scrollPaneComp, BorderLayout.CENTER);
		scrollPaneComp.getViewport().add(comparativeTable);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);

	}

	private void setData()
	{
		int nEvent = Heap.getCurrentEvent1();
		int nEtalon = Heap.getCurrentEtalonEvent1();
		if(Heap.getMTMEtalon() == null || Heap.getMTAEPrimary() == null || alignedDataMT == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		double deltaX = Heap.getMTAEPrimary().getDeltaX();

//		ComplexReflectogramEvent dataEvent =
//			Heap.getMTAEPrimary().getComplexEvents()[nEvent];
//		ComplexReflectogramEvent etalonEvent = nEtalon != -1
//				? Heap.getMTMEtalon().getMTAE().getComplexEvents()[nEtalon]
//				: null;
        DetailedEvent dataEvent = nEvent != -1
            ? Heap.getMTAEPrimary().getDetailedEvents()[nEvent]
            : null;
        DetailedEvent etalonEvent = nEtalon != -1
                ? Heap.getMTMEtalon().getMTAE().getDetailedEvents()[nEtalon]
                : null;
                
		int dataType = dataEvent != null
                    ? dataEvent.getEventType()
                    : SimpleReflectogramEvent.RESERVED; // 'no event'
		int etalonType = etalonEvent != null
			        ? etalonEvent.getEventType()
					: SimpleReflectogramEvent.RESERVED; // 'no event'

		((CompareTableRenderer)comparativeTable.getDefaultRenderer(Object.class))
			.setSameType(dataType == etalonType && dataType != SimpleReflectogramEvent.RESERVED);

		// сравнение по модельной кривой
		ModelTrace etalonMT = Heap.getMTMEtalon().getMTAE().getModelTrace();
		res.initComparative(dataEvent, etalonEvent, etalonMT, deltaX);
		comparativeTable.updateUI();
	}
	
	private void updateTableModel()
	{
		int num = Heap.getCurrentEvent1();
		if (num < 0) {

            return;
        }
		DetailedEvent ev = Heap.getMTAEPrimary().getDetailedEvents()[num];
		int eventType = ev.getEventType();
		double resMt =  Heap.getBSPrimaryTrace().getResolution();
    double resKm = resMt / 1000.0;
		res.initAdditional(ev, resKm);
		switch (eventType)
		{
			case SimpleReflectogramEvent.LINEAR:
				tModel.setKeys(linearKeys);
				break;
			case SimpleReflectogramEvent.DEADZONE:
				tModel.setKeys(dzKeys);
				break;
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				tModel.setKeys(notidKeys);
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				tModel.setKeys(reflectionKeys);
				break;
			case SimpleReflectogramEvent.LOSS:
			case SimpleReflectogramEvent.GAIN:
				tModel.setKeys(spliceKeys);
				break;
			case SimpleReflectogramEvent.ENDOFTRACE:
				tModel.setKeys(endKeys);
				break;
			}
		mainTable.updateUI();
	}

	public void etalonMTMCUpdated()
	{
        makeAlignedDataMT();
		this.clearCTModelValues();
		if(Heap.getRefAnalysisPrimary() != null) {
			tabbedPane.setEnabledAt(1, true);
		}
		this.updateTableModel();
	}

	public void etalonMTMRemoved()
	{
		alignedDataMT = null;
		this.clearCTModelValues();
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
		this.mainTable.repaint();
		this.mainTable.revalidate();
	}

	public void currentEventChanged()
	{
		this.updateTableModel();
		this.setData();
	}

    public void primaryRefAnalysisCUpdated() {
        makeAlignedDataMT();
        if (Heap.getMTMEtalon() != null)
            tabbedPane.setEnabledAt(1, true);
        this.updateTableModel();
        setVisible(true);
    }

    public void primaryRefAnalysisRemoved() {
        alignedDataMT = null;
		this.clearCTModelValues();
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);
        setVisible(false);
    }
	
	private void clearCTModelValues() {
		for(int i=0;i<this.ctModel.getRowCount();i++) {
			this.ctModel.setValueAt("--", i, 1);
		}
	}
	
	private class CompareTableRenderer extends ADefaultTableCellRenderer.ObjectRenderer {

		private boolean	sameType	= true;

		public void setSameType(boolean key) {
			sameType = key;
		}

		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected1,
														boolean hasFocus,
														int row,
														int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected1, hasFocus, row, column);

			component.setForeground(sameType || row > 1 ? Color.BLACK : Color.RED);

			return component;
		}
	}
}
