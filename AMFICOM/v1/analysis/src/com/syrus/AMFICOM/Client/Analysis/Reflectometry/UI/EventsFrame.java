package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.General.UI.UIGeneralStorage;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ComplexReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener,
    CurrentEventChangeListener
{
	private static final String DASH = "-----";

	private FixedSizeEditableTableModel tModel;
	private JTable jTable;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();

	public EventsFrame()
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

		this.initModule();
	}

	private void initModule()
	{
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
        Heap.addPrimaryRefAnalysisListener(this);
	}

	private void selectRow(int row) {
		this.jTable.setRowSelectionInterval(row, row);
		this.jTable.scrollRectToVisible(this.jTable.getCellRect(this.jTable.getSelectedRow(), this.jTable.getSelectedColumn(), true));
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("eventTableTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}


	private void setComparedWithEtalonEventsColor()
	{
		if(getEtalon() == null || getData() == null)
		{
			setNoComparedWithEtalonColor();
			return;
		}

		int []newEvents = SimpleReflectogramEventComparer.getNewEventsList(getData(), getEtalon());
		int []amplChengedEvents = SimpleReflectogramEventComparer.getChangedAmplitudeEventsList(getData(), getEtalon(), .5);
		int []lossChengedEvents = SimpleReflectogramEventComparer.getChangedLossEventsList(getData(), getEtalon(), .5);

		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(newEvents);
		rend.setAmplitudeChangedEventsList(amplChengedEvents);
		rend.setLossChangedEventsList(lossChengedEvents);
		this.jTable.revalidate();
		this.jTable.repaint();
        
        // FIXME: debug: development-time console code
        ModelTraceComparer.compareMTAEToMTM(Heap.getMTAEPrimary(), Heap.getMTMEtalon()); // XXX: will crush if no etalon will be at this moment
	}

	private void setNoComparedWithEtalonColor()
	{
		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(null);
		rend.setAmplitudeChangedEventsList(null);
		rend.setLossChangedEventsList(null);
		this.jTable.revalidate();
		this.jTable.repaint();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		tModel = new FixedSizeEditableTableModel(
					new String[] {LangModelAnalyse.getString("eventNum"),
												LangModelAnalyse.getString("eventType"),
												LangModelAnalyse.getString("eventStartLocationKM"),
												LangModelAnalyse.getString("eventLengthKM"),
												LangModelAnalyse.getString("eventReflectanceDB"),
												LangModelAnalyse.getString("eventLossDB"),
												LangModelAnalyse.getString("eventLeadAttenuationDBKM")},
					new Object[] {"", "", "", "", "", "", "", ""},
					null,
					null);

		jTable = new ATable(tModel);

		{
			FontMetrics fontMetrics = this.jTable.getFontMetrics(this.jTable.getFont());			
			jTable.getColumnModel().getColumn(0).setPreferredWidth(fontMetrics.stringWidth("WW"));
			jTable.getColumnModel().getColumn(0).setMaxWidth(fontMetrics.stringWidth("WWWW"));
		}
//		jTable.getColumnModel().getColumn(1).setPreferredWidth(85);
//		jTable.getColumnModel().getColumn(2).setPreferredWidth(75);
//		jTable.getColumnModel().getColumn(3).setPreferredWidth(80);
//		jTable.getColumnModel().getColumn(4).setPreferredWidth(75);
//		jTable.getColumnModel().getColumn(5).setPreferredWidth(70);
//		jTable.getColumnModel().getColumn(6).setPreferredWidth(85);

		setContentPane(mainPanel);
//		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("eventTableTitle"));

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
			if (e.getValueIsAdjusting()) return;

			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty())
			{
						//no rows are selected
			}
			else
			{
				int selected = lsm.getMinSelectionIndex();
				Heap.setCurrentEvent(selected);
			}
		}
		});

//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMinimumSize(new Dimension(200, 213));

		jTable.setDefaultRenderer(Object.class, new EventTableRenderer(jTable));

		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		updColorModel();
	}

	private void updColorModel()
	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
//		repaint();
	}

	private void updateTableModel()
	{
		int nEvent = Heap.getCurrentEvent();
		if (nEvent != -1 && nEvent < this.jTable.getRowCount())
			{
				this.jTable.setRowSelectionInterval(nEvent, nEvent);
				this.jTable.scrollRectToVisible(this.jTable.getCellRect(this.jTable.getSelectedRow(), this.jTable.getSelectedColumn(), true));
			}
	}
    
    private void updateColors() {
        if (Heap.getMTMEtalon() == null)
            setNoComparedWithEtalonColor();
        else
            setComparedWithEtalonEventsColor();
    }

	private void setTableModel()
	{
        RefAnalysis ra = Heap.getRefAnalysisPrimary();
        if (ra == null)
            return;
        BellcoreStructure bs = ra.getBS(); 
        TraceEvent[] events = ra.events;
		double res_km = bs.getResolution() / 1000.0;
		double sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());

		tModel.clearTable();

		for (int i = 0; i < events.length; i++)
		{
			int eventType = events[i].getType();
			String eventTypeName = AnalysisUtil.getTraceEventNameByType(eventType);
			switch (eventType)
			{
			case TraceEvent.INITIATE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3(res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 DASH, // отраж
					 DASH, // потери
					 DASH  // затух
				});
				break;
			case TraceEvent.LINEAR:
			    // TODO: использовать только один параметр в data[] вместо трех
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3(res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 DASH, // отраж
					 //Double.toString(MathRef.round_3(events[i].data[1] - events[i].data[0])), // потери
					 Double.toString(MathRef.round_3(events[i].getLoss())), // потери
					 Double.toString(MathRef.round_4(events[i].linearAsympLoss() / res_km)) //затух
				});
				break;
			case TraceEvent.NON_IDENTIFIED:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3(res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 DASH, // отраж
					 //dash, // потери
					 Double.toString(MathRef.round_3(events[i].getLoss())), // потери
					 DASH  // затух
				});
				break;
			case TraceEvent.CONNECTOR:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3(res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, events[i].connectorPeak()))), // отраж
					 //Double.toString(MathRef.round_3(events[i].data[1] - events[i].data[0])), // потери
					 Double.toString(MathRef.round_3(events[i].getLoss())), // потери
					 DASH  // затух
				});
				break;
			case TraceEvent.GAIN:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3 (res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3 (res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 DASH, // отраж
					 //Double.toString( MathRef.round_3 ( events[i].data[2])), // потери
					 Double.toString(MathRef.round_3(events[i].getLoss())), // потери
					 DASH  // затух
				});
				break;
			case TraceEvent.LOSS:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3 (res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3 (res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 DASH, // отраж
					 //Double.toString( MathRef.round_3 ( events[i].data[2])), // потери
					 Double.toString(MathRef.round_3(events[i].getLoss())), // потери
					 DASH  // затух
				});
				break;
			case TraceEvent.TERMINATE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 eventTypeName,
					 Double.toString(MathRef.round_3(res_km * events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, events[i].terminateReflection()))), // отраж
					 DASH, // потери
					 DASH  // затух
				});
				break;
			}			
		}
	}
	
	private class EventTableRenderer extends DefaultTableCellRenderer
	{
		int []newEventsList;
		int []amplitudeChangedEventsList;
		int []lossChangedEventsList;

		JTable table;

		public void setNewEventsList(int []newEventsList)
		{
			this.newEventsList = newEventsList;
		}

		public void setAmplitudeChangedEventsList(int []amplitudeChangedEventsList)
		{
			this.amplitudeChangedEventsList = amplitudeChangedEventsList;
		}

		public void setLossChangedEventsList(int []lossChangedEventsList)
		{
			this.lossChangedEventsList = lossChangedEventsList;
		}

		public EventTableRenderer(JTable table)
		{
			this.table = table;
		}

		public Component getTableCellRendererComponent(	JTable table1,
														Object value,
														boolean isSelected1,
														boolean hasFocus,
														int row,
														int column) {
			Component c = super.getTableCellRendererComponent(table1, value, isSelected1, hasFocus, row, column);

			Color color = null;
			if (containsRow(row, newEventsList))
				color = isSelected1 ? UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED) : UIManager
						.getColor(AnalysisResourceKeys.COLOR_EVENTS_NEW);
			else if (containsRow(row, lossChangedEventsList))
				color = isSelected1 ? UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED)
						: UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED);
			else if (containsRow(row, amplitudeChangedEventsList))
				color = isSelected1 ? UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED)
						: UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED);
			else
				color = isSelected1 ? UIManager.getColor(AnalysisResourceKeys.COLOR_EVENTS_SELECTED) : UIManager
						.getColor(AnalysisResourceKeys.COLOR_EVENTS);

			if (color != null) {
				c.setForeground(color);
			}

			return c;
		}

		private boolean containsRow(int row,
									int[] array) {
			boolean result = false;
			if (array == null)
				return false;

			for (int i = 0; i < array.length; i++) {
				if (array[i] == row) {
					result = true;
					break;
				}
			}
			return result;
		}
	}

	public void etalonMTMCUpdated()
	{
        updateColors();
	}

	public void etalonMTMRemoved()
	{
        updateColors();
	}

	public void currentEventChanged()
	{
		updateTableModel();
	}

	private ComplexReflectogramEvent []getData() {
		return Heap.getMTAEPrimary().getComplexEvents();
	}
	private ComplexReflectogramEvent []getEtalon() {
		return Heap.getMTMEtalon().getMTAE().getComplexEvents();
	}

    public void primaryRefAnalysisCUpdated() {
        updateColors();
        if (Heap.getRefAnalysisPrimary() != null)
        {
            setTableModel();
            updateTableModel();
            UIGeneralStorage.arrangeTableColumns(this.jTable);
        }
        setVisible(true);
    }

    public void primaryRefAnalysisRemoved() {
        tModel.clearTable();
        setNoComparedWithEtalonColor();
        setVisible(false);
    }

}
