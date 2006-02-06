package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class FileCloseCommand extends AbstractCommand
{
	public FileCloseCommand()
	{ // empty
	}

	@Override
	public Object clone()
	{
		return new FileCloseCommand();
	}

	@Override
	public void execute()
	{
		Heap.closeAll();
	}
}
