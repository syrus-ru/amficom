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
	protected Dispatcher dispatcher;
	private ATable jTable;

	//private EventTableModel tModelLinear;
	//private EventTableModel tModelConnector;
	private BellcoreStructure bs; // для доступа к самой р/г во время пересчета порогов

	protected ModelTraceManager mtm;

	private Threshold[] init_Threshs;
	protected int current_ev = -1;
	private boolean selected_there = false;
	//private int _type = -1;

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	//AComboBox jComboBox1 = new AComboBox();
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

		/*tModelLinear = new EventTableModel(
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
		);*/

		EventTableModel tModelUseless = new EventTableModel( // FIXME - переделать
			new String[] {LangModelAnalyse.getString("thresholdsKey"),
										LangModelAnalyse.getString("thresholdsAmplitude")},
			new Object[] {new Double(1), new Double(1) },
			new String[] {LangModelAnalyse.getString("thresholdsUpWarning"),
										LangModelAnalyse.getString("thresholdsUpAlarm"),
										LangModelAnalyse.getString("thresholdsDownWarning"),
										LangModelAnalyse.getString("thresholdsDownAlarm")},
			new int[]    { 1 }
		);
		jTable = new ATable (tModelUseless);

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

		/*
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
		*/

		//jToolBar1.setBorderPainted(true);
		jToolBar1.setFloatable(false);
		jToolBar1.add(jButton1);
		jToolBar1.add(jButton3);

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
		//northPanel.add(jComboBox1,  BorderLayout.CENTER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		updColorModel();
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
//		jComboBox1.setBackground(SystemColor.window);
//		jComboBox1.setForeground(ColorManager.getColor("textColor"));
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
					this.mtm = null;
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
					mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id);
					
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

					ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id);
				    //ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", id);
					bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
					
					if (mtm != null)
					{
						// FIXME:
						// --- тут была попытка сохранить пороги при
						// замене списка событий, и, в принципе, ее можно бы
						// вернуть, если только она делала что-то осмысленное
					
						selected_there = true;
						updThresholds();
						selected_there = false;
					}
				}
			}
			if(rue.eventSelected())
			{
				current_ev = Integer.parseInt((String)rue.getSource());
				if (current_ev < 0 || current_ev >= mtm.getNEvents())
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
		if (mtm != null && current_ev != -1)
		{
			mtm.setThreshold(current_ev, init_Threshs[current_ev].copy());
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jButton3_actionPerformed(ActionEvent e)
	{
		if (mtm != null && current_ev != -1)
		{
			mtm.setDefaultThreshold(current_ev);
			updThresholds();
			dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
	}

	void jComboBox1_itemStateChanged(ItemEvent e)
	{
		if (mtm == null || selected_there || current_ev == -1)
			return;

		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			String type = (String)e.getItem();
			int newType = SimpleReflectogramEvent.RESERVED;
			
			double[] y = bs == null ? null : bs.getTraceData(); // reflectogram
			if (type.equals(LangModelAnalyse.getString("thresholdsConnector")))
			    newType = SimpleReflectogramEvent.CONNECTOR;
			else if (type.equals(LangModelAnalyse.getString("thresholdsWeld")))
			    newType = SimpleReflectogramEvent.SPLICE;
			else if (type.equals(LangModelAnalyse.getString("thresholdsLinear")))
			    newType = SimpleReflectogramEvent.LINEAR;
			
			System.out.println("TSF: change threshold type: newType="+newType+"; y="+y);
			
			if (newType != SimpleReflectogramEvent.RESERVED)
			{
			    // change thresold type
				mtm.changeThresholdType(current_ev, newType, y);
				// redraw thresholds
				updThresholds();
				// notify others (is there any listener for this event??)
				dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
		}
	}

	void updThresholds()
	{
		if (mtm == null)
			return;
		if (current_ev == -1)
			return;
		if (current_ev >= mtm.getNEvents())
			current_ev = mtm.getNEvents() - 1;

		ModelTraceManager.ThreshEditor[] te = mtm.getThreshEditor(current_ev);
		String[] pColumns = new String[te.length + 1];
		pColumns[0] = LangModelAnalyse.getString("thresholdsKey");
		for (int i = 1; i < pColumns.length; i++)
		{
			switch(te[i - 1].getType())
			{
			case ModelTraceManager.ThreshEditor.TYPE_A:
				pColumns[i] = LangModelAnalyse.getString("thresholdsAmplitude");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_L:
				pColumns[i] = LangModelAnalyse.getString("thresholdsHeight");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXF:
				pColumns[i] = LangModelAnalyse.getString("thresholdsDXL");
				break;
			case ModelTraceManager.ThreshEditor.TYPE_DXT:
				pColumns[i] = LangModelAnalyse.getString("thresholdsDXR");
				break;
			// default... - @todo
			}
		}
		Object[] pObjects = new Object[te.length + 1];
		pObjects[0] = new Double(0);
		for (int i = 1; i < pColumns.length; i++)
			pObjects[i] = new Double(0); // XXX

		EventTableModel tModel = new EventTableModel(
			pColumns,
			pObjects,
			new String[] {
					LangModelAnalyse.getString("thresholdsUpWarning"),
					LangModelAnalyse.getString("thresholdsUpAlarm"),
					LangModelAnalyse.getString("thresholdsDownWarning"),
					LangModelAnalyse.getString("thresholdsDownAlarm")
			},
			new int[]    { 1, 2, 3, 4 });

		Object[][] pData = new Object[4][];
		for (int k = 0; k < 4; k++)
		{
			pData[k] = new Double[te.length];
			for (int i = 0; i < te.length; i++)
				((Double[] )pData[k])[i] = new Double(te[i].getValue(k));
		}

		tModel.updateData(pData);
		jTable.setModel(tModel);

//				jTable.setModel(tModelConnector);
//				jTable.getColumnModel().getColumn(0).setPreferredWidth(150);
//				jTable.getColumnModel().getColumn(1).setPreferredWidth(65);
//				jTable.getColumnModel().getColumn(2).setPreferredWidth(50);
//				jTable.getColumnModel().getColumn(3).setPreferredWidth(50);
//				jTable.getColumnModel().getColumn(4).setPreferredWidth(50);
//				jTable.updateUI();

//				jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
//				jTable.updateUI();

//				jTable.setModel(tModelLinear);
//				jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
//				jTable.updateUI();
	}

	class EventTableModel extends FixedSizeEditableTableModel {
		public EventTableModel(String[] p_columns, Object[] p_defaultv,
				String[] p_rows, int[] editable) {
			super(p_columns, p_defaultv, p_rows, editable);
		}

		public void setValueAt(Object value, int row, int col) {
			super.setValueAt(value, row, col);
			if (mtm != null && current_ev != -1) {
				ModelTraceManager.ThreshEditor[] te =
					mtm.getThreshEditor(current_ev);
				te[col - 1].setValue(
						row,
						((Double) value).doubleValue());
				dispatcher.notify(new RefUpdateEvent(this,
						RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
			}
		}
	}
}