package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class DetailedEventsFrame extends JInternalFrame
															 implements OperationListener
{
	private ReflectogramEvent []data;
	private ReflectogramEvent []data_;
	private ReflectogramEvent []etalon;

	private Dispatcher dispatcher;
	private Map tModels = new HashMap(6);

	private ATable jTable;
	private int selected = 0;
	private int concSelected = 0;
	private RefAnalysis a;
	private BellcoreStructure bs;
	private double res;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	boolean analysis_performed = false;
	boolean etalon_loaded = false;

	private CompareTableModel ctModel;
	private ATable jTableComp;

	BorderLayout borderLayoutComp = new BorderLayout();
	JPanel mainPanelComp = new JPanel();
	JScrollPane scrollPaneComp = new JScrollPane();
	JViewport viewportComp = new JViewport();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private static StringBuffer km = new StringBuffer(' ').append(LangModelAnalyse.getString("km"));
	private static StringBuffer mt = new StringBuffer(' ').append(LangModelAnalyse.getString("mt"));
	private static StringBuffer db = new StringBuffer(' ').append(LangModelAnalyse.getString("dB"));

	private static String linear = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.LINEAR));
	private static String connector = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.CONNECTOR));
	private static String weld = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.WELD));
	private static String initiate = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.INITIATE));
	private static String terminate = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.TERMINATE));
	private static String noid = LangModelAnalyse.getString("eventType" + String.valueOf(TraceEvent.NON_IDENTIFIED));

	public DetailedEventsFrame()
	{
		this(new Dispatcher());
	}

	public DetailedEventsFrame(Dispatcher dispatcher)
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
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
				/*	if ((RefAnalysis)Pool.get("refanalysis", id) != null)
					{
						a = (RefAnalysis)Pool.get("refanalysis", id);
						bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
						res = (double)(3 * bs.fxdParams.DS[0]) / (double)(bs.fxdParams.GI * 10000);
						updTableModel (0);
					}*/
					data_ = null;
//					ctModel.clearTable();
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setEnabledAt(1, false);
					setVisible(true);
				}
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					data_ = null;
//					tModel.clearTable();
					ctModel.clearTable();
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(0, true);
					analysis_performed = false;
					tabbedPane.setEnabledAt(1, false);
					setVisible(false);
				}
				else if(id.equals(AnalysisUtil.ETALON))
				{
					data_ = null;
					ctModel.clearTable();
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setSelectedIndex(0);
					etalon_loaded = false;
					tabbedPane.setEnabledAt(1, false);
				}
			}
			if(rce.OPEN_ETALON)
			{
				this.etalon = (ReflectogramEvent[])Pool.get("eventparams", AnalysisUtil.ETALON);
				if(data !=null)
					this.data_ = ReflectogramMath.alignClone(data, etalon);
				else
					data_ = null;
				ctModel.clearTable();
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setSelectedIndex(0);
				etalon_loaded = true;
				if(analysis_performed)
					tabbedPane.setEnabledAt(1, true);
			}
			if(rce.CLOSE_ETALON)
			{
				data_ = null;
				ctModel.clearTable();
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setSelectedIndex(0);
				etalon_loaded = false;
				tabbedPane.setEnabledAt(1, false);
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
						a = (RefAnalysis)Pool.get("refanalysis", id);
						bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
						res = bs.getResolution();
						this.data =  (ReflectogramEvent [])Pool.get("eventparams", "primarytrace");
						if(data != null && etalon != null)
							this.data_ = ReflectogramMath.alignClone(data, etalon);
						if (selected >= a.events.length)
							selected = a.events.length - 1;
						updTableModel (selected);
					}
				}
				if (Pool.get("eventparams", "primarytrace") != null)
					analysis_performed = true;
				if(etalon_loaded)
					tabbedPane.setEnabledAt(1, true);
			}
			if (rue.EVENT_SELECTED)
			{
				selected = Integer.parseInt((String)rue.getSource());
				updTableModel (selected);
				setData(selected);
			}
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

		FixedSizeEditableTableModel linearModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventRMSDeviation"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(linear, linearModel);

		FixedSizeEditableTableModel initialModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventEDZ"),
						LangModelAnalyse.getString("eventADZ")
				},
				null);
		tModels.put(initiate, initialModel);

		FixedSizeEditableTableModel noidModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventMaxLevel"),
						LangModelAnalyse.getString("eventMinLevel"),
						LangModelAnalyse.getString("eventMaxDeviation")
				},
				null);
		tModels.put(noid, noidModel);

		FixedSizeEditableTableModel connectorModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel"),
						LangModelAnalyse.getString("eventReflectionLevel"),
						LangModelAnalyse.getString("eventFormFactor")
				},
				null);
		tModels.put(connector, connectorModel);

		FixedSizeEditableTableModel spliceModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventEndLevel")
				},
				null);
		tModels.put(weld, spliceModel);

		FixedSizeEditableTableModel terminateModel = new FixedSizeEditableTableModel(
				new String[] {LangModelAnalyse.getString("eventDetailedParam"),
											LangModelAnalyse.getString("eventDetailedValue")},
				new Object[] {""},
				new String[] {
						"",
						LangModelAnalyse.getString("eventLength"), // протяженность
						LangModelAnalyse.getString("eventStartLevel"),
						LangModelAnalyse.getString("eventReflectionLevel"),
						LangModelAnalyse.getString("eventFormFactor")
				},
				null);
		tModels.put(terminate, terminateModel);

		jTable = new ATable();

		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

//		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 200));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("eventDetailedTableTitle"));

		tabbedPane.add("Основные", mainPanel);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
		jTable.setMinimumSize(new Dimension(200, 200));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		tabbedPane.setEnabledAt(0, true);


		tabbedPane.add("Сравнительные", mainPanelComp);

		ctModel = new CompareTableModel();
		jTableComp = new ATable (ctModel);
		jTableComp.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTableComp.getColumnModel().getColumn(1).setPreferredWidth(100);

		mainPanelComp.setLayout(new BorderLayout());
		mainPanelComp.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPaneComp.setViewport(viewportComp);
		scrollPaneComp.setAutoscrolls(true);
		jTableComp.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTableComp.setMinimumSize(new Dimension(200, 213));
		jTableComp.setDefaultRenderer(Object.class, new CompareTableRenderer());

		mainPanelComp.add(scrollPaneComp, BorderLayout.CENTER);
		scrollPaneComp.getViewport().add(jTableComp);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);

		updColorModel();
	}


	private void setData(int nEvent)
	{

		if(etalon == null || data_ == null)
		{
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			return;
		}
		if(nEvent > data_.length || nEvent<0)
		{
			return;
		}
		double delta_x = data_[0].getDeltaX();

		//int dataType = WorkWithReflectoEventsArray.getEventType(data_[nEvent]);
		int coord = ReflectogramMath.getEventCenter(data_[nEvent]);
		//int etalonType = WorkWithReflectoEventsArray.getEventType(coord, etalon);

		if(data_[nEvent].getType() == etalon[nEvent].getType())
			((CompareTableRenderer)jTableComp.getDefaultRenderer(Object.class)).setSameType(true);
		else
			((CompareTableRenderer)jTableComp.getDefaultRenderer(Object.class)).setSameType(false);

		String dataT = LangModelAnalyse.getString("eventTypeUnk");
		String etalonT = LangModelAnalyse.getString("eventTypeUnk");
		if(data_[nEvent].getType() == ReflectogramEvent.CONNECTOR)
		{
			dataT = LangModelAnalyse.getString("eventType4");
		}
		else if(data_[nEvent].getType() == ReflectogramEvent.WELD)
		{
			dataT = LangModelAnalyse.getString("eventType3");
		}
		else if(data_[nEvent].getType() == ReflectogramEvent.LINEAR)
		{
			dataT = LangModelAnalyse.getString("eventType0");
		}

		if(etalon[nEvent].getType() == ReflectogramEvent.CONNECTOR)
		{
			etalonT = LangModelAnalyse.getString("eventType4");
		}
		else if(etalon[nEvent].getType() == ReflectogramEvent.WELD)
		{
			etalonT = LangModelAnalyse.getString("eventType3");
		}
		else if(etalon[nEvent].getType() == ReflectogramEvent.LINEAR)
		{
			etalonT = LangModelAnalyse.getString("eventType0");
		}
		ctModel.setValueAt(dataT, 0, 1);
		ctModel.setValueAt(etalonT, 1, 1);

		double difference = ReflectogramComparer.getDeviation(etalon, data_, nEvent);
		double meanDeviation = ReflectogramComparer.getMeanDeviation(data_, etalon, nEvent);
		double loss = ReflectogramComparer.getLoss(etalon, data_, nEvent);
		double locationDiff = ReflectogramComparer.getLocationDifference(etalon, data_, nEvent)*delta_x;
		double widthDiff = ReflectogramComparer.getWidthDifference(etalon, data_, nEvent)*delta_x;

		difference = ((int)(difference*1000.))/1000.;
		loss = ((int)(loss*1000.))/1000.;
		locationDiff = ((int)(locationDiff*1000.))/1000.;
		widthDiff = ((int)(widthDiff*1000.))/1000.;
		meanDeviation = ((int)(meanDeviation*1000.))/1000.;

		ctModel.setValueAt(String.valueOf(difference)+" дБ", 2, 1);
		ctModel.setValueAt(String.valueOf(meanDeviation)+" дБ", 3, 1);
		ctModel.setValueAt(String.valueOf(locationDiff)+" м", 6, 1);


		if(etalon[nEvent].getType() == data_[nEvent].getType())
		{
			ctModel.setValueAt(String.valueOf(loss)+" дБ", 4, 1);
			ctModel.setValueAt(String.valueOf(widthDiff)+" м", 5, 1);
		}
		else
		{
			ctModel.setValueAt("--", 4, 1);
			ctModel.setValueAt("--", 5, 1);
		}
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));

		scrollPaneComp.getViewport().setBackground(SystemColor.window);
		jTableComp.setBackground(SystemColor.window);
		jTableComp.setForeground(ColorManager.getColor("textColor"));
		jTableComp.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void updTableModel(int num)
	{
		if (num == -1)
			return;
		TraceEvent ev = a.events[num];

		FixedSizeEditableTableModel tModel = null;
		switch (ev.getType())
		{
			case TraceEvent.LINEAR:
				tModel = (FixedSizeEditableTableModel)tModels.get(linear);
				tModel.setValueAt(linear,	0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[3]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[4]))
								.append(db).toString()
				}, 1);
				break;
			case TraceEvent.INITIATE:
				tModel = (FixedSizeEditableTableModel)tModels.get(initiate);
				tModel.setValueAt(initiate,	0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString(),
						new StringBuffer().append(Math.round(ev.data[2] * res * 1000d))
								.append(mt).toString(),
						new StringBuffer().append(Math.round(ev.data[3] * res * 1000d))
								.append(mt).toString()
					}, 1);
					break;
			case TraceEvent.NON_IDENTIFIED:
				tModel = (FixedSizeEditableTableModel)tModels.get(noid);
				tModel.setValueAt(noid,	0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[2]))
								.append(db).toString()
					}, 1);
					break;
			case TraceEvent.CONNECTOR:
				tModel = (FixedSizeEditableTableModel)tModels.get(connector);
				tModel.setValueAt(connector, 0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[2]))
								.append(db).toString(),
						String.valueOf(MathRef.round_4(ev.data[3]))
					}, 1);
					break;
			case TraceEvent.WELD:
				tModel = (FixedSizeEditableTableModel)tModels.get(weld);
				tModel.setValueAt(weld,	0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString()
					}, 1);
					break;
			case TraceEvent.TERMINATE:
				tModel = (FixedSizeEditableTableModel)tModels.get(terminate);
				tModel.setValueAt(terminate, 0, 0);
				tModel.updateColumn(new Object[] { String.valueOf(num + 1),
						new StringBuffer().append(MathRef.round_3((ev.last_point - ev.first_point)*res))
								.append(km).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[0]))
								.append(db).toString(),
						new StringBuffer().append(MathRef.round_4(ev.data[1]))
								.append(db).toString(),
						String.valueOf(MathRef.round_4(ev.data[2]))
					}, 1);
				 break;
			}
		jTable.setModel(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		jTable.updateUI();
	}
}



class CompareTableModel extends AbstractTableModel
{
//  String[] columnNames = {LangModelModel.String("parameter") ,
//    LangModelModel.String("value")};

	String[] columnNames = {null ,
		null};

	Object[][] data = {
		{LangModelAnalyse.getString("eventType"), "--"},
		{LangModelAnalyse.getString("etEventType"), "--"},
		{LangModelAnalyse.getString("maxDeviation"), "--"},
		{LangModelAnalyse.getString("meanDeviation"), "--"},
		{LangModelAnalyse.getString("delta"), "--"},
		{LangModelAnalyse.getString("dWidth"), "--"},
		{LangModelAnalyse.getString("dLocation"), "--"}
	};

	CompareTableModel()
	{
		super();
	}

	public void clearTable()
	{
		setValueAt("--", 0, 1);
		setValueAt("--", 1, 1);
		setValueAt("--", 2, 1);
		setValueAt("--", 3, 1);
		setValueAt("--", 4, 1);
		setValueAt("--", 5, 1);
		setValueAt("--", 6, 1);

		super.fireTableDataChanged();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public double getvalueat(int row, int col) {
		return ((Double)(data[row][col])).doubleValue();
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}


	public void setInicialValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}


class CompareTableRenderer extends DefaultTableCellRenderer
{

	private boolean sameType = true;

	public void setSameType(boolean key)
	{
		sameType = key;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
												boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if(!sameType && row == 0)
			c.setForeground(Color.red);
		else if(!sameType && row == 1)
			c.setForeground(Color.red);
		else
			c.setForeground(Color.black);

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


