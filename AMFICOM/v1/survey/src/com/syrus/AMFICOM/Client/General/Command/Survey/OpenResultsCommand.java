package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Survey.*;
import com.syrus.AMFICOM.Client.Survey.Result.*;
import java.util.*;

public class OpenResultsCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public ResultFrame frame;

	public OpenResultsCommand()
	{
	}

	public OpenResultsCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
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
		return new OpenResultsCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.reflectogrammObserving))
		{
			return;
		}

		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

		DataSourceInterface dataSource = aC.getDataSourceInterface();
		if(dataSource == null)
			;//			return;
		System.out.println("Starting Results frame");

		//		dataSource.GetResults();

		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof ResultFrame)
				frame = (ResultFrame)comp;
		}
		if (frame == null)
		{
			frame = new ResultFrame(aC);
			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 3 / 5, 0);
			frame.setSize(dim.width * 2 / 5, dim.height / 4);
		}

		frame.show();
		frame.toFront();

	/*	Enumeration enum = Pool.getHash("result").elements();
		Result r = (Result)enum.nextElement();
		while (enum.hasMoreElements())
			r = (Result)enum.nextElement();
		//frame.setResult(r);
		frame.showActiveResult();*/
	}

}