package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

public class OpenMapEditorCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	ApplicationModelFactory factory;

	MapContext mc = null;

	public OpenMapEditorCommand()
	{
	}

	public OpenMapEditorCommand(Dispatcher dispatcher, ApplicationContext aContext, ApplicationModelFactory factory)
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
		else
		if(field.equals("mapContext"))
			this.mc = (MapContext )value;
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
		return new OpenMapEditorCommand(dispatcher, aContext, factory);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.topologyEditing))
		{
			return;
		}

		if(mc != null)
			Pool.put("InterModuleParameters", "MapToEdit", mc);

        System.out.println("Starting Map Editor window");

		LangModelConfig.initialize();
		LangModelMap.initialize();
		LangModelSchematics.initialize();
		new com.syrus.AMFICOM.Client.Map.Editor.MapEditor(new DefaultMapEditorApplicationModelFactory());
/*
		try
		{
			String st = "com.syrus.AMFICOM.Client.Map.Editor.MapEditor";
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
		catch(Exception e)
		{
			System.out.println("Ошибка запуска модуля - " + e.getMessage());
			dispatcher.notify(new StatusMessageEvent("Ошибка открытия модуля MapEditor"));
			e.printStackTrace();
		}
*/
	}
}