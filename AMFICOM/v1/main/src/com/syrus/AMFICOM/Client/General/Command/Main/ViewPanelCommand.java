package com.syrus.AMFICOM.Client.General.Command.Main;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.Main.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

public class ViewPanelCommand extends VoidCommand
{
	private Dispatcher dispatcher;

	public ViewPanelCommand(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new ViewPanelCommand(dispatcher);
	}


	public ViewPanelCommand()
	{
	}

	public void execute()
	{
		if(dispatcher != null)
			dispatcher.notify(new ContextChangeEvent(
					this,
					ContextChangeEvent.VIEW_CHANGED_EVENT));

	}

}

