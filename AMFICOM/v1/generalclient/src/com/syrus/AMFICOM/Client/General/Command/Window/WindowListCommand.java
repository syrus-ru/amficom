package com.syrus.AMFICOM.Client.General.Command.Window;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;

import javax.swing.JDesktopPane;

public class WindowListCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	private JDesktopPane desktop;
	
	public WindowListCommand()
	{
	}

	public WindowListCommand(Dispatcher dispatcher, JDesktopPane desktop)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
	}

	public WindowListCommand(JDesktopPane desktop)
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
		return new WindowListCommand(dispatcher, desktop);
	}

	public void execute()
	{
		if(desktop== null)
			return;

	}
}
