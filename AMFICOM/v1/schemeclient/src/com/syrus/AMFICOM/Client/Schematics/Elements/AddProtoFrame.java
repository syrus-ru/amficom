package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.Collection;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import oracle.jdeveloper.layout.*;

public class AddProtoFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	protected String proto_class;

	AComboBox jComboBox1 = new AComboBox();
	JPanel panel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JTextField nameField = new JTextField();
	JTextField descrField = new JTextField();
	JLabel name = new JLabel();
	JLabel descr = new JLabel();
	JButton okButton = new JButton();
	JButton cancelButton = new JButton();

	public AddProtoFrame(Frame parent, String title)
	{
		super(parent, title);
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
		setResizable(true);

		name.setText("Имя");
		descr.setText("Описание");

		nameField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{}
			public void keyReleased(KeyEvent e)
			{
				okButton.setEnabled(!nameField.getText().equals(""));
			}
			public void keyTyped(KeyEvent e)
			{}
		});

		panel.setLayout(new XYLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Новое"));
		panel.add(name, new XYConstraints(10, 0, -1, -1));
		panel.add(nameField, new XYConstraints(80, 0, 180, -1));
		panel.add(descr, new XYConstraints(10, 30, -1, -1));
		panel.add(descrField,  new XYConstraints(80, 30, 180, -1));

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

		jComboBox1.addItem("Новое");
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

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jComboBox1, BorderLayout.NORTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.addComponentListener (new ComponentListener()
		{
		 public void componentResized(ComponentEvent e)
		 {
			 Dimension d = getSize();
			 nameField.setSize(d.width - descr.getSize().width - 60, nameField.getSize().height);
			 descrField.setSize(d.width - descr.getSize().width - 60, descrField.getSize().height);
		 }
		 public void componentMoved(ComponentEvent e)
		 {}
		 public void componentShown(ComponentEvent e)
		 {}
		 public void componentHidden(ComponentEvent e)
		 {}
		});
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		res = OK;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}

	void comboBoxItemSelected()
	{
	}

	public int showDialog(String ch_class, Collection chars)
	{
		setModal(true);
		setVisible(true);
		return res;
	}

	public String getSelectedName()
	{
		return "";
	}

	public String getSelectedType()
	{
		return "";
	}
}
