package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*; 
import javax.swing.*; 

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Configure.*;

public class OpenSchemeEditorCommand extends VoidCommand 
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	ApplicationModelFactory factory;

	public OpenSchemeEditorCommand()
	{
	}

	public OpenSchemeEditorCommand(Dispatcher dispatcher, ApplicationContext aContext, ApplicationModelFactory factory)
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
		return new OpenSchemeEditorCommand(dispatcher, aContext, factory);
	}

	public void execute()
	{
/*
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);
*/
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.schemeEditing))
		{
			return;
		}

        System.out.println("Starting Scheme Editor window");

		LangModelSchematics.initialize();
		new com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor(new DefaultSchematicsApplicationModelFactory());
/*
		try
		{
			String st = "com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor";
			System.out.println("Opening " + st);
			Class cl = Class.forName(st);
			String args[] = new String[0];
			java.lang.reflect.Method mainMethod = cl.getMethod("main", new Class [] { args.getClass() });
			mainMethod.invoke(cl, new Object [] { args });
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("�� ������ ������ ������. ��������� ��������� �������");
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("��� ���� �� �������� �������� ������");
		}
//		catch(InstantiationException ie)
//		{
//			System.out.println("������ �������� ������� ������");
//		}
		catch(Exception e)
		{
			System.out.println("������ ������� ������ - " + e.getMessage());
			dispatcher.notify(new StatusMessageEvent("������ �������� ������ SchemeEditor"));
			e.printStackTrace();
		}
*/
	}
}