package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class ThresholdsSelectionFrame extends ATableFrame
	implements OperationListener
{
	public static final Dimension btn_size = new Dimension(24, 24);
	private Dispatcher dispatcher;
	private ATable jTable;

	private EventTableModel tModelLinear;
	private EventTableModel tModelConnector;
	private ReflectogramEvent[] ep;
	
	private BellcoreStructure bs; // для доступа к самой р/г во время пересчета порогов

	private Threshold[] init_Threshs;
	private int current_ev = -1;
	private boolean selected_there = false;
	private int _type = -1;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	AComboBox jComboBox1 = new AComboBox();
	JToolBar jToolBar1 = new JToolBar();
	JButton jButton1 = new JButton();
	JButton jButton3 = new JButton();
	JCheckBox jCheckBox1 = new JCheckBox(); // LeftJoin

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
				new String[] {
						LangModelAnalyse.getString("thresholdsKey"),
						LangModelAnalyse.getString("thresholdsAmplitude"),
						LangModelAnalyse.getString("thresholdsCenter"),
						LangModelAnalyse.getString("thresholdsWidth"),
						LangModelAnalyse.getString("thresholdsHeight")
				},
				new Object[] {new Double(1), new Double(1), new Double(1), new Double(1), new Double(1)},
				new String[] {
						LangModelAnalyse.getString("thresholdsUpWarning"),
						LangModelAnalyse.getString("thresholdsUpAlarm"),
						LangModelAnalyse.getString("thresholdsDownWarning"),
						LangModelAnalyse.getString("thresholdsDownAlarm")
				},
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
		
		jCheckBox1.setText(LangModelAnalyse.getString("modelLeftJoin"));
		jCheckBox1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (current_ev >= 0 && ep[current_ev].setLeftLink(jCheckBox1.isSelected()))
		        {
		            //System.out.println("LeftLink type changed");
		            dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.MODEL_CHANGED_EVENT));
		            dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		        }
		    }
		});

		//jToolBar1.setBorderPainted(true);
		jToolBar1.setFloatable(false);
		jToolBar1.add(jButton1);
		jToolBar1.add(jButton3);
		jToolBar1.add(jCheckBox1);

		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
			if(rue.thresholdsUpdated())
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
			if(rue.analysisPerformed())
			{
				String id = (String)(rue.getSource());
				//if (id.equals("primarytrace"))
				{
				    //System.out.println("thresholdsSelectionFrame: ANALYSIS_PERFORMED: source = '"+id+"'"); // FIXIT

				    ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);
					bs = (BellcoreStructure)Pool.get("bellcorestructure", id);

					if (ep != null)
					{
						if (false && this.ep != null && this.ep.length <= ep.length) // пофикшено мною -- saa
						{
							for (int i = 0; i < this.ep.length; i++)
							{
								Threshold t = this.ep[i].getThreshold();
								ep[i].setThreshold(t);
							}
						}

						init_Threshs = new Threshold[ep.length];
						for (int i = 0; i < ep.length; i++)
							init_Threshs[i] = (Threshold)ep[i].getThreshold().copy();

						this.ep = ep;

						selected_there = true;
						updThresholds();
						selected_there = false;
					}
				}
			}
			if(rue.eventSelected())
			{
				current_ev = Integer.parseInt((String)rue.getSource());
				if (current_ev < 0 || current_ev >= ep.length)
				{
				    System.out.println("Warning: current_ev out of range");
				    current_ev = 0;
				}
				selected_there = true;
				updThresholds();
				selected_there = false;
			}
			if(rue.thresholdChanged())
			{
				selected_there = true;
				updThresholds();
				selected_there = false;
			}
		}
	}

	void jButton1_actionPerformed(ActionEvent e)
	{
		if (ep != null && current_ev != -1)
		{
			ep[current_ev].setThreshold((Threshold)init_Threshs[current_ev].copy());
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jButton3_actionPerformed(ActionEvent e)
	{
		if (ep != null && current_ev != -1)
		{
			ep[current_ev].setThreshold(new Threshold());
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jComboBox1_itemStateChanged(ItemEvent e)
	{
		if (ep == null || selected_there || current_ev == -1)
			return;

		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			String type = (String)e.getItem();
			int newType = ReflectogramEvent.RESERVED_VALUE;
			
			double[] y = bs == null ? null : bs.getTraceData(); // reflectogram
			if (type.equals(LangModelAnalyse.getString("thresholdsConnector")))
			    newType = ReflectogramEvent.CONNECTOR;
			else if (type.equals(LangModelAnalyse.getString("thresholdsWeld")))
			    newType = ReflectogramEvent.WELD;
			else if (type.equals(LangModelAnalyse.getString("thresholdsLinear")))
			    newType = ReflectogramEvent.LINEAR;
			
			System.out.println("TSF: change threshold type: newType="+newType+"; y="+y);
			
			if (newType != ReflectogramEvent.RESERVED_VALUE)
			{
			    // change thresold type
				ep[current_ev].changeThresholdType(newType, y);
				// redraw thresholds
				updThresholds();
				// notify others (is there any listener for this event??)
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

		int ttype   = ep[current_ev].getThresholdType(); 
		Threshold t = ep[current_ev].getThreshold();

		if (ttype == ReflectogramEvent.CONNECTOR)
		{
			// FIXIT: предоставляем больше данных чем надо
			Double[] u1v = t.getThresholdsObject(Threshold.UP1);
			Double[] u2v = t.getThresholdsObject(Threshold.UP2);
			Double[] d1v = t.getThresholdsObject(Threshold.DOWN1);
			Double[] d2v = t.getThresholdsObject(Threshold.DOWN2);
			tModelConnector.updateData(
			    new Double[][]{u1v, u2v, d1v, d2v}
			    );
				/*
				new Double[][]{
						{u1v[0], u1v[1], u1v[2], u1v[3]},
						{u2v[0], u2v[1], u2v[2], u2v[3]},
						{d1v[0], d1v[1], d1v[2], d1v[3]},
						{d2v[0], d2v[1], d2v[2], d2v[3]}
				});
				*/

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
		else if (ttype == ReflectogramEvent.WELD)
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
		else if (ttype == ReflectogramEvent.LINEAR)
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
		
		if (jCheckBox1.isSelected() ^ ep[current_ev].getLeftLink())
		{
		    jCheckBox1.setSelected(ep[current_ev].getLeftLink());
		}
	}

	class EventTableModel extends FixedSizeEditableTableModel {
		public EventTableModel(String[] p_columns, Object[] p_defaultv,
				String[] p_rows, int[] editable) {
			super(p_columns, p_defaultv, p_rows, editable);
		}

		public void setValueAt(Object value, int row, int col) {
			super.setValueAt(value, row, col);
			if (ep != null) {
				ep[current_ev].getThreshold().setThresholdValue(
						((Double) value).doubleValue(), col, ep[current_ev],
						row);
				dispatcher.notify(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
		}
	}
}