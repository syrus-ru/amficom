package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemePort;

public class PortPropsPanel extends JPanel
{
	private AComboBox sortComboBox = new AComboBox();
	private ObjComboBox typeComboBox = new ObjComboBox(
			 PortTypeController.getInstance(),
			 PortTypeController.KEY_NAME);

	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField nameText = new JTextField();
	private JCheckBox isAccessCheckBox = new JCheckBox();
	private ObjComboBox accessTypeComboBox = new ObjComboBox(
			 MeasurementPortController.getInstance(),
			 MeasurementPortController.KEY_NAME);
	private String undoDescription;
	ApplicationContext aContext;
	private boolean skip_changes = false;

	SchemePort[] ports;
	List portTypes;
	PortType pt;

	private static PortTypeSort[] portTypeSorts = new PortTypeSort[] {
		PortTypeSort.PORTTYPESORT_OPTICAL,
		PortTypeSort.PORTTYPESORT_THERMAL,
		PortTypeSort.PORTTYPESORT_ELECTRICAL
	};

	public PortPropsPanel(ApplicationContext aContext)
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
		JPanel p22 = new JPanel(new BorderLayout());
		JPanel p23 = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);

		isAccessCheckBox.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				isAccessCheckBox_stateChanged();
			}
		});
		accessTypeComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					accessTypeComboBox_stateChanged();
			}
		});

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

		JPanel accessLabelPanel = new JPanel();
		accessLabelPanel.setPreferredSize(new Dimension (60, 10));
		accessLabelPanel.add(new JLabel("Тестовый"));

		p11.add(clLabelPanel, BorderLayout.WEST);
		p12.add(typeLabelPanel, BorderLayout.WEST);
		p13.add(nameLabelPanel, BorderLayout.WEST);
		p21.add(descrLabelPanel, BorderLayout.WEST);
		p22.add(accessLabelPanel, BorderLayout.WEST);

		p11.add(sortComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);
		p23.add(accessTypeComboBox, BorderLayout.CENTER);
		p22.add(p23, BorderLayout.CENTER);

		p12.add(addTypeButton, BorderLayout.EAST);
		p23.add(isAccessCheckBox, BorderLayout.WEST);

		typeComboBox.setPreferredSize(sortComboBox.getPreferredSize());
		accessTypeComboBox.setPreferredSize(sortComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.CENTER);
		p1.add(p12, BorderLayout.SOUTH);
		p2.add(p13, BorderLayout.NORTH);
		p2.add(p21, BorderLayout.CENTER);
		p2.add(p22, BorderLayout.SOUTH);

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
				aContext.getDispatcher().notify(new SchemeElementsEvent(ports[0].id(), ports[0].name(), SchemeElementsEvent.PORT_NAME_UPDATE_EVENT));
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
				if (ports == null || ports.length != 1)
					return;
				ports[0].name(nameText.getText());
				ports[0].description(descriptionTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		descriptionTextArea.setPreferredSize(new Dimension (300, 80));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);

		try {
			DomainCondition condition = new DomainCondition(null, ObjectEntities.PORTTYPE_ENTITY_CODE);
			portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (int i = 0; i < portTypeSorts.length; i++) {
				sortComboBox.addItem(portTypeSorts[i]);
			}

			condition = new DomainCondition(null, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			List measurementPortTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			accessTypeComboBox.addElements(measurementPortTypes);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void setEditable(boolean b)
	{
		sortComboBox.setEnabled(b);
		addTypeButton.setEnabled(b);
		typeComboBox.setEnabled(b);
		isAccessCheckBox.setEnabled(b);
		accessTypeComboBox.setEnabled(b);

		if (ports != null && ports.length != 1)
			nameText.setEnabled(false);
		else
			nameText.setEnabled(b);
		descriptionTextArea.setEnabled(b);
	}

	public void init(SchemePort[] ports)
	{
		this.ports = ports;

		if (ports.length != 0)
		{
			pt = ports[0].portTypeImpl();

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

			boolean b = ports[0].measurementPortType() != null;
			for (int i = 0; i < ports.length; i++)
			{
				if (ports[i].measurementPortType() != null != b)
				{
					isAccessCheckBox.setEnabled(false);
					accessTypeComboBox.setEnabled(false);
					return;
				}
			}
			isAccessCheckBox.setSelected(b);
			accessTypeComboBox.setEnabled(b);

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

	void isAccessCheckBox_stateChanged()
	{
		boolean b = isAccessCheckBox.isSelected();
		accessTypeComboBox.setEnabled(b);
		accessTypeComboBox_stateChanged();
	}

	void accessTypeComboBox_stateChanged()
	{
		if (!isAccessCheckBox.isEnabled())
			return;
		if (isAccessCheckBox.isSelected())
		{
			for (int i = 0; i < ports.length; i++)
			{
				//System.out.println("setting for " + ports[i].getId() + " access true");
				ports[i].measurementPortTypeImpl(
								((MeasurementPortType)accessTypeComboBox.getSelectedItem()));
			}
		}
	}

	void sortComboBox_stateChanged()
	{
		typeComboBox.removeAll();
		PortTypeSort selected_sort = (PortTypeSort)sortComboBox.getSelectedItem();

		for(Iterator it = portTypes.iterator(); it.hasNext();)
		{
			PortType pt = (PortType)it.next();
			if (pt.getSort().equals(selected_sort))
				typeComboBox.addItem(pt);
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
		//aContext.getDispatcher().notify(new OperationEvent(cpt, 1, "elementslistvaluechanged"));
		aContext.getDispatcher().notify(new SchemeElementsEvent(ports, pt, SchemeElementsEvent.PORT_TYPE_UPDATE_EVENT));
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
			PortType type = ports[0].portTypeImpl();
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			PortType new_type = null;
			try {
				new_type = PortType.createInstance(
						user_id,
						name,
						"",
						name,
						(PortTypeSort)sortComboBox.getSelectedItem());

				for (int i = 0; i < ports.length; i++) {
					ports[i].portTypeImpl(new_type);

				}
				ConfigurationStorableObjectPool.putStorableObject(new_type);

				typeComboBox.addItem(new_type);
				typeComboBox.setSelectedItem(new_type);
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
		}
	}
}

