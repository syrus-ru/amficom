package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class RemoveEtalonCommand extends VoidCommand
{
	private ApplicationContext aContext;

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
		aContext.getDispatcher().notify(new RefChangeEvent(AnalysisUtil.ETALON, RefChangeEvent.CLOSE_EVENT));
	}
}