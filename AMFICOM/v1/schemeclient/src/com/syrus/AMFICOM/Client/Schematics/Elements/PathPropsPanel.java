package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
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
//	ObjectResourceTablePane table = new ObjectResourceTablePane();
	//JTable table = new JTable();

	private String undoCompName;
	private String undoEndDevId;
	private String undoStartDevId;
	private String undoTypeId;
	private List undoPathLinks;
	private Map undoPeOrder;
	boolean skip_change = false;

//	public ArrayList elements_to_add = new ArrayList();
	ObjectResource element_to_add;

	SchemePath path;
	ApplicationContext aContext;

	UniTreePanel utp;
	JScrollPane scroll = new JScrollPane();

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

		JPanel cl2Panel = new JPanel(new BorderLayout());
		JPanel cl1Panel = new JPanel(new BorderLayout());
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

		cl1Panel.add(clLabelPanel, BorderLayout.WEST);
		cl2Panel.add(linksLabelPanel, BorderLayout.WEST);
		co1Panel.add(nameLabelPanel, BorderLayout.WEST);
		co2Panel.add(descrLabelPanel, BorderLayout.WEST);
		co3Panel.add(manLabelPanel, BorderLayout.WEST);
		cl1Panel.add(typeComboBox, BorderLayout.CENTER);
		cl2Panel.add(scroll, BorderLayout.CENTER);
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
				if (path.typeId == null || path == null)
					return;
				path.name = compNameTextField.getText();
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

	}

	private void setDefaults()
	{
		typeComboBox.removeAllItems();
		Map hash = new HashMap();

		if (Pool.getMap(TransmissionPathType.typ) != null)
		{
			for(Iterator it = Pool.getMap(TransmissionPathType.typ).values().iterator(); it.hasNext();)
			{
				TransmissionPathType tpt = (TransmissionPathType)it.next();
				typeComboBox.add(tpt);
			}
		}
	}

	public void setEditable(boolean b)
	{
		typeComboBox.setEnabled(b);
		addTypeButton.setEnabled(b);
		compNameTextField.setEnabled(b);
		endDevTextField.setEnabled(b);
		startDevTextField.setEnabled(b);
//		table.getTable().setEnabled(b);
	}

	public void init(SchemePath path, DataSourceInterface dataSource)
	{
		skip_change = true;
		setDefaults();
		this.path = path;

		compNameTextField.setText(path.getName());
		compNameTextField.setCaretPosition(0);

		String end = "";
		if (!path.endDeviceId.equals(""))
		{
			SchemeElement s_el = (SchemeElement)Pool.get(SchemeElement.typ, path.endDeviceId);
			end = s_el.getName();
		}
		endDevTextField.setText(end);
		endDevTextField.setCaretPosition(0);

		String start = "";
		if (!path.startDeviceId.equals(""))
		{
			SchemeElement s_el = (SchemeElement)Pool.get(SchemeElement.typ, path.startDeviceId);
			start = s_el.getName();
		}
		startDevTextField.setText(start);
		startDevTextField.setCaretPosition(0);

		typeComboBox.setSelected(Pool.get(TransmissionPathType.typ, path.typeId));

		PathTreeModel model = new PathTreeModel(path);
		utp = new UniTreePanel(aContext.getDispatcher(), aContext, model);
		//utp.setBorder(BorderFactory.createLoweredBevelBorder());
		utp.getTree().setRootVisible(false);
		scroll.getViewport().add(utp, BorderLayout.CENTER);

		undoCompName = path.getName();
		undoEndDevId = path.endDeviceId;
		undoStartDevId = path.startDeviceId;
		undoTypeId = path.typeId;

		undoPathLinks = new ArrayList();
		undoPeOrder = new HashMap();
		for (Iterator it = path.links.iterator(); it.hasNext(); )
		{
			PathElement pe = (PathElement)it.next();
			undoPeOrder.put(pe.getObjectId(), new Integer(pe.n));
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
		path.typeId = tpt.getId();
	}

	public void removeLink()
	{
		/*
		PathElement pe = (PathElement)table.getSelectedObject();
		if (pe != null)
		{
			path.links.remove(pe);
			table.setContents(path.links);
			table.updateUI();
		}

/*
		for (int i = 0; i < path.links.size(); i++)
		{
			PathElement pe = (PathElement)path.links.get(i);
			if (pe.link_id.equals(link_id))
			{
				path.links.remove(pe);
				DataSet ds = new DataSet(path.links);

				table.setContents(ds);
				table.updateUI();
				break;
			}
		}*/
	}

	public void selectLink(String link_id)
	{
		for (int i = 0; i < path.links.size(); i++)
		{
			PathElement pe = (PathElement)path.links.get(i);
			if (pe.getObjectId().equals(link_id))
			{
//				table.setSelected(pe);
				break;
			}
		}
	}

	public void addSelectedElements()
	{
		if (path.startDeviceId.length() == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Не введено начальное устройство",
																		"Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}

		if (element_to_add instanceof SchemeLink)
			PathBuilder.addLink(path.links, (SchemeLink)element_to_add);
		else if (element_to_add instanceof SchemeCableLink)
			PathBuilder.addCableLink(path.links, (SchemeCableLink)element_to_add);
		else if (element_to_add instanceof SchemeElement)
			PathBuilder.addSchemeElement(path.links, (SchemeElement)element_to_add);

//		table.setContents(path.links);
//		table.updateUI();

		updatePathElements();
		element_to_add = null;
	}

	public void setStartDevice()
	{
		if (element_to_add instanceof SchemeElement)
		{
			SchemeElement se = (SchemeElement)element_to_add;
			path.links.clear();
			PathElement pe = PathBuilder.addSchemeElement(path.links, se);
			if (pe != null)
			{
				startDevTextField.setText(se.getName());
				startDevTextField.setCaretPosition(0);
				path.startDeviceId = se.getId();
			}
			updatePathElements();
			element_to_add = null;
		}
	}

	public void updatePathElements()
	{
		PathTreeModel model = new PathTreeModel(path);
		utp.setModel(model);
	}

	public void setEndDevice()
	{
		if (element_to_add instanceof SchemeElement)
		{
			SchemeElement se = (SchemeElement)element_to_add;
			if (se.getInternalSchemeId().length() != 0)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Конечным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return;
			}
			endDevTextField.setText(se.getName());
			endDevTextField.setCaretPosition(0);
			path.endDeviceId = se.getId();
			element_to_add = null;
		}
	}
/*
	boolean hasAccessPort (SchemeElement se)
	{
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator pit = dev.ports.iterator(); pit.hasNext();)
			{
				SchemePort port = (SchemePort)pit.next();
				if (!port.access_port_type_id.equals(""))
					return true;
			}
		}

		for (Iterator it = se.element_ids.iterator(); it.hasNext();)
		{
			SchemeElement inner = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			if (hasAccessPort(inner))
				return true;
		}
		return false;
	}*/
/*
	boolean hasCablePort (ProtoElement proto)
	{
		for (Iterator it = proto.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			if (!dev.cableports.isEmpty())
				return true;
		}

		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			if (hasCablePort(p))
				return true;
		}
		return false;
	}*/

	public void undo()
	{
		path.typeId = undoTypeId;
		path.name = undoCompName;
		path.endDeviceId = undoEndDevId;
		path.startDeviceId = undoStartDevId;
		path.links = new ArrayList();
		for (Iterator it = undoPathLinks.iterator(); it.hasNext(); )
		{
			PathElement pe = (PathElement)it.next();
			pe.n = ((Integer)undoPeOrder.get(pe.getObjectId())).intValue();
			path.links.add(pe);
		}
	}

	public String getCompName()
	{
		return compNameTextField.getText();
	}

	public String getEndDiviceId()
	{
		return path.endDeviceId;
	}

	public String getStartDiviceId()
	{
		return path.startDeviceId;
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
			TransmissionPathType type = (TransmissionPathType)Pool.get(TransmissionPathType.typ, path.typeId);
			TransmissionPathType new_type = new TransmissionPathType();
			new_type.name = name;
			new_type.id = aContext.getDataSourceInterface().GetUId(TransmissionPathType.typ);
			path.typeId = new_type.getId();
			Pool.put(TransmissionPathType.typ, path.typeId, new_type);

			typeComboBox.add(new_type);
			typeComboBox.setSelected(new_type);
		}
	}
}

class PathTreeModel extends ObjectResourceTreeModel
{
	SchemePath path;
	Scheme topScheme;

	public PathTreeModel(SchemePath path)
	{
		this.path = path;
		topScheme = (Scheme)Pool.get(Scheme.typ, path.getSchemeId());
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
				"root",
				path.getName(),
				true,
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
			return ObjectResource.class;
		}
		if(node.getObject() instanceof Scheme)
			return ObjectResource.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				String cur_scheme_id = null;
//				List scheme_ids = new ArrayList();
				for (Iterator it = path.links.iterator(); it.hasNext();)
				{
					PathElement pe = (PathElement)it.next();
					if (pe.schemeId.equals(path.getSchemeId()))
					{
						cur_scheme_id = pe.schemeId;
						vec.add(new ObjectResourceTreeNode(pe, pe.getName(), true, true));
					}
					else if (!pe.schemeId.equals(cur_scheme_id))
					{
						cur_scheme_id = pe.schemeId;
						if (cur_scheme_id.length() != 0)
						{
							Scheme scheme = (Scheme)Pool.get(Scheme.typ, cur_scheme_id);
							vec.add(new ObjectResourceTreeNode(scheme, scheme.getName(), true, null));
						}
					}
				}
			}
		}
		else if (node.getObject() instanceof Scheme)
		{
			Scheme scheme = (Scheme)node.getObject();
			for (Iterator it = path.links.iterator(); it.hasNext();)
			{
				PathElement pe = (PathElement)it.next();
				if (pe.schemeId.equals(scheme.getId()))
					vec.add(new ObjectResourceTreeNode(pe, pe.getName(), true, true));
			}
		}
		return vec;
	}
/*
	ObjectResourceTreeNode addPathElement(PathElement pe)
	{
		if (pe.getType() == PathElement.SCHEME_ELEMENT)
		{
			SchemeElement se = (SchemeElement) Pool.get(SchemeElement.typ, pe.scheme_element_id);
			return new ObjectResourceTreeNode(se, se.getName(), true, null);
		}
		else if (pe.getType() == PathElement.CABLE_LINK)
		{
			SchemeCableLink link = (SchemeCableLink) Pool.get(SchemeCableLink.typ, pe.link_id);
			return new ObjectResourceTreeNode(link, link.getName(), true, null);
		}
		else if (pe.getType() == PathElement.LINK)
		{
			SchemeLink link = (SchemeLink) Pool.get(SchemeLink.typ, pe.link_id);
			return new ObjectResourceTreeNode(link, link.getName(), true, null);
		}
		return null;
	}*/
}

