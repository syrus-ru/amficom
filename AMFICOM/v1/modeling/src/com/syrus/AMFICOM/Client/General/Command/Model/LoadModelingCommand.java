package com.syrus.AMFICOM.Client.General.Command.Model;

import javax.swing.JOptionPane;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.InitialAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Modeling;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;

import com.syrus.AMFICOM.Client.Analysis.ReflectogrammLoadDialog;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class LoadModelingCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	private Checker checker;

	public LoadModelingCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new LoadModelingCommand(dispatcher, aContext);
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


		ReflectogrammLoadDialog dialog;
		if(Pool.get("dialog", "TraceLoadDialog") == null)
		{
			dialog = new ReflectogrammLoadDialog (this.aContext);
			Pool.put("dialog", "TraceLoadDialog", dialog);
		}
		else
		{
			dialog = (ReflectogrammLoadDialog)Pool.get("dialog", "TraceLoadDialog");
		}

		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (!(dialog.getResult() instanceof Result))
			return;

		Result res = (Result)dialog.getResult();

		if(res == null)
		{
			String error = "Ошибка при загрузке смоделированной рефлектограммы.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = null;

		Iterator it = res.getParameterList().iterator();
		while (it.hasNext())
		{
			Parameter param = (Parameter)it.next();
			if (param.getGpt().getId().equals("reflectogramm"))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		bs.title = res.getName();
		Pool.put("bellcorestructure", "primarytrace", bs);


		if(Pool.get(Modeling.TYPE, res.getModelingId()) != null)
		{
			String path_id = ((Modeling)Pool.get(Modeling.TYPE, res.getModelingId())).getSchemePathId();
			Pool.put("activecontext", "activepathid", path_id);
		}

		new InitialAnalysisCommand().execute();

		dispatcher.notify(new RefChangeEvent("primarytrace", RefChangeEvent.CLOSE_EVENT));
		dispatcher.notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
	}
}

