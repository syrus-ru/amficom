package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

public class RemoveEtalonCommand extends VoidCommand
{
	public RemoveEtalonCommand()
	{ // yes, it's really empty
	}

	public void execute()
	{
		Heap.notifyBsHashRemove(Heap.ETALON_TRACE_KEY);
		// FIXME: кажется, тут еще неплохо бы кидать CLOSE_ETALON_EVENT
	}
}
