package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class FileCloseCommand extends AbstractCommand
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
        Heap.closeAll();
	}
}
