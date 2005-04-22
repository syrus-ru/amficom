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
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
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
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
implements OperationListener, BsHashChangeListener, EtalonMTMListener, CurrentEventChangeListener
{
	public static final String DASH = "-----";

	protected Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private JTable jTable;
	protected int selected = 0;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	
	public EventsFrame()
	{
		this(new Dispatcher());
	}
	
	public EventsFrame(Dispatcher dispatcher)
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

		this.initModule(dispatcher);
	}

	private void initModule(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		//dispatcher.register(this, RefChangeEvent.typ);
		this.dispatcher.register(this, RefUpdateEvent.typ);
		this.dispatcher.register(this, AnalyseApplicationModel.SELECT_NEXT_EVENT);
		this.dispatcher.register(this, AnalyseApplicationModel.SELECT_PREVIOUS_EVENT);
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		String actionCommand = ae.getActionCommand();
		if(actionCommand.equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if (rue.analysisPerformed())
			{
				if (Heap.getRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY) != null)
				{
					RefAnalysis a = Heap.getRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY);
					BellcoreStructure bs = Heap.getAnyBSTraceByKey(Heap.PRIMARY_TRACE_KEY);
					setTableModel(bs, a.events);
					if (selected >= a.events.length)
						selected = a.events.length-1;
					updateTableModel (selected);
				}
				setVisible(true);
			}
		} else if (actionCommand.equals(AnalyseApplicationModel.SELECT_PREVIOUS_EVENT)) {
			int selectedRow = this.jTable.getSelectedRow();
			if (selectedRow > 0) {
				selectedRow--;
			} else selectedRow =  this.jTable.getModel().getRowCount() - 1;				
			this.selectRow(selectedRow);
		}else if (actionCommand.equals(AnalyseApplicationModel.SELECT_NEXT_EVENT)) {
			int selectedRow = this.jTable.getSelectedRow();
			if (selectedRow < this.jTable.getModel().getRowCount() - 1) {
				selectedRow++;
			}
			else selectedRow = 0;
			this.selectRow(selectedRow);			
		}
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


	public void setComparedWithEtalonEventsColor()
	{
		if(getEtalon() == null || getData() == null)
		{
			setNoComparedWithEtalonColor();
			return;
		}

		int []newEvents = ReflectogramComparer.getNewEventsList(getData(), getEtalon());
		int []amplChengedEvents = ReflectogramComparer.getChangedAmplitudeEventsList(getData(), getEtalon(), .5);
		int []lossChengedEvents = ReflectogramComparer.getChangedLossEventsList(getData(), getEtalon(), .5);

		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(newEvents);
		rend.setAmplitudeChangedEventsList(amplChengedEvents);
		rend.setLossChangedEventsList(lossChengedEvents);
		this.jTable.revalidate();
		this.jTable.repaint();
        
        // FIXME: development-time console code
        ModelTraceComparer.compareMTAEToMTM(Heap.getMTAEPrimary(), Heap.getMTMEtalon()); // XXX: will crush if no etalon will be at this moment
	}

	public void setNoComparedWithEtalonColor()
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
				selected = lsm.getMinSelectionIndex();
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

	private void updateTableModel(int activeEvent)
	{
		if (activeEvent != -1 && activeEvent < this.jTable.getRowCount())
//			if (selected != activeEvent)
			{
				this.selected = activeEvent;
			
				this.jTable.setRowSelectionInterval(this.selected, this.selected);
				this.jTable.scrollRectToVisible(this.jTable.getCellRect(this.jTable.getSelectedRow(), this.jTable.getSelectedColumn(), true));
			}
		UIGeneralStorage.arrangeTableColumns(this.jTable);
	}

	void setTableModel(BellcoreStructure bs, TraceEvent[] events)
	{
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

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(Heap.PRIMARY_TRACE_KEY))
		{
			setNoComparedWithEtalonColor();
			if (Heap.getRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY) != null)
			{
				RefAnalysis a = Heap.getRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY);
				setTableModel(bs, a.events);
				this.updateTableModel(0);
			}
			setVisible(true);
		}
	}


	public void bsHashRemoved(String key)
	{
		if(key.equals(AnalysisUtil.ETALON))
		{
			setNoComparedWithEtalonColor();
		}
	}


	public void bsHashRemovedAll()
	{
		tModel.clearTable();
		setNoComparedWithEtalonColor();
		setVisible(false);
	}

	public void etalonMTMCUpdated()
	{
		setComparedWithEtalonEventsColor();
	}

	public void etalonMTMRemoved()
	{
		setNoComparedWithEtalonColor();
	}

	public void currentEventChanged()
	{
		updateTableModel(Heap.getCurrentEvent());
	}

	private ComplexReflectogramEvent []getData() {
		return Heap.getMTAEPrimary().getComplexEvents();
	}
	private ComplexReflectogramEvent []getEtalon() {
		return Heap.getMTMEtalon().getMTAE().getComplexEvents();
	}

}
