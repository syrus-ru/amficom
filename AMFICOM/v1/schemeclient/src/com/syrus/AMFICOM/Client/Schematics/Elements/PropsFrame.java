package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.tree.TreeNode;

import com.syrus.AMFICOM.Client.Configure.UI.AddPropFrame;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
import com.syrus.AMFICOM.Client.General.Scheme.DeviceGroup;
import oracle.jdeveloper.layout.*;

public class PropsFrame extends JInternalFrame
		implements OperationListener
{
	PropsADToolBar toolBar;
	UniTreePanel utp;

	protected Dispatcher dispatcher = new Dispatcher();
	protected ATable jTable;
	protected PropsTableModel tModel;

	public boolean can_be_editable = true;
	boolean editable_property = false;
	Object[] objs;
	String selected_item = "";
	String selected_type = "";

	ApplicationContext aContext;

	private Hashtable char_hash = new Hashtable(1);
	private Hashtable attr_hash = new Hashtable(1);

	public PropsFrame(ApplicationContext aContext, boolean can_be_editable)
	{
		super();
		this.aContext = aContext;
		this.can_be_editable = can_be_editable;

		init_module(aContext.getDispatcher());
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setTitle(LangModelSchematics.String("characteristicsTitle"));
		toolBar = new PropsADToolBar(aContext);
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMinimumSize(new Dimension(200, 150));

		tModel = new PropsTableModel(
				new String[] {"", ""},
				new Object[] {""},
				null,
				new int[]    { 1 }
				);

		jTable = new ATable (tModel);
		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting())
					return;
				toolBar.setCancelButtonEnabled(!jTable.getSelectionModel().isSelectionEmpty() && editable_property);
			}
		});
		jTable.getColumnModel().getColumn(0).setPreferredWidth(180);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//jTable.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(jTable);
		scrollPane.getViewport().setBackground(SystemColor.window);
		scrollPane.setAutoscrolls(true);

		PropsTreeModel model = new PropsTreeModel(aContext.getDataSourceInterface());
		utp = new UniTreePanel(this.dispatcher, aContext, model);
		utp.getTree().setRootVisible(false);
		utp.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		n_panel.add(toolBar,  BorderLayout.NORTH);
		n_panel.add(utp,  BorderLayout.CENTER);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(n_panel, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	void init_module(Dispatcher dispatcher)
	{
		dispatcher.register(this, SchemeElementsEvent.type);
		dispatcher.register(this, TreeDataSelectionEvent.type);
		dispatcher.register(this, SchemeNavigateEvent.type);

		this.dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	static boolean hasGroupedChild (Object obj)
	{
		if (obj instanceof TreeNode)
		{
			TreeNode node = (TreeNode)obj;
			if (node.isLeaf())
				return false;
			for (int i = 0; i < node.getChildCount(); i++)
				if (node.getChildAt(i) instanceof DeviceGroup)
					return true;
			return false;
		}
		return false;
	}


	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{/*
			ObjectResource res = (ObjectResource)ae.getSource();
			String type = res.getTyp();
			if (type == null || selected_type == null)
				return;

			if (type.equals(EquipmentType.typ))
				char_hash = ((EquipmentType)res).characteristics;
			else if (type.equals(PortType.typ))
				char_hash = ((PortType)res).characteristics;
			else if (type.equals(LinkType.typ))
				char_hash = ((LinkType)res).characteristics;
			else
				return;

			editable_property = ((ae.getID() == 0) ? false : true);
			if (selected_item.equals(SchemeLink.typ) || selected_item.equals(SchemeCableLink.typ))
				setPropsEditable(editable_property);
			else
				setPropsEditable(editable_property && can_be_editable);

			elementSelected(char_hash);*/
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;
			editable_property = ev.isEditable;
//			setPropsEditable(ev.isEditable);
			objs = (Object[])ev.getSource();

			if (objs == null || selected_type == null)
			{
				showNoSelection();
				return;
			}
			if (ev.SCHEME_ALL_DESELECTED)
				showNoSelection();

			if (objs.length == 0 || objs[0] == null)
			{
				showNoSelection();
				return;
			}
			if (ev.SCHEME_ELEMENT_SELECTED)
			{
				SchemeElement element = (SchemeElement)objs[0];
				attr_hash = element.attributes;

				if (!element.equipment_id.equals(""))
				{
					editable_property = true;
					Equipment eq = (Equipment)Pool.get("kisequipment", element.equipment_id);
					char_hash = eq.characteristics;
				}
				else
				{
					//editable_property = false;
					ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, element.proto_element_id);
					if (proto != null)
					{
						EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
						if (type != null)
							char_hash = type.characteristics;
						else
							showNoSelection();
					}
					else
						showNoSelection();
				}
			}
			else if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				ProtoElement element = (ProtoElement)objs[0];
				attr_hash = element.attributes;

				EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, element.equipment_type_id);
				if (type != null)
					char_hash = type.characteristics;
				else
					showNoSelection();
			}
			else if (ev.SCHEME_PORT_SELECTED)
			{
				SchemePort port = (SchemePort)objs[0];
				attr_hash = port.attributes;

				if (!port.port_id.equals(""))
				{
					editable_property = true;
					Port p = (Port)Pool.get(Port.typ, port.port_id);
					char_hash = p.characteristics;
				}
				else
				{
//					editable_property = false;
					PortType type = (PortType)Pool.get(PortType.typ, port.port_type_id);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();
				}
			}
			else if (ev.SCHEME_CABLE_PORT_SELECTED)
			{
				SchemeCablePort port = (SchemeCablePort)objs[0];
				attr_hash = port.attributes;

				if (!port.cable_port_id.equals(""))
				{
					editable_property = true;
					CablePort p = (CablePort)Pool.get(CablePort.typ, port.cable_port_id);
					char_hash = p.characteristics;
				}
				else
				{
//					editable_property = false;
					CablePortType type = (CablePortType)Pool.get(CablePortType.typ, port.cable_port_type_id);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();
				}
			}
			else if (ev.SCHEME_LINK_SELECTED)
			{
				SchemeLink link = (SchemeLink)objs[0];
				attr_hash = link.attributes;

				if (!link.link_id.equals(""))
				{
					editable_property = true;
					Link l = (Link)Pool.get(Link.typ, link.link_id);
					char_hash = l.characteristics;
				}
				else
				{
//					editable_property = false;
					LinkType type = (LinkType)Pool.get(LinkType.typ, link.link_type_id);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();
				}
			}
			else if (ev.SCHEME_CABLE_LINK_SELECTED)
			{
				SchemeCableLink link = (SchemeCableLink)objs[0];
				attr_hash = link.attributes;

				if (!link.cable_link_id.equals(""))
				{
					editable_property = true;
					CableLink l = (CableLink)Pool.get(CableLink.typ, link.cable_link_id);
					char_hash = l.characteristics;
				}
				else
				{
//					editable_property = false;
					CableLinkType type = (CableLinkType)Pool.get(CableLinkType.typ, link.cable_link_type_id);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();
				}
			}
			else if (ev.SCHEME_PATH_SELECTED)
			{
				showNoSelection();
			}
			else
				showNoSelection();

			if (selected_type.equals("attribute"))
			{
				elementSelected(attr_hash);
				setPropsEditable(can_be_editable);
			}
			else
			{
				elementSelected(char_hash);
				setPropsEditable(editable_property);
			}
		}
		else if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (selected_type == null)
			{
				showNoSelection();
				return;
			}
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass() == null)
				return;
			if (ev.getDataClass().equals(ProtoElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					ProtoElement element = (ProtoElement)ev.getDataSet().get(ev.getSelectionNumber());
					attr_hash = element.attributes;

					EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, element.equipment_type_id);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();

//					editable_property = false;
					setPropsEditable(editable_property);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(String.class))
			{
				selected_type = (String)ev.selectedObject;
				setPropsEditable(editable_property);
			}
			else if (ev.getDataClass().equals(SchemeElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					SchemeElement element = (SchemeElement)ev.getDataSet().get(ev.getSelectionNumber());
					attr_hash = element.attributes;

					if (!element.equipment_id.equals(""))
					{
						Equipment eq = (Equipment)Pool.get("kisequipment", element.equipment_id);
						char_hash = eq.characteristics;
					}
					else
					{
						ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, element.proto_element_id);
						if (proto != null)
						{
							EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
							if (type != null)
								char_hash = type.characteristics;
							else
								showNoSelection();
						}
						else
							showNoSelection();
//					editable_property = false;
					setPropsEditable(editable_property);
					}
				}
				else
					showNoSelection();
			}
			else
				showNoSelection();

			if (selected_type.equals("attribute"))
			{
				elementSelected(attr_hash);
				setPropsEditable(can_be_editable);
			}
			else
			{
				elementSelected(char_hash);
				setPropsEditable(editable_property);
			}
		}
	}

	void setPropsEditable(boolean b)
	{
		toolBar.setAddButtonEnabled(b && !selected_type.equals(""));
		toolBar.setCancelButtonEnabled(!jTable.getSelectionModel().isSelectionEmpty() && b);
		if (b)
			tModel.setEditableColumns(new int[] {1});
		else
			tModel.setEditableColumns(new int[0]);
	}

	void elementSelected(Hashtable t)
	{
		if (jTable.getEditingRow() != -1)
			jTable.getCellEditor(jTable.getEditingRow(), jTable.getEditingColumn()).cancelCellEditing();

		tModel.clearTable();
		if (selected_type == null || t == null)
			return;

		if (selected_type.equals("attribute"))
			for (Enumeration en = t.keys(); en.hasMoreElements();)
			{
				String key = (String)en.nextElement();
				ElementAttribute attr = (ElementAttribute)t.get(key);
				ElementAttributeType at = (ElementAttributeType)Pool.get(ElementAttributeType.typ, attr.type_id);
				if (at == null)
				{
					System.err.println("ElementAttributeType not found " + attr.type_id);
				}
				else
				{
					tModel.addRow(attr.getName(), new Object[] {attr.value});
				}
			}
		else
			for (Enumeration en = t.keys(); en.hasMoreElements();)
			{
				String key = (String)en.nextElement();
				Characteristic chr = (Characteristic)t.get(key);
				CharacteristicType ch = (CharacteristicType)Pool.get(CharacteristicType.typ, chr.type_id);
				if (ch == null)
				{
					System.err.println("CharacteristicType not found " + chr.type_id);
				}
				else if (ch.ch_class.equals(selected_type))
				{
					tModel.addRow(ch.getName(), new Object[] {chr.value});
				}
			}
	}

	void showNoSelection()
	{
		tModel.clearTable();
		selected_item = "";
		char_hash = new Hashtable(1);
		attr_hash = new Hashtable(1);
		editable_property = false;
		setPropsEditable(false);
	}

	void tableUpdated(Object value, int row, int col)
	{
		String name = (String)tModel.getValueAt(row, 0);

		if (selected_type.equals("attribute"))
			setAttributeAtHash(attr_hash, name, (String)value);
		else
			setCharacteristicAtHash(char_hash, name, (String)value);
	}

	class PropsTableModel extends FixedSizeEditableTableModel
	{
		public PropsTableModel(String[] p_columns, // заголовки столбцов
													 Object[] p_defaultv,// дефолтные значения
							String[] p_rows,    // заголовки (0й столбец) строк
							int[] editable)
		{
			super(p_columns, p_defaultv, p_rows, editable);
		}

		public void setValueAt(Object value, int row, int col)
		{
			super.setValueAt(value, row, col);
			tableUpdated(value, row, col);
		}

		public Vector getData()
		{
			return rows;
		}
	}

	void removeCharacterisricFromHash(Hashtable table, String name)
	{
		for (Enumeration enum = table.keys(); enum.hasMoreElements(); )
		{
			String key = (String)enum.nextElement();
			Characteristic ch = (Characteristic)table.get(key);
			CharacteristicType t = (CharacteristicType)Pool.get(CharacteristicType.typ, ch.type_id);
			if (t.getName().equals(name))
			{
				table.remove(key);
				break;
			}
		}
	}

	void removeAttributeFromHash(Hashtable table, String name)
	{
		for (Enumeration enum = table.keys(); enum.hasMoreElements(); )
		{
			String key = (String)enum.nextElement();
			ElementAttribute attr = (ElementAttribute)table.get(key);
			ElementAttributeType t = (ElementAttributeType)Pool.get(ElementAttributeType.typ, attr.type_id);
			if (t.getName().equals(name))
			{
				table.remove(key);
				break;
			}
		}
	}


	void setCharacteristicAtHash(Hashtable table, String name, String value)
	{
		for (Enumeration enum = table.keys(); enum.hasMoreElements(); )
		{
			String key = (String)enum.nextElement();
			Characteristic ch = (Characteristic)table.get(key);
			CharacteristicType cht = (CharacteristicType)Pool.get(CharacteristicType.typ, ch.type_id);
			if (cht.getName().equals(name))
			{
				ch.value = value;
				break;
			}
		}
	}

	void setAttributeAtHash(Hashtable table, String name, String value)
	{
		for (Enumeration enum = table.keys(); enum.hasMoreElements(); )
		{
			String key = (String)enum.nextElement();
			ElementAttribute attr = (ElementAttribute)table.get(key);
			ElementAttributeType at = (ElementAttributeType)Pool.get(ElementAttributeType.typ, attr.type_id);
			if (at.getName().equals(name))
			{
				attr.value = value;
				break;
			}
		}
	}

	class PropsADToolBar extends JPanel
	{
		public final Dimension btn_size = new Dimension(24, 24);
		JButton addButton = new JButton();
		JButton deleteButton = new JButton();
		ApplicationContext aContext;

		public PropsADToolBar(ApplicationContext aContext)
		{
			this.aContext = aContext;
			try
			{
				jbInit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private void jbInit() throws Exception
		{
			addButton.setPreferredSize(btn_size);
			addButton.setMaximumSize(btn_size);
			//addButton.setFocusable(false);
			addButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			addButton.setFocusPainted(false);
			addButton.setEnabled(false);
			addButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newprop.gif")));
			addButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addButton_actionPerformed(e);
				}
			});

			deleteButton.setPreferredSize(btn_size);
			deleteButton.setMaximumSize(btn_size);
			//deleteButton.setFocusable(false);
			deleteButton.setEnabled(false);
			deleteButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			deleteButton.setFocusPainted(false);
			deleteButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")));
			deleteButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					deleteButton_actionPerformed(e);
				}
			});

			setLayout(new XYLayout());
			add(addButton, new XYConstraints(0, 0, -1, -1));
			add(deleteButton, new XYConstraints(btn_size.width, 0, -1, -1));
		}

		public void setAddButtonEnabled (boolean b)
		{
			addButton.setEnabled(b);
		}

		public void setCancelButtonEnabled (boolean b)
		{
			deleteButton.setEnabled(b);
		}

		void addButton_actionPerformed(ActionEvent e)
		{
			if (selected_type.equals("attribute"))
			{
				AddAttribFrame frame = new AddAttribFrame(null, getTitle(), this.aContext);
				if (frame.showDialog(tModel.getData()) == AddPropFrame.OK)
				{
					ElementAttributeType type = frame.getSelectedType();
					ElementAttribute attr = new ElementAttribute();
					//attr.id = this.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
					attr.name = type.getName();
					attr.type_id = type.getId();
					attr_hash.put(attr.type_id, attr);

					int n = tModel.addRow(type.getName(), new String[] {""});
					jTable.setRowSelectionInterval(n, n);
				}
			}
			else
			{
				AddPropFrame frame = new AddPropFrame(Environment.getActiveWindow(), getTitle(), this.aContext);
				if (frame.showDialog(selected_type, tModel.getData()) == AddPropFrame.OK)
				{
					CharacteristicType type = frame.getSelectedType();
					Characteristic ch = new Characteristic();
					ch.ch_class = type.ch_class;
					ch.name = type.getName();
					ch.type_id = type.getId();
					char_hash.put(ch.type_id, ch);

					int n = tModel.addRow(type.getName(), new String[] {""});
					jTable.setRowSelectionInterval(n, n);
				}
			}
			jTable.updateUI();
		}

		void deleteButton_actionPerformed(ActionEvent e)
		{
			int n = jTable.getSelectedRow();
			if (n == -1)
				return;

			String name = (String)tModel.getValueAt(n, 0);

			if (selected_type.equals("attribute"))
				removeAttributeFromHash(attr_hash, name);
			else
				removeCharacterisricFromHash(char_hash, name);
			tModel.removeRow(n);
			jTable.updateUI();
		}
	}
}

class PropsTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public PropsTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode ("root", "Характеристики компонента", true,
																			 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return String.class;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();

			if(s.equals("root"))
			{
				vec.add(new ObjectResourceTreeNode("optical", "Оптические характеристики", true, null, true));
				vec.add(new ObjectResourceTreeNode("electrical", "Электрические характеристики", true, null, true));
				vec.add(new ObjectResourceTreeNode("operational", "Эксплуатационные характеристики", true, null, true));
				vec.add(new ObjectResourceTreeNode("interface", "Интерфейсные характеристики", true, null, true));
				vec.add(new ObjectResourceTreeNode("attribute", "Атрибуты отображения", true, null, true));
			}
		}
		return vec;
	}
}
