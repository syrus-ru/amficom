package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.MathRef;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
												 implements OperationListener
{
	private ReflectogramEvent []data_;
	private ReflectogramEvent []data;
	private ReflectogramEvent []etalon;

	private Dispatcher dispatcher;
	private GeneralTableModel tModel;
	private ATable jTable;
	private int selected = 0;
	private boolean skip = false;

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
					this.data = (ReflectogramEvent [])Pool.get("eventparams", "primarytrace");
					etalon = null;
					data_ = null;
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
					data_ = null;
					setNoComparedWithEtalonColor();
				}
				if (id.equals("all"))
				{
					etalon = null;
					data_ = null;
					tModel.clearTable();
					setNoComparedWithEtalonColor();
					setVisible(false);
				}
			}
			if(rce.OPEN_ETALON)
			{
				String et_id = (String)rce.getSource();
				if(et_id != null)
					etalon = (ReflectogramEvent [])Pool.get("eventparams", et_id);
				else
					etalon = null;

				if(etalon != null && data!= null)
					data_ = ReflectogramMath.alignClone(data, etalon);
				else
					data_ = null;

				setComparedWithEtalonEventsColor();
			}
			if(rce.CLOSE_ETALON)
			{
				data_ = null;
				etalon = null;
				setNoComparedWithEtalonColor();
			}
		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if (rue.ANALYSIS_PERFORMED)
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

			if (rue.EVENT_SELECTED)
			{
				updTableModel (Integer.parseInt((String)rue.getSource()));
			}

			if (rue.CONCAVITY_SELECTED)
			{
				if (jTable.getSelectedRow() != -1)
					jTable.removeRowSelectionInterval(jTable.getSelectedRow(), jTable.getSelectedRow());
			}
		}
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.String("eventTableTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}


	public void setComparedWithEtalonEventsColor()
	{

		if(etalon == null || data_ == null)
		{
			setNoComparedWithEtalonColor();
			return;
		}

		int []newEvents = ReflectogramComparer.getNewEventsList(data_, etalon);
		int []amplChengedEvents = ReflectogramComparer.getChangedAmplitudeEventsList(data_, etalon, .5);
		int []lossChengedEvents = ReflectogramComparer.getChangedLossEventsList(data_, etalon, .5);

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

		tModel = new GeneralTableModel(
					new String[] {LangModelAnalyse.String("eventNum"),
												LangModelAnalyse.String("eventType"),
												LangModelAnalyse.String("eventStartLocationKM"),
												LangModelAnalyse.String("eventLengthKM"),
												LangModelAnalyse.String("eventReflectanceDB"),
												LangModelAnalyse.String("eventLossDB"),
												LangModelAnalyse.String("eventLeadAttenuationDBKM")},
					new Object[] {"", "", "", "", "", "", "", ""},
					0);

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
		this.setTitle(LangModelAnalyse.String("eventTableTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
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
		if (activeEvent != -1)
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

		double res = (double)(3 * bs.fxdParams.DS[0]) / (double)(bs.fxdParams.GI * 10000);
		double sigma = MathRef.calcSigma(bs.fxdParams.AW/10, bs.fxdParams.PWU[0]);
		Vector row;
		tModel.clearTable();

		for (int i = 0; i < events.length; i++)
		{
			row = new Vector(6);

			switch (events[i].getType())
			{
				case TraceEvent.INITIATE:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add("-----"); // отраж
						 row.add("0.0"); // потери
						 row.add("-----"); //затух
						 tModel.insertRow(row);
						 break;
				case TraceEvent.LINEAR:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add("-----"); // отраж
						 row.add(Double.toString( MathRef.round_4 (events[i].data[1] - events[i].data[0]))); // потери
						 row.add(Double.toString( MathRef.round_4 (events[i].data[2] / res))); //затух
						 tModel.insertRow(row);
						 break;
				case TraceEvent.NON_IDENTIFIED:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add("-----"); // отраж
						 row.add("-----"); // потери
						 row.add("-----"); //затух
						 tModel.insertRow(row);
						 break;
				case TraceEvent.CONNECTOR:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add(Double.toString( MathRef.round_4 (MathRef.calcReflectance(sigma, Math.abs(events[i].data[0] - events[i].data[2]))))); // отраж
						 row.add(Double.toString( MathRef.round_4 ( events[i].data[1] - events[i].data[0]))); // потери
						 row.add("-----"); //затух
						 tModel.insertRow(row);
						 break;
				case TraceEvent.WELD:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add("-----"); // отраж
						 row.add(Double.toString( MathRef.round_4 ( events[i].data[2]))); // потери
						 row.add("-----"); //затух
						 tModel.insertRow(row);
						 break;
				case TraceEvent.TERMINATE:
						 row.add(Integer.toString(i+1)); // номер
						 row.add(LangModelAnalyse.String("eventType" + String.valueOf(events[i].getType()))); // тип
						 row.add(Double.toString( MathRef.round_3 (res * (double)events[i].first_point))); //начало
						 row.add(Double.toString( MathRef.round_3 (res * (events[i].last_point - events[i].first_point)))); //протяженность
						 row.add(Double.toString( MathRef.round_4 (MathRef.calcReflectance(sigma, Math.abs(events[i].data[0]-events[i].data[1]))))); // отраж
						 row.add("-----"); // потери
						 row.add("-----"); //затух
						 tModel.insertRow(row);
						 break;
				default: ;
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

		if(table.getSelectedRow() == row && containsRow(row, newEventsList))
		{
			c.setForeground(Color.MAGENTA);
		}
		else if(table.getSelectedRow() == row && (containsRow(row, lossChangedEventsList) || containsRow(row, amplitudeChangedEventsList)))
		{
			c.setForeground(Color.ORANGE);
		}
		else if(table.getSelectedRow() == row)
		{
			c.setForeground(Color.WHITE);
		}
		else if(containsRow(row, newEventsList))
		{
			c.setForeground(Color.red);
		}
		else if(containsRow(row, lossChangedEventsList) || containsRow(row, amplitudeChangedEventsList))
		{
			c.setForeground(Color.CYAN);
		}
		else
		{
			c.setForeground(Color.black);
		}
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

