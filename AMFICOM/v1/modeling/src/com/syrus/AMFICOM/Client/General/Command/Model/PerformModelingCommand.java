package com.syrus.AMFICOM.Client.General.Command.Model;

import javax.swing.JFrame;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.Model.ModelMDIMain;
import com.syrus.AMFICOM.Client.Model.*;

public class PerformModelingCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	private Checker checker;
	JFrame frame;

	public PerformModelingCommand(Dispatcher dispatcher, JFrame frame, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
		this.frame = frame;
	}

	public void setParameter(String field, Object value)
	{
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new PerformModelingCommand(dispatcher, frame, aContext);
	}

	public void execute()
	{
		this.checker = new Checker(this.aContext.getSessionInterface());
		if(!checker.checkCommand
			 (checker.performReflectoModeling))
			return;

		((ModelMDIMain)this.frame).paramsFrame.doIt();
	}
}
