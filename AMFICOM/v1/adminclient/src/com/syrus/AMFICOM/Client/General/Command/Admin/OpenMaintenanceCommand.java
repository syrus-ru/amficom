package com.syrus.AMFICOM.Client.General.Command.Admin;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.Administrate.Maintain.AlarmToAlert;

//A0A
public class OpenMaintenanceCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public AlarmToAlert frame;

	private JFrame ownerWindow;

	public OpenMaintenanceCommand()
	{
	}

	public OpenMaintenanceCommand(JDesktopPane desktop, ApplicationContext aContext, JFrame oW)
	{
		this.desktop = desktop;
		this.aContext = aContext;
		this.ownerWindow = oW;
	}

	public Object clone()
	{
		return new OpenMaintenanceCommand(desktop, aContext, ownerWindow);
	}

	public void execute()
	{
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (dsi == null)
			return;

		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				AlarmToAlert comp = (AlarmToAlert)desktop.getComponent(i);
				// уже есть окно
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
			}
		}

		if(frame == null)
		{
			frame = new AlarmToAlert(desktop, aContext, ownerWindow);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 3 / 10, 0);
			frame.setSize(dim.width * 7 / 10, dim.height);
		}

		frame.setVisible(true);
	}
}