package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class FileCloseCommand extends VoidCommand
{
	private ApplicationContext aContext; // @todo: remove

	public FileCloseCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileCloseCommand(aContext);
	}

	public void execute()
	{
		if(false) // FIXME: security bypass??
		try
		{
			new Checker();
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}
        Heap.closeAll();
	}
}