package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathBuilder;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeModel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.general.ui_.tree.UniTreePanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class PathPropsPanel extends JPanel
{
	private ObjComboBox typeComboBox = new ObjComboBox(
			 TransmissionPathTypeController.getInstance(),
			 TransmissionPathTypeController.KEY_NAME);
	private JButton addTypeButton = new JButton("...");
	private JTextField compNameTextField = new JTextField();
	private JTextField startDevTextField = new JTextField();
	private JTextField endDevTextField = new JTextField();
//	ObjectResourceTablePane table = new ObjectResourceTablePane();
	//JTable table = new JTable();

	private String undoCompName;
	private TransmissionPathType undoTypeId;
	private List undoPathLinks;
	private Map undoPeOrder;
	boolean skip_change = false;

//	public ArrayList elements_to_add = new ArrayList();
	Object element_to_add;
	List pathTypes;
	SchemePath path;
	ApplicationContext aContext;
	List links;

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

		startDevTextField.setEnabled(false);
		endDevTextField.setEnabled(false);

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
				this_addTypeButtonActionPerformed();
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
				if (path == null || path.type() == null)
					return;
				path.name(compNameTextField.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
			pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
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

	public void init(SchemePath path)
	{
		skip_change = true;
		this.path = path;

		compNameTextField.setText(path.name());
		compNameTextField.setCaretPosition(0);


		if (path.endDevice() != null)
			endDevTextField.setText(path.endDevice().name());
		else
			endDevTextField.setText("");
		endDevTextField.setCaretPosition(0);

		if (path.startDevice() != null)
			startDevTextField.setText(path.startDevice().name());
		else
			startDevTextField.setText("");
		startDevTextField.setCaretPosition(0);

		typeComboBox.setSelectedItem(path.typeImpl());
		links = Arrays.asList(path.links());

		PathTreeModel model = new PathTreeModel(path);
		utp = new UniTreePanel(aContext.getDispatcher(), aContext, model);
		//utp.setBorder(BorderFactory.createLoweredBevelBorder());
		utp.getTree().setRootVisible(false);
		scroll.getViewport().add(utp, BorderLayout.CENTER);

		undoCompName = path.name();
		undoTypeId = path.typeImpl();

		undoPathLinks = new ArrayList();
		undoPeOrder = new HashMap();
		for (int i = 0 ; i < path.links().length; i++)
		{
			PathElement pe = path.links()[i];
			undoPeOrder.put(pe.abstractSchemeElement(), new Integer(pe.sequentialNumber()));
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
		path.typeImpl(tpt);
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

	public void setSelectedElement(Object element)
	{
		element_to_add = element;
	}

	public void addSelectedElements()
	{
		if (path.startDevice() == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Не введено начальное устройство",
																		"Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}

		if (element_to_add instanceof SchemeLink)
			PathBuilder.addLink(links, (SchemeLink)element_to_add);
		else if (element_to_add instanceof SchemeCableLink)
			PathBuilder.addCableLink(links, (SchemeCableLink)element_to_add);
		else if (element_to_add instanceof SchemeElement)
			PathBuilder.addSchemeElement(links, (SchemeElement)element_to_add);

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
			links.clear();
			PathElement pe = PathBuilder.addSchemeElement(links, se);
			if (pe != null)
			{
				startDevTextField.setText(se.name());
				startDevTextField.setCaretPosition(0);
				path.startDevice(se);
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
			if (se.internalScheme() != null)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Конечным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return;
			}
			endDevTextField.setText(se.name());
			endDevTextField.setCaretPosition(0);
			path.endDevice(se);
			element_to_add = null;
		}
	}

	public void undo()
	{
		path.typeImpl(undoTypeId);
		path.name(undoCompName);

		PathElement[] pes = new PathElement[undoPathLinks.size()];
		Iterator it = undoPathLinks.iterator();
		for (int i = 0; i < pes.length; i++ )
		{
			pes[i] = (PathElement)it.next();
			pes[i].sequentialNumber(((Integer)undoPeOrder.get(pes[i].abstractSchemeElement())).intValue());
		}
		path.links(pes);
	}

	public String getCompName()
	{
		return compNameTextField.getText();
	}

	public SchemeElement getEndDivice()
	{
		return path.endDevice();
	}

	public SchemeElement getStartDivice()
	{
		return path.startDevice();
	}

	void this_addTypeButtonActionPerformed()
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
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().user_id);

			TransmissionPathType new_type = null;
			try {
				new_type = TransmissionPathType.createInstance(
						user_id,
						name,
						"",
						name);
				pathTypes.add(new_type);
				typeComboBox.addItem(new_type);
				typeComboBox.setSelectedItem(new_type);
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}

		}
	}
}

class PathTreeModel extends ObjectResourceTreeModel
{
	SchemePath path;

	public PathTreeModel(SchemePath path)
	{
		this.path = path;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
				"root",
				path.name(),
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
			return Object.class;
		}
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
/*		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				Scheme cur_scheme = null;
//				List scheme_ids = new ArrayList();
				for (int i = 0; i < path.links().length; i++)
				{
					PathElement pe = (PathElement)path.links()[i];
					if (pe.scheme().equals(path.getSchemeId()))
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
		}*/
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

