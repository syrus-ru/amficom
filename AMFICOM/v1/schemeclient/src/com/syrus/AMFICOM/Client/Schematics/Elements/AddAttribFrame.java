package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ElementAttributeType;

public class AddAttribFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	String selected = "Новый атрибут";
	Hashtable h;
	Hashtable h_named = new Hashtable();
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
			/*h = Pool.getHash(CharacteristicType.typ);
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

	public int showDialog(Vector attribs)
	{
		h = Pool.getHash(ElementAttributeType.typ);

		Hashtable used = new Hashtable(attribs.size());
		for (int i = 0; i < attribs.size(); i++)
		{
			Vector el = (Vector)attribs.get(i);
			used.put(el.get(0), el);
		}
		if (h != null)
		{
			for (Enumeration enum = h.elements(); enum.hasMoreElements();)
			{
				ElementAttributeType type = (ElementAttributeType)enum.nextElement();
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

