package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;

public class ShowCatalogFrameCommand extends VoidCommand
{
	JDesktopPane desktopPane;
	ApplicationContext aContext;

	public ShowCatalogFrameCommand(ApplicationContext aContext, JDesktopPane desktopPane)
	{
		this.aContext = aContext;
		this.desktopPane = desktopPane;
	}

	public Object clone()
	{
		return new ShowCatalogFrameCommand(aContext, desktopPane);
	}

	public void execute()
	{
		ObjectResourceCatalogFrame catalogFrame = null;
		JInternalFrame[] frames = desktopPane.getAllFrames();
		for (int i = 0; i < frames.length; i++)
			if (frames[i] instanceof ObjectResourceCatalogFrame)
			{
				catalogFrame = (ObjectResourceCatalogFrame)frames[i];
				break;
			}

		if (catalogFrame == null)
		{
			catalogFrame = new ObjectResourceCatalogFrame("", aContext);
			desktopPane.add(catalogFrame);

			int w = desktopPane.getSize().width;
			int h = desktopPane.getSize().height;
			catalogFrame.setSize(3*w/5, h);
			catalogFrame.setLocation(w/5, 0);
		}
		catalogFrame.setVisible(true);
		catalogFrame.toFront();
	}
}

