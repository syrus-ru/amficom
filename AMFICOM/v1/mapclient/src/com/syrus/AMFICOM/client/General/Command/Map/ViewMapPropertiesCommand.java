package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.MapPropertyFrame;

import java.awt.Dimension;

import java.util.Vector;

import javax.swing.JDesktopPane;

//A0A
public class ViewMapPropertiesCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public MapPropertyFrame frame;

	public ViewMapPropertiesCommand()
	{
	}

	public ViewMapPropertiesCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewMapPropertiesCommand(desktop, aContext);
	}

	public void execute()
	{
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				MapPropertyFrame comp = (MapPropertyFrame )desktop.getComponent(i);
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
			frame = new MapPropertyFrame("", aContext);
			frame.initialize();

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, dim.height / 4);
			frame.setSize(dim.width / 5, dim.height / 4);
		}

		frame.setVisible(true);
	}

}