package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.BellcoreStructure;

public class FileRemoveCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private String activeRefId;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;

	public FileRemoveCommand(Dispatcher dispatcher, String activeRefId,
													 ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.activeRefId = activeRefId;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
		if(field.equals("activeRefId"))
		{
			activeRefId = (String)value;
		}
	}
	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new FileRemoveCommand(dispatcher, activeRefId, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
		/*
			The code for administrating should be placed here
		*/
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}


		bs = (BellcoreStructure)(Pool.get("bellcorestructure", activeRefId));
		if (!activeRefId.equals(AnalysisUtil.ETALON))
			//Pool.remove("bellcorestructure", activeRefId);
			Pool.remove(bs);
		bs = null;
		dispatcher.notify(new RefChangeEvent(activeRefId, RefChangeEvent.CLOSE_EVENT));
		dispatcher.notify(new RefChangeEvent("primarytrace", RefChangeEvent.SELECT_EVENT));
	}
}
