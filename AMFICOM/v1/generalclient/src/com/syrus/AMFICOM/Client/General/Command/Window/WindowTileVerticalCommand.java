package com.syrus.AMFICOM.Client.General.Command.Window;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;

import java.awt.Dimension;

import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class WindowTileVerticalCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	private JDesktopPane desktop;
	
	public WindowTileVerticalCommand()
	{
	}

	public WindowTileVerticalCommand(Dispatcher dispatcher, JDesktopPane desktop)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
	}

	public WindowTileVerticalCommand(JDesktopPane desktop)
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
		return new WindowTileVerticalCommand(dispatcher, desktop);
	}

	public void execute()
	{
		if(desktop== null)
			return;

		Dimension deskDim = desktop.getSize();
		int deskWidth = deskDim.width;
		int deskHeight = deskDim.height;
		JInternalFrame[] frames = desktop.getAllFrames();
		int frameCount = frames.length;
		int frameWidth = 0;
		int frameHeight = 0;
		int xpos = 0;
		int ypos = 0;
		double scale = 0.6;
		int spacer = 30;
		int frameCounter = 0;
		Vector frameVec = new Vector(1,1);
		boolean areIcons = false;
		int tempy = 0;
		int tempx = 0;

		int i;

		for (i = 0; i < frameCount; i++)
		{
			if (
					frames[i].isVisible() &&
					!frames[i].isIcon() &&
					frames[i].isIconifiable())
			{
				frameVec.addElement(frames[i]);
				frameCounter++;
			}
			else
			if(frames[i].isIcon())
				areIcons = true;
		}
		if(areIcons)
			deskHeight = deskHeight - 50;

		for (i = 0; i < frameCounter; i++)
		{
			JInternalFrame temp = (JInternalFrame)frameVec.elementAt(i);
			frameWidth = (int)(deskWidth / frameCounter);
			frameHeight = deskHeight;
			if (temp.isResizable())
				temp.reshape(xpos, ypos, frameWidth, frameHeight);
			else
				temp.setLocation(xpos, ypos);
			xpos = xpos + frameWidth;
			temp.moveToFront();
		}
	}
}
