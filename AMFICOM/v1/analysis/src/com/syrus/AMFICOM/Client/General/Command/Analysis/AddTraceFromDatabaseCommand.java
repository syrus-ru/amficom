package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.*;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.io.*;

public class AddTraceFromDatabaseCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	private Checker checker;


	public AddTraceFromDatabaseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
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
		return new AddTraceFromDatabaseCommand(dispatcher, aContext);
	}

	public void execute()
	{

		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.loadReflectogrammFromDB))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;



//    TraceLoadDialog dialog;
		ReflectogrammLoadDialog dialog;
		if(Pool.get("dialog", "TraceLoadDialog") == null)
		{
			dataSource.LoadISM();
//      dialog = new TraceLoadDialog (this.aContext);
			dialog = new ReflectogrammLoadDialog (this.aContext);
			Pool.put("dialog", "TraceLoadDialog", dialog);
		}
		else
		{
//      dialog = (TraceLoadDialog)Pool.get("dialog", "TraceLoadDialog");
			dialog = (ReflectogrammLoadDialog)Pool.get("dialog", "TraceLoadDialog");

		}
		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (dialog.getResult() == null)
			return;

		BellcoreStructure bs = null;
		Result res = dialog.getResult();

		java.util.Iterator it = res.getParameterList().iterator();
		while (it.hasNext())
		{
			Parameter param = (Parameter)it.next();
			if (param.getGpt().getId().equals(AnalysisUtil.REFLECTOGRAMM))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		bs.title = res.getName();
		Pool.put("bellcorestructure", bs.title, bs);

		dispatcher.notify(new RefChangeEvent(bs.title,
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}
