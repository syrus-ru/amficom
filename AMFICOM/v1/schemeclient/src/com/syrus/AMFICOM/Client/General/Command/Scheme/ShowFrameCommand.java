package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

public class ShowFrameCommand extends VoidCommand
{
	JDesktopPane desktop;
	JInternalFrame frame;

	public ShowFrameCommand(JDesktopPane desktop, JInternalFrame frame)
	{
		this.desktop = desktop;
		this.frame = frame;
	}

	public Object clone()
	{
		return new ShowFrameCommand(desktop, frame);
	}

	public void execute()
	{
		if (frame != null)
		{
			frame.setVisible(true);
			frame.toFront();
		}
	}
}