package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class OpenModelingCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	ApplicationModelFactory factory;

	public OpenModelingCommand()
	{
		// nothing
	}

	public OpenModelingCommand(Dispatcher dispatcher, ApplicationContext aContext, ApplicationModelFactory factory)
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
		return new OpenModelingCommand(dispatcher, aContext, factory);
	}

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

        System.out.println("Starting Modeling window");

		LangModelModel.initialize();
		LangModelAnalyse.initialize();
		LangModelSchematics.initialize();
		LangModelMap.initialize();
		new com.syrus.AMFICOM.Client.Model.Model(new DefaultModelApplicationModelFactory());
/*
		try
		{
			String st = "com.syrus.AMFICOM.Client.Model.Model";
			System.out.println("Opening " + st);
			Class cl = Class.forName(st);
			String args[] = new String[0];
			java.lang.reflect.Method mainMethod = cl.getMethod("main", new Class [] { args.getClass() });
			mainMethod.invoke(cl, new Object [] { args });
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Не найден ресурс класса. проверьте настройки системы");
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("Нет прав на создание элемента класса");
		}
//		catch(InstantiationException ie)
//		{
//			System.out.println("Ошибка создания объекта класса");
//		}
		catch(Exception e)
		{
			System.out.println("Ошибка запуска модуля - " + e.getMessage());
			dispatcher.notify(new StatusMessageEvent("Ошибка открытия модуля Model"));
		}
*/
	}
}