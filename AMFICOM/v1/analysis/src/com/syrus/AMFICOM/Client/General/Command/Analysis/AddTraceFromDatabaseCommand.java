package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;

import com.syrus.AMFICOM.Client.Analysis.ReflectogrammLoadDialog;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

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

		java.util.Enumeration enum = res.parameters.elements();
		while (enum.hasMoreElements())
		{
			Parameter param = (Parameter)enum.nextElement();
			if (param.gpt.id.equals("reflectogramm"))
				bs = new BellcoreReader().getData(param.value);
		}
		if (bs == null)
			return;

		bs.title = res.getName();
		Pool.put("bellcorestructure", bs.title, bs);

		dispatcher.notify(new RefChangeEvent(bs.title,
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}
