package com.syrus.AMFICOM.Client.Model;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ATableFrame;
import com.syrus.AMFICOM.Client.Model.ModelMath.*;
import com.syrus.io.BellcoreStructure;

public class RefModelParamsFrame extends ATableFrame
{
	ArrayList reflectoElements = new ArrayList();
	ApplicationContext aContext;

	ObjectResourceComboBox pathIDsComboBox = new ObjectResourceComboBox();
	ATable table;
	ParamTableModel tableModel;

	Scheme scheme;
	SchemePath path;

//---------------------------Constructor---------------------------------------
	public RefModelParamsFrame(ApplicationContext aContext)
	{
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


//-----------------------------------------------------------------------------
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


//-----------------------------------------------------------------------------
	public void doIt()
	{
		path = (SchemePath)pathIDsComboBox.getSelectedObjectResource();
		if (path == null || path.getId().equals(""))
		{
			String error = "Не задан маршрут моделирования.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = getTrace();
		if(bs == null)
			return;
		bs.title = "Модель \"" + path.getName() + "\"";
		bs.monitored_element_id = path.getId();
		Pool.put("bellcorestructure", "primarytrace", bs);
		aContext.getDispatcher().notify(new RefChangeEvent("all", RefChangeEvent.CLOSE_EVENT));
		aContext.getDispatcher().notify(new RefChangeEvent("primarytrace", RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}

//-----------------------------------------------------------------------------
	void setDefaults() // Default parameters of the modeling
	{
		tableModel.updateData(new Object[]{
			new Integer(64),
			new Double(4),
			new Integer(1000),
			new Integer(1625),
			new Double(40),
			new Double(0.0),
			new Double(0.95),
			new Double(0.1),
			new Double(-40),
			new Double(0.5),
			new Double(0.2)});
	}

	public void setModelingScheme(Scheme scheme)
	{
		this.scheme = scheme;
		pathIDsComboBox.removeAllItems();
		for(Iterator it = scheme.getTopLevelPaths().iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath)it.next();
			pathIDsComboBox.addItem(path);
		}
	}

//-----------------------------------------------------------------------------
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
			String error = "Неправильно заданы либо отсутствуют данные в схеме.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		ModelingEvent []rmip = (ModelingEvent [])
				reflectoElements.toArray(new ModelingEvent[reflectoElements.size()]);
		if(rmip.length<2)
		{
			String error = "Ошибка при определении параметров маршрута.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
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


//-----------------------------------------------------------------------------
	BellcoreStructure createBS (double[] y, double delta_x, double wl, double pulseWidth)
	{
		BellcoreStructure bs = new BellcoreStructure();
		int size = y.length;
		int averages = 1;
		short wavelength = (short)wl;
		int actualwavelength = 10 * wavelength;
		double groupindex = 1.46800;
		double pulsewidth = pulseWidth;
		double resolution = delta_x;
		long datetime = System.currentTimeMillis();
		String omid = "AMFICOM generated";

		bs.addField(BellcoreStructure.GENPARAMS);
		bs.genParams.NW = wavelength;

		bs.addField(BellcoreStructure.SUPPARAMS);
		bs.supParams.OMID = omid;
		bs.supParams.OT = path.getId();

		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.DTS = datetime / 1000;
		bs.fxdParams.UD = "mt";
		bs.fxdParams.AW = (short)(actualwavelength);
		bs.fxdParams.TPW = 1;
		bs.fxdParams.PWU = new short[1];
		bs.fxdParams.DS = new int [1];
		bs.fxdParams.NPPW = new int [1];
		bs.fxdParams.PWU[0] = (short)pulsewidth;
		bs.fxdParams.DS[0] = (int)(resolution * groupindex / 3d * 100d * 10000d * 1000d);
		bs.fxdParams.NPPW[0] = size;
		bs.fxdParams.GI = (int)(groupindex * 100000);
		bs.fxdParams.NAV = averages;
		bs.fxdParams.AR = (int)(resolution * size * groupindex / 3d * 100d * 1000d);

		bs.addField(BellcoreStructure.DATAPOINTS);
		bs.dataPts.TNDP = size;
		bs.dataPts.TSF = 1;
		bs.dataPts.TPS = new int [1];
		bs.dataPts.SF = new short [1];
		bs.dataPts.TPS[0] = size;
		bs.dataPts.SF[0] = 1000;
		bs.dataPts.DSF = new int[1][size];
		for (int i = 0; i < size; i++)
			bs.dataPts.DSF[0][i] = 65535 - (int)(y[i]*1000);

		bs.addField(BellcoreStructure.CKSUM);
		bs.cksum.CSM = 0;

		bs.addField(BellcoreStructure.MAP);
		bs.map.MRN = 100;
		bs.map.NB = 6;

		bs.map.B_id = new String[6];
		bs.map.B_rev = new int[6];
		bs.map.B_size = new int[6];
		bs.map.B_id[0] = "Map";
		bs.map.B_id[1] = "GenParams";
		bs.map.B_id[2] = "SupParams";
		bs.map.B_id[3] = "FxdParams";
		bs.map.B_id[4] = "DataPts";
		bs.map.B_id[5] = "Cksum";

		for (int i = 1; i < 6; i++)
			bs.map.B_rev[i] = 100;

		bs.map.B_size[1] = bs.genParams.getSize();
		bs.map.B_size[2] = bs.supParams.getSize();
		bs.map.B_size[3] = bs.fxdParams.getSize();
		bs.map.B_size[4] = bs.dataPts.getSize();
		bs.map.B_size[5] = bs.cksum.getSize();
		bs.map.MBS = bs.map.getSize();

		return bs;
	}



///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
////////////     Additional Necessary Class Definition     ////////////////////
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//-----------------------------------------------------------------------------
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
		{"Длина трассы, км", lengthComboBox},
		{"Разрешение, м", resolutionComboBox},
		{"Длительность импульса, нс", pulsWidthComboBox},
		{"Длина волны, нм", waveLengthComboBox},
		{"Динамический диапазон, дБ", new Double(40)},
		{"Добавочный шум, дБ", new Double(0)},
		{"Форм-фактор коннектора", new Double(0.95)},
		{"Потери на сварке, дБ", new Double(0.1)},
		{"Отражение на коннекторе, дБ", new Double(-40)},
		{"Потери на коннекторе, дБ", new Double(0.5)},
		{"Затухание на линейном уч-ке, дБ/км", new Double(0.2)}
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