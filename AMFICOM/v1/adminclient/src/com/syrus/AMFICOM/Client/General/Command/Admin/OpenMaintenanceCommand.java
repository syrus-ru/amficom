/*
 * $Id: OpenMaintenanceCommand.java,v 1.3 2004/09/27 16:34:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Admin;

import com.syrus.AMFICOM.Client.Administrate.Maintain.AlarmToAlert;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import java.awt.Dimension;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 16:34:55 $
 * @module admin_v1
 */
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
		DataSourceInterface dsi = aContext.getDataSource();
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