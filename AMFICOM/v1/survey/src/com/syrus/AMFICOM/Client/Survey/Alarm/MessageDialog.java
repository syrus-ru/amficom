package com.syrus.AMFICOM.Client.Survey.Alarm;

import javax.swing.*;
import java.awt.*;
import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import java.awt.event.*;

public class MessageDialog extends JDialog
{
	private Message message;
	private XYLayout xYLayout1 = new XYLayout();
	private JPanel mainPanel = new JPanel();
	private JButton okButton = new JButton();
	private JTextPane jTextPane1 = new JTextPane();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JViewport jViewport1 = new JViewport();
	private JFrame parent;

	public MessageDialog(JFrame parent, String title, boolean modal, Message message)
	{
		super (parent, title, modal);
		this.message = message;
		this.parent = parent;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit () throws Exception
	{
		setSize (new Dimension(300, 210));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

		setResizable(false);
		mainPanel.setLayout(xYLayout1);

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});

		jTextPane1.setText(message.text);
		jTextPane1.setBorder(BorderFactory.createLoweredBevelBorder());
		jTextPane1.setEditable(false);

		jScrollPane1.setViewport(jViewport1);
		jViewport1.add(jTextPane1);

		mainPanel.add(jScrollPane1,  new XYConstraints(0, 0, 295, 150));
		mainPanel.add(okButton,   new XYConstraints(110, 156, 80, -1));

		this.getContentPane().add(mainPanel, null);
		this.setContentPane(mainPanel);
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}
}
