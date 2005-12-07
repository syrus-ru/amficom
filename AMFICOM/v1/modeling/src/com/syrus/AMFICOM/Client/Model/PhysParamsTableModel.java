/*-
 * $Id: PhysParamsTableModel.java,v 1.1 2005/12/07 14:31:49 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Model;

import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.syrus.AMFICOM.client.UI.AComboBox;

public class PhysParamsTableModel extends AbstractTableModel {
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
		{"Добавочный шум, дБ", new Double(0)},
		{"Форм-фактор коннектора", new Double(0.95)},
		{"Потери на сварке, дБ", new Double(0.1)},
		{"Отражение на коннекторе, дБ", new Double(-40)},
		{"Потери на коннекторе, дБ", new Double(0.5)},
		{"Затухание на линейном уч-ке, дБ/км", new Double(0.2)}
	};

	PhysParamsTableModel()
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

class PhysModelParamsTableEditor extends DefaultCellEditor {
	Object editor;
	PhysParamsTableModel model;

	public PhysModelParamsTableEditor(PhysParamsTableModel model) {
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

class PhysModelParamsTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6245907018616151050L;
	PhysParamsTableModel model;
	public PhysModelParamsTableRenderer(PhysParamsTableModel model) {
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