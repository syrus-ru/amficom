package com.syrus.AMFICOM.Client.General.Command.Window;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class WindowArrangeIconsCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	private JDesktopPane desktop;
	
	public WindowArrangeIconsCommand()
	{
	}

	public WindowArrangeIconsCommand(Dispatcher dispatcher, JDesktopPane desktop)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
	}

	public WindowArrangeIconsCommand(JDesktopPane desktop)
	{
		this.dispatcher = null;
		this.desktop = desktop;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("desktop"))
			setDesktop((JDesktopPane)value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}
	
	public void setDesktop(JDesktopPane desktop)
	{
		this.desktop = desktop;
	}

	public Object clone()
	{
		return new WindowArrangeIconsCommand(dispatcher, desktop);
	}

	public void execute()
	{
		if(desktop== null)
			return;

		int iconCnt = 0;
		JInternalFrame  allFrames[] = desktop.getAllFrames();
		for (int x = 0; x < allFrames.length; x++)
			if (
					(allFrames[x].isVisible()) &&
					(allFrames[x].isIcon()))
				iconCnt++;
		int height = desktop.getBounds().height;
		int yPos = height;
		if (iconCnt != 0)
		{
			int width = desktop.getBounds().width;
			int xPos = 0;
			for (int x = 0; x < allFrames.length; x++)
			{
				JInternalFrame frame = allFrames[x];
				if ((frame.isVisible()) && (frame.isIcon()))
				{
					Dimension dim = frame.getDesktopIcon().getSize();
					int iWidth = dim.width;
					int iHeight = dim.height;
					if (yPos == height)
						yPos = height - iHeight;
					if ((xPos + iWidth > width) && (xPos != 0))
					{
						xPos = 0;
						yPos -= iHeight;
					}
					frame.getDesktopIcon().setLocation(xPos,yPos);
					xPos += iWidth;
				} // End if
			} // End for
		} // End if
//		return(yPos);
	}
}
