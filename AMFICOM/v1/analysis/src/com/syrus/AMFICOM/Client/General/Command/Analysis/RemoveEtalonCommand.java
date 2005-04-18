package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class RemoveEtalonCommand extends VoidCommand
{
	private ApplicationContext aContext; // FIXME: remove

	public RemoveEtalonCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new RemoveEtalonCommand(aContext);
	}

	public void execute()
	{
		Heap.notifyBsHashRemove(Heap.ETALON_TRACE_KEY);
		// FIXME: кажется, тут еще неплохо бы кидать CLOSE_ETALON_EVENT
	}
}
