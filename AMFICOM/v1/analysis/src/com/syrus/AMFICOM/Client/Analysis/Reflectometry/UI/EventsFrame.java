package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
												 implements OperationListener
{
	private static String linear = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.LINEAR));
	private static String connector = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.CONNECTOR));
	private static String weld = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.WELD));
	private static String initiate = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.INITIATE));
	private static String terminate = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.TERMINATE));
	private static String noid = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.NON_IDENTIFIED));
	private static String dash = "-----";

	private ComplexReflectogramEvent []data;
	//private ComplexReflectogramEvent []data_; -- не требуется, т.к. сравниваются только относительные параметры 
	private ComplexReflectogramEvent []etalon;

	protected Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private JTable jTable;
	protected int selected = 0;
	protected boolean skip = false;

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

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					this.data = ((ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, "primarytrace")).getComplexEvents();
					etalon = null;
					setNoComparedWithEtalonColor();
					if ((RefAnalysis)Pool.get("refanalysis", id) != null)
					{
						RefAnalysis a = (RefAnalysis)Pool.get("refanalysis", id);
						BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
						setTableModel(bs, a.events);
						updTableModel(0);
					}
					setVisible(true);
				}
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if(id.equals(AnalysisUtil.ETALON))
				{
					etalon = null;
					data = null;
					setNoComparedWithEtalonColor();
				}
				if (id.equals("all"))
				{
					etalon = null;
					data = null;
					tModel.clearTable();
					setNoComparedWithEtalonColor();
					setVisible(false);
				}
			}
			if(rce.OPEN_ETALON)
			{
				String etId = (String)rce.getSource();
				if(etId != null)
					etalon = ((ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, etId)).getComplexEvents();
				else
					etalon = null;

// выравнивание не требуется, т.к. сравниваются только относительные параметры
//				if(etalon != null && data!= null)
//					data_ = ReflectogramMath.alignClone(data, etalon); 
//				else
//					data_ = null;

				setComparedWithEtalonEventsColor();
			}
			if(rce.CLOSE_ETALON)
			{
				data = null;
				etalon = null;
				setNoComparedWithEtalonColor();
			}
		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if (rue.analysisPerformed())
			{
				String id = (String)(rue.getSource());
				if (id.equals("primarytrace"))
				{
					if ((RefAnalysis)Pool.get("refanalysis", id) != null)
					{
						RefAnalysis a = (RefAnalysis)Pool.get("refanalysis", id);
						BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
						setTableModel(bs, a.events);
						if (selected >= a.events.length)
							selected = a.events.length-1;
						updTableModel (selected);
					}
					setVisible(true);
				}
			}

			if (rue.eventSelected())
			{
				updTableModel (Integer.parseInt((String)rue.getSource()));
			}

//			if (rue.CONCAVITY_SELECTED)
//			{
//				if (jTable.getSelectedRow() != -1)
//					jTable.removeRowSelectionInterval(jTable.getSelectedRow(), jTable.getSelectedRow());
//			}
		}
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

		if(etalon == null || data == null)
		{
			setNoComparedWithEtalonColor();
			return;
		}

		int []newEvents = ReflectogramComparer.getNewEventsList(data, etalon);
		int []amplChengedEvents = ReflectogramComparer.getChangedAmplitudeEventsList(data, etalon, .5);
		int []lossChengedEvents = ReflectogramComparer.getChangedLossEventsList(data, etalon, .5);

		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(newEvents);
		rend.setAmplitudeChangedEventsList(amplChengedEvents);
		rend.setLossChangedEventsList(lossChengedEvents);
		jTable.updateUI();
	}

	public void setNoComparedWithEtalonColor()
	{
		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(null);
		rend.setAmplitudeChangedEventsList(null);
		rend.setLossChangedEventsList(null);
		jTable.updateUI();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

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

		jTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		jTable.getColumnModel().getColumn(0).setMaxWidth(40);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(85);
		jTable.getColumnModel().getColumn(2).setPreferredWidth(75);
		jTable.getColumnModel().getColumn(3).setPreferredWidth(80);
		jTable.getColumnModel().getColumn(4).setPreferredWidth(75);
		jTable.getColumnModel().getColumn(5).setPreferredWidth(70);
		jTable.getColumnModel().getColumn(6).setPreferredWidth(85);

		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 213));
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
				if (!skip)
				{
					dispatcher.notify(new RefUpdateEvent(String.valueOf(selected), RefUpdateEvent.EVENT_SELECTED_EVENT));
				}
				skip = false;
			}
		}
		});

		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));

		jTable.setDefaultRenderer(Object.class, new EventTableRenderer(jTable));

		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		updColorModel();
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
		repaint();
	}

	void updTableModel(int activeEvent)
	{
		if (activeEvent != -1 && activeEvent < jTable.getRowCount())
//			if (selected != activeEvent)
			{
				selected = activeEvent;
				skip = true;
				jTable.setRowSelectionInterval(selected, selected);
				jTable.scrollRectToVisible(jTable.getCellRect(jTable.getSelectedRow(), jTable.getSelectedColumn(), true));
			}
	}

	void setTableModel(BellcoreStructure bs, TraceEvent[] events)
	{
		double res_km = bs.getResolution() / 1000.0;
		double sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());

		tModel.clearTable();

		for (int i = 0; i < events.length; i++)
		{
			switch (events[i].getType())
			{
			case TraceEvent.INITIATE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 initiate,
					 Double.toString(MathRef.round_3(res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 dash, // отраж
					 dash, // потери
					 dash  // затух
				});
				break;
			case TraceEvent.LINEAR:
			    // TODO: использовать только один параметр в data[] вместо трех
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 linear,
					 Double.toString(MathRef.round_3(res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 dash, // отраж
					 Double.toString(MathRef.round_3(events[i].data[1] - events[i].data[0])), // потери
					 Double.toString(MathRef.round_4(events[i].data[2] / res_km)) //затух
				});
				break;
			case TraceEvent.NON_IDENTIFIED:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 noid,
					 Double.toString(MathRef.round_3(res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 dash, // отраж
					 dash, // потери
					 dash  // затух
				});
				break;
			case TraceEvent.CONNECTOR:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 connector,
					 Double.toString(MathRef.round_3(res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, events[i].data[0] - events[i].data[2]))), // отраж
					 Double.toString(MathRef.round_3(events[i].data[1] - events[i].data[0])), // потери
					 dash  // затух
				});
				break;
			case TraceEvent.WELD:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 weld,
					 Double.toString(MathRef.round_3 (res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3 (res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 dash, // отраж
					 Double.toString( MathRef.round_3 ( events[i].data[2])), // потери
					 dash  // затух
				});
				break;
			case TraceEvent.TERMINATE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 terminate,
					 Double.toString(MathRef.round_3(res_km * (double)events[i].first_point)), //начало
					 Double.toString(MathRef.round_3(res_km * (events[i].last_point - events[i].first_point))), //протяженность
					 Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, events[i].data[0]-events[i].data[1]))), // отраж
					 dash, // потери
					 dash  // затух
				});
				break;
			}

		}
	}
}

class EventTableRenderer extends DefaultTableCellRenderer
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




	public Component getTableCellRendererComponent(JTable table, Object value,
												boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		boolean isCurrent = table.getSelectedRow() == row;
		
		if (containsRow(row, newEventsList))
		    c.setForeground(isCurrent ? Color.MAGENTA : Color.RED);
		else if (containsRow(row, lossChangedEventsList) || containsRow(row, amplitudeChangedEventsList))
		    c.setForeground(isCurrent ? Color.ORANGE : Color.CYAN); // maybe yellow is better that cyan?
		else
		    c.setForeground(isCurrent ? Color.WHITE : Color.BLACK);

		return c;
	}

	private boolean containsRow(int row, int []array)
	{
		if(array == null)
			return false;

		for(int i=0; i<array.length; i++)
		{
			if(array[i] == row)
				return true;
		}
		return false;
	}
}

