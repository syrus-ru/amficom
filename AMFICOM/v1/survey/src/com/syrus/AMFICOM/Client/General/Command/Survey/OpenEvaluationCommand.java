package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation;

public class OpenEvaluationCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	ApplicationModelFactory factory;

	public OpenEvaluationCommand()
	{
		// nothing
	}

	public OpenEvaluationCommand(Dispatcher dispatcher, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext)value);
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
		return new OpenEvaluationCommand(dispatcher, aContext, factory);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.enterThresholdModul))
		{
			return;
		}

		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

		  System.out.println("Starting Norms window");


		new com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation(new ReflectometryAnalyseApplicationModelFactory());

//		try
//		{
//			String st = "com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation";
//			System.out.println("Opening " + st);
//			Class cl = Class.forName(st);
//			String args[] = new String[0];
//			java.lang.reflect.Method mainMethod = cl.getMethod("main", new Class [] { args.getClass() });
//			mainMethod.invoke(cl, new Object [] { args });
//		}
//		catch(ClassNotFoundException cnfe)
//		{
//			System.out.println("Не найден ресурс класса. проверьте настройки системы");
//		}
//		catch(IllegalAccessException iae)
//		{
//			System.out.println("Нет прав на создание элемента класса");
//		}
////		catch(InstantiationException ie)
////		{
////			System.out.println("Ошибка создания объекта класса");
////		}
//		catch(Exception e)
//		{
//			System.out.println("Ошибка запуска модуля - " + e.getMessage());
//			dispatcher.notify(new StatusMessageEvent("Ошибка открытия модуля Evaluation"));
//		}

	}
}
