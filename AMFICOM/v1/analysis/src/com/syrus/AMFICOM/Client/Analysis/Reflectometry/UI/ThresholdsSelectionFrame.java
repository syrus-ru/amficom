package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class ThresholdsSelectionFrame extends ATableFrame
																			implements OperationListener
{
	public static final Dimension btn_size = new Dimension(24, 24);
	private Dispatcher dispatcher;
	private ATable jTable;

	private EventTableModel tModelLinear;
	private EventTableModel tModelConnector;
	ReflectogramEvent[] ep;

	Threshold[] init_Threshs;
	int current_ev = 0;
	boolean selected_there = false;
	int _type = -1;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	AComboBox jComboBox1 = new AComboBox();
	JToolBar jToolBar1 = new JToolBar();
	JButton jButton1 = new JButton();
	JButton jButton3 = new JButton();

	public ThresholdsSelectionFrame(Dispatcher dispatcher)
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

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("thresholdsTableTitle");
	}

	public TableModel getTableModel()
	{
		return jTable.getModel();
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("thresholdsTableTitle"));

		tModelLinear = new EventTableModel(
				new String[] {LangModelAnalyse.getString("thresholdsKey"),
											LangModelAnalyse.getString("thresholdsAmplitude")},
				new Object[] {new Double(1), new Double(1) },
				new String[] {LangModelAnalyse.getString("thresholdsUpWarning"),
											LangModelAnalyse.getString("thresholdsUpAlarm"),
											LangModelAnalyse.getString("thresholdsDownWarning"),
											LangModelAnalyse.getString("thresholdsDownAlarm")},
				new int[]    { 1 }
		);

		tModelConnector = new EventTableModel(
				new String[] {LangModelAnalyse.getString("thresholdsKey"),
											LangModelAnalyse.getString("thresholdsAmplitude"),
											LangModelAnalyse.getString("thresholdsCenter"),
											LangModelAnalyse.getString("thresholdsWidth"),
											LangModelAnalyse.getString("thresholdsHeight")},
				new Object[] {new Double(1), new Double(1), new Double(1), new Double(1), new Double(1)},
				new String[] {LangModelAnalyse.getString("thresholdsUpWarning"),
										 LangModelAnalyse.getString("thresholdsUpAlarm"),
										 LangModelAnalyse.getString("thresholdsDownWarning"),
										 LangModelAnalyse.getString("thresholdsDownAlarm")},
				new int[]    { 1, 2, 3, 4 }
		);

		jTable = new ATable (tModelLinear);

		jButton1.setMaximumSize(btn_size);
		jButton1.setMinimumSize(btn_size);
		jButton1.setPreferredSize(btn_size);
		jButton1.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		jButton1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/th_initial.gif")));
		jButton1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton1_actionPerformed(e);
			}
		});

		jButton3.setMaximumSize(btn_size);
		jButton3.setMinimumSize(btn_size);
		jButton3.setPreferredSize(btn_size);
		jButton3.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		jButton3.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/th_default.gif")));
		jButton3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton3_actionPerformed(e);
			}
		});

		this.setContentPane(mainPanel);

		jComboBox1.addItemListener(new java.awt.event.ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				jComboBox1_itemStateChanged(e);
			}
		});
		jComboBox1.addItem(LangModelAnalyse.getString("thresholdsConnector"));
		jComboBox1.addItem(LangModelAnalyse.getString("thresholdsWeld"));
		jComboBox1.addItem(LangModelAnalyse.getString("thresholdsLinear"));

		//jToolBar1.setBorderPainted(true);
		jToolBar1.setFloatable(false);
		jToolBar1.setLayout(new XYLayout());
		jToolBar1.add(jButton1, new XYConstraints(0, 0, -1, -1));
		jToolBar1.add(jButton3, new XYConstraints(btn_size.width, 0, -1, -1));

		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(jTable);
		scrollPane.setAutoscrolls(true);

		JPanel northPanel = new JPanel(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		mainPanel.add(northPanel,  BorderLayout.NORTH);
		northPanel.add(jToolBar1,  BorderLayout.NORTH);
		northPanel.add(jComboBox1,  BorderLayout.CENTER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		updColorModel();
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
		jComboBox1.setBackground(SystemColor.window);
		jComboBox1.setForeground(ColorManager.getColor("textColor"));
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefUpdateEvent.typ);
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
					this.ep = null;
			}
		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;
			if(rue.THRESHOLDS_UPDATED)
			{
				String id = (String)(rue.getSource());
				//if (id.equals("primarytrace"))
				{
					ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);
					this.ep = ep;

					selected_there = true;
					updThresholds();
					selected_there = false;
				}
			}
			if(rue.ANALYSIS_PERFORMED)
			{
				String id = (String)(rue.getSource());
				//if (id.equals("primarytrace"))
				{
					ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);

					if (ep != null)
					{
						if (this.ep != null && this.ep.length <= ep.length)
						{
							for (int i = 0; i < this.ep.length; i++)
							{
								Threshold t = this.ep[i].getThreshold();
								t.setReflectogramEvent(ep[i]);
								ep[i].setThreshold(t);
							}
						}
						init_Threshs = new Threshold[ep.length];
						for (int i = 0; i < ep.length; i++)
							init_Threshs[i] = (Threshold)ep[i].getThreshold().clone();

						this.ep = ep;

						selected_there = true;
						updThresholds();
						selected_there = false;
					}
				}
			}
			if(rue.EVENT_SELECTED)
			{
				current_ev = Integer.parseInt((String)rue.getSource());
				selected_there = true;
				updThresholds();
				selected_there = false;
			}
			if(rue.THRESHOLD_CHANGED)
			{
				selected_there = true;
				updThresholds();
				selected_there = false;
			}
		}
	}

	void jButton1_actionPerformed(ActionEvent e)
	{
		if (ep != null)
		{
			ep[current_ev].setThreshold((Threshold)init_Threshs[current_ev].clone());
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jButton3_actionPerformed(ActionEvent e)
	{
		if (ep != null)
		{
			ep[current_ev].setThreshold(new Threshold(ep[current_ev]));
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jComboBox1_itemStateChanged(ItemEvent e)
	{
		if ((ep == null) || (selected_there))
			return;

		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			String type = (String)e.getItem();
			if (type.equals(LangModelAnalyse.getString("thresholdsConnector")))
			{
				ep[current_ev].setType(ReflectogramEvent.CONNECTOR);
				updThresholds();
				dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
			else if (type.equals(LangModelAnalyse.getString("thresholdsWeld")))
			{
				ep[current_ev].setType(ReflectogramEvent.WELD);
				updThresholds();
				dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
			else if (type.equals(LangModelAnalyse.getString("thresholdsLinear")))
			{
				ep[current_ev].setType(ReflectogramEvent.LINEAR);
				updThresholds();
				dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
		}
	}

	void updThresholds()
	{
		if (ep == null)
			return;
		if (current_ev == -1)
			return;
		if (current_ev >= ep.length)
			current_ev = ep.length - 1;

		Threshold t = ep[current_ev].getThreshold();

		if (ep[current_ev].getType() == ReflectogramEvent.CONNECTOR)
		{
			tModelConnector.updateData (
				new Double[][]{
												t.getThresholdsObject(Threshold.UP1),
												t.getThresholdsObject(Threshold.UP2),
												t.getThresholdsObject(Threshold.DOWN1),
												t.getThresholdsObject(Threshold.DOWN2)
			});

			if (_type != ReflectogramEvent.CONNECTOR)
			{
				jComboBox1.setSelectedItem(LangModelAnalyse.getString("thresholdsConnector"));
				_type = ReflectogramEvent.CONNECTOR;
				jTable.setModel(tModelConnector);
				jTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				jTable.getColumnModel().getColumn(1).setPreferredWidth(65);
				jTable.getColumnModel().getColumn(2).setPreferredWidth(50);
				jTable.getColumnModel().getColumn(3).setPreferredWidth(50);
				jTable.getColumnModel().getColumn(4).setPreferredWidth(50);
				jTable.updateUI();
			}
		}
		else if (ep[current_ev].getType() == ReflectogramEvent.WELD)
		{
			tModelLinear.updateData( new Object[][] {
				{ t.getThresholdsObject(Threshold.UP1)[0]},
				{	t.getThresholdsObject(Threshold.UP2)[0]},
				{	t.getThresholdsObject(Threshold.DOWN1)[0]},
				{	t.getThresholdsObject(Threshold.DOWN2)[0]}
			});

			if (_type != ReflectogramEvent.WELD)
			{
				jComboBox1.setSelectedItem(LangModelAnalyse.getString("thresholdsWeld"));
				_type = ReflectogramEvent.WELD;
				jTable.setModel(tModelLinear);
				jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
				jTable.updateUI();
			}
		}
		else if (ep[current_ev].getType() == ReflectogramEvent.LINEAR)
		{
			tModelLinear.updateData( new Object[][] {
				{	t.getThresholdsObject(Threshold.UP1)[0]},
				{	t.getThresholdsObject(Threshold.UP2)[0]},
				{	t.getThresholdsObject(Threshold.DOWN1)[0]},
				{	t.getThresholdsObject(Threshold.DOWN2)[0]}
			});

			if (_type != ReflectogramEvent.LINEAR)
			{
				jComboBox1.setSelectedItem(LangModelAnalyse.getString("thresholdsLinear"));
				_type = ReflectogramEvent.LINEAR;
				jTable.setModel(tModelLinear);
				jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
				jTable.updateUI();
			}
		}
	}

	class EventTableModel extends FixedSizeEditableTableModel
	{
		public EventTableModel (String[] p_columns,
																		 Object[] p_defaultv,
																		 String[] p_rows,
																		 int[] editable)
		{
			super ( p_columns, p_defaultv, p_rows, editable);
		}

		public void setValueAt(Object value, int row, int col)
		{
			super.setValueAt(value, row, col);
			if (ep != null)
			{
				ep[current_ev].getThreshold().setThresholdValue(((Double)value).doubleValue(), col, row);
				dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
		}
	}
}