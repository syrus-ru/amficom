package com.syrus.AMFICOM.Client.Schematics.Elements;


import java.awt.*;
import java.util.*;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class PathPropsPanel extends JPanel
{
	private ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox();
	private JButton addTypeButton = new JButton("...");
	private JTextField compNameTextField = new JTextField();
	private JTextField startDevTextField = new JTextField();
	private JTextField endDevTextField = new JTextField();
	//ObjectResourceTablePane table = new ObjectResourceTablePane();
	//JTable table = new JTable();
	ObjectResourceTreeModel model;
	UniTreePanel utp;

	private String undoCompName;
	private String undoEndDevId;
	private String undoStartDevId;
	private String undoTypeId;
	private Vector undoPathLinks;
	private Hashtable undoPeOrder;
	boolean skip_change = false;

	public ArrayList links_to_add = new ArrayList();
	public SchemeElement device_to_add;

	SchemePath path;
	ApplicationContext aContext;

	public PathPropsPanel(ApplicationContext aContext)
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
		JPanel classPanel = new JPanel(new BorderLayout());
		JPanel compPanel = new JPanel(new BorderLayout());

		JPanel cl1Panel = new JPanel(new BorderLayout());
		JPanel cl2Panel = new JPanel(new BorderLayout());
		JPanel co1Panel = new JPanel(new BorderLayout());
		JPanel co2Panel = new JPanel(new BorderLayout());
		JPanel co3Panel = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());

		add(classPanel, BorderLayout.NORTH);
		add(compPanel, BorderLayout.CENTER);

		JPanel clLabelPanel = new JPanel();
		clLabelPanel.setPreferredSize(new Dimension (60, 10));
		clLabelPanel.add(new JLabel("Класс"));

		JPanel linksLabelPanel = new JPanel();
		linksLabelPanel.setPreferredSize(new Dimension (60, 10));
		linksLabelPanel.add(new JLabel("Связи"));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Название"));

		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("Нач. устр."));

		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setPreferredSize(new Dimension (60, 10));
		manLabelPanel.add(new JLabel("Кон. устр."));

		utp = new UniTreePanel(aContext.getDispatcher(), aContext, model);

/*		table.initialize(
				new PathDisplayModel(),
				new DataSet());
		table.setSorter(PathElement.getSorter());

		table.getTable().getSelectionModel().addListSelectionListener(new MySelectionListener());
		/*table.getTable().getModel().addTableModelListener(new TableModelListener()
		{
				public void tableChanged(TableModelEvent e)
				{
					if (e.getType() == TableModelEvent.UPDATE)
					{
						table_updated();
					}
				}
		});/

		table.getViewport().setBackground(SystemColor.window);*/

		cl1Panel.add(clLabelPanel, BorderLayout.WEST);
		cl2Panel.add(linksLabelPanel, BorderLayout.WEST);
		co1Panel.add(nameLabelPanel, BorderLayout.WEST);
		co2Panel.add(descrLabelPanel, BorderLayout.WEST);
		co3Panel.add(manLabelPanel, BorderLayout.WEST);
		cl1Panel.add(typeComboBox, BorderLayout.CENTER);
		//cl2Panel.add(table, BorderLayout.CENTER);
		cl2Panel.add(utp, BorderLayout.CENTER);
		co1Panel.add(compNameTextField, BorderLayout.CENTER);
		co2Panel.add(startDevTextField, BorderLayout.CENTER);
		co3Panel.add(endDevTextField, BorderLayout.CENTER);
		cl1Panel.add(addTypeButton, BorderLayout.EAST);

		classPanel.add(cl1Panel, BorderLayout.NORTH);
		classPanel.add(co1Panel, BorderLayout.CENTER);
		classPanel.add(co2Panel, BorderLayout.SOUTH);
		compPanel.add(co3Panel, BorderLayout.NORTH);
		compPanel.add(cl2Panel, BorderLayout.CENTER);

		addTypeButton.setPreferredSize(new Dimension(25, 7));
		addTypeButton.setBorder(BorderFactory.createEtchedBorder());
		addTypeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				this_addEqClassButtonActionPerformed();
			}
		});
		typeComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				pathTypeChanged();
			}
		});
		compNameTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (path.type_id == null || path == null)
					return;
				path.name = compNameTextField.getText();
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

//		linksList.setPreferredSize(new Dimension (300, 80));
//		table.setBorder(BorderFactory.createLoweredBevelBorder());
		//table.setAutoscrolls(true);
	}

	private void setDefaults()
	{
		typeComboBox.removeAllItems();
		Hashtable hash = new Hashtable();

		if (Pool.getHash(TransmissionPathType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(TransmissionPathType.typ).elements(); enum.hasMoreElements();)
			{
				TransmissionPathType tpt = (TransmissionPathType)enum.nextElement();
				typeComboBox.add(tpt);
			}
		}
	}

/*	class MySelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting()) return;

			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty())
			{
				//no rows are selected
			}
			else
			{
				PathElement pe = (PathElement)table.getSelectedObject();
				SchemeCableLink cl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				aContext.getDispatcher().notify(new CreatePathEvent(this, new PathElement[] {pe},
						CreatePathEvent.PE_SELECTED_EVENT));
			}
		}
	}*/

	public void setEditable(boolean b)
	{
		typeComboBox.setEnabled(b);
		addTypeButton.setEnabled(b);
		compNameTextField.setEnabled(b);
		endDevTextField.setEnabled(b);
		startDevTextField.setEnabled(b);
		//table.getTable().setEnabled(b);
	}

	public void init(SchemePath path, DataSourceInterface dataSource)
	{
		skip_change = true;
		setDefaults();
		this.path = path;

		compNameTextField.setText(path.getName());
		compNameTextField.setCaretPosition(0);

		String end = "";
		if (!path.end_device_id.equals(""))
		{
			SchemeElement s_el = (SchemeElement)Pool.get(SchemeElement.typ, path.end_device_id);
			if (s_el.equipment_id.equals(""))
				end = s_el.getName();
			else
				end = ((Equipment)Pool.get("kisequipment", s_el.equipment_id)).getName();
		}
		endDevTextField.setText(end);
		endDevTextField.setCaretPosition(0);

		String start = "";
		if (!path.start_device_id.equals(""))
		{
			SchemeElement s_el = (SchemeElement)Pool.get(SchemeElement.typ, path.start_device_id);
			if (s_el.equipment_id.equals(""))
				start = s_el.getName();
			else
				start = ((Equipment)Pool.get("kisequipment", s_el.equipment_id)).getName();
		}
		startDevTextField.setText(start);
		startDevTextField.setCaretPosition(0);

		typeComboBox.setSelected(Pool.get(TransmissionPathType.typ, path.type_id));

		model = new PathTreeModel(aContext.getDataSourceInterface(), path);
		utp.setModel(model);

		//table.setContents(ds);

		undoCompName = path.getName();
		undoEndDevId = path.end_device_id;
		undoStartDevId = path.start_device_id;
		undoTypeId = path.type_id;

		undoPathLinks = new Vector();
		undoPeOrder = new Hashtable();
		for (Enumeration en = path.links.elements(); en.hasMoreElements(); )
		{
			PathElement pe = (PathElement)en.nextElement();
			undoPeOrder.put(pe.link_id, new Integer(pe.n));
			undoPathLinks.add(pe);
		}
		updateUI();
		skip_change = false;
	}

	void pathTypeChanged()
	{
		if (skip_change)
			return;
		if (path == null)
			return;
		TransmissionPathType tpt = (TransmissionPathType)typeComboBox.getSelectedItem();
		path.type_id = tpt.getId();
	}

	public void updateLink()
	{
	/*	PathElement pe = (PathElement)table.getSelectedObject();
		if (pe != null)
		{
			if (!links_to_add.isEmpty())
			{
				Object obj = links_to_add.get(links_to_add.size()-1);

				if (obj instanceof SchemeLink)
				{
					pe.link_id = ((SchemeLink)obj).getId();
					pe.thread_id = "";
					pe.is_cable = false;
				}
				else if (obj instanceof SchemeCableLink)
				{
					CableThreadSelectionDialog panel = new CableThreadSelectionDialog(aContext);
					SchemeCableThread thread = panel.init((SchemeCableLink)obj);
					if (thread != null)
					{
						pe.link_id = ((SchemeCableLink)obj).getId();
						pe.thread_id = thread.getId();
					}
				}

				DataSet ds = new DataSet(path.links);
				table.setContents(ds);
				table.updateUI();

				links_to_add = new ArrayList();
			}
		}*/
	}

	public void removeLink()
	{
		/*PathElement pe = (PathElement)table.getSelectedObject();
		if (pe != null)
		{
			path.links.remove(pe);
			DataSet ds = new DataSet(path.links);

			table.setContents(ds);
			table.updateUI();
		}*/
	}

	public void selectLink(String link_id)
	{
		for (int i = 0; i < path.links.size(); i++)
		{
			PathElement pe = (PathElement)path.links.get(i);
			if (pe.link_id.equals(link_id))
			{
				//table.setSelected(pe);
				break;
			}
		}
	}

	public void addSelectedLinks()
	{
		for (Iterator it = links_to_add.iterator(); it.hasNext();)
		{
			Object obj = it.next();
			if (obj instanceof SchemeLink)
			{
				addLink(((SchemeLink)obj).getId());
			}
			else if (obj instanceof SchemeCableLink)
			{
				SchemeCableLink link = (SchemeCableLink)obj;
				SchemeCableThread thread = null;
				if (links_to_add.size() > 1)
				{
					if (link.cable_threads.size() > 0)
						thread = (SchemeCableThread)link.cable_threads.get(0);
				}
				else
				{
					CableThreadSelectionDialog panel = new CableThreadSelectionDialog(aContext);
					thread = panel.init(link);
				}
				if (thread != null)
					addCableLink(link.getId(), thread.getId());
			}
		}
		DataSet ds = new DataSet(path.links);
		//table.setContents(ds);
		//table.updateUI();

		links_to_add = new ArrayList();
	}

	public void addLink(String link_id)
	{
		PathElement pe = new PathElement();
		pe.is_cable = false;
		pe.n = path.links.size();
		pe.link_id = link_id;
		pe.thread_id = "";
		path.links.add(pe);
	}

	public void addCableLink(String cable_id, String thread_id)
	{
		PathElement pe = new PathElement();
		pe.is_cable = true;
		pe.n = path.links.size();
		pe.link_id = cable_id;
		pe.thread_id = thread_id;
		path.links.add(pe);
	}

	public void setStartDevice(String id)
	{
		String start = "";
		SchemeElement st_dev = (SchemeElement)Pool.get(SchemeElement.typ, id);
		if (!st_dev.scheme_id.equals(""))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		if (hasAccessPort(st_dev))
		{
			if (st_dev.equipment_id.equals(""))
				start = st_dev.getName();
			else
				start = ((Equipment)Pool.get("kisequipment", st_dev.equipment_id)).getName();
			startDevTextField.setText(start);
			startDevTextField.setCaretPosition(0);
			path.start_device_id = id;
		}
		else
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "У начального устройства должен быть тестовый порт\nс которого должен начинаться маршрут тестирования", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
	}

	public void setEndDevice(String id)
	{
		String end = "";
		SchemeElement end_dev = (SchemeElement)Pool.get(SchemeElement.typ, id);
		if (!end_dev.scheme_id.equals(""))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		if (end_dev.equipment_id.equals(""))
			end = end_dev.getName();
		else
			end = ((Equipment)Pool.get("kisequipment", end_dev.equipment_id)).getName();

		endDevTextField.setText(end);
		endDevTextField.setCaretPosition(0);
		path.end_device_id = id;
	}

	boolean hasAccessPort (SchemeElement se)
	{
		for (int i = 0; i < se.devices.size(); i++)
		{
			SchemeDevice dev = (SchemeDevice)se.devices.get(i);
			for (int j = 0; j < dev.ports.size(); j++)
			{
				SchemePort port = (SchemePort)dev.ports.get(j);
				if (!port.access_port_type_id.equals(""))
					return true;
			}
		}

		for (int i = 0; i < se.element_ids.size(); i++)
		{
			SchemeElement inner = (SchemeElement)Pool.get(SchemeElement.typ, (String)se.element_ids.get(i));
			if (hasAccessPort(inner))
				return true;
		}
		return false;
	}

	boolean hasCablePort (ProtoElement proto)
	{
		for (int i = 0; i < proto.devices.size(); i++)
		{
			SchemeDevice dev = (SchemeDevice)proto.devices.get(i);
			if (!dev.cableports.isEmpty())
				return true;
		}

		for (int i = 0; i < proto.protoelement_ids.size(); i++)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)proto.protoelement_ids.get(i));
			if (hasCablePort(p))
				return true;
		}
		return false;
	}

	public void undo()
	{
		path.type_id = undoTypeId;
		path.name = undoCompName;
		path.end_device_id = undoEndDevId;
		path.start_device_id = undoStartDevId;
		path.links = new Vector();
		for (Enumeration en = undoPathLinks.elements(); en.hasMoreElements(); )
		{
			PathElement pe = (PathElement)en.nextElement();
			pe.n = ((Integer)undoPeOrder.get(pe.link_id)).intValue();
			path.links.add(pe);
		}
	}

	public String getCompName()
	{
		return compNameTextField.getText();
	}

	public String getEndDiviceId()
	{
		return path.end_device_id;
	}

	public String getStartDiviceId()
	{
		return path.start_device_id;
	}

	void this_addEqClassButtonActionPerformed()
	{
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый класс");
		dialog.setSize(this.getSize().width, dialog.preferredSize.height);
		Point loc = this.getLocationOnScreen();
		dialog.setLocation(loc.x, loc.y + 30);
		dialog.setVisible(true);

		if (dialog.getStatus() == dialog.OK && !dialog.getName().equals(""))
		{
			String name = dialog.getName();
			for (int i = 0; i < typeComboBox.getItemCount(); i++)
			{
				if (typeComboBox.getItemAt(i).equals(name))
				{
					typeComboBox.setSelectedItem(name);
					return;
				}
			}
			TransmissionPathType type = (TransmissionPathType)Pool.get(TransmissionPathType.typ, path.type_id);
			TransmissionPathType new_type = new TransmissionPathType();
			new_type.name = name;
			new_type.id = aContext.getDataSourceInterface().GetUId(TransmissionPathType.typ);
			path.type_id = new_type.getId();
			Pool.put(TransmissionPathType.typ, path.type_id, new_type);

			typeComboBox.add(new_type);
			typeComboBox.setSelected(new_type);
		}
	}
}

class PathDisplayModel extends StubDisplayModel
{
	public PathDisplayModel()
	{
		super();
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("num"))
			return 20;
		if(col_id.equals("thread"))
			return 100;
		if(col_id.equals("cable"))
			return 100;
		return 100;
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();

		cols.add("num");
		cols.add("thread");
		cols.add("cable");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("num"))
			return "#";
		if(col_id.equals("thread"))
			return "Волокно";
		if(col_id.equals("cable"))
			return "Кабель";
		return "";
	}

	public boolean isColumnEditable(String col_id)
	{
		if (col_id.equals("num"))
			return true;
		return false;
	}
}


class PathTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;
	SchemePath path;

	public PathTreeModel(DataSourceInterface dsi, SchemePath path)
	{
		this.dsi = dsi;
		this.path = path;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode (path, path.getName(), true,
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
			String s = (String )node.getObject();
			if(s.equals(MapProtoGroup.typ))
				return MapProtoGroup.class;
		}
		else if (node.getObject() instanceof MapProtoElement)
			return ProtoElement.class;
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		if(node.getObject() instanceof SchemePath)
		{
			HashSet hs = new HashSet();
			for (Enumeration enum = path.links.elements(); enum.hasMoreElements();)
				hs.add(((PathElement)enum.nextElement()).scheme_id);

			for (Iterator it = hs.iterator(); it.hasNext();)
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, (String)it.next());
				vec.add(new ObjectResourceTreeNode(scheme, scheme.getName(), true, false));
			}
		}
		else if(node.getObject() instanceof Scheme)
		{
			Scheme scheme = (Scheme)node.getObject();
			Vector v = new Vector();
			for (Enumeration enum = path.links.elements(); enum.hasMoreElements();)
			{
				PathElement pe = (PathElement)enum.nextElement();
				if (pe.scheme_id.equals(scheme.getId()))
					v.add(pe);
			}
			DataSet ds = new DataSet(v);
			ObjectResourceSorter sorter = PathElement.getDefaultSorter();
			sorter.setDataSet(ds);
			ds = sorter.default_sort();
			for (Enumeration enum = ds.elements(); enum.hasMoreElements();)
			{
				PathElement pe = (PathElement)enum.nextElement();
				vec.add(new ObjectResourceTreeNode(pe, pe.getName(), true, false));
			}
		}
		else if(node.getObject() instanceof PathElement)
		{
			PathElement pe = (PathElement)node.getObject();
			if (pe.link_id.length() == 0)
			{
				SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, pe.scheme_element_id);
				//ports
				vec.add(new ObjectResourceTreeNode(se, se.getName(), true, false));
			}
			else if (pe.is_cable)
			{
				SchemeCableLink sl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				vec.add(new ObjectResourceTreeNode(sl, sl.getName(), true, false));
			}
			else
			{
				SchemeLink sl = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
				vec.add(new ObjectResourceTreeNode(sl, sl.getName(), true, false));
			}
		}

		return vec;
	}
}
