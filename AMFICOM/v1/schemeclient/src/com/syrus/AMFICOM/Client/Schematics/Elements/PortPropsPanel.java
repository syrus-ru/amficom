package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;

public class PortPropsPanel extends JPanel
{
	private AComboBox classComboBox = new AComboBox();
	private JButton addClassButton = new JButton("...");
	private ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox();
	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField nameText = new JTextField();
	private JCheckBox isAccessCheckBox = new JCheckBox();
	private ObjectResourceComboBox accessTypeComboBox = new ObjectResourceComboBox();
	private String undoDescription;
	ApplicationContext aContext;
	private boolean skip_changes = false;

	SchemePort[] ports;
	PortType pt;

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

		p11.add(classComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);
		p23.add(accessTypeComboBox, BorderLayout.CENTER);
		p22.add(p23, BorderLayout.CENTER);

		p11.add(addClassButton, BorderLayout.EAST);
		p12.add(addTypeButton, BorderLayout.EAST);
		p23.add(isAccessCheckBox, BorderLayout.WEST);

		typeComboBox.setPreferredSize(classComboBox.getPreferredSize());
		accessTypeComboBox.setPreferredSize(classComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.CENTER);
		p1.add(p12, BorderLayout.SOUTH);
		p2.add(p13, BorderLayout.NORTH);
		p2.add(p21, BorderLayout.CENTER);
		p2.add(p22, BorderLayout.SOUTH);

		addClassButton.setPreferredSize(new Dimension(25, 7));
		addClassButton.setBorder(BorderFactory.createEtchedBorder());
		addClassButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				addClassButton_actionPerformed();
			}
		});
		addTypeButton.setPreferredSize(new Dimension(25, 7));
		addTypeButton.setBorder(BorderFactory.createEtchedBorder());
		addTypeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				addTypeButton_actionPerformed();
			}
		});
		classComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					classComboBox_stateChanged();
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
				ports[0].name = nameText.getText();
				aContext.getDispatcher().notify(new SchemeElementsEvent(ports[0].getId(), ports[0].name, SchemeElementsEvent.PORT_NAME_UPDATE_EVENT));
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
				pt.description = descriptionTextArea.getText();
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		descriptionTextArea.setPreferredSize(new Dimension (300, 80));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);
	}

	private void setDefaults()
	{
		skip_changes = true;
		classComboBox.removeAllItems();
		Hashtable hash = new Hashtable();

		if (Pool.getHash(PortType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(PortType.typ).elements(); enum.hasMoreElements();)
			{
				PortType pt = (PortType)enum.nextElement();
				hash.put(pt.p_class, pt.p_class);
			}
			for(Enumeration enum = hash.elements(); enum.hasMoreElements(); )
				classComboBox.addItem(enum.nextElement());

			if (pt != null)
			{
				classComboBox.setSelectedItem(pt.p_class);
			}
		}
		skip_changes = false;

		if (Pool.getHash(AccessPortType.typ) != null)
		{
			accessTypeComboBox.setContents(Pool.getHash(AccessPortType.typ), false);
		}
	}

	public void setEditable(boolean b)
	{
		classComboBox.setEnabled(b);
		addClassButton.setEnabled(b);
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
		pt = (PortType)Pool.get(PortType.typ, ports[0].port_type_id);
		setDefaults();
		if (pt != null)
		{
			classComboBox.setSelectedItem(pt.p_class);
			typeComboBox.setSelectedItem(pt);
			descriptionTextArea.setText(pt.description);

			undoDescription = pt.description;
		}
		else
		{
			typeComboBox_stateChanged();
		}

		if (ports.length != 0)
		{
			boolean b = ports[0].is_access_port;
			for (int i = 0; i < ports.length; i++)
			{
				if (ports[i].is_access_port != b)
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
				nameText.setText(ports[0].getName());
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
			pt.description = undoDescription;
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
				ports[i].is_access_port = true;
				//System.out.println("setting for " + ports[i].getId() + " access true");
				ports[i].access_port_type_id =
						((AccessPortType)accessTypeComboBox.getSelectedItem()).getId();
			}
		}
		else
		{
			for (int i = 0; i < ports.length; i++)
			{
				System.out.println("setting for " + ports[i].getId() + " access false");
				ports[i].is_access_port = false;
			}
		}
	}

	void classComboBox_stateChanged()
	{
		typeComboBox.removeAllItems();
		String selected_class = (String)classComboBox.getSelectedItem();

		if (Pool.getHash(PortType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(PortType.typ).elements(); enum.hasMoreElements();)
			{
				PortType pt = (PortType)enum.nextElement();
				if (pt.p_class.equals(selected_class))
					typeComboBox.addItem(pt);
			}
			if (pt != null)
				typeComboBox.setSelectedItem(pt);
		}
	}

	void typeComboBox_stateChanged()
	{
		if (skip_changes)
			return;
		undo();

		PortType cpt = (PortType)typeComboBox.getSelectedItem();
		pt = cpt;

		for (int i = 0; i < ports.length; i++)
			ports[i].port_type_id = cpt.getId();
		descriptionTextArea.setText(cpt.description);
		//aContext.getDispatcher().notify(new OperationEvent(cpt, 1, "elementslistvaluechanged"));
		aContext.getDispatcher().notify(new SchemeElementsEvent(ports, cpt, SchemeElementsEvent.PORT_TYPE_UPDATE_EVENT));
	}

	void addClassButton_actionPerformed()
	{
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый класс");
		dialog.setSize(this.getSize().width, dialog.preferredSize.height);
		Point loc = this.getLocationOnScreen();
		dialog.setLocation(loc.x, loc.y + 30);
		dialog.setVisible(true);

		if (dialog.getStatus() == dialog.OK && !dialog.getName().equals(""))
		{
			String name = dialog.getName();
			for (int i = 0; i < classComboBox.getItemCount(); i++)
			{
				if (classComboBox.getItemAt(i).equals(name))
				{
					classComboBox.setSelectedItem(name);
					return;
				}
			}
			classComboBox.addItem(name);
			classComboBox.setSelectedItem(name);
		}
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
			PortType type = (PortType)Pool.get(PortType.typ, ports[0].port_type_id);
			PortType new_type = new PortType();
			new_type.is_modified = true;
			new_type.name = name;
			new_type.id = aContext.getDataSourceInterface().GetUId(PortType.typ);
			new_type.p_class = (String)classComboBox.getSelectedItem();
			for (int i = 0; i < ports.length; i++)
				ports[i].port_type_id = new_type.getId();
			Pool.put(PortType.typ, ports[0].port_type_id, new_type);

			typeComboBox.add(new_type);
			typeComboBox.setSelected(new_type);
		}
	}
}