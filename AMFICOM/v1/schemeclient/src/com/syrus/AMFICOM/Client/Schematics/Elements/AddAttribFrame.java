package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;

public class AddAttribFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	String selected = "Новый атрибут";

	Map h_named = new HashMap();
	ElementAttributeType type = new ElementAttributeType();

	private AComboBox jComboBox1 = new AComboBox();
	private JPanel panel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JTextField nameField = new JTextField();
	private JLabel name = new JLabel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	ApplicationContext aContext;

	public AddAttribFrame(Frame parent, String title, ApplicationContext aContext)
	{
		super(parent, title);
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 110);

		setLocation((screenSize.width-frameSize.width)/2, (screenSize.height-frameSize.height)/2);
		setSize(frameSize);
		setResizable(false);
		setTitle("Атрибут");

		name.setText("Название");

		nameField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelButton.doClick();
			}
			public void keyReleased(KeyEvent e)
			{
				okButton.setEnabled(!nameField.getText().equals(""));

			}
			public void keyTyped(KeyEvent e)
					{}
		});

		panel.setLayout(new BorderLayout());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		//name.setPreferredSize(new Dimension(65, 5));
		n_panel.add(name, BorderLayout.WEST);
		n_panel.add(nameField, BorderLayout.CENTER);

		panel.add(n_panel, BorderLayout.NORTH);
		okButton.setText("OK");
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});

		cancelButton.setText("Отмена");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		jComboBox1.addItem("Новый атрибут");
		jComboBox1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					comboBoxItemSelected();
				}
			}
		});

		panel.add(buttonPanel, BorderLayout.SOUTH);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jComboBox1, BorderLayout.NORTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		res = OK;
		if (selected.equals("Новый атрибут"))
		{
			type.name = nameField.getText();
			type.id = aContext.getDataSourceInterface().GetUId(ElementAttributeType.typ);
			Pool.put(ElementAttributeType.typ, type.getId(), type);
		}
		else
		{
			/*h = Pool.getMap(CharacteristicType.typ);
			for (Enumeration enum = h.elements(); enum.hasMoreElements();)
			{
				CharacteristicType type = (CharacteristicType)enum.nextElement();
				if (type.getName().equals(selected))

			}*/
		}

		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}

	void comboBoxItemSelected()
	{
		selected = (String)jComboBox1.getSelectedItem();
		boolean b = selected.equals("Новый атрибут");
		nameField.setEnabled(b);
		okButton.setEnabled(!b);
		if (!b)
		{
			nameField.setText(((ElementAttributeType)h_named.get(selected)).getName());
			nameField.setCaretPosition(0);
			type = (ElementAttributeType)h_named.get(selected);
		}
		else
		{
			type = new ElementAttributeType();
			nameField.setText("");
		}
	}

	public int showDialog(Collection attribs)
	{
		Map h = Pool.getMap(ElementAttributeType.typ);

		Map used = new HashMap(attribs.size());
		for (Iterator it = attribs.iterator(); it.hasNext();)
		{
			List el = (List)it.next();
			used.put(el.get(0), el);
		}
		if (h != null)
		{
			for (Iterator it = h.values().iterator(); it.hasNext();)
			{
				ElementAttributeType type = (ElementAttributeType)it.next();
				if (!used.containsKey(type.getName()))
					jComboBox1.addItem(type.getName());
				h_named.put(type.getName(), type);
			}
		}
		setModal(true);
		setVisible(true);
		return res;
	}

	public String getSelectedName()
	{
		return selected;
	}

	public ElementAttributeType getSelectedType()
	{
		return type;
	}
}

