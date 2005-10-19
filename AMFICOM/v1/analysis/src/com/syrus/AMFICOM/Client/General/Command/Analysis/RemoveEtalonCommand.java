package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.AbstractCommand;

//FIXME: unused
public class RemoveEtalonCommand extends AbstractCommand
{
	public RemoveEtalonCommand()
	{ // yes, it's really empty
	}

	@Override
	public void execute()
	{
		Heap.closeTrace(Heap.ETALON_TRACE_KEY);
		// FIXME: кажется, тут еще неплохо бы кидать CLOSE_ETALON_EVENT
	}
}
