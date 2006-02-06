package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class FileRemoveCommand extends AbstractCommand
{
	private String activeRefId;
	private ApplicationContext aContext;

	public FileRemoveCommand(String activeRefId, ApplicationContext aContext)
	{
		this.activeRefId = activeRefId;
		this.aContext = aContext;
	}

	@Override
	public void setParameter(String field, Object value)
	{
		if(field.equals("activeRefId"))
		{
			this.activeRefId = (String)value;
		}
	}

	@Override
	public Object clone()
	{
		return new FileRemoveCommand(this.activeRefId, this.aContext);
	}

	@Override
	public void execute()
	{
		// FIXME: activerefId can be null?

		Heap.closeTrace(this.activeRefId);
	}
}
