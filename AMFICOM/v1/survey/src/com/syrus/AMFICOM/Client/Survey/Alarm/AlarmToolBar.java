
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey.Alarm;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class AlarmToolBar extends JToolBar implements ApplicationModelListener{
	ApplicationModel aModel;

	JButton openButton = new JButton();
	JButton closeButton = new JButton();
	JButton helpButton = new JButton();
	JButton exitButton = new JButton();

	ImageIcon imageOpen = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif"));
	ImageIcon imageClose = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/closefile.gif"));
	ImageIcon imageHelp = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/help_.gif"));
	ImageIcon imageExit = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/exit.gif"));

	public AlarmToolBar()
	{
		super();
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
		MyToolBar_this_actionAdapter actionAdapter =
				new MyToolBar_this_actionAdapter(this);

		openButton.setToolTipText("Open File");
		openButton.setName("menuSessionOpen");
		openButton.setIcon(imageOpen);
		openButton.addActionListener(actionAdapter);
		closeButton.setToolTipText("Close File");
		closeButton.setName("menuSessionClose");
		closeButton.setIcon(imageClose);
		closeButton.addActionListener(actionAdapter);
		helpButton.setToolTipText("About");
		helpButton.setName("menuHelpAbout");
		helpButton.setIcon(imageHelp);
		helpButton.addActionListener(actionAdapter);
		exitButton.setToolTipText("Exit");
		exitButton.setName("menuSessionExit");
		exitButton.setIcon(imageExit);
		exitButton.addActionListener(actionAdapter);

		openButton.setText("");
		closeButton.setText("");
		helpButton.setText("");
		exitButton.setText("");
		this.add(openButton, null);
		this.add(closeButton, null);
		this.add(helpButton, null);
		this.add(exitButton, null);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		int count = e.length;
		int i;

		openButton.setVisible(aModel.isVisible("menuSessionOpen"));
		openButton.setEnabled(aModel.isEnabled("menuSessionOpen"));

		closeButton.setVisible(aModel.isVisible("menuSessionClose"));
		closeButton.setEnabled(aModel.isEnabled("menuSessionClose"));
	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}

}

class MyToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	AlarmToolBar adaptee;

	MyToolBar_this_actionAdapter(AlarmToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}


