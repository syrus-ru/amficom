package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;

public class CablePortPropsPanel extends JPanel
{
	private AComboBox classComboBox = new AComboBox();
	private JButton addClassButton = new JButton("...");
	private ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox();
	private JTextField nameText = new JTextField();
	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private String undoDescription;
	private boolean skip_changes = false;

	SchemeCablePort[] ports;
	CablePortType pt;
	ApplicationContext aContext;

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

		p11.add(classComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);

		p11.add(addClassButton, BorderLayout.EAST);
		p12.add(addTypeButton, BorderLayout.EAST);

		typeComboBox.setPreferredSize(classComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.CENTER);
		p1.add(p12, BorderLayout.SOUTH);
		p2.add(p13, BorderLayout.NORTH);
		p2.add(p21, BorderLayout.CENTER);

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
				aContext.getDispatcher().notify(new SchemeElementsEvent(ports[0].getId(), ports[0].name, SchemeElementsEvent.CABLE_PORT_NAME_UPDATE_EVENT));
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

		if (Pool.getHash(CablePortType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(CablePortType.typ).elements(); enum.hasMoreElements();)
			{
				CablePortType pt = (CablePortType)enum.nextElement();
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
	}

	public void setEditable(boolean b)
	{
		classComboBox.setEnabled(b);

		addClassButton.setEnabled(b);
		addTypeButton.setEnabled(b);

	//	typeComboBox.setRenderer(new ComboBoxRenderer(typeComboBox));
		typeComboBox.setEnabled(b);
		if (ports != null && ports.length != 1)
			nameText.setEnabled(false);
		else
			nameText.setEnabled(b);
		descriptionTextArea.setEnabled(b);
	}

	class ComboBoxRenderer extends DefaultListCellRenderer
			//JLabel implements ListCellRenderer
	{
		private final Color back;
		JComboBox box;

		public ComboBoxRenderer(JComboBox box)
		{
			this.box = box;

			int r = SystemColor.window.getRed();
			int g = SystemColor.window.getGreen();
			int b = SystemColor.window.getBlue();

			back = new Color(r < 25 ? 0 : r - 25,
											 g < 25 ? 0 : g - 25,
											 b < 25 ? 0 : b - 25);
		}

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			Component c = super.getListCellRendererComponent(
				list,
				value,
				index,
				isSelected,
				cellHasFocus);

			if (box.isEnabled())
			{
				c.setBackground(back);
				c.setForeground(Color.blue);
			}
			if (!box.isEnabled())
			{
				c.setBackground(back);
				c.setForeground(Color.yellow);
			}

			return c;
		}
	}

	public void init(SchemeCablePort[] ports)
	{
		this.ports = ports;
		pt = (CablePortType)Pool.get(CablePortType.typ, ports[0].cable_port_type_id);
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

		updateUI();
	}

	public void undo()
	{
		if (pt != null)
			pt.description = undoDescription;
	}

	public CablePortType getSelectedPortType()
	{
		return (CablePortType)typeComboBox.getSelectedItem();
	}

	public String getCompDescription()
	{
		return descriptionTextArea.getText();
	}

	void classComboBox_stateChanged()
	{
		typeComboBox.removeAllItems();
		String selected_class = (String)classComboBox.getSelectedItem();

		if (Pool.getHash(CablePortType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(CablePortType.typ).elements(); enum.hasMoreElements();)
			{
				CablePortType pt = (CablePortType)enum.nextElement();
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

		CablePortType cpt = (CablePortType)typeComboBox.getSelectedItem();
		pt = cpt;

		for (int i = 0; i < ports.length; i++)
			ports[i].cable_port_type_id = cpt.getId();
		descriptionTextArea.setText(cpt.description);
//		aContext.getDispatcher().notify(new OperationEvent(cpt, 1, "elementslistvaluechanged"));
		aContext.getDispatcher().notify(new SchemeElementsEvent(ports, cpt, SchemeElementsEvent.CABLE_PORT_TYPE_UPDATE_EVENT));
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
			CablePortType type = (CablePortType)Pool.get(CablePortType.typ, ports[0].cable_port_type_id);
			CablePortType new_type = new CablePortType();
			new_type.is_modified = true;
			new_type.name = name;
			new_type.id = aContext.getDataSourceInterface().GetUId(CablePortType.typ);
			new_type.p_class = (String)classComboBox.getSelectedItem();
			for (int i = 0; i < ports.length; i++)
				ports[i].cable_port_type_id = new_type.getId();
			Pool.put(CablePortType.typ, ports[0].cable_port_type_id, new_type);

			typeComboBox.addItem(new_type);
			typeComboBox.setSelectedItem(new_type);
		}
	}
}