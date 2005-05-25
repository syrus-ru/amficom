package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
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

	public void setParameter(String field, Object value)
	{
		if(field.equals("activeRefId"))
		{
			activeRefId = (String)value;
		}
	}

	public Object clone()
	{
		return new FileRemoveCommand(activeRefId, aContext);
	}

	public void execute()
	{
        // FIXME: activerefId can be null?

        Heap.removeAnyBSByName(activeRefId);
		Heap.traceClosed(activeRefId);
		Heap.setCurrentTracePrimary();
	}
}
