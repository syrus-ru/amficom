package com.syrus.AMFICOM.Client.Model;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ReportTable;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.Model.ModelMath.ModelGenerator;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.modelling.ModelEvent;
import com.syrus.AMFICOM.modelling.TraceGenerator;
import com.syrus.AMFICOM.modelling.TraceGenerator.Parameters;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePathWrapper;
import com.syrus.io.BellcoreModelWriter;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class RefModelParamsFrame extends JInternalFrame 
		implements PropertyChangeListener, ReportTable {
	
	private static final long serialVersionUID = 5115725103762876658L;

	public static final String NAME = "RefModelParamsFrame";
	
	ApplicationContext aContext;

	WrapperedComboBox<SchemePath> pathComboBox = new WrapperedComboBox<SchemePath>(
			SchemePathWrapper.getInstance(), 
			StorableObjectWrapper.COLUMN_NAME, 
			StorableObjectWrapper.COLUMN_ID);
	
	ATable table;
	ParamTableModel tableModel;

	public RefModelParamsFrame(ApplicationContext aContext) {
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
	}

	private void jbInit() throws Exception {
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setTitle(LangModelModel.getString("ParamsTitle"));
		setClosable(true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setIconifiable(true);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout());

		this.tableModel = new ParamTableModel();
		this.table = new ATable(this.tableModel);
		this.table.setDefaultRenderer(Object.class, new ModelParamsTableRenderer(this.tableModel));
		this.table.setDefaultEditor(Object.class, new ModelParamsTableEditor(this.tableModel));
		this.table.getColumnModel().getColumn(0).setPreferredWidth(150);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(50);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.getViewport().add(this.table);

		JButton doItButton = new JButton();
		doItButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doIt();
			}
		});
		doItButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		doItButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		doItButton.setToolTipText(LangModelModel.getString("menuViewPerformModeling"));
		
		JPanel north = new JPanel(new BorderLayout());
		north.add(this.pathComboBox, BorderLayout.CENTER);
		north.add(doItButton, BorderLayout.EAST);
		JPanel basePanel = new JPanel(new BorderLayout());
		basePanel.add(north, BorderLayout.NORTH);
		basePanel.add(jScrollPane1, BorderLayout.CENTER);
		getContentPane().add(basePanel, BorderLayout.CENTER);

		jScrollPane1.getViewport().setBackground(SystemColor.window);
		setDefaults();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent se = (SchemeEvent)evt;
			if (se.isType(SchemeEvent.OPEN_SCHEME)) {
				try {
					Scheme scheme = (Scheme)se.getStorableObject();
					if (scheme.getParentSchemeElement() != null || 
							!scheme.getSchemePathsRecursively(false).isEmpty()) {
						setModelingScheme(scheme);
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}

	public String getReportTitle() {
		return LangModelModel.getString("ParamsTitle");
	}

	public TableModel getTableModel() {
		return this.tableModel;
	}

	public void doIt() {
		SchemePath path = (SchemePath)this.pathComboBox.getSelectedItem();
		if (path == null) {
			String error = "Не задан маршрут моделирования.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = getTrace(path);
		if (bs == null)
			return;
		bs.title = "Модель \"" + path.getName() + "\"";
		bs.schemePathId = path.getId().getIdentifierString();
		
		Trace tr = new Trace(bs, Heap.MODELED_TRACE_KEY, Heap.getMinuitAnalysisParams());
		Heap.openPrimaryTrace(tr);
		Heap.setRefAnalysisPrimary(new RefAnalysis(tr.getPFTrace()));
		Heap.primaryTraceOpened();
//		Heap.sPRIMARY_TRACE_KEY
	}

	void setDefaults() // Default parameters of the modeling
	{
		this.tableModel.updateData(new Object[] {
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

	public void setModelingSchemes(Collection<Scheme> schemes) {
		this.pathComboBox.removeAllItems();
		try {
			for (Scheme scheme : schemes) {
				for (SchemePath path : scheme.getSchemePathsRecursively(false)) {
					this.pathComboBox.addItem(path);
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	public void setModelingScheme(Scheme scheme) {
		this.pathComboBox.removeAllItems();
		try {
			for (SchemePath path : scheme.getSchemePathsRecursively(false)) {
				this.pathComboBox.addItem(path);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	private BellcoreStructure getTrace(SchemePath path) {
		double length = ((Integer)this.tableModel.getValueAt(0, 1)).doubleValue() * 1000;
		double resolution = ((Double)this.tableModel.getValueAt(1, 1)).doubleValue();
		double pulsWidth = ((Integer)this.tableModel.getValueAt(2, 1)).doubleValue();
		double wave_length = ((Integer)this.tableModel.getValueAt(3, 1)).doubleValue();
		double dinam_area = ((Double)this.tableModel.getValueAt(4, 1)).doubleValue();
		double addNoise = Math.abs(((Double)this.tableModel.getValueAt(5,1)).doubleValue());
		double formFactor = ((Double)this.tableModel.getValueAt(6, 1)).doubleValue();
		double weldAtt = ((Double)this.tableModel.getValueAt(7, 1)).doubleValue();
		double connectorRef = ((Double)this.tableModel.getValueAt(8, 1)).doubleValue();
		double connectorAtt = ((Double)this.tableModel.getValueAt(9, 1)).doubleValue();
		double linearAtt = ((Double)this.tableModel.getValueAt(10, 1)).doubleValue();
		
		List reflectoElements;
		try {
			reflectoElements = new ModelGenerator(path, (int)wave_length, connectorAtt, weldAtt, connectorRef, linearAtt).createModelingEvents();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
		//mode((int)wave_length, (int)pulsWidth, formFactor, resolution, connectorAtt, weldAtt, connectorRef, linearAtt);
		if(reflectoElements == null) {
			String error = "Неправильно заданы либо отсутствуют данные в схеме.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		ModelEvent[] rmip = (ModelEvent[])reflectoElements.toArray(new ModelEvent[reflectoElements.size()]);
		if(rmip.length < 2) {
			String error = "Ошибка при определении параметров маршрута.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		
		Parameters pars = new Parameters(-5.0, -20.0, -30.0,
				4.0, 8000.0, 1625, 500, 1.468);
		
		TraceGenerator generator = new TraceGenerator(pars, rmip);
		return generator.getBellcore();
		/*
		NewModelGenerator mg = new NewModelGenerator(rmip,
				resolution,
				dinam_area,
				pulsWidth,
				length,
				addNoise,
				formFactor);*/
//		double []y = generator. mg.getModelArray();
		//double[] y = ReflectogramMath.getReflectogrammFromEvents(re, 0);
		
//		return createBS (path, y, resolution/1000., wave_length, (int)pulsWidth);
	}

	/*
	BellcoreStructure createBS (SchemePath path, double[] y, double delta_x, double wl, int pw)
	{
		BellcoreStructure bs = new BellcoreStructure();

		double groupindex = 1.46800;

		BellcoreModelWriter writer = new BellcoreModelWriter(bs);
		writer.setWavelength((int)wl);
		writer.setPulseWidth(pw);
		writer.setTime(System.currentTimeMillis() / 1000);
		writer.setOpticalModule("AMFICOM generated");
		writer.setPathId(path.getId().getIdentifierString());
		writer.setUnits("mt");
		writer.setAverages(1);
		writer.setRangeParameters(groupindex, delta_x, delta_x * y.length);
		writer.setData(y);
		writer.finalizeChanges();


		return bs;
	}
*/
	
////////////     Additional Necessary Class Definition     ////////////////////
}

class ParamTableModel extends AbstractTableModel {
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

	AComboBox lengthComboBox = new AComboBox();
	AComboBox resolutionComboBox = new AComboBox();
	AComboBox pulsWidthComboBox = new AComboBox();
	AComboBox waveLengthComboBox = new AComboBox();

	String[] columnNames = {"", ""};

	Object[][] data = {
		{"Длина трассы, км", lengthComboBox},
		{"Разрешение, м", resolutionComboBox},
		{"Длительность импульса, нс", pulsWidthComboBox},
		{"Длина волны, нм", waveLengthComboBox},
		{"Динамический диапазон, дБ", new Double(40)},
		{"Коэффициент шума", new Double(0)},
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

class ModelParamsTableEditor extends DefaultCellEditor {
	Object editor;
	ParamTableModel model;

	public ModelParamsTableEditor(ParamTableModel model) {
		super(new JTextField());
		this.model = model;
		setClickCountToStart(1);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,
			int row, int column) {
		this.editor = value;
		if (column == 1)
		{
			if(row == 0) {
				this.model.lengthComboBox.setBackground(SystemColor.window);
				return this.model.lengthComboBox;
			}
			if(row == 1) {
				this.model.resolutionComboBox.setBackground(SystemColor.window);
				return this.model.resolutionComboBox;
			}
			if(row == 2) {
				this.model.pulsWidthComboBox.setBackground(SystemColor.window);
				return this.model.pulsWidthComboBox;
			}
			if(row == 3) {
				this.model.waveLengthComboBox.setBackground(SystemColor.window);
				return this.model.waveLengthComboBox;
			}
		}
		return super.getTableCellEditorComponent (table, value, isSelected, row,  column);
	}

	@Override
	public Object getCellEditorValue() {
		if(this.editor instanceof JComboBox) {
			return this.editor;
		}
		Object obj = super.getCellEditorValue();
		if (obj instanceof String) {
			String str = (String)obj;
			while (str.length() > 0)
			{
				try {
					return Double.valueOf(str);
				}
				catch (NumberFormatException ex) {
					str = str.substring(0, str.length() - 1);
				}
			}
			return new Double (0);
		}
		return obj;
	}
}

class ModelParamsTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6245907018616151050L;
	ParamTableModel model;
	public ModelParamsTableRenderer(ParamTableModel model) {
		this.model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(column == 1 && row == 0)
			return this.model.lengthComboBox;
		if(column == 1 && row == 1)
			return this.model.resolutionComboBox;
		if(column == 1 && row == 2)
			return this.model.pulsWidthComboBox;
		if(column == 1 && row == 3)
			return this.model.waveLengthComboBox;
		return  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}