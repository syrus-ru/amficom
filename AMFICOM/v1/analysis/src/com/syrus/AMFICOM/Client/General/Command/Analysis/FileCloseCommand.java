package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class FileCloseCommand extends VoidCommand
{
	private Dispatcher dispatcher; // @todo: remove
	private ApplicationContext aContext; // @todo: remove

	public FileCloseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileCloseCommand(dispatcher, aContext);
	}

	public void execute()
	{
		if(false) // FIXME: security bypass??
		try
		{
			new Checker(this.aContext.getSessionInterface());
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}
        Heap.closeAll();
	}
}