package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.General.Command.Analysis.MinuitAnalyseCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;
import oracle.jdeveloper.layout.*;

public class AnalysisSelectionFrame extends ATableFrame
																		implements OperationListener
{
	public static final Dimension btn_size = new Dimension(24, 24);
	private Dispatcher dispatcher;
	private ParamTableModel tModelMinuit;
	private ATable jTable;
	private ColorManager cManager;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JToolBar jToolBar1 = new JToolBar();
	JButton jButton1 = new JButton();
	JButton jButton2 = new JButton();
	JButton jButton3 = new JButton();
	ApplicationContext aContext;

	public AnalysisSelectionFrame(ApplicationContext aContext)
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
		this.aContext = aContext;

		init_module(aContext.getDispatcher());
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
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
					BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
					if (bs.measurementId == null)
						setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (шаблона нет)");
					else
					{
						try
						{
							Measurement m = (Measurement)MeasurementStorableObjectPool.getStorableObject(bs.measurementId, true);
							setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (шаблон: " +
										 (m.getSetup().getDescription().equals("") ? "(нет)" : m.getSetup().getDescription()) + ")");
						}
						catch(ApplicationException ex)
						{
							System.err.println("Exception retrieving measurenent with " + bs.measurementId);
							ex.printStackTrace();
							return;
						}
					}

					double[] minuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
					setDefaults(minuitParams);
					setVisible(true);
				}
			}
			if(rce.THRESHOLDS_CALC)
			{
				String id = (String)(rce.getSource());

				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
				if (bs.measurementId == null)
					setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (шаблона нет)");
				else
				{
					try
					{
						Measurement m = (Measurement)MeasurementStorableObjectPool.getStorableObject(bs.measurementId, true);

						MeasurementSetup ms = m.getSetup();
						setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (шаблон: " +
									 (ms.getDescription().equals("") ? ms.getId().getIdentifierString() : ms.getDescription()) + ")");

						if (ms.getCriteriaSet() != null)
						{
							double[] minuitParams = (double[])Pool.get("analysisparameters", "minuitanalysis");
							setDefaults(minuitParams);
						}
					}
					catch(ApplicationException ex)
					{
						System.err.println("Measurement not found. id = " + bs.measurementId);
						ex.printStackTrace();
						return;
					}
				}
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					jTable.setModel(new FixedSizeEditableTableModel(
							new String[] { "" },
							new String[] { "" },
							new String[] { "" },
							new int[] { }));
					setVisible(false);
				}
			}
		}
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("analysisSelectionTitle");
	}

	public TableModel getTableModel()
	{
		return tModelMinuit;
	}

	void setDefaults(double[] minuitParams)
	{
		tModelMinuit.updateData(new Object[]{
			 new Double(minuitParams[2]),
			 new Double(minuitParams[1]),
			 new Double(minuitParams[3]),
			 new Double(minuitParams[0]),
			 new Double(minuitParams[4]),
			 new Double(minuitParams[5]),
			 new Integer((int)minuitParams[6]),
			 new Integer((int)minuitParams[7])
		});
		/*
		tModelMinuit.updateData(new Double[][]{
			{ new Double(minuitParams[2])},
			{ new Double(minuitParams[1])},
			{ new Double(minuitParams[3])},
			{ new Double(minuitParams[0])},
			{ new Double(minuitParams[4])},
			{ new Double(minuitParams[5])},
			{ new Double(minuitParams[6])},
			{ new Double(minuitParams[7]) }
		});*/

		jTable.setModel(tModelMinuit);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(cManager.getColor("textColor"));
		jTable.setGridColor(cManager.getColor("tableGridColor"));
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("analysisSelectionTitle"));

		tModelMinuit = new ParamTableModel();
		/*tModelMinuit = new FixedSizeEditableTableModel (
				new String[] { LangModelAnalyse.getString("analysisSelectionKey"),
											 LangModelAnalyse.getString("analysisSelectionValue")},
				new Double[] { new Double(1), new Double(1) },
				new String[] { LangModelAnalyse.getString("analysisMinConnector"),
											 LangModelAnalyse.getString("analysisMinWeld"),
											 LangModelAnalyse.getString("analysisMinEnd"),
											 LangModelAnalyse.getString("analysisMinEvent"),
											 LangModelAnalyse.getString("analysisNSigma"),
											 LangModelAnalyse.getString("analysisFormFactor"),
											 LangModelAnalyse.getString("analysisStrategy"),
											 LangModelAnalyse.getString("analysisWavelet")},
				new int[] { 1 });
*/
		jTable = new ATable (tModelMinuit);
		jTable.setDefaultRenderer(Object.class, new ModelParamsTableRenderer(tModelMinuit));
		jTable.setDefaultEditor(Object.class, new ModelParamsTableEditor(tModelMinuit));

		jButton1.setMaximumSize(btn_size);
		jButton1.setMinimumSize(btn_size);
		jButton1.setPreferredSize(btn_size);
		jButton1.setToolTipText(LangModelAnalyse.getString("analysisStart"));
		jButton1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif")));
		jButton1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton1_actionPerformed(e);
			}
		});

		jButton2.setMaximumSize(btn_size);
		jButton2.setMinimumSize(btn_size);
		jButton2.setPreferredSize(btn_size);
		jButton2.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		jButton2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cs_initial.gif")));
		jButton2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});


		jButton3.setMaximumSize(btn_size);
		jButton3.setMinimumSize(btn_size);
		jButton3.setPreferredSize(btn_size);
		jButton3.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		jButton3.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cs_default.gif")));
		jButton3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton3_actionPerformed(e);
			}
		});

		this.setContentPane(mainPanel);

//		jToolBar1.setBorderPainted(true);
		jToolBar1.setFloatable(false);
		jToolBar1.setLayout(new XYLayout());
		jToolBar1.add(jButton1, new XYConstraints(0, 0, -1, -1));
		jToolBar1.add(jButton2, new XYConstraints(btn_size.width*2, 0, -1, -1));
		jToolBar1.add(jButton3, new XYConstraints(btn_size.width*3, 0, -1, -1));

		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(jTable);
		scrollPane.setAutoscrolls(true);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(jToolBar1,  BorderLayout.NORTH);

		updColorModel();
	}

	public Object getDoubleValueAt(Object obj, int row)
	{
		return obj;
		/*if (row < 5)
			return obj;
		if (row == 5)
			return ((JComboBox)obj).getSelectedItem();
		if (row == 6)
			return new Double(((JComboBox)obj).getSelectedIndex()-1);
		return ((JComboBox)obj).getSelectedItem();*/
	 }

	void jButton1_actionPerformed(ActionEvent e)
	{
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		double[] minuitParams = new double[8];
		minuitParams[2] = ((Double)getDoubleValueAt(jTable.getValueAt(0, 1), 0)).doubleValue();
		minuitParams[1] = ((Double)getDoubleValueAt(jTable.getValueAt(1, 1), 1)).doubleValue();
		minuitParams[3] = ((Double)getDoubleValueAt(jTable.getValueAt(2, 1), 2)).doubleValue();
		minuitParams[0] = ((Double)getDoubleValueAt(jTable.getValueAt(3, 1), 3)).doubleValue();
		minuitParams[4] = ((Double)getDoubleValueAt(jTable.getValueAt(4, 1), 4)).doubleValue();
		minuitParams[5] = ((Double)getDoubleValueAt(jTable.getValueAt(5, 1), 5)).doubleValue();
		minuitParams[6] = ((Double)getDoubleValueAt(jTable.getValueAt(6, 1), 6)).doubleValue();
		minuitParams[7] = ((Integer)getDoubleValueAt(jTable.getValueAt(7, 1), 7)).doubleValue();

		Pool.put("analysisparameters", "minuitanalysis", minuitParams);
		new MinuitAnalyseCommand(dispatcher, "primarytrace", aContext).execute();
		dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
		dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	void jButton2_actionPerformed(ActionEvent e)
	{
		double[] defaults = (double[])Pool.get("analysisparameters", "minuitinitials");
		setDefaults(defaults);
	}

	void jButton3_actionPerformed(ActionEvent e)
	{
		double[] defaults = (double[])Pool.get("analysisparameters", "minuitdefaults");
		setDefaults(defaults);
	}
}


class ParamTableModel extends AbstractTableModel
 {

	 public static final Double[] ff =
	 {
		 new Double(0.0), new Double(0.05), new Double(0.1), new Double(0.15),
		 new Double(0.2), new Double(0.25), new Double(0.3), new Double(0.35),
		 new Double(0.4), new Double(0.45), new Double(0.5)
	 };

	 public static final String[] strategy =
	 {
		 LangModelAnalyse.getString("strategy-1"),
		 LangModelAnalyse.getString("strategy0"),
		 LangModelAnalyse.getString("strategy1"),
		 LangModelAnalyse.getString("strategy2"),
		 LangModelAnalyse.getString("strategy3"),
		 LangModelAnalyse.getString("strategy4")
	 };

	 public static final Integer[] tactics =
	 {
		 new Integer(0), new Integer(1), new Integer(2), new Integer(3),
		 new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8)
	 };

	 AComboBox ffComboBox = new AComboBox(AComboBox.SMALL_FONT);
	 AComboBox strComboBox = new AComboBox(AComboBox.SMALL_FONT);
	 AComboBox tactComboBox = new AComboBox(AComboBox.SMALL_FONT);

	 String[] columnNames = {"", "" };

	 Object[][] data =
	 {
		 { LangModelAnalyse.getString("analysisMinConnector"), new Double(0) },
		 { LangModelAnalyse.getString("analysisMinWeld"), new Double(0) },
		 { LangModelAnalyse.getString("analysisMinEnd"), new Double(0) },
		 { LangModelAnalyse.getString("analysisMinEvent"), new Double(0) },
		 { LangModelAnalyse.getString("analysisNSigma"), new Double(0) },
		 { LangModelAnalyse.getString("analysisFormFactor"), ffComboBox },
		 { LangModelAnalyse.getString("analysisStrategy"), strComboBox },
		 { LangModelAnalyse.getString("analysisWavelet"), tactComboBox }
	 };

	 ParamTableModel()
	 {
		 for(int i=0; i<ff.length; i++)
			 ffComboBox.addItem(ff[i]);

		 for(int i=0; i<strategy.length; i++)
			 strComboBox.addItem(strategy[i]);

		 for(int i=0; i<tactics.length; i++)
			 tactComboBox.addItem(tactics[i]);
	 }

	 void updateData(Object[] d)
	 {
		 for (int i = 0; i < d.length; i++)
		 {
			 if (i < 5)
				 data[i][1] = d[i];
			 else if (i == 5)
				 ffComboBox.setSelectedItem(d[i]);
			 else if (i == 6)
				 strComboBox.setSelectedItem(strategy[((Integer)d[i]).intValue()+1]);
			 else if (i == 7)
				 tactComboBox.setSelectedItem(d[i]);

		 }
		 super.fireTableDataChanged();
	 }

	 public void clearTable()
	 {
		 data = new Object[][]{};
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
			 return getDoubleValueAt(row, col);//data[row][col];
	 }

	 public Object getDoubleValueAt(int row, int col)
	 {
		 if (col < 1)
			 return data[row][col];
		 if (row < 5)
			 return data[row][col];
		 if (row == 5)
			 return ffComboBox.getSelectedItem();
		 if (row == 6)
			 return new Double(strComboBox.getSelectedIndex()-1);
		 return tactComboBox.getSelectedItem();
	 }

	 public Class getColumnClass(int p_col)
	 {
		 return Object.class;
	 }

	 public boolean isCellEditable(int row, int col)
	 {
		 if (col < 1)
			 return false;
		 else
			 return true;
	 }

	 public void setValueAt(Object value, int row, int col)
	 {
		 data[row][col] = value;
		 fireTableCellUpdated(row, col);
	 }

	 public void setValueAt(double value, int row, int col)
	 {
		 data[row][col] = new Double(value);
		 fireTableCellUpdated(row, col);
	 }
 }

 class ModelParamsTableEditor extends DefaultCellEditor
 {
	 Object editor;
	 ParamTableModel model;

	 public ModelParamsTableEditor(ParamTableModel model)
	 {
		 super(new JTextField());
		 this.model = model;
		 setClickCountToStart(1);
	 }

	 public Component getTableCellEditorComponent(JTable table, Object value,
						 boolean isSelected,
						 int row, int column)
	 {
		 editor = value;
		 if(row == 5 && column == 1)
		 {
			 model.ffComboBox.setBackground(SystemColor.window);
			 return (Component)model.ffComboBox;
		 }
		 if(row == 6 && column == 1)
		 {
			 model.strComboBox.setBackground(SystemColor.window);
			 return (Component)model.strComboBox;
		 }
		 if(row == 7 && column == 1)
		 {
			 model.tactComboBox.setBackground(SystemColor.window);
			 return (Component)model.tactComboBox;
		 }
		 return super.getTableCellEditorComponent (table, value, isSelected, row,  column);
		}

	 public Object getCellEditorValue()
	 {
		 if(editor instanceof JComboBox)
			 return editor;
		 Object obj = super.getCellEditorValue();
		 if (obj instanceof String)
		 {
			 String str = (String)obj;
			 while (str.length() > 0)
			 {
				 try
				 {
					 return Double.valueOf(str);
				 }
				 catch (NumberFormatException ex)
				 {
					 str = str.substring(0, str.length() - 1);
				 }
			 }
			 return new Double (0);
		 }
		 else
			 return obj;
	 }
}

class ModelParamsTableRenderer extends DefaultTableCellRenderer
{
	ParamTableModel model;
	public ModelParamsTableRenderer(ParamTableModel model)
	{
		this.model = model;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		if(column == 1 && row == 5)
		{
			//Component c = (Component)value;
			return model.ffComboBox;
		}
		if(column == 1 && row == 6)
			return model.strComboBox;
		if(column == 1 && row == 7)
			return model.tactComboBox;

		return  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}