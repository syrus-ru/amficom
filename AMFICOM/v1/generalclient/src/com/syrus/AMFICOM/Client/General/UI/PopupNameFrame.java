package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import oracle.jdeveloper.layout.VerticalFlowLayout;

public class PopupNameFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	public Dimension preferredSize = new Dimension(300, 97);
	public Dimension minimumSize = new Dimension(200, 97);
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Отмена");
	int status = CANCEL;

	JTextField compNameTextField = new JTextField();

	public PopupNameFrame(JFrame parent, String title)
	{
		super (parent);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setTitle(title);
	}

	private void jbInit() throws Exception
	{
		this.getContentPane().setLayout(new VerticalFlowLayout());
		this.setSize(preferredSize);
		this.setResizable(false);
		this.setModal(true);

		compNameTextField.addKeyListener(new KeyListener()
		{
			public void keyReleased(KeyEvent e) { }
			public void keyTyped(KeyEvent e) { }
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelButton.doClick();
			}
		});

		this.getContentPane().add(compNameTextField, VerticalFlowLayout.TOP);

		// BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(300, 30));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
		buttonPanel.setLayout(new FlowLayout());
		//okButton.setPreferredSize(new Dimension(80, 25));
		//cancelButton.setPreferredSize(new Dimension(80, 25));
		buttonPanel.add(okButton, FlowLayout.LEFT);
		buttonPanel.add(cancelButton, FlowLayout.CENTER);
		this.getContentPane().add(buttonPanel, VerticalFlowLayout.MIDDLE);

		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				this_addOkButtonActionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				dispose();
			}
		});
	}

	void this_addOkButtonActionPerformed()
	{
		status = OK;
		dispose();
	}

	public int getStatus()
	{
		return status;
	}

	public String getName()
	{
		return compNameTextField.getText();
	}
}


