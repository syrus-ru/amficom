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
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.UI.*;

//A0A
public class ViewMapElementsCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public MapElementsFrame frame;

	public ViewMapElementsCommand()
	{
	}

	public ViewMapElementsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewMapElementsCommand(desktop, aContext);
	}

	public void execute()
	{
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				MapElementsFrame comp = (MapElementsFrame )desktop.getComponent(i);
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
			frame = new MapElementsFrame(aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, 0);
			frame.setSize(dim.width / 5, dim.height / 4);
		}

		frame.setVisible(true);
	}

}