package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ElementAttributeType;
import oracle.jdeveloper.layout.*;

public class CharacteristicsPanel extends JPanel
		implements OperationListener
{
	PropsADToolBar toolBar;
	UniTreePanel utp;

	protected Dispatcher dispatcher = new Dispatcher();
	protected JTable jTable;
	protected PropsTableModel tModel;

	boolean can_be_editable = true;
	boolean editable_property = true;
	Object[] objs;
	String selected_item = "";
	String selected_type = "";

	private Map char_hash = new Hashtable(1);
	private Map attr_hash = new Hashtable(1);

	ApplicationContext aContext;

	public CharacteristicsPanel(boolean can_be_editable)
	{
		super();
		this.can_be_editable = can_be_editable;
		aContext = new ApplicationContext();
		aContext.setDispatcher(dispatcher);

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public CharacteristicsPanel()
	{
		this(true);
	}

	public CharacteristicsPanel(ObjectResource or, boolean can_be_editable)
	{
		this(can_be_editable);
		setCharHash(or);
	}

	public CharacteristicsPanel(ObjectResource or)
	{
		this(or, true);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext.setDataSourceInterface(aContext.getDataSourceInterface());
	}

	private void jbInit() throws Exception
	{
		toolBar = new PropsADToolBar();

		tModel = new PropsTableModel(
				new String[] {"", ""},
				new Object[] {""},
				null,
				new int[]    { 1 }
				);

		jTable = new JTable (tModel);
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

		PropsTreeModel model = new PropsTreeModel();
		utp = new UniTreePanel(this.dispatcher, aContext, model);
		utp.getTree().setRootVisible(false);
		utp.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		n_panel.add(toolBar,  BorderLayout.NORTH);
		n_panel.add(utp,  BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(n_panel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setCharHash(ObjectResource res)
	{
		String type = res.getTyp();
		if (type == null || selected_type == null)
		{
			char_hash = new Hashtable(1);
			elementSelected(char_hash);
			showNoSelection();
			return;
	 }

	 if (type.equals(Equipment.typ))
		char_hash = ((Equipment)res).characteristics;
	 else if (type.equals(KIS.typ))
		char_hash = ((KIS)res).characteristics;
	 else if (type.equals(Port.typ))
		char_hash = ((Port)res).characteristics;
	 else if (type.equals(CablePort.typ))
		char_hash = ((CablePort)res).characteristics;
	 else if (type.equals(AccessPort.typ))
		char_hash = ((AccessPort)res).characteristics;
	 else if (type.equals(Link.typ))
		char_hash = ((Link)res).characteristics;
	 else if (type.equals(CableLink.typ))
		char_hash = ((CableLink)res).characteristics;

	 else if (type.equals(EquipmentType.typ))
		char_hash = ((EquipmentType)res).characteristics;
	 else if (type.equals(PortType.typ))
		char_hash = ((PortType)res).characteristics;
	 else if (type.equals(CablePortType.typ))
		char_hash = ((CablePortType)res).characteristics;
	 else if (type.equals(AccessPortType.typ))
		char_hash = ((AccessPortType)res).characteristics;
	 else if (type.equals(LinkType.typ))
		char_hash = ((LinkType)res).characteristics;
	 else if (type.equals(CableLinkType.typ))
		char_hash = ((CableLinkType)res).characteristics;

		else
		{
			char_hash = new Hashtable(1);
			elementSelected(char_hash);
			showNoSelection();
			return;
		}
		setPropsEditable(can_be_editable);
		elementSelected(char_hash);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (selected_type == null)
			{
				showNoSelection();
				return;
			}
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass().equals(String.class))
			{
				selected_type = (String)ev.getSelectedObject();
				setPropsEditable(editable_property);
			}
			else
				showNoSelection();

			if (selected_type.equals("attribute"))
				elementSelected(attr_hash);
			else
				elementSelected(char_hash);
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

	void elementSelected(Map t)
	{
		tModel.clearTable();
		if (selected_type == null || t == null)
			return;

		if (selected_type.equals("attribute"))
			for (Iterator it = t.values().iterator(); it.hasNext();)
			{
				ElementAttribute attr = (ElementAttribute)it.next();
				ElementAttributeType at = (ElementAttributeType)Pool.get(ElementAttributeType.typ, attr.type_id);
				tModel.addRow(attr.getName(), new Object[] {attr.value});
			}
		else
			for (Iterator it = t.values().iterator(); it.hasNext();)
			{
				Characteristic chr = (Characteristic)it.next();
				CharacteristicType ch = (CharacteristicType)Pool.get(CharacteristicType.typ, chr.type_id);
				if (ch.ch_class.equals(selected_type))
				{
					tModel.addRow(ch.getName(), new Object[] {chr.value});
				}
			}
	}

	void showNoSelection()
	{
		tModel.clearTable();
		selected_item = "";
		char_hash = null;
		attr_hash = null;
		editable_property = true;
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
		public PropsTableModel(
				String[] p_columns, // заголовки столбцов
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

	void removeCharacterisricFromHash(Map table, String name)
	{
		for (Iterator it = table.values().iterator(); it.hasNext(); )
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
		for (Iterator it = table.values().iterator(); it.hasNext(); )
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
		for (Iterator it = table.values().iterator(); it.hasNext(); )
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
		for (Iterator it = table.values().iterator(); it.hasNext(); )
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

		public PropsADToolBar()
		{
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
			addButton.setToolTipText(LangModelConfig.getString("label_add_char"));
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

			deleteButton.setToolTipText(LangModelConfig.getString("label_delete_char"));
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
			}
			else
			{
				AddPropFrame frame = new AddPropFrame(null, "Unknown parameter!!!", null);
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

	public PropsTreeModel()
	{
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode ("root", LangModelConfig.getString("label_comp_chars"), true,
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
		if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();
			if (s.equals("root"))
				return String.class;
		}
		return String.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();

			if(s.equals("root"))
			{
				vec.add(new ObjectResourceTreeNode("optical", LangModelConfig.getString("label_opt_chars"), true, true));
				vec.add(new ObjectResourceTreeNode("electrical", LangModelConfig.getString("label_el_chars"), true, true));
				vec.add(new ObjectResourceTreeNode("operational", LangModelConfig.getString("label_exp_chars"), true, true));
				vec.add(new ObjectResourceTreeNode("interface", LangModelConfig.getString("label_interface_chars"), true, true));
//				vec.add(new ObjectResourceTreeNode("attribute", "Атрибуты отображения", true, true));
			}
		}
		return vec;
	}
}
