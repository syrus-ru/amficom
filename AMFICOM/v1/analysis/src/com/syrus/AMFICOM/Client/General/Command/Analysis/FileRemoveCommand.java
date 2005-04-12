package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class FileRemoveCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private String activeRefId;
	private ApplicationContext aContext;

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
		if (!AnalyseMainFrameSimplified.DEBUG) // XXX: saa: security bypass
		{
			try
			{
				new Checker(this.aContext.getSessionInterface());
			}
			catch (NullPointerException ex)
			{
				System.out.println("Application context and/or user are not defined");
				return;
			}
		}

		// FIXME: тут тоже какая-то ерунда
		if (!activeRefId.equals(AnalysisUtil.ETALON))
			Heap.removeAnyBSByName(activeRefId); // XXX: was Pool.remove(bs);
		Heap.traceClosed(activeRefId);
		Heap.setCurrentTracePrimary();
	}
}
