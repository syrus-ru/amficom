package com.syrus.AMFICOM.Client.General.Command;

import java.awt.*;

import javax.swing.*;

public class CloseAllInternalCommand extends VoidCommand
{
	public JDesktopPane desktop;

	public CloseAllInternalCommand(JDesktopPane desktop)
	{
		this.desktop = desktop;
	}

	public Object clone()
	{
		return new CloseAllInternalCommand(desktop);
	}

	public void execute()
	{

		JInternalFrame frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try 
			{
				frame = (JInternalFrame )desktop.getComponent(i);
				frame.setVisible(false);
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
	}
}
