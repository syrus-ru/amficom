package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeCableThreadController;
import com.syrus.AMFICOM.scheme.corba.*;

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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		add(new JLabel("Соответствие волокон кабеля портам"), BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		setBackground(SystemColor.window);
		scrollPane.getViewport().setBackground(SystemColor.window);
	}

	public void setEditable(boolean b)
	{
	}

	public void init(SchemeDevice dev, boolean show_is_kis)
	{
		this.dev = dev;

		RouteTableModel tmodel = new RouteTableModel(dev.schemePorts());
		table.setModel(tmodel);
		table.setDefaultEditor(Object.class, new RouteTableEditor(tmodel));
		table.setDefaultRenderer(Object.class, new RouteTableRenderer(tmodel));
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);

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
				if (e.getStateChange() == ItemEvent.SELECTED &&
						e.getItem() instanceof SchemeCableThread)
				{
					for (int i = 0; i < getRowCount(); i++)
					{
						ObjComboBox box = (ObjComboBox)getValueAt(i, 1);
						if (e.getItem().equals(box.getSelectedItem()) &&
								!e.getSource().equals(box))
						{
							box.setSelectedItem("");
							table.setRowSelectionInterval(i, i);
							SchemePort port = (SchemePort)getObjectResource(i, 0);
							port.schemeCableThread(null);
						}
					}
					for (int i = 0; i < getRowCount(); i++)
					{
						ObjComboBox box = (ObjComboBox) getValueAt(i, 1);
						if (e.getItem().equals(box.getSelectedItem()) &&
								e.getSource().equals(box))
						{
							table.setRowSelectionInterval(i, i);
							SchemePort port = (SchemePort)getObjectResource(i, 0);
							port.schemeCableThread((SchemeCableThread)box.getSelectedItem());
						}
					}
				}
			}
		}

		RouteTableModel(SchemePort[] ports)
		{
//			List sortedPorts = Arrays.asList(ports);
			Set schemeCables = new HashSet();
			List sortedThreads = new LinkedList();

			for (int i = 0; i < ports.length; i++)
			{
				SchemeCableThread thread = ports[i].schemeCableThread();
				if (thread != null)
					schemeCables.add(thread.schemeCablelink());
			}
			for (Iterator it = schemeCables.iterator(); it.hasNext();)
			{
				SchemeCableLink link = (SchemeCableLink)it.next();
				sortedThreads.addAll(Arrays.asList(link.schemeCableThreads()));
			}

			Object[][] data = new Object[ports.length][2];

			MyItemListener itemListener = new MyItemListener();
			for (int i = 0; i < ports.length; i++)
			{
				data[i][0] = ports[i];

				SchemeCableThread thread = ports[i].schemeCableThread();
				ObjComboBox box = new ObjComboBox(
								SchemeCableThreadController.getInstance(),
								StorableObjectWrapper.COLUMN_NAME);
				box.addItemListener(itemListener);
				box.setFontSize(AComboBox.SMALL_FONT);
				data[i][1] = box;
				box.addItem("");
				box.addElements(sortedThreads);
				if (thread != null)
					box.setSelectedItem(thread);
				else
					box.setSelectedItem("");
			}
			super.setDataVector(data, columnNames);
		}

		public boolean isCellEditable(int row, int col)
		{
			if (col == 0)
				return false;
			return true;
		}

		public Object getValueAt(int row, int col)
		{
			if (col == 0)
				return ((SchemePort)super.getValueAt(row, col)).name();
			return super.getValueAt(row, col);
		}

		public Object getObjectResource(int row, int col)
		{
			if (col == 0)
				return super.getValueAt(row, col);
			return ((ObjComboBox)super.getValueAt(row, col)).getSelectedItem();
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
			setClickCountToStart(1);
		}

		public Component getTableCellEditorComponent(JTable jTable, Object value,
				boolean isSelected,
				int row, int column)
		{
			editor = value;
			if (column == 1)
			{
				return (Component) model.getValueAt(row, 1);
			}
			return super.getTableCellEditorComponent(jTable, value, isSelected, row, column);
		}

		public Object getCellEditorValue()
		{
			if (editor instanceof JComboBox)
				return editor;
			Object obj = super.getCellEditorValue();
			return obj;
		}
	}

	class RouteTableRenderer extends DefaultTableCellRenderer
	{
		RouteTableModel model;
		public RouteTableRenderer(RouteTableModel model)
		{
			this.model = model;
		}

		public Component getTableCellRendererComponent(JTable jTable, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (column == 1)
				return (Component) model.getValueAt(row, 1);

			return super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		}
	}
}