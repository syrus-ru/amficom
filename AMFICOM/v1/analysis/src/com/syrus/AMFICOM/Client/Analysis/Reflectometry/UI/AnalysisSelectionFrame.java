package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.Analysis.MinuitAnalyseCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.client_.general.ui_.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.BellcoreStructure;

public class AnalysisSelectionFrame extends ATableFrame
																		implements OperationListener
{
	
	static final Double[] ff =
	 {
		 new Double(0.0), new Double(0.05), new Double(0.1), new Double(0.15),
		 new Double(0.2), new Double(0.25), new Double(0.3), new Double(0.35),
		 new Double(0.4), new Double(0.45), new Double(0.5)
	 };

	 static final String[] strategy =
	 {
		 LangModelAnalyse.getString("strategy-1"),
		 LangModelAnalyse.getString("strategy0"),
		 LangModelAnalyse.getString("strategy1"),
		 LangModelAnalyse.getString("strategy2"),
		 LangModelAnalyse.getString("strategy3"),
		 LangModelAnalyse.getString("strategy4")
	 };

	 static final Integer[] tactics =
	 {
		 new Integer(0), new Integer(1), new Integer(2), new Integer(3),
		 new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8)
	 };
	 
	private Dispatcher dispatcher;
	private ParamTableModel tModelMinuit;
	private ATable jTable;
	private static final String OT_analysisparameters = "analysisparameters";
	private static final String OID_minuitanalysis = "minuitanalysis";
	private static final String OID_minuitinitials = "minuitinitials";
	private static final String OID_minuitdefaults = "minuitdefaults";

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	ApplicationContext aContext;
	
	private Object selectedEventId;
	
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
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		String actionCommand = ae.getActionCommand();
		if(actionCommand.equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
				{
					BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
					if (bs.measurementId == null)
						setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (" + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) + ')');
					else
					{
//						try
//						{
//							Measurement m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
//												 new Identifier(bs.measurementId), true);
							MeasurementSetup ms = Heap.getContextMeasurementSetup();
							setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " ("
								+ (ms == null ? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) : 
									LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN) + ':' + ms.getDescription()) + ')');
//						}
//						catch(ApplicationException ex)
//						{
//							System.err.println("Exception retrieving measurenent with " + bs.measurementId);
//							ex.printStackTrace();
//							return;
//						}
					}

					double[] minuitParams = Heap.getMinuitAnalysisParams();
					setDefaults(minuitParams);
					setVisible(true);
				}
			}
			if(rce.THRESHOLDS_CALC)
			{
				String id = (String)(rce.getSource());

				BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
				if (bs.measurementId == null)
					setTitle(LangModelAnalyse.getString("analysisSelectionTitle") + " (" + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) + ')');
				else
				{
//					try
//					{
//						Measurement m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
//											new Identifier(bs.measurementId), true);

//						MeasurementSetup ms = m.getSetup();
						MeasurementSetup ms = Heap.getContextMeasurementSetup();
						setTitle(LangModelAnalyse.getString("analysisSelectionTitle")  + " ("
							+ (ms == null ? LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NO_PATTERN) : 
								LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_PATTERN) + ':' + ms.getDescription()) + ')');

						if (ms.getCriteriaSet() != null)
						{
							double[] minuitParams = Heap.getMinuitAnalysisParams();
							setDefaults(minuitParams);
						}
//					}
//					catch(ApplicationException ex)
//					{
//						System.err.println("Measurement not found. id = " + bs.measurementId);
//						ex.printStackTrace();
//						return;
//					}
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
		} else if (actionCommand.equals(RefUpdateEvent.typ)) {
			RefUpdateEvent refUpdateEvent = (RefUpdateEvent)ae;
			if (refUpdateEvent.eventSelected()) {
				this.selectedEventId = refUpdateEvent.getSource();
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
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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

		JButton analysisStartButton = new JButton();
		JButton analysisInitialButton = new JButton();
		JButton analysisDefaultsButton = new JButton();

		analysisStartButton.setMaximumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisStartButton.setMinimumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisStartButton.setPreferredSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisStartButton.setToolTipText(LangModelAnalyse.getString("analysisStart"));
		analysisStartButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS));
		analysisStartButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisStartButton_actionPerformed(e);
			}
		});

		analysisInitialButton.setMaximumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisInitialButton.setMinimumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisInitialButton.setPreferredSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisInitialButton.setToolTipText(LangModelAnalyse.getString("analysisInitial"));
		analysisInitialButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS));
		analysisInitialButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisInitialButton_actionPerformed(e);
			}
		});


		analysisDefaultsButton.setMaximumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisDefaultsButton.setMinimumSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisDefaultsButton.setPreferredSize(UIManager.getDimension(ResourceKeys.SIZE_BUTTON));
		analysisDefaultsButton.setToolTipText(LangModelAnalyse.getString("analysisDefaults"));
		analysisDefaultsButton.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS));
		analysisDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				analysisDefaultsButton_actionPerformed(e);
			}
		});

		this.setContentPane(mainPanel);

		JToolBar jToolBar1 = new JToolBar();
		jToolBar1.setFloatable(false);
		jToolBar1.add(analysisStartButton);
		jToolBar1.add(Box.createRigidArea(UIManager.getDimension(ResourceKeys.SIZE_BUTTON)));
		jToolBar1.add(analysisInitialButton);
		jToolBar1.add(analysisDefaultsButton);

		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewport(viewport);
		scrollPane.getViewport().add(jTable);
		scrollPane.setAutoscrolls(true);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(jToolBar1,  BorderLayout.NORTH);

		this.updColorModel();
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

	void analysisStartButton_actionPerformed(ActionEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		double[] minuitParams = new double[8];
		minuitParams[2] = ((Double)getDoubleValueAt(jTable.getValueAt(0, 1), 0)).doubleValue();
		minuitParams[1] = ((Double)getDoubleValueAt(jTable.getValueAt(1, 1), 1)).doubleValue();
		minuitParams[3] = ((Double)getDoubleValueAt(jTable.getValueAt(2, 1), 2)).doubleValue();
		minuitParams[0] = ((Double)getDoubleValueAt(jTable.getValueAt(3, 1), 3)).doubleValue();
		minuitParams[4] = ((Double)getDoubleValueAt(jTable.getValueAt(4, 1), 4)).doubleValue();
		minuitParams[5] = ((Double)getDoubleValueAt(jTable.getValueAt(5, 1), 5)).doubleValue();
		minuitParams[6] = ((Double)getDoubleValueAt(jTable.getValueAt(6, 1), 6)).doubleValue();
		minuitParams[7] = ((Integer)getDoubleValueAt(jTable.getValueAt(7, 1), 7)).doubleValue();

		Heap.setMinuitAnalysisParams(minuitParams);
		new MinuitAnalyseCommand(dispatcher, aContext).execute();
		dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE, RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
		dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE, RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		if (this.selectedEventId != null) {
			dispatcher.notify(new RefUpdateEvent(this.selectedEventId, RefUpdateEvent.EVENT_SELECTED_EVENT));
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	void analysisInitialButton_actionPerformed(ActionEvent e)
	{
		double[] defaults = Heap.getMinuitInitialParams();
		setDefaults(defaults);
	}

	void analysisDefaultsButton_actionPerformed(ActionEvent e)
	{
		double[] defaults = Heap.getMinuitDefaultParams();
		setDefaults(defaults);
	}


	private class ParamTableModel extends AbstractTableModel
 {	

	 AComboBox ffComboBox = new AComboBox(AComboBox.SMALL_FONT);
	 AComboBox strComboBox = new AComboBox(AComboBox.SMALL_FONT);
	 AComboBox tactComboBox = new AComboBox(AComboBox.SMALL_FONT);

	 String[] columnNames = {"", "" };

	 Object[][] data =
	 {
		 { LangModelAnalyse.getString("analysisMinConnector"), new Double(0) },
		 { LangModelAnalyse.getString("analysisMinWeld"), new Double(0) },
		 { LangModelAnalyse.getString("analysisMinEnd"), new Double(0) }, // @todo: remove
		 { LangModelAnalyse.getString("analysisMinEvent"), new Double(0) },
		 { LangModelAnalyse.getString("analysisNSigma"), new Double(0) }, // @todo: remove
		 { LangModelAnalyse.getString("analysisFormFactor"), ffComboBox }, // @todo: remove
		 { LangModelAnalyse.getString("analysisStrategy"), strComboBox }, // @todo: remove
		 { LangModelAnalyse.getString("analysisWavelet"), tactComboBox } // @todo: remove
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

 private class ModelParamsTableEditor extends DefaultCellEditor
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
			 return model.ffComboBox;
		 }
		 if(row == 6 && column == 1)
		 {
			 model.strComboBox.setBackground(SystemColor.window);
			 return model.strComboBox;
		 }
		 if(row == 7 && column == 1)
		 {
			 model.tactComboBox.setBackground(SystemColor.window);
			 return model.tactComboBox;
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

private class ModelParamsTableRenderer extends ADefaultTableCellRenderer
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

}