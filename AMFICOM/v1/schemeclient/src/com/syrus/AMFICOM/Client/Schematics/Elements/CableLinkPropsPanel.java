package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class CableLinkPropsPanel extends JPanel
{
	private AComboBox classComboBox = new AComboBox();
	private JButton addClassButton = new JButton("...");
	private ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox();
	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField manufacturerTextField = new JTextField();
	private JTextField nameText = new JTextField();
	private JTextField optLen = new JTextField();
	private JTextField strLen = new JTextField();
	private String undoDescr;
	private String undoManufacturer;
	ApplicationContext aContext;
	private boolean skip_changes = false;

	private boolean smooth_length = false;

	SchemeCableLink[] links;
	CableLinkType lt;

	public CableLinkPropsPanel(ApplicationContext aContext)
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
		JPanel p221 = new JPanel(new BorderLayout());
		JPanel p222 = new JPanel(new BorderLayout());

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

		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setPreferredSize(new Dimension (60, 10));
		manLabelPanel.add(new JLabel("Произв."));

		JPanel lengthLabelPanel = new JPanel();
		lengthLabelPanel.setPreferredSize(new Dimension (60, 10));
		lengthLabelPanel.add(new JLabel("Длина(м)"));

		JPanel lenPanel1 = new JPanel(new BorderLayout());
		JPanel lenPanel2 = new JPanel(new BorderLayout());

		lenPanel1.add(new JLabel(" Строит. "), BorderLayout.WEST);
		lenPanel1.add(strLen, BorderLayout.CENTER);
		lenPanel2.add(new JLabel(" Оптич. "), BorderLayout.WEST);
		lenPanel2.add(optLen, BorderLayout.CENTER);

		JSplitPane lenPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lenPanel1, lenPanel2);
		lenPanel.setBorder(BorderFactory.createEmptyBorder());
		lenPanel.setResizeWeight(.5);
		lenPanel.setDividerSize(0);

		p11.add(clLabelPanel, BorderLayout.WEST);
		p12.add(typeLabelPanel, BorderLayout.WEST);
		p13.add(nameLabelPanel, BorderLayout.WEST);
		p21.add(descrLabelPanel, BorderLayout.WEST);
		p221.add(manLabelPanel, BorderLayout.WEST);
		p222.add(lengthLabelPanel, BorderLayout.WEST);

		p11.add(classComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);
		p221.add(manufacturerTextField, BorderLayout.CENTER);
		p222.add(lenPanel, BorderLayout.CENTER);

		p11.add(addClassButton, BorderLayout.EAST);
		p12.add(addTypeButton, BorderLayout.EAST);

		typeComboBox.setPreferredSize(classComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.NORTH);
		p1.add(p12, BorderLayout.CENTER);
		p1.add(p13, BorderLayout.SOUTH);
		p2.add(p21, BorderLayout.CENTER);
		p2.add(p22, BorderLayout.SOUTH);
		p22.add(p221, BorderLayout.CENTER);
		p22.add(p222, BorderLayout.SOUTH);

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
		descriptionTextArea.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (lt == null)
					return;
				lt.description = descriptionTextArea.getText();
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		manufacturerTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (lt == null)
					return;
				lt.manufacturer = manufacturerTextField.getText();
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		nameText.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				links[0].name = nameText.getText();
				aContext.getDispatcher().notify(new SchemeElementsEvent(links[0].getId(), links[0].name, SchemeElementsEvent.CABLE_LINK_NAME_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		optLen.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				try
				{
					double d = Double.parseDouble(optLen.getText());
					links[0].optical_length = d;
					optLen.setForeground(nameText.getForeground());
					if (smooth_length)
					{
						links[0].physical_length = d;
						strLen.setText(optLen.getText());
						strLen.setForeground(nameText.getForeground());
					}
				}
				catch (NumberFormatException e)
				{
					optLen.setForeground(Color.red);
				}
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		strLen.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				try
				{
					double d = Double.parseDouble(strLen.getText());
					links[0].physical_length = d;
					strLen.setForeground(nameText.getForeground());
					if (smooth_length)
					{
						links[0].optical_length = d;
						optLen.setText(strLen.getText());
						optLen.setForeground(nameText.getForeground());
					}
				}
				catch (NumberFormatException e)
				{
					strLen.setForeground(Color.red);
				}
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

		if (Pool.getHash(CableLinkType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(CableLinkType.typ).elements(); enum.hasMoreElements();)
			{
				CableLinkType pt = (CableLinkType)enum.nextElement();
				hash.put(pt.link_class, pt.link_class);
			}
			for(Enumeration enum = hash.elements(); enum.hasMoreElements(); )
				classComboBox.addItem(enum.nextElement());

			if (lt != null)
			{
				classComboBox.setSelectedItem(lt.link_class);
			}
		}
		skip_changes = false;
	}

	public void setEditable(boolean b)
	{
		classComboBox.setEnabled(b);
		addClassButton.setEnabled(b);
		addTypeButton.setEnabled(b);
		typeComboBox.setEnabled(b);
		typeComboBox.setEnabled(b);
		if (links != null && links.length != 1)
		{
			nameText.setEnabled(false);
			optLen.setEnabled(false);
			strLen.setEnabled(false);
		}
		else
		{
			nameText.setEnabled(b);
			optLen.setEnabled(b);
			strLen.setEnabled(b);
		}
		descriptionTextArea.setEnabled(b);
		manufacturerTextField.setEnabled(b);
	}

	public void init(SchemeCableLink[] links)
	{
		this.links = links;
		lt = (CableLinkType)Pool.get(CableLinkType.typ, links[0].cable_link_type_id);
		setDefaults();
		if (lt != null)
		{
			classComboBox.setSelectedItem(lt.link_class);
			typeComboBox.setSelected(lt);
			descriptionTextArea.setText(lt.description);

			undoManufacturer = lt.manufacturer;
			undoDescr = lt.description;
		}
		else
			typeComboBox_stateChanged();

		if (links.length == 1)
		{
			if (links[0].optical_length == 0 || links[0].physical_length == 0)
				smooth_length = true;

			nameText.setText(links[0].getName());
			nameText.setCaretPosition(0);
			optLen.setText(String.valueOf(links[0].optical_length));
			strLen.setText(String.valueOf(links[0].physical_length));
		}
		else
		{
			nameText.setText("<множественный выбор>");
			nameText.setCaretPosition(0);
			optLen.setText("<...>");
			strLen.setText("<...>");
			nameText.setEnabled(false);
			optLen.setEnabled(false);
			strLen.setEnabled(false);
		}
		updateUI();
	}

	public void undo()
	{
		if (lt != null)
		{
			lt.description = undoDescr;
			lt.manufacturer = undoManufacturer;
		}
	}

	public CableLinkType getSelectedLinkType()
	{
		return (CableLinkType)typeComboBox.getSelectedItem();
	}

	void classComboBox_stateChanged()
	{
		typeComboBox.setContents(new Hashtable(), false);
		String selected_class = (String)classComboBox.getSelectedItem();

		if (Pool.getHash(CableLinkType.typ) != null)
		{
			for(Enumeration enum = Pool.getHash(CableLinkType.typ).elements(); enum.hasMoreElements();)
			{
				CableLinkType clt = (CableLinkType)enum.nextElement();
				if (clt.link_class.equals(selected_class))
					typeComboBox.add(clt);
			}
			if (lt != null)
				typeComboBox.setSelected(lt);
		}
		typeComboBox_stateChanged();
	}

	void typeComboBox_stateChanged()
	{
		if (skip_changes)
			return;
		undo();
		if (typeComboBox.getItemCount() == 0)
			return;
		CableLinkType clt = (CableLinkType)typeComboBox.getSelectedItem();
		lt = clt;
		for (int i = 0; i < links.length; i++)
		{
			links[i].cable_link_type_id = clt.getId();
			links[i].cable_threads = new ArrayList();
			for (int j = 0; j < clt.cable_threads.size(); j++)
			{
				CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(j);
				SchemeCableThread scheme_cable_thread = new SchemeCableThread(aContext.getDataSourceInterface().GetUId(SchemeCableThread.typ));
				scheme_cable_thread.link_type_id = ctt.link_type_id;
				scheme_cable_thread.name = ctt.getName();
				links[i].cable_threads.add(scheme_cable_thread);
			}
		}
		descriptionTextArea.setText(clt.description);
		manufacturerTextField.setText(clt.manufacturer);
		manufacturerTextField.setCaretPosition(0);
		descriptionTextArea.setCaretPosition(0);
		//aContext.getDispatcher().notify(new OperationEvent(clt, 1, "elementslistvaluechanged"));
	 // aContext.getDispatcher().notify(new SchemeElementsEvent(links, clt, SchemeElementsEvent.OBJECT_TYPE_UPDATE_EVENT));
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
			CableLinkType type = (CableLinkType)Pool.get(CableLinkType.typ, links[0].cable_link_type_id);
			CableLinkType new_type = new CableLinkType();
			new_type.is_modified = true;
			new_type.name = name;
			new_type.id = aContext.getDataSourceInterface().GetUId(CableLinkType.typ);
			new_type.link_class = (String)classComboBox.getSelectedItem();

			for (int i = 0; i < links.length; i++)
				links[i].cable_link_type_id = new_type.getId();
			Pool.put(CableLinkType.typ, links[0].cable_link_type_id, new_type);

			typeComboBox.add(new_type);
			typeComboBox.setSelected(new_type);
		}
	}
}
