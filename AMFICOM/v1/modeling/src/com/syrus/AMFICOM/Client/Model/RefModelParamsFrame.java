package com.syrus.AMFICOM.Client.Model;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ATableFrame;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Model.ModelMath.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.io.*;

public class RefModelParamsFrame extends ATableFrame
{
	ArrayList reflectoElements = new ArrayList();
	ApplicationContext aContext;

	ObjectResourceComboBox pathIDsComboBox = new ObjectResourceComboBox();
	ATable table;
	ParamTableModel tableModel;

	SchemePath path;

	public RefModelParamsFrame(ApplicationContext aContext)
	{
		this.aContext = aContext;

		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setTitle(LangModelModel.getString("ParamsTitle"));
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setIconifiable(true);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout());

		tableModel = new ParamTableModel();
		table = new ATable(tableModel);
		table.setDefaultRenderer(Object.class, new ModelParamsTableRenderer(tableModel));
		table.setDefaultEditor(Object.class, new ModelParamsTableEditor(tableModel));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.getViewport().add(table);

		JPanel basePanel = new JPanel(new BorderLayout());
		basePanel.add(pathIDsComboBox, BorderLayout.NORTH);
		basePanel.add(jScrollPane1, BorderLayout.CENTER);
		getContentPane().add(basePanel, BorderLayout.CENTER);

		jScrollPane1.getViewport().setBackground(SystemColor.window);

		setDefaults();

		pathIDsComboBox.setFontSize(AComboBox.SMALL_FONT);
	}

	public String getReportTitle()
	{
		return LangModelModel.getString("ParamsTitle");
	}

	public TableModel getTableModel()
	{
		return tableModel;
	}

	public void doIt()
	{
		path = (SchemePath)pathIDsComboBox.getSelectedObjectResource();
		if (path == null) {
			String error = "�� ����� ������� �������������.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "������",
																		JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = getTrace();
		if (bs == null)
			return;
		bs.title = "������ \"" + path.name() + "\"";
		bs.schemePathId = path.getId().getIdentifierString();
		Pool.put("bellcorestructure", "primarytrace", bs);
		aContext.getDispatcher().notify(new RefChangeEvent("all", RefChangeEvent.CLOSE_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace", RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}

	void setDefaults() // Default parameters of the modeling
	{
		tableModel.updateData(new Object[] {
													new Integer(64),
													new Double(4),
													new Integer(1000),
													new Integer(1625),
													new Double(40),
													new Double(0.0),
													new Double(0.95),
													new Double(0.1),
													new Double( -40),
													new Double(0.5),
													new Double(0.2)});
	}

	public void setModelingSchemes(List schemes)
	{
		pathIDsComboBox.removeAllItems();
		for (Iterator sit = schemes.iterator(); sit.hasNext(); ) {
			Scheme scheme = (Scheme)sit.next();
			SchemePath[] paths = scheme.schemeMonitoringSolution().schemePaths();
			for (int i = 0;  i < paths.length; i++) {
				pathIDsComboBox.addItem(paths[i]);
			}
		}
	}

	public void setModelingScheme(Scheme scheme)
	{
		pathIDsComboBox.removeAllItems();
		SchemePath[] paths = scheme.schemeMonitoringSolution().schemePaths();
		for (int i = 0; i < paths.length; i++) {
			pathIDsComboBox.addItem(paths[i]);
		}
	}

	BellcoreStructure getTrace()
	{
		double length = ((Integer)tableModel.getValueAt(0, 1)).doubleValue() * 1000;
		double resolution = ((Double)tableModel.getValueAt(1, 1)).doubleValue();
		double pulsWidth = ((Integer)tableModel.getValueAt(2, 1)).doubleValue();
		double wave_length = ((Integer)tableModel.getValueAt(3, 1)).doubleValue();
		double dinam_area = ((Double)tableModel.getValueAt(4, 1)).doubleValue();
		double addNoise = Math.abs(((Double)tableModel.getValueAt(5,1)).doubleValue());
		double formFactor = ((Double)tableModel.getValueAt(6, 1)).doubleValue();
		double weldAtt = ((Double)tableModel.getValueAt(7, 1)).doubleValue();
		double connectorRef = ((Double)tableModel.getValueAt(8, 1)).doubleValue();
		double connectorAtt = ((Double)tableModel.getValueAt(9, 1)).doubleValue();
		double linearAtt = ((Double)tableModel.getValueAt(10, 1)).doubleValue();

		reflectoElements = new ModelGenerator(path, (int)wave_length, connectorAtt, weldAtt, connectorRef, linearAtt).createModelingEvents();
		//mode((int)wave_length, (int)pulsWidth, formFactor, resolution, connectorAtt, weldAtt, connectorRef, linearAtt);
		if(reflectoElements == null)
		{
			String error = "����������� ������ ���� ����������� ������ � �����.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "������", JOptionPane.OK_OPTION);
			return null;
		}
		ModelingEvent []rmip = (ModelingEvent [])
				reflectoElements.toArray(new ModelingEvent[reflectoElements.size()]);
		if(rmip.length<2)
		{
			String error = "������ ��� ����������� ���������� ��������.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "������", JOptionPane.OK_OPTION);
			return null;
		}

		NewModelGenerator mg = new NewModelGenerator(rmip,
																					 resolution,
																					 dinam_area,
																					 pulsWidth,
																					 length,
																					 addNoise,
																					 formFactor);
		double []y = mg.getModelArray();
		//double[] y = ReflectogramMath.getReflectogrammFromEvents(re, 0);

		return createBS (y, resolution/1000., wave_length, pulsWidth);
	}

	BellcoreStructure createBS (double[] y, double delta_x, double wl, double pulseWidth)
	{
		BellcoreStructure bs = new BellcoreStructure();

		double groupindex = 1.46800;
		double pulsewidth = pulseWidth;
		double resolution = delta_x;

		BellcoreModelWriter writer = new BellcoreModelWriter(bs);
		writer.setWavelength((int)wl);
		writer.setTime(System.currentTimeMillis() / 1000);
		writer.setOpticalModule("AMFICOM generated");
		writer.setPathId(path.getId().getIdentifierString());
		writer.setUnits("mt");
		writer.setAverages(1);
		writer.setRangeParameters(1.46800, delta_x, delta_x * y.length);
		writer.setData(y);
		writer.finalizeChanges();


		return bs;
	}

////////////     Additional Necessary Class Definition     ////////////////////
}

class ParamTableModel extends AbstractTableModel
{
	public static final Integer[] length = {
		new Integer(4), new Integer(8), new Integer(16), new Integer(32),
		new Integer(64), new Integer(128), new Integer(256)};

	public static final Double[] resolution = {
		new Double(0.25), new Double(0.5), new Double(1.), new Double(2.),
		new Double(4.), new Double(8.), new Double(16.)};

	public static final Integer[] pulsWidth = {
		new Integer(100), new Integer(200), new Integer(500), new Integer(1000),
		new Integer(5000), new Integer(10000)};

	public static final Integer[] waveLength = {
		new Integer(850), new Integer(1310), new Integer(1550), new Integer(1625)};

	AComboBox lengthComboBox = new AComboBox(AComboBox.SMALL_FONT);
	AComboBox resolutionComboBox = new AComboBox(AComboBox.SMALL_FONT);
	AComboBox pulsWidthComboBox = new AComboBox(AComboBox.SMALL_FONT);
	AComboBox waveLengthComboBox = new AComboBox(AComboBox.SMALL_FONT);

	String[] columnNames = {"", ""};

	Object[][] data =
	{
		{"����� ������, ��", lengthComboBox},
		{"����������, �", resolutionComboBox},
		{"������������ ��������, ��", pulsWidthComboBox},
		{"����� �����, ��", waveLengthComboBox},
		{"������������ ��������, ��", new Double(40)},
		{"���������� ���, ��", new Double(0)},
		{"����-������ ����������", new Double(0.95)},
		{"������ �� ������, ��", new Double(0.1)},
		{"��������� �� ����������, ��", new Double(-40)},
		{"������ �� ����������, ��", new Double(0.5)},
		{"��������� �� �������� ��-��, ��/��", new Double(0.2)}
	};

	ParamTableModel()
	{
		lengthComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					int length = ((Integer)e.getItem()).intValue();
					Double _res = (Double)resolutionComboBox.getSelectedItem();
					Integer _pw = (Integer)pulsWidthComboBox.getSelectedItem();
					resolutionComboBox.removeAllItems();
					pulsWidthComboBox.removeAllItems();
					switch (length)
					{
						case 4:
						case 8:
							for(int i = Math.min(0, resolution.length); i < Math.min(3, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(0, pulsWidth.length); i < Math.min(3, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
						case 16:
							for(int i = Math.min(1, resolution.length); i < Math.min(4, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(0, pulsWidth.length); i < Math.min(3, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
						case 32:
							for(int i = Math.min(2, resolution.length); i < Math.min(5, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(1, pulsWidth.length); i < Math.min(4, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
						case 64:
							for(int i = Math.min(3, resolution.length); i < Math.min(6, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(1, pulsWidth.length); i < Math.min(4, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
						case 128:
							for(int i = Math.min(4, resolution.length); i < Math.min(7, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(2, pulsWidth.length); i < Math.min(5, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
						case 256:
							for(int i = Math.min(5, resolution.length); i < Math.min(7, resolution.length); i++)
								resolutionComboBox.addItem(resolution[i]);
							for(int i = Math.min(3, pulsWidth.length); i < Math.min(6, pulsWidth.length); i++)
								pulsWidthComboBox.addItem(pulsWidth[i]);
							break;
					}
					resolutionComboBox.setSelectedItem(_res);
					pulsWidthComboBox.setSelectedItem(_pw);
				}
			}
		});

		for(int i = 0; i < length.length; i++)
			lengthComboBox.addItem(length[i]);
		for(int i = 0; i < waveLength.length; i++)
			waveLengthComboBox.addItem(waveLength[i]);
//		for(int i = 0; i < resolution.length; i++)
//			resolutionComboBox.addItem(resolution[i]);
//		for(int i = 0; i < pulsWidth.length; i++)
//			pulsWidthComboBox.addItem(pulsWidth[i]);

	}

	void updateData(Object[] d)
	{
		for (int i = 0; i < d.length; i++)
		{
			if (i == 0)
				lengthComboBox.setSelectedItem(d[i]);
			else if (i == 1)
				resolutionComboBox.setSelectedItem(d[i]);
			else if (i == 2)
				pulsWidthComboBox.setSelectedItem(d[i]);
			else if (i == 3)
				waveLengthComboBox.setSelectedItem(d[i]);
			else
				data[i][1] = d[i];
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
		return getDoubleValueAt(row, col);
	}

	public Object getDoubleValueAt(int row, int col)
	{
		if (col == 1)
		{
			if (row == 0)
				return lengthComboBox.getSelectedItem();
			if (row == 1)
				return resolutionComboBox.getSelectedItem();
			if (row == 2)
				return pulsWidthComboBox.getSelectedItem();
			if (row == 3)
				return waveLengthComboBox.getSelectedItem();
		}
		return data[row][col];
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
		if (column == 1)
		{
			if(row == 0)
			{
				model.lengthComboBox.setBackground(SystemColor.window);
				return (Component)model.lengthComboBox;
			}
			if(row == 1)
			{
				model.resolutionComboBox.setBackground(SystemColor.window);
				return (Component)model.resolutionComboBox;
			}
			if(row == 2)
			{
				model.pulsWidthComboBox.setBackground(SystemColor.window);
				return (Component)model.pulsWidthComboBox;
			}
			if(row == 3)
			{
				model.waveLengthComboBox.setBackground(SystemColor.window);
				return (Component)model.waveLengthComboBox;
			}
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
		if(column == 1 && row == 0)
			return model.lengthComboBox;
		if(column == 1 && row == 1)
			return model.resolutionComboBox;
		if(column == 1 && row == 2)
			return model.pulsWidthComboBox;
		if(column == 1 && row == 3)
			return model.waveLengthComboBox;
		return  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}