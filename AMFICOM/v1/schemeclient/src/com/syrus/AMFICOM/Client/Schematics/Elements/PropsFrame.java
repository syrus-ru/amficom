package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.tree.TreeNode;

import com.syrus.AMFICOM.Client.Configure.UI.AddPropFrame;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.DeviceGroup;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
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
	boolean editableProperty = false;
	Object[] objs;
	String selectedItem = "";
	String selectedType = "";

	ApplicationContext aContext;

	private Map char_hash = new HashMap(1);
	private Map attr_hash = new HashMap(1);

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
		setTitle(LangModelSchematics.getString("characteristicsTitle"));
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
				toolBar.setCancelButtonEnabled(!jTable.getSelectionModel().isSelectionEmpty() && editableProperty);
			}
		});
		jTable.getColumnModel().getColumn(0).setPreferredWidth(180);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//jTable.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(jTable);
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setAutoscrolls(true);

		PropsTreeModel model = new PropsTreeModel(aContext.getDataSourceInterface());
		utp = new UniTreePanel(this.dispatcher, aContext, model);
		utp.getTree().setRootVisible(false);
		utp.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel nPanel = new JPanel();
		nPanel.setLayout(new BorderLayout());
		nPanel.add(toolBar,  BorderLayout.NORTH);
		nPanel.add(utp,  BorderLayout.CENTER);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(nPanel, BorderLayout.NORTH);
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
			if (type == null || selectedType == null)
				return;

			if (type.equals(EquipmentType.typ))
				char_hash = ((EquipmentType)res).characteristics;
			else if (type.equals(PortType.typ))
				char_hash = ((PortType)res).characteristics;
			else if (type.equals(LinkType.typ))
				char_hash = ((LinkType)res).characteristics;
			else
				return;

			editableProperty = ((ae.getID() == 0) ? false : true);
			if (selectedItem.equals(SchemeLink.typ) || selectedItem.equals(SchemeCableLink.typ))
				setPropsEditable(editableProperty);
			else
				setPropsEditable(editableProperty && can_be_editable);

			elementSelected(char_hash);*/
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;
			editableProperty = ev.isEditable;
//			setPropsEditable(ev.isEditable);
			objs = (Object[])ev.getSource();

			if (objs == null || selectedType == null)
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

				if (element.equipment != null)
				{
					editableProperty = true;
					char_hash = element.equipment.characteristics;
				}
				else
				{
					//editableProperty = false;
					ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, element.protoElementId);
					if (proto != null)
					{
						EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipmentTypeId);
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

				EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, element.equipmentTypeId);
				if (type != null)
					char_hash = type.characteristics;
				else
					showNoSelection();
			}
			else if (ev.SCHEME_PORT_SELECTED)
			{
				SchemePort port = (SchemePort)objs[0];
				attr_hash = port.attributes;

				if (port.port != null)
				{
					editableProperty = true;
					char_hash = port.port.characteristics;
				}
				else
				{
//					editableProperty = false;
					PortType type = (PortType)Pool.get(PortType.typ, port.portTypeId);
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

				if (port.cablePort != null)
				{
					editableProperty = true;
					char_hash = port.cablePort.characteristics;
				}
				else
				{
//					editableProperty = false;
					CablePortType type = (CablePortType)Pool.get(CablePortType.typ, port.cablePortTypeId);
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

				if (link.link != null)
				{
					editableProperty = true;
					char_hash = link.link.characteristics;
				}
				else
				{
//					editableProperty = false;
					LinkType type = (LinkType)Pool.get(LinkType.typ, link.linkTypeId);
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

				if (link.cableLink != null)
				{
					editableProperty = true;
					char_hash = link.cableLink.characteristics;
				}
				else
				{
//					editableProperty = false;
					CableLinkType type = (CableLinkType)Pool.get(CableLinkType.typ, link.cableLinkTypeId);
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

			if (selectedType.equals("attribute"))
			{
				elementSelected(attr_hash);
				setPropsEditable(can_be_editable);
			}
			else
			{
				elementSelected(char_hash);
				setPropsEditable(editableProperty);
			}
		}
		else if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (selectedType == null)
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
					ProtoElement element = (ProtoElement)ev.getList().get(ev.getSelectionNumber());
					attr_hash = element.attributes;

					EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, element.equipmentTypeId);
					if (type != null)
						char_hash = type.characteristics;
					else
						showNoSelection();

//					editableProperty = false;
					setPropsEditable(editableProperty);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(String.class))
			{
				selectedType = (String)ev.getSelectedObject();
				setPropsEditable(editableProperty);
			}
			else if (ev.getDataClass().equals(SchemeElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					SchemeElement element = (SchemeElement)ev.getList().get(ev.getSelectionNumber());
					attr_hash = element.attributes;

					if (element.equipment != null)
					{
						char_hash = element.equipment.characteristics;
					}
					else
					{
						ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, element.protoElementId);
						if (proto != null)
						{
							EquipmentType type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipmentTypeId);
							if (type != null)
								char_hash = type.characteristics;
							else
								showNoSelection();
						}
						else
							showNoSelection();
//					editableProperty = false;
					setPropsEditable(editableProperty);
					}
				}
				else
					showNoSelection();
			}
			else
				showNoSelection();

			if (selectedType.equals("attribute"))
			{
				elementSelected(attr_hash);
				setPropsEditable(can_be_editable);
			}
			else
			{
				elementSelected(char_hash);
				setPropsEditable(editableProperty);
			}
		}
	}

	void setPropsEditable(boolean b)
	{
		toolBar.setAddButtonEnabled(b && !selectedType.equals(""));
		toolBar.setCancelButtonEnabled(!jTable.getSelectionModel().isSelectionEmpty() && b);
		if (b)
			tModel.setEditableColumns(new int[] {1});
		else
			tModel.setEditableColumns(new int[0]);
	}

	void elementSelected(Map t)
	{
		if (jTable.getEditingRow() != -1)
			jTable.getCellEditor(jTable.getEditingRow(), jTable.getEditingColumn()).cancelCellEditing();

		tModel.clearTable();
		if (selectedType == null || t == null)
			return;

		if (selectedType.equals("attribute"))
			for (Iterator it = t.values().iterator(); it.hasNext();)
			{
				ElementAttribute attr = (ElementAttribute)it.next();
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
			for (Iterator it = t.values().iterator(); it.hasNext();)
			{
				Characteristic chr = (Characteristic)it.next();
				CharacteristicType ch = (CharacteristicType)Pool.get(CharacteristicType.typ, chr.type_id);
				if (ch == null)
				{
					System.err.println("CharacteristicType not found " + chr.type_id);
				}
				else if (ch.ch_class.equals(selectedType))
				{
					tModel.addRow(ch.getName(), new Object[] {chr.value});
				}
			}
	}

	void showNoSelection()
	{
		tModel.clearTable();
		selectedItem = "";
		char_hash = new HashMap(1);
		attr_hash = new HashMap(1);
		editableProperty = false;
		setPropsEditable(false);
	}

	void tableUpdated(Object value, int row, int col)
	{
		String name = (String)tModel.getValueAt(row, 0);

		if (selectedType.equals("attribute"))
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

		public List getData()
		{
			return rows;
		}
	}

	void removeCharacterisricFromHash(Map table, String name)
	{
		for (Iterator it = table.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			CharacteristicType t = (CharacteristicType)Pool.get(CharacteristicType.typ, ch.type_id);
			if (t.getName().equals(name))
			{
				it.remove();
				break;
			}
		}
	}

	void removeAttributeFromHash(Map table, String name)
	{
		for (Iterator it = table.values().iterator(); it.hasNext();)
		{
			ElementAttribute attr = (ElementAttribute)it.next();
			ElementAttributeType t = (ElementAttributeType)Pool.get(ElementAttributeType.typ, attr.type_id);
			if (t.getName().equals(name))
			{
				it.remove();
				break;
			}
		}
	}


	void setCharacteristicAtHash(Map table, String name, String value)
	{
		for (Iterator it = table.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			CharacteristicType cht = (CharacteristicType)Pool.get(CharacteristicType.typ, ch.type_id);
			if (cht.getName().equals(name))
			{
				ch.value = value;
				break;
			}
		}
	}

	void setAttributeAtHash(Map table, String name, String value)
	{
		for (Iterator it = table.values().iterator(); it.hasNext();)
		{
			ElementAttribute attr = (ElementAttribute)it.next();
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
			if (selectedType.equals("attribute"))
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
				if (frame.showDialog(selectedType, tModel.getData()) == AddPropFrame.OK)
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

			if (selectedType.equals("attribute"))
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

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList(5);

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
