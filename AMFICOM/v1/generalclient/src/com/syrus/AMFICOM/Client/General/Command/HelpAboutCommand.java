package com.syrus.AMFICOM.Client.General.Command;

import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.UI.AboutBoxPanel;

public class HelpAboutCommand extends VoidCommand
{
	Window parent;
	JPanel about = new AboutBoxPanel();

	public HelpAboutCommand()
	{
	}

	public HelpAboutCommand(Window parent)
	{
		this.parent = parent;
	}

	public Object clone()
	{
		return new HelpAboutCommand(parent);
	}

	public void execute()
	{
		JOptionPane. showMessageDialog(parent, about, "О программе", JOptionPane.PLAIN_MESSAGE);
	}
}

 