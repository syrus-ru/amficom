package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.Setup.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.UI.*;

//A0A
public class ViewMapSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public OfxControlsFrame frame;

	public ViewMapSetupCommand()
	{
	}

	public ViewMapSetupCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewMapSetupCommand(desktop, aContext);
	}

	public void execute()
	{
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				OfxControlsFrame comp = (OfxControlsFrame )desktop.getComponent(i);
				// уже есть окно карты
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
			}
		}

		if(frame == null)
		{
			frame = new OfxControlsFrame(null, aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			frame.setSize(dim.width / 5, dim.height / 2);
		}

		frame.setVisible(true);
	}

}