package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort;

public class CablePortPropsPanel extends JPanel
{
	private AComboBox sortComboBox = new AComboBox();
	private ObjComboBox typeComboBox = new ObjComboBox(
			 PortTypeController.getInstance(),
			 PortTypeController.KEY_NAME);
	private JTextField nameText = new JTextField();
	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private String undoDescription;
	private boolean skip_changes = false;

	SchemeCablePort[] ports;
	List portTypes;
	PortType pt;
	ApplicationContext aContext;

	private static PortTypeSort[] portTypeSorts = new PortTypeSort[] {
		PortTypeSort.PORTTYPESORT_OPTICAL,
		PortTypeSort.PORTTYPESORT_THERMAL,
		PortTypeSort.PORTTYPESORT_ELECTRICAL
	};

	public CablePortPropsPanel(ApplicationContext aContext)
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
		JPanel p1 = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new BorderLayout());

		JPanel p11 = new JPanel(new BorderLayout());
		JPanel p12 = new JPanel(new BorderLayout());
		JPanel p13 = new JPanel(new BorderLayout());
		JPanel p21 = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);

		JPanel clLabelPanel = new JPanel();
		clLabelPanel.setPreferredSize(new Dimension (60, 10));
		clLabelPanel.add(new JLabel("Класс"));

		JPanel typeLabelPanel = new JPanel();
		typeLabelPanel.setPreferredSize(new Dimension (60, 10));
		typeLabelPanel.add(new JLabel("Тип"));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Название"));

		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("Описание"));

		p11.add(clLabelPanel, BorderLayout.WEST);
		p12.add(typeLabelPanel, BorderLayout.WEST);
		p13.add(nameLabelPanel, BorderLayout.WEST);
		p21.add(descrLabelPanel, BorderLayout.WEST);

		p11.add(sortComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);

		p12.add(addTypeButton, BorderLayout.EAST);

		typeComboBox.setPreferredSize(sortComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.CENTER);
		p1.add(p12, BorderLayout.SOUTH);
		p2.add(p13, BorderLayout.NORTH);
		p2.add(p21, BorderLayout.CENTER);

		addTypeButton.setPreferredSize(new Dimension(25, 7));
		addTypeButton.setBorder(BorderFactory.createEtchedBorder());
		addTypeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				addTypeButton_actionPerformed();
			}
		});
		sortComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					sortComboBox_stateChanged();
			}
		});

		typeComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					typeComboBox_stateChanged();
			}
		});
		nameText.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (ports == null || ports.length != 1)
					return;
				ports[0].name(nameText.getText());
				aContext.getDispatcher().notify(new SchemeElementsEvent(ports[0].id(), ports[0].name(), SchemeElementsEvent.CABLE_PORT_NAME_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		descriptionTextArea.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (pt == null)
					return;
				pt.setDescription(descriptionTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		descriptionTextArea.setPreferredSize(new Dimension (300, 80));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.PORTTYPE_ENTITY_CODE);
			portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (int i = 0; i < portTypeSorts.length; i++) {
				sortComboBox.addItem(portTypeSorts[i]);
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void setEditable(boolean b)
	{
		sortComboBox.setEnabled(b);

		addTypeButton.setEnabled(b);

	//	typeComboBox.setRenderer(new ComboBoxRenderer(typeComboBox));
		typeComboBox.setEnabled(b);
		if (ports != null && ports.length != 1)
			nameText.setEnabled(false);
		else
			nameText.setEnabled(b);
		descriptionTextArea.setEnabled(b);
	}

	public void init(SchemeCablePort[] ports)
	{
		this.ports = ports;
		if (ports.length > 0)
			pt = ports[0].portTypeImpl();
		else
		{
			setEditable(false);
			return;
		}
		if (pt != null)
		{
			sortComboBox.setSelectedItem(pt.getSort());
			typeComboBox.setSelectedItem(pt);
			descriptionTextArea.setText(pt.getDescription());
			undoDescription = pt.getDescription();
		}
		else
		{
			typeComboBox_stateChanged();
		}

		if (ports.length == 1)
		{
			nameText.setText(ports[0].name());
			nameText.setCaretPosition(0);
		}
		else
		{
			nameText.setText("<множественный выбор>");
			nameText.setCaretPosition(0);
			nameText.setEnabled(false);
		}
		updateUI();
	}

	public void undo()
	{
		if (pt != null)
			pt.setDescription(undoDescription);
	}

	public PortType getSelectedPortType()
	{
		return (PortType)typeComboBox.getSelectedItem();
	}

	public String getCompDescription()
	{
		return descriptionTextArea.getText();
	}

	void sortComboBox_stateChanged()
	{
		typeComboBox.removeAll();
		PortTypeSort sort = (PortTypeSort)sortComboBox.getSelectedItem();

		for (Iterator it = portTypes.iterator(); it.hasNext();)
		{
			PortType type = (PortType)it.next();
			if (type.getSort().equals(sort))
				typeComboBox.addItem(type);
		}
		if (pt != null)
			typeComboBox.setSelectedItem(pt);
	}

	void typeComboBox_stateChanged()
	{
		if (skip_changes)
			return;
		undo();

		pt = (PortType)typeComboBox.getSelectedItem();

		for (int i = 0; i < ports.length; i++)
			ports[i].portTypeImpl(pt);
		descriptionTextArea.setText(pt.getDescription());
//		aContext.getDispatcher().notify(new OperationEvent(cpt, 1, "elementslistvaluechanged"));
		aContext.getDispatcher().notify(new SchemeElementsEvent(ports, pt, SchemeElementsEvent.CABLE_PORT_TYPE_UPDATE_EVENT));
	}

	void addTypeButton_actionPerformed()
	{
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
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
			try {
				PortType new_type = PortType.createInstance(
						user_id,
						name,
						"",
						name,
						(PortTypeSort)sortComboBox.getSelectedItem());

				portTypes.add(new_type);
				ConfigurationStorableObjectPool.putStorableObject(new_type);
				typeComboBox.addItem(new_type);
				typeComboBox.setSelectedItem(new_type);
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}

		}
	}
}
