package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class DevicePropsPanel extends JPanel
{
	SchemeDevice dev;
	ApplicationContext aContext;
	JTable table;

	public DevicePropsPanel(ApplicationContext aContext)
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
	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());
		table = new JTable();
		table.setSelectionMode(table.getSelectionModel().SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		add(new JLabel("Соответствие волокон кабеля портам"), BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		setBackground(SystemColor.window);
		scrollPane.getViewport().setBackground(SystemColor.window);
	}

	public void setEditable(boolean b)
	{
	}

	public void init(SchemeDevice dev, DataSourceInterface dataSource, boolean show_is_kis)
	{
		this.dev = dev;
		if (!dev.isCrossRouteValid())
			dev.createDefaultCrossRoute();

		RouteTableModel tmodel = new RouteTableModel(dev.ports, dev.crossroute);
		table.setModel(tmodel);
		table.setDefaultEditor(Object.class, new RouteTableEditor(tmodel));
		table.setDefaultRenderer(Object.class, new RouteTableRenderer(tmodel));

		updateUI();
	}

	public void undo()
	{
	}

	class RouteTableModel extends DefaultTableModel
	{
		String[] columnNames ={"Порт", "Волокно"};

		class MyItemListener implements ItemListener
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					for (int i = 0; i < getRowCount(); i++)
					{
						ObjectResourceComboBox box = (ObjectResourceComboBox)getValueAt(i, 1);
						if (e.getItem().equals(box.getSelectedItem()) &&
								!e.getSource().equals(box))
						{
							box.setSelected(null);
							table.setRowSelectionInterval(i, i);
						}
					}
				}
			}
		}

		RouteTableModel(Collection ports, Map crossroute)
		{
			ObjectResourceSorter sorter = SchemePort.getDefaultSorter();
			sorter.setDataSet(ports);
			List sortedPorts = sorter.default_sort();

			sorter.setDataSet(crossroute);
			List sortedThreads = sorter.default_sort();

			Object[][] data = new Object[sortedPorts.size()][2];
			Iterator it = sortedPorts.iterator();

			MyItemListener itemListener = new MyItemListener();
			for (int i = 0; i < sortedPorts.size(); i++)
			{
				SchemePort p = (SchemePort)it.next();
				data[i][0] = p.getName();

				SchemeCableThread thread = (SchemeCableThread)crossroute.get(p);
				ObjectResourceComboBox box = new ObjectResourceComboBox();
				box.addItemListener(itemListener);

				box.setFontSize(AComboBox.SMALL_FONT);
				data[i][1] = box;
				box.setContents(sortedThreads, true);
				box.setSelected(thread);
			}
			super.setDataVector(data, columnNames);
		}

		public void setValueAt(Object value, int row, int col)
		{
			if (col == 1)
			{
				for (int i = 0; i < getRowCount(); i++)
					if (getValueAt(i, 1).equals(value))
					{
						super.setValueAt(null, i, 1);
						fireTableCellUpdated(i, 1);
					}
			}
			super.setValueAt(value, row, col);
		}

		public boolean isCellEditable(int row, int col)
		{
			if (col == 0)
				return false;
			return true;
		}
	}

	class RouteTableEditor extends DefaultCellEditor
{
	Object editor;
	RouteTableModel model;

	public RouteTableEditor(RouteTableModel model)
	{
		super(new JTextField());
		this.model = model;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
						boolean isSelected,
						int row, int column)
	{
		editor = value;
		if(column == 1)
		{
			return (Component)model.getValueAt(row, 1);
		}
		return super.getTableCellEditorComponent (table, value, isSelected, row,  column);
	 }

	public Object getCellEditorValue()
	{
		if(editor instanceof JComboBox)
			return editor;
		Object obj = super.getCellEditorValue();
		return obj;
	}

	public int getClickCountToStart()
	{
		return 0;
	}
}

class RouteTableRenderer extends DefaultTableCellRenderer
{
	RouteTableModel model;
	public RouteTableRenderer(RouteTableModel model)
	{
		this.model = model;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		if(column == 1)
			return (Component)model.getValueAt(row, 1);

		return  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}


}