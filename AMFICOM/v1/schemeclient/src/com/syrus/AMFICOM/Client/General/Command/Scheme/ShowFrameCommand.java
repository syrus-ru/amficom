package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.util.Log;

public class ShowFrameCommand extends AbstractCommand
{
	JDesktopPane desktop;
	JInternalFrame frame;

	public ShowFrameCommand(JDesktopPane desktop, JInternalFrame frame)
	{
		this.desktop = desktop;
		this.frame = frame;
	}

	@Override
	public void execute()
	{
		if (this.frame != null)
		{
			this.frame.setVisible(true);
			if (this.frame.isIcon())
			{
				try
				{
					this.frame.setIcon(false);
				} catch (PropertyVetoException ex)
				{
					Log.errorMessage(ex);
				}
			}
			this.frame.moveToFront();
		}
	}
}

